package com.example.agimob_v1.dto;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
public class SimulacaoPriceResponseDto extends SimulacaoResponseDto {
    private List<ParcelaDto> parcelas;
}
