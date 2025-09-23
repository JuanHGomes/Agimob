package com.example.agimob_v1.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor

public class Taxa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id_taxa")
    private Long idTaxa;
    @Column(name = "tipo_taxa")
    private String tipo_taxa;
    @Column(name = "valor_taxa")
    private Double valor_taxa;

    //verificar join clolumn e relacionamento no material
    @Column(name = "Id_simulacao")
    private String id_simulacao;
}
