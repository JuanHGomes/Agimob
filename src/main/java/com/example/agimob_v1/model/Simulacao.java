package com.example.agimob_v1.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Simulacao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDateTime data;
    @Column(nullable = false)
    private double valor_total;
    @Column(nullable = false)
    private double valor_entrada;

    @Column(nullable = false)
    private int prazo;
    private double renda_usuario;
    private double renda_participante;

    @ManyToOne
    @JoinColumn(name = "id_usuario")
    private Usuario usuario;

    @Column(nullable = false)
    private double taxaAplicada;

    @Column(nullable = false)
    private String tipo;

    public Simulacao(double valor_total, double valor_entrada, int prazo, Usuario usuario, double taxaAplicada, String tipo) {
        this.data = LocalDateTime.now();
        this.valor_total = valor_total;
        this.valor_entrada = valor_entrada;
        this.prazo = prazo;
        this.usuario = usuario;
        this.taxaAplicada = taxaAplicada;
        this.tipo = tipo;
    }
}
