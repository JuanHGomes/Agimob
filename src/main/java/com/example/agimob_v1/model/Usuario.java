package com.example.agimob_v1.model;

import jakarta.persistence.*;

@Entity(name = "Usuario")
public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_usuario;
    @Column(unique = true, nullable = false)
    private String email_usuario;

}
