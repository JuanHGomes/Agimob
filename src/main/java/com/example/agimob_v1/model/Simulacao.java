package com.example.agimob_v1.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Simulacao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_simulacao")
    private Long id;

    @Column(nullable = false)
    private LocalDateTime data;

    @Column(nullable = false, name ="valor_total")
    private double valor_total;

    @Column(nullable = false)
    private double valor_entrada;

    @Column(nullable = false)
    private int prazo;

    private double taxaAplicada;

    private double renda_usuario;

    private double renda_participante;

    @ManyToOne
    @JoinColumn(name = "id_usuario")
    private Usuario usuario;


    public Simulacao(double valor_total, double valor_entrada, int prazo, double taxaAplicada, Usuario usuario) {
        this.data = LocalDateTime.now();
        this.valor_total = valor_total;
        this.valor_entrada = valor_entrada;
        this.prazo = prazo;
        this.taxaAplicada = taxaAplicada;
        this.usuario = usuario;
    }
}
