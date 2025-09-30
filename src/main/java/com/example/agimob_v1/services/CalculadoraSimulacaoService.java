package com.example.agimob_v1.services;

import com.example.agimob_v1.dto.ParcelaDto;
import com.example.agimob_v1.dto.SimulacaoAgibankResponseDt
import com.example.agimob_v1.dto.SimulacaoRequestDto;

import com.example.agimob_v1.dto.SimulacaoPriceResponseDto;
import com.example.agimob_v1.dto.SimulacaoSacResponseDto;

import com.example.agimob_v1.model.Simulacao;
import org.springframework.stereotype.Service;

import javax.crypto.spec.OAEPParameterSpec;
import java.util.ArrayList;
import java.util.List;


@Service
public class CalculadoraSimulacaoService {


    public int prazoConvertido(Simulacao simulacao){
       simulacao.setPrazo(simulacao.getPrazo()*12);

       return simulacao.getPrazo();
    }

    public  List<ParcelaDto> sac(Simulacao simulacao){
        //adiciona o metodo de conversao a uma variavel do tipo int
        int prazo = prazoConvertido(simulacao);

    public List<ParcelaDto> sac(Simulacao simulacao){

        double saldoDevedor = simulacao.getValor_total()-simulacao.getValor_entrada();
        double amortizacao = saldoDevedor/prazo;

        List<ParcelaDto> parcelas = new ArrayList<>();
        for (int i = 1; i <= prazo ; i++) {
            double valorJurosParcela = saldoDevedor * simulacao.getId_taxa().getValor_taxa();
            double valorTotalParcela = amortizacao + valorJurosParcela;

            saldoDevedor -= amortizacao;

            parcelas.add(
                    new ParcelaDto(
                            i,
                            valorTotalParcela,
                            valorJurosParcela,
                            amortizacao,
                            saldoDevedor
                    ));

        }
        return parcelas;
    }

    public List<ParcelaDto> price(Simulacao simulacao){
        //m = p * (j*[(1+j)^n]/[(1+j)^n]-1)
        //m = parcela fixa
        //p = valor do financiamento
        //j = taxaAplicada de jurosMesOutrosBancos
        //n = numero de parcelas

        //adiciona o metodo de conversao a uma variavel do tipo int
        int prazo = prazoConvertido(simulacao);
        double juros = simulacao.getId_taxa().getValor_taxa();

        List<ParcelaDto> parcelas = new ArrayList<>();
        double saldoDevedor = simulacao.getValor_total()-simulacao.getValor_entrada();

        double parcelaFixa = saldoDevedor*
                (juros*(Math.pow(1+juros,prazo)
                        /(Math.pow(1+ juros,prazo)-1))
                );

        for (int i = 1; i <= prazo ; i++) {
            double jurosSaldoDevedor = saldoDevedor* juros;
            double amortizacaoMes = parcelaFixa - jurosSaldoDevedor;

            saldoDevedor -= amortizacaoMes;

            parcelas.add(
                    new ParcelaDto(
                            i,
                            parcelaFixa,
                            jurosSaldoDevedor,
                            amortizacaoMes,
                            saldoDevedor
                    )
            );
        }

        return parcelas;

    }


    public SimulacaoAgibankResponseDto agibank(Simulacao simulacao){
        int prazo = prazoConvertido(simulacao);
        double juros = simulacao.getId_taxa().getValor_taxa();

        double saldoDevedor = simulacao.getValor_total()-simulacao.getValor_entrada();
        double amortizacao = saldoDevedor/prazo;
        double valorJurosParcela = saldoDevedor * juros;
        double valorTotalParcela = amortizacao + valorJurosParcela;

        double valorTotalFinanciamento = price(simulacao).stream().mapToDouble(ParcelaDto::getValorTotalParcela).sum();
        double valorTotalJuros = price(simulacao).stream().mapToDouble(ParcelaDto::getValorJurosParcela).sum();


        return new SimulacaoAgibankResponseDto(valorJurosParcela,valorTotalFinanciamento,valorTotalJuros);
    }

}
