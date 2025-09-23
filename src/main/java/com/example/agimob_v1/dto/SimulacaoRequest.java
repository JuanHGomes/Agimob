package com.example.agimob_v1.dto;


import com.example.agimob_v1.model.Usuario;

import java.time.LocalDateTime;

public class SimulacaoRequest {
    private double valorFinanciamento;
    private double valorEntrada;
    private int prazo;
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
