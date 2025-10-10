package com.example.agimob_v1.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
    @Column(name = "Id_simulacao")
    private Long id;

    @Column(nullable = false, name="data")
    private LocalDateTime data;

    @Column(nullable = false, name ="valor_total")
    private double valor_total;

    @Column(nullable = false, name ="valor_entrada")
    private double valor_entrada;

    @Column(nullable = false, name ="prazo")
    private int prazo;

    @Column(name = "renda_usuario")
    private double renda_usuario;

    @Column(name ="renda_participante")
    private double renda_participante;

    @ManyToOne
    @JoinColumn(name = "Id_usuario")
    private Usuario usuario;

    @Column(name ="tipo_modalidade")
    private String tipo_modalidade;

    @ManyToOne
    @JoinColumn(name = "Id_taxa")
    private Taxa id_taxa;


    public Simulacao(double valor_total, double valor_entrada, int prazo, double renda_usuario, double renda_participante, Taxa id_taxa, String tipo_modalidade) {
        this.data = LocalDateTime.now();
        this.valor_total = valor_total;
        this.valor_entrada = valor_entrada;
        this.prazo = prazo;
        this.renda_usuario = renda_usuario;
        this.renda_participante = renda_participante;
        this.id_taxa = id_taxa;
        this.tipo_modalidade = tipo_modalidade;
    }



}

