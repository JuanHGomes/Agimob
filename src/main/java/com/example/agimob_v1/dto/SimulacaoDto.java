package com.example.agimob_v1.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class SimulacaoDto {
private Long id;
private LocalDateTime data;
private double valorTotal;
private double valorEntrada;
private int prazo;
private double taxaAplicada;

}
