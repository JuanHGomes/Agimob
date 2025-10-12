package com.example.agimob_v1.services;

import com.example.agimob_v1.dto.ScoreDto;
import com.example.agimob_v1.model.Simulacao;
import com.example.agimob_v1.model.Usuario;
import com.example.agimob_v1.repository.TaxaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.example.agimob_v1.model.Taxa;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class TaxaService {

    private final ScoreApiService scoreApiService;
    private final TaxaRepository taxaRepository;
    private final UsuarioService usuarioService;
    private final String taxaPadrao = "AGIBANK";

    public Taxa determinarTaxa(Simulacao simulacao) {

        Taxa taxaPadraoVigente = taxaRepository.findVigenteByCodigo(taxaPadrao, LocalDateTime.now()).orElseThrow();

        if(simulacao.getTipoUsuario().equalsIgnoreCase("CLIENTE")) {

            Usuario usuario = simulacao.getUsuario();

            return determinarTaxaPorScore(usuario.getCpf());
        }
            return taxaPadraoVigente;

    }

    private Taxa determinarTaxaPorScore(String cpfUsuario){

        ScoreDto analiseScore = scoreApiService.consultarScore(cpfUsuario).getBody();

        return taxaRepository.findVigenteByRisco(analiseScore.typeOfRisk(), LocalDateTime.now()).orElseThrow();

    }

    public Taxa getTaxaPadraoVigente(){
        return taxaRepository.findVigenteByCodigo(taxaPadrao, LocalDateTime.now()).orElseThrow();
    }
}
