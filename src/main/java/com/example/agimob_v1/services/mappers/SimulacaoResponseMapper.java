package com.example.agimob_v1.services.mappers;

import com.example.agimob_v1.dto.InformacoesAdicionaisDto;
import com.example.agimob_v1.dto.ParcelaDto;
import com.example.agimob_v1.dto.SimulacaoResponseDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface SimulacaoResponseMapper {

    // Método para o caso SAC, com nomes mais claros
    @Mapping(target = "tipo", source = "tipo")
    @Mapping(target = "parcelasSac", source = "parcelas")
    @Mapping(target = "parcelasPrice", ignore = true)
    @Mapping(target = "informacoesAdicionais", source = "informacoesAdicionais")
    SimulacaoResponseDto toSacResponseDto(String tipo, List<ParcelaDto> parcelas, InformacoesAdicionaisDto informacoesAdicionais);

    // Método para o caso PRICE, com nomes mais claros
    @Mapping(target = "tipo", source = "tipo")
    @Mapping(target = "parcelasPrice", source = "parcelas")
    @Mapping(target = "parcelasSac", ignore = true)
    SimulacaoResponseDto toPriceResponseDto(String tipo, List<ParcelaDto> parcelas);

    // Método para o caso AMBOS
    @Mapping(target = "tipo", source = "tipo")
    @Mapping(target = "parcelasSac", source = "parcelasSac")
    @Mapping(target = "parcelasPrice", source = "parcelasPrice")
    SimulacaoResponseDto toAmbosResponseDto(String tipo, List<ParcelaDto> parcelasSac, List<ParcelaDto> parcelasPrice);
}