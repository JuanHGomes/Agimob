package com.example.agimob_v1.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class rv dUsuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id_usuario")
    private Long id;

    @Column(unique = true, name="email")
    private String email;

    @OneToMany(mappedBy = "usuario")
    private List<Simulacao> simulacoes;

    public Usuario(String email){
        this.email = email;
    }
}
