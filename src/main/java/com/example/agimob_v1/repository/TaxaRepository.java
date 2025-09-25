package com.example.agimob_v1.repository;

import com.example.agimob_v1.model.Taxa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.Optional;

public interface TaxaRepository extends JpaRepository<Taxa, Long> {

    @Query("SELECT t FROM Taxa t WHERE t.codigo = :codigo AND t.dataFim >= :dataAtual ")
    Optional<Taxa> findVigenteByCodigo(@Param("codigo") String codigo, @Param("dataAtual") LocalDateTime dataAtual );
}
