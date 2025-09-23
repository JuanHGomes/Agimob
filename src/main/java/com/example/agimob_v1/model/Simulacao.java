package com.example.agimob_v1.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
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


    public Simulacao() {
    }

    public Simulacao(Usuario usuario, double valor_total, double valor_entrada, int prazo) {
        this.data = LocalDateTime.now();
        this.valor_total = valor_total;
        this.valor_entrada = valor_entrada;
        this.prazo = prazo;
        this.usuario = usuario;
    }


}
