package com.example.agimob_v1.services;

import com.example.agimob_v1.dto.ScoreDto;
import com.example.agimob_v1.repository.TaxaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import com.example.agimob_v1.model.Taxa;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class TaxaService {

    private final ScoreApiService scoreApiService;
    private final TaxaRepository taxaRepository;

    public Taxa determinarTaxaPorScore(String cpfUsuario){

        ScoreDto analiseScore = scoreApiService.consultarScore(cpfUsuario).getBody();

        return taxaRepository.findVigenteByRisco(analiseScore.typeOfRisk(), LocalDateTime.now()).orElseThrow();


    }

}
