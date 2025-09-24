package com.example.agimob_v1.dto;


public record SimulacaoRequestDto (
         double valorFinanciamento,
         double valorEntrada,
         int prazo,
         String email_usuario,
         String tipo
){

}
