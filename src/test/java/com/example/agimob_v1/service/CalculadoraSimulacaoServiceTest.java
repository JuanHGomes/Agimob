package com.example.agimob_v1.service;

import com.example.agimob_v1.dto.InformacoesAdicionaisDto;
import com.example.agimob_v1.dto.ParcelaDto;
import com.example.agimob_v1.dto.SimulacaoResponseDto;
import com.example.agimob_v1.model.Simulacao;
import com.example.agimob_v1.model.Taxa;
import com.example.agimob_v1.repository.SimulacaoRepository;
import com.example.agimob_v1.repository.TaxaRepository;
import com.example.agimob_v1.services.CalculadoraSimulacaoService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CalculadoraSimulacaoServiceTest {

    @Mock
    private SimulacaoRepository repository;

    @InjectMocks
    private CalculadoraSimulacaoService service;

    @Test
    void calculaParcelasCorretamente(){
        Taxa taxa = new Taxa();
        taxa.setId(1L);

        List<ParcelaDto> parcelasSac = new ArrayList<>();

        SimulacaoResponseDto response = new SimulacaoResponseDto();
        Simulacao simulacao = new Simulacao(300000,100000,30,
                3000,0,taxa.getId(),"SAC");

        double somaParcleasSac=parcelasSac.stream().mapToDouble(ParcelaDto::getValorTotalParcela).sum();
        double totalJuros = somaParcleasSac - service.valorTotalFinanciamento(parcelasSac);
        InformacoesAdicionaisDto info = new InformacoesAdicionaisDto(service.valorPrimeiraParcela(parcelasSac), service.valorUltimaParcela(parcelasSac),
                service.valorTotalFinanciamento(parcelasSac),totalJuros,service.rendaComprometida(simulacao,parcelasSac),service.diferencaPriceSac(simulacao));

        when(service.calcularParcelas(simulacao)).thenReturn(response);

        SimulacaoResponseDto retornoCorreto = service.calcularParcelas(simulacao);

        assertNotNull(retornoCorreto);

        assertEquals(185L, retornoCorreto.getId());
        assertEquals("SAC", retornoCorreto.getTipo());
        assertEquals(parcelasSac, retornoCorreto.getParcelasSac());
        assertEquals(info, retornoCorreto.getInformacoesAdicionaisSac());

        verify(service).calcularParcelas(simulacao);

    }
}
