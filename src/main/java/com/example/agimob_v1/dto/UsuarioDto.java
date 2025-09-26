package com.example.agimob_v1.dto;

import com.example.agimob_v1.model.Simulacao;
import lombok.Data;

import java.util.List;

@Data
public class UsuarioDto {
    private Long id;
    private String email;
    private List<SimulacaoDto> simulacoes;
}
