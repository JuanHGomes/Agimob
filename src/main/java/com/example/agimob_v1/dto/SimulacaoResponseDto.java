package com.example.agimob_v1.dto;

import java.util.List;

public class SimulacaoResponseDto {
    private List<ParcelaDto> parcelasSac;
    private List<ParcelaDto> parcelasPrice;
    private SimulacaoAgibankResponseDto simulacaoAgibank;


    public SimulacaoResponseDto() {
    }

    public SimulacaoResponseDto(List<ParcelaDto> parcelasSac, List<ParcelaDto> parcelasPrice, SimulacaoAgibankResponseDto simulacaoAgibank) {
        this.parcelasSac = parcelasSac;
        this.parcelasPrice = parcelasPrice;
        this.simulacaoAgibank = simulacaoAgibank;
    }

    public List<ParcelaDto> getParcelasSac() {
        return parcelasSac;
    }

    public List<ParcelaDto> getParcelasPrice() {
        return parcelasPrice;
    }

    public SimulacaoAgibankResponseDto getSimulacaoAgibank() {
        return simulacaoAgibank;
    }
}


