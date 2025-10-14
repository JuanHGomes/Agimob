package com.example.agimob_v1.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.br.CPF;

import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id_usuario")
    private Long id;

    @Column(unique = true, name="email")
    private String email;


    @Column(name = "cpf", unique = true)
    private String cpf;

    @Column(name = "id_score")
    private int idScore;

    @OneToMany(mappedBy = "usuario")
    @JsonIgnore
    private List<Simulacao> simulacoes;

    public Usuario(String email){
        this.email = email;
    }
    public Usuario(String email, String cpf){
        this.email = email;
        this.cpf = cpf;
    }
}
