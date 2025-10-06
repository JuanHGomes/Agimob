package com.example.agimob_v1.services.mappers;


import com.example.agimob_v1.dto.SimulacaoDto;
import com.example.agimob_v1.dto.UsuarioDto;
import com.example.agimob_v1.model.Simulacao;
import com.example.agimob_v1.model.Usuario;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UsuarioDtoMapper {

    UsuarioDto toDto(Usuario usuario);

    SimulacaoDto toDto(Simulacao simulacao);

    List<SimulacaoDto> toSimulacaoDtoList(List<Simulacao> simulacoes);
}
