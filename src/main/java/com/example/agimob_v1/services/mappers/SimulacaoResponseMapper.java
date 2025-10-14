package com.example.agimob_v1.services.mappers;

import com.example.agimob_v1.dto.*;
import com.example.agimob_v1.model.Taxa;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;
import java.util.Optional;

@Mapper(componentModel = "spring")
public interface SimulacaoResponseMapper {


    @Mapping(target = "id", source="id")
    @Mapping(target = "tipo", source="modalidade")
    @Mapping(target = "sac", source="sacResponse")
    @Mapping(target = "price", ignore = true)
    SimulacaoResponseDto toSacReponseDto(
            Long id,
            String modalidade,
            Taxa taxa,
            String tipoUsuario,
            SacResponseDto sacResponse);

    @Mapping(target = "id", source="id")
    @Mapping(target = "tipo", source="modalidade")
    @Mapping(target = "sac", ignore = true)
    @Mapping(target = "price", source="priceResponse")
    SimulacaoResponseDto toPriceReponseDto(
            Long id,
            String modalidade,
            Taxa taxa,
            String tipoUsuario,
            PriceResponseDto priceResponse);

    @Mapping(target = "id", source="id")
    @Mapping(target = "tipo", source="modalidade")
    @Mapping(target = "sac", source = "sacResponse")
    @Mapping(target = "price", source="priceResponse")

    SimulacaoResponseDto toAmbosResponseDto(
            Long id,
            String modalidade,
            Taxa taxa,
            String tipoUsuario,
            SacResponseDto sacResponse,
            PriceResponseDto priceResponse);


}