package com.example.agimob_v1.dto;

import lombok.Builder;

import java.util.List;

@Builder
public record PriceResponseDto(
        List<ParcelaDto> parcelas,
        InformacoesAdicionaisDto informacoesAdicionais

) {
}
