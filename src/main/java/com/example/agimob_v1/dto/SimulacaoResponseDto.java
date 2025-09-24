package com.example.agimob_v1.dto;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
public class SimulacaoResponseDto {
    protected List<ParcelaDto> parcelasSac;
    protected List<ParcelaDto> parcelasPrice;
    protected SimulacaoAgibankResponseDto simulacaoAgiban;
}


