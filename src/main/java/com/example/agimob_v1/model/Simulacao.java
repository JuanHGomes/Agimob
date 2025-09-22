package com.example.agimob_v1.model;


import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity(name = "Simulacao")
public class Simulacao {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_simulacao;
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
    @JoinColumn(name =  "id_usuario")
    private Usuario id_usuario;

}
