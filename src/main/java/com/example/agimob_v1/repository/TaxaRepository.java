package com.example.agimob_v1.repository;

import com.example.agimob_v1.model.Taxa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.validation.ObjectError;

import java.util.Optional;

public interface TaxaRepository extends JpaRepository<Taxa, Long> {
    Optional<Taxa> findByCodigo(String codigo);
}
