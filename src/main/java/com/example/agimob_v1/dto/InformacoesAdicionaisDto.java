package com.example.agimob_v1.dto;


import lombok.Builder;

@Builder
public record InformacoesAdicionaisDto(
        double taxa,
        double primeiraParcela,
        double ultimaParcela,
        double valorTotalFinanciamento,
        double valorTotalJuros,
        double rendaComprometida,
        double diferencaPriceSac) {
}
