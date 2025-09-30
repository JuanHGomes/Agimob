package com.example.agimob_v1.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class SimulacaoRequestDto {
    private double valorFinanciamento;
    private double valorEntrada;
    private double rendaUsuario;
    private double rendaParticipante;
    private int prazo;
    private String email;
    private String tipo;



}
