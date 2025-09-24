package com.example.agimob_v1.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class SimulacaoSacResponseDto extends SimulacaoResponseDto{
    private List<ParcelaDto> parcelas;


}
