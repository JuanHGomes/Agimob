package com.example.agimob_v1.dto;

import java.util.List;

public class SimulacaoResponseDTO {
    private List<ParcelaDto> parcelasSac;
    private List<ParcelaDto> parcelasPrice;
    private SimulacaoAgibankResponse simulacaoAgibank;

    public SimulacaoResponseDTO() {
    }

    public SimulacaoResponseDTO(List<ParcelaDto> parcelasSac, List<ParcelaDto> parcelasPrice, SimulacaoAgibankResponse simulacaoAgibank) {
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

    public SimulacaoAgibankResponse getSimulacaoAgibank() {
        return simulacaoAgibank;
    }
}


