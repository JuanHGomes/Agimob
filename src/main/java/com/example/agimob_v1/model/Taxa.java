package com.example.agimob_v1.model;


import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
public class Taxa {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name ="id_taxa")
    private Long id;

    @Column(nullable = false, unique = true, name = "codigo")
    private String codigo;

    @Column(nullable = false, name="valor_taxa")
    private double valor_taxa;

    @Column(nullable = false, name="data_inicio")
    private LocalDateTime dataInicio;

    @Column(name ="data_fim")
    private LocalDateTime dataFim;

    @OneToMany(mappedBy = "id_taxa")
    @JsonManagedReference
    private List<Simulacao> simulacoes;

    @Column(name = "risco")
    private String risco;
}
