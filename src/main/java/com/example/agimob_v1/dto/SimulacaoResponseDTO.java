package com.example.agimob_v1.dto;

import com.example.agimob_v1.model.Taxa;
import com.example.agimob_v1.model.Usuario;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class SimulacaoResponseDTO {
    private Long id;
    private LocalDate data;
    private Double valor_total;
    private Double valor_entrada;
    private Integer prazo;
    private Double renda_usuario;
    private Double renda_participante;
    private String tipo_modalidade;
    private Taxa taxa;
    private Usuario usuario;

    // resultados calculados
    private List<ParcelaDto> sacAgibank;
    private List<ParcelaDto> priceAgibank;
    private Double totalJurosSacAgibank;
    private Double totalJurosPriceAgibank;

}


