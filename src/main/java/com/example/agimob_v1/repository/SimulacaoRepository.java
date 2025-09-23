package com.example.agimob_v1.repository;

import com.example.agimob_v1.model.Simulacao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


public interface SimulacaoRepository extends JpaRepository<Simulacao, Long> {
}
