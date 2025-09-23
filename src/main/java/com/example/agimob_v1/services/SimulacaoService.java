package com.example.agimob_v1.services;

import com.example.agimob_v1.dto.SimulacaoRequest;
import com.example.agimob_v1.dto.SimulacaoResponse;
import com.example.agimob_v1.exceptions.FormatoIndevidoException;
import com.example.agimob_v1.model.Simulacao;
import com.example.agimob_v1.model.Usuario;
import com.example.agimob_v1.repository.SimulacaoRepository;
import com.example.agimob_v1.repository.UsuarioRepository;
import com.example.agimob_v1.services.UsuarioService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SimulacaoService {
    private final SimulacaoRepository simulacaoRepository;
    private final UsuarioRepository usuarioRepository;
    private final UsuarioService usuarioService;
    private final CalculadoraSimulacaoService calculadoraSimulacaoService;

    public SimulacaoService(SimulacaoRepository simulacaoRepository, UsuarioRepository usuarioRepository, UsuarioService usuarioService, CalculadoraSimulacaoService calculadoraSimulacaoService) {
        this.simulacaoRepository = simulacaoRepository;
        this.usuarioRepository = usuarioRepository;
        this.usuarioService = usuarioService;
        this.calculadoraSimulacaoService = calculadoraSimulacaoService;
    }

    public List<Simulacao> listarSimulacoes(){
        return simulacaoRepository.findAll();
    }

    public SimulacaoResponse novaSimulacao(SimulacaoRequest simulacaoRequest) throws Exception {
        Usuario usuario = usuarioRepository.findByEmail(simulacaoRequest.getEmail_usuario()).orElseGet(() -> usuarioService.novoUsuario(simulacaoRequest));

        double valorFinanciamento = simulacaoRequest.getValorFinanciamento();
        double valorEntrada = simulacaoRequest.getValorEntrada();
        int prazo = simulacaoRequest.getPrazo();



        Simulacao simulacao = new Simulacao(usuario, valorFinanciamento, valorEntrada, prazo);

        simulacaoRepository.save(simulacao);

        return new SimulacaoResponse(calculadoraSimulacaoService.sac(simulacao), calculadoraSimulacaoService.price(simulacao), calculadoraSimulacaoService.agibank(simulacao));

    }

}
