package com.example.agimob_v1.services;

import com.example.agimob_v1.dto.ParcelaDto;
import com.example.agimob_v1.dto.SimulacaoAgibankResponse;
import com.example.agimob_v1.model.Simulacao;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service
public class CalculadoraSimulacaoService {
    private static final double jurosMesOutrosBancos = 0.12/12;
    private static final double jurosMesAgibank = 0.09/12;


    public  List<ParcelaDto> sac(Simulacao simulacao){
        double saldoDevedor = simulacao.getValor_total()-simulacao.getValor_entrada();
        double amortizacao = saldoDevedor/simulacao.getPrazo();

        List<ParcelaDto> parcelas = new ArrayList<>();
        for (int i = 1; i <= simulacao.getPrazo() ; i++) {
            double valorJurosParcela = saldoDevedor * jurosMesOutrosBancos;
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
        //j = taxa de jurosMesOutrosBancos
        //n = numero de parcelas
        List<ParcelaDto> parcelas = new ArrayList<>();
        double saldoDevedor = simulacao.getValor_total()-simulacao.getValor_entrada();

        double parcelaFixa = saldoDevedor*
                (jurosMesOutrosBancos*(Math.pow(1+jurosMesOutrosBancos,simulacao.getPrazo())
                        /(Math.pow(1+ jurosMesOutrosBancos,simulacao.getPrazo())-1))
                );

        for (int i = 1; i <= simulacao.getPrazo() ; i++) {
            double jurosSaldoDevedor = saldoDevedor* jurosMesOutrosBancos;
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

    public SimulacaoAgibankResponse agibank(Simulacao simulacao){

        double saldoDevedor = simulacao.getValor_total()-simulacao.getValor_entrada();
        double amortizacao = saldoDevedor/simulacao.getPrazo();
        double valorJurosParcela = saldoDevedor * jurosMesAgibank;
        double valorTotalParcela = amortizacao + valorJurosParcela;

        double valorTotalFinanciamento = price(simulacao).stream().mapToDouble(ParcelaDto::getValorTotalParcela).sum();
        double valorTotalJuros = price(simulacao).stream().mapToDouble(ParcelaDto::getValorJurosParcela).sum();


        return new SimulacaoAgibankResponse(valorJurosParcela,valorTotalFinanciamento,valorTotalJuros);
    }
}
