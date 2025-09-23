package com.example.agimob_v1.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id_usuario")
    private Long idUsuario;

    @Column(unique = true, name = "email_usuario")
    private String email;

    @OneToMany(mappedBy = "usuario")
    private List<Simulacao> simulacoes;
}