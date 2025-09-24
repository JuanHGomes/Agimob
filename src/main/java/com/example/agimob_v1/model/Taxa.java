package com.example.agimob_v1.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Entity
public class Taxa {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, unique = true)
    private String codigo;
    @Column(nullable = false)
    private double valor;
    @Column(nullable = false)
    private LocalDateTime dataInicio;
    @Column(nullable = false)
    private LocalDateTime dataFim;

}
