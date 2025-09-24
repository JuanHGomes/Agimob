package com.example.agimob_v1.dto;

import lombok.Data;

import java.util.List;

@Data
public class SimulacaoOutrosResponseDTO {
   private List<ParcelaDto> sacOutros;
   private List<ParcelaDto> priceOutros;
   private Double totalJurosSacOutros;
   private Double totalJurosPriceOutros;
}


