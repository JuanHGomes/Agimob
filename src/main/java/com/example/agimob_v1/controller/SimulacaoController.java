package com.example.agimob_v1.controller;

import com.example.agimob_v1.dto.ScoreDto;
import com.example.agimob_v1.dto.SimulacaoRequestDto;
import com.example.agimob_v1.dto.SimulacaoResponseDto;
import com.example.agimob_v1.model.Simulacao;
import com.example.agimob_v1.services.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/agimob/simulacao")
@RequiredArgsConstructor
@CrossOrigin("*")
public class SimulacaoController {

    private final SimulacaoService simulacaoService;
    private final EmailService emailService;
    private final PdfService pdfService;
    private final ScoreApiService scoreApiService;


    @GetMapping
    public List<Simulacao> listarSimulacoes(){
       return simulacaoService.listarSimulacoes();
    }

    @PostMapping
    public SimulacaoResponseDto novaSimulacao(@RequestBody SimulacaoRequestDto simulacaoRequest) throws RuntimeException {

        simulacaoService.validarSimulacaoRequest(simulacaoRequest);

        return simulacaoService.novaSimulacao(simulacaoRequest);

    }

    @PostMapping("/enviarSimulacao/{emailUsuario}/{idSimulacao}")
    public ResponseEntity<Void> enviarSimulacao(@PathVariable String emailUsuario, @PathVariable Long idSimulacao) throws Exception {
        return emailService.enviarEmail(emailUsuario, idSimulacao);
    }

    @PostMapping("/baixarSimulacao/{idSimulacao}")
    public ResponseEntity<byte[]> baixarSimulacao(@PathVariable Long idSimulacao) {
        return pdfService.baixarPdf(idSimulacao);
    }







}
