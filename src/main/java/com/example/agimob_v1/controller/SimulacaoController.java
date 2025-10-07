package com.example.agimob_v1.controller;

import com.example.agimob_v1.dto.SimulacaoRequestDto;
import com.example.agimob_v1.dto.SimulacaoResponseDto;
import com.example.agimob_v1.dto.UsuarioDto;
import com.example.agimob_v1.model.Simulacao;
import com.example.agimob_v1.services.EmailService;
import com.example.agimob_v1.services.SimulacaoService;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.function.EntityResponse;

import java.util.List;

@RestController
@RequestMapping("/agimob/simulacao")
@RequiredArgsConstructor
public class SimulacaoController {

    private final SimulacaoService simulacaoService;
    private final EmailService emailService;


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

    @PostMapping("/enviarSimulacao/{email}/{idSimulacao}")
    public ResponseEntity<Void> enviarSimulacao(@PathVariable String email, @PathVariable Long idSimulacao) throws Exception {
        return emailService.enviarEmail(email, idSimulacao);
    }
}
