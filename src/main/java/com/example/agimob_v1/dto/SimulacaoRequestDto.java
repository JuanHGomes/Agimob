package com.example.agimob_v1.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


public record SimulacaoRequestDto (double valorTotal,
         double valorEntrada,
         double rendaUsuario,
         double rendaParticipante,
         int prazo,
         String email,
         String tipo){

}
