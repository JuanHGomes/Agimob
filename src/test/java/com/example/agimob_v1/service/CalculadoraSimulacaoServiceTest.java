package com.example.agimob_v1.service;

import com.example.agimob_v1.dto.InformacoesAdicionaisDto;
import com.example.agimob_v1.dto.ParcelaDto;
import com.example.agimob_v1.dto.SimulacaoResponseDto;
import com.example.agimob_v1.model.Simulacao;
import com.example.agimob_v1.model.Taxa;
import com.example.agimob_v1.model.Usuario;
import com.example.agimob_v1.repository.SimulacaoRepository;
import com.example.agimob_v1.services.CalculadoraSimulacaoService;
import com.example.agimob_v1.services.mappers.SimulacaoResponseMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CalculadoraSimulacaoServiceTest {
    @Mock
    private Usuario usuarioMock;
    @Mock
    private Taxa taxaMock;
    @Mock
    private SimulacaoResponseMapper mapperMock;

    @InjectMocks
    private CalculadoraSimulacaoService service;

    @Test
    void calculaParcelasCorretamente(){
        taxaMock.setId(1L);
        taxaMock.setValor_taxa(0.09);

        List<ParcelaDto> parcelas = new ArrayList<>();
        parcelas.add(new ParcelaDto(1,2458.33,375,2083.33,47916.67));

        SimulacaoResponseDto response = new SimulacaoResponseDto();
        response.setId(1L);
        response.setTipo("SAC");
        response.setParcelasSac(parcelas);

        Simulacao simulacao = new Simulacao(1L, LocalDateTime.now(),100000,50000,
                2,3000, 0,usuarioMock,"SAC",taxaMock);


        when(mapperMock.toSacResponseDto(
                anyLong(),
                anyString(),
                anyList(),
                any(InformacoesAdicionaisDto.class))).thenReturn(response);

        SimulacaoResponseDto retornoCorreto = service.calcularParcelas(simulacao);

        assertNotNull(retornoCorreto);

        assertEquals(1L, retornoCorreto.getId());
        assertEquals("SAC", retornoCorreto.getTipo());
        assertEquals(response.getParcelasSac(), retornoCorreto.getParcelasSac());

        verify(mapperMock).toSacResponseDto(
                eq(simulacao.getId()),
                eq(simulacao.getTipo_modalidade()),
                anyList(),
                any(InformacoesAdicionaisDto.class));

    }

}
