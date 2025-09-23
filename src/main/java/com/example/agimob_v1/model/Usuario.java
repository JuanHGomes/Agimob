package com.example.agimob_v1.model;

import jakarta.persistence.*;

import java.util.List;

@Entity
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String email;

    @OneToMany(mappedBy = "usuario")
    private List<Simulacao> simulacoes;

    public Usuario() {
    }

    public Usuario(String email) {
        this.email = email;
    }
}
