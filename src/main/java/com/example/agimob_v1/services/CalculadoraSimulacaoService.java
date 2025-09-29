package com.example.agimob_v1.services;

import com.example.agimob_v1.dto.ParcelaDto;
import com.example.agimob_v1.dto.SimulacaoAgibankResponseDto;
import com.example.agimob_v1.dto.SimulacaoPriceResponseDto;
import com.example.agimob_v1.dto.SimulacaoSacResponseDto;
import com.example.agimob_v1.model.Simulacao;
import org.springframework.stereotype.Service;

import javax.crypto.spec.OAEPParameterSpec;
import java.util.ArrayList;
import java.util.List;


@Service
public class CalculadoraSimulacaoService {

    public List<ParcelaDto> sac(Simulacao simulacao){
        double saldoDevedor = simulacao.getValor_total()-simulacao.getValor_entrada();
        double amortizacao = saldoDevedor/simulacao.getPrazo();

        List<ParcelaDto> parcelas = new ArrayList<>();
        for (int i = 1; i <= simulacao.getPrazo() ; i++) {
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
        double juros = simulacao.getId_taxa().getValor_taxa();
        List<ParcelaDto> parcelas = new ArrayList<>();
        double saldoDevedor = simulacao.getValor_total()-simulacao.getValor_entrada();

        double parcelaFixa = saldoDevedor*
                (juros*(Math.pow(1+juros,simulacao.getPrazo())
                        /(Math.pow(1+ juros,simulacao.getPrazo())-1))
                );

        for (int i = 1; i <= simulacao.getPrazo() ; i++) {
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

//    public SimulacaoAgibankResponseDto agibank(Simulacao simulacao){
//        double juros = simulacao.getId_taxa().getValor_taxa();
//
//        double saldoDevedor = simulacao.getValor_total()-simulacao.getValor_entrada();
//        double amortizacao = saldoDevedor/simulacao.getPrazo();
//        double valorJurosParcela = saldoDevedor * juros;
//        double valorTotalParcela = amortizacao + valorJurosParcela;
//
//        double valorTotalFinanciamento = price(simulacao).stream().mapToDouble(ParcelaDto::getValorTotalParcela).sum();
//        double valorTotalJuros = price(simulacao).stream().mapToDouble(ParcelaDto::getValorJurosParcela).sum();
//
//
//        return new SimulacaoAgibankResponseDto(valorJurosParcela,valorTotalFinanciamento,valorTotalJuros);
//    }
}
