package com.example.agimob_v1.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class SimulacaoResponseDto {
    private SimulacaoSacResponseDto parcelasSac;
    private SimulacaoPriceResponseDto parcelasPrice;
    private SimulacaoAgibankResponseDto simulacaoAgibank;

    public SimulacaoResponseDto(SimulacaoSacResponseDto parcelasSac) {
        this.parcelasSac = parcelasSac;
    }

    public SimulacaoResponseDto(SimulacaoPriceResponseDto parcelasPrice) {
        this.parcelasPrice = parcelasPrice;
    }

}




