package com.example.agimob_v1.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.swing.text.ParagraphView;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class SimulacaoResponseDto {
    private String tipo;
    private List<ParcelaDto> parcelasSac;
    private List<ParcelaDto> parcelasPrice;
    private InformacoesAdicionaisDto informacoesAdicionais;
}




