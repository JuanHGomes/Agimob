package com.example.agimob_v1.repository;

import com.example.agimob_v1.model.Simulacao;
import com.example.agimob_v1.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


public interface SimulacaoRepository extends JpaRepository<Simulacao, Long> {
    Optional<List<Simulacao>> findByUsuario(Usuario usuario);

    Long id(Long id);
}
