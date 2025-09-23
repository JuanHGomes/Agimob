package com.example.agimob_v1.dto;

//DTO DE ENTRADA!

public class SimulacaoRequestDTO {
    private Double valorFinanciamento;
    private Double valorEntrada;
    private Integer prazo;
    private Double renda_usuario;
    private Double renda_participante;
    private String email_usuario;

    public double getValorFinanciamento() {
        return valorFinanciamento;
    }

    public double getValorEntrada() {
        return valorEntrada;
    }

    public int getPrazo() {
        return prazo;
    }

    public String getEmail_usuario(){
        return email_usuario;
    }

}
