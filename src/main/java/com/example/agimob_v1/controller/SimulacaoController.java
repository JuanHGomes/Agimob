package com.example.agimob_v1.controller;

import com.example.agimob_v1.dto.SimulacaoRequestDto;
import com.example.agimob_v1.dto.SimulacaoResponseDto;
import com.example.agimob_v1.dto.UsuarioDto;
import com.example.agimob_v1.model.Simulacao;
import com.example.agimob_v1.services.SimulacaoService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/agimob/simulacao")
public class SimulacaoController {

    private final SimulacaoService simulacaoService;

    public SimulacaoController(SimulacaoService simulacaoService) {
        this.simulacaoService = simulacaoService;
    }

    @GetMapping
    public List<Simulacao> listarSimulacoes(){
       return simulacaoService.listarSimulacoes();
    }

    @PostMapping
    public SimulacaoResponseDto novaSimulacao(@RequestBody SimulacaoRequestDto simulacaoRequestDto) throws Exception {
        return simulacaoService.novaSimulacao(simulacaoRequestDto);
    }

    @GetMapping("/{email}")
    public UsuarioDto simulacoesPorUsuario(@PathVariable String email){
       return simulacaoService.listarSimulacoesPorUsuarioId(email);
    }
}
