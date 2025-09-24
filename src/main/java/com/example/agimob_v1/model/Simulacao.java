package com.example.agimob_v1.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor

public class Simulacao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id_simulacao")
    private Long idSimulacao;

    @Column(nullable = false, name = "data")
    @CreationTimestamp
    private LocalDate data;

    @Column(nullable = false, name = "valor_total")
    private Double valor_total;

    @Column(nullable = false, name = "valor_entrada")
    private Double valor_entrada;

    @Column(nullable = false, name = "prazo")
    private Integer prazo;

    @Column(nullable = false, name = "renda_usuario")
    private Double renda_usuario;

    @Column(name = "renda_participante")
    private Double renda_participante;

    @Column(name = "tipo_modalidade")
    private String tipo_modalidade;


    @Column(name = "taxa")
    private Taxa taxa;

    @ManyToOne
    @JoinColumn(name = "Id_usuario")
    private Usuario usuario;

}
