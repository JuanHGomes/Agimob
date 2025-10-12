package com.example.agimob_v1.services.mappers;

import com.example.agimob_v1.dto.*;
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
            SacResponseDto sacResponse);

    @Mapping(target = "id", source="id")
    @Mapping(target = "tipo", source="modalidade")
    @Mapping(target = "sac", ignore = true)
    @Mapping(target = "price", source="priceResponse")
    SimulacaoResponseDto toPriceReponseDto(
            Long id,
            String modalidade,
            PriceResponseDto priceResponse);

    @Mapping(target = "id", source="id")
    @Mapping(target = "tipo", source="modalidade")
    @Mapping(target = "sac", source = "sacResponse")
    @Mapping(target = "price", source="priceResponse")
    SimulacaoResponseDto toAmbosResponseDto(
            Long id,
            String modalidade,
            SacResponseDto sacResponse,
            PriceResponseDto priceResponse);

   /* @Mapping(target = "id", source = "id")
    @Mapping(target = "tipo", source = "tipo")
    @Mapping(target = "parcelasSac", source = "parcelas")
    @Mapping(target = "parcelasPrice", ignore = true)
    @Mapping(target = "informacoesAdicionaisSac", source = "informacoesAdicionais")
    @Mapping(target = "informacoesAdicionaisPrice", ignore = true)
    @Mapping(target = "informacoesAdicionaisAgi", source="informacoesAdicionaisAgi")
    SimulacaoResponseDto toSacResponseDto(Long id,
                                          String tipo,
                                          List<ParcelaDto> parcelas,
                                          InformacoesAdicionaisDto informacoesAdicionais,
                                          InformacoesAdicionaisClienteAgiDto informacoesAdicionaisAgi);


    @Mapping(target = "id", source = "id")
    @Mapping(target = "tipo", source = "tipo")
    @Mapping(target = "parcelasPrice", source = "parcelas")
    @Mapping(target = "parcelasSac", ignore = true)
    @Mapping(target = "informacoesAdicionaisPrice", source = "informacoesAdicionais")
    @Mapping(target = "informacoesAdicionaisSac", ignore = true)
    SimulacaoResponseDto toPriceResponseDto(Long id,
                                            String tipo,
                                            List<ParcelaDto> parcelas,
                                            InformacoesAdicionaisDto informacoesAdicionais,
                                            InformacoesAdicionaisClienteAgiDto informacoesAdicionaisAgi);


    @Mapping(target = "id", source = "id")
    @Mapping(target = "tipo", source = "tipo")
    @Mapping(target = "parcelasSac", source = "parcelasSac")
    @Mapping(target = "parcelasPrice", source = "parcelasPrice")
    @Mapping(target = "informacoesAdicionaisSac", source = "informacoesAdicionaisSac")
    @Mapping(target = "informacoesAdicionaisPrice", source = "informacoesAdicionaisPrice")
    SimulacaoResponseDto toAmbosResponseDto(Long id,
                                            String tipo,
                                            List<ParcelaDto> parcelasSac,
                                            List<ParcelaDto> parcelasPrice,
                                            InformacoesAdicionaisDto informacoesAdicionaisSac,
                                            InformacoesAdicionaisDto informacoesAdicionaisPrice,
                                            InformacoesAdicionaisClienteAgiDto informacoesAdicionaisAgi);

    */
}