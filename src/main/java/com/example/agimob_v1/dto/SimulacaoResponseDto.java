package com.example.agimob_v1.dto;

import com.example.agimob_v1.model.Taxa;
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
    private String tipoUsuario;
    private Taxa taxa;
    private SacResponseDto sac;
    private PriceResponseDto price;

}




