package com.example.agimob_v1.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class SimulacaoResponseDto {
    private Long id;
    private String tipo;
    private SacResponseDto sac;
    private PriceResponseDto price;

    /*
    private List<ParcelaDto> parcelasSac;
    private InformacoesAdicionaisDto informacoesAdicionaisSac;
    private List<ParcelaDto> parcelasPrice;
    private InformacoesAdicionaisDto informacoesAdicionaisPrice;
    private InformacoesAdicionaisClienteAgiDto informacoesClienteAgi;
    */

}




