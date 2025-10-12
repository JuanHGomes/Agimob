package com.example.agimob_v1.dto;


public record SimulacaoRequestDto (
         String cpfUsuario,
         String emailUsuario,
         double valorTotal,
         double valorEntrada,
         double rendaUsuario,
         double rendaParticipante,
         int prazo,
         String tipo){

}
