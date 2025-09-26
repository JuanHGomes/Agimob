package com.example.agimob_v1.services;

import com.example.agimob_v1.dto.SimulacaoRequestDto;
import com.example.agimob_v1.dto.SimulacaoResponseDto;
import com.example.agimob_v1.dto.UsuarioDto;
import com.example.agimob_v1.model.Simulacao;
import com.example.agimob_v1.model.Taxa;
import com.example.agimob_v1.model.Usuario;
import com.example.agimob_v1.repository.SimulacaoRepository;
import com.example.agimob_v1.repository.TaxaRepository;
import com.example.agimob_v1.repository.UsuarioRepository;
import com.example.agimob_v1.services.mappers.UsuarioDtoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class SimulacaoService {
    private final SimulacaoRepository simulacaoRepository;
    private final UsuarioRepository usuarioRepository;
    private final UsuarioService usuarioService;
    private final CalculadoraSimulacaoService calculadoraSimulacaoService;
    private final TaxaRepository taxaRepository;
    @Autowired
    private final UsuarioDtoMapper usuarioDtoMapper;

    public SimulacaoService(SimulacaoRepository simulacaoRepository, UsuarioRepository usuarioRepository, UsuarioService usuarioService, CalculadoraSimulacaoService calculadoraSimulacaoService, TaxaRepository taxaRepository, UsuarioDtoMapper usuarioDtoMapper) {
        this.simulacaoRepository = simulacaoRepository;
        this.usuarioRepository = usuarioRepository;
        this.usuarioService = usuarioService;
        this.calculadoraSimulacaoService = calculadoraSimulacaoService;
        this.taxaRepository = taxaRepository;
        this.usuarioDtoMapper = usuarioDtoMapper;
    }

    public List<Simulacao> listarSimulacoes(){
        return simulacaoRepository.findAll();
    }

    public SimulacaoResponseDto novaSimulacao(SimulacaoRequestDto simulacaoRequest) throws Exception {
        Usuario usuario = usuarioRepository.findByEmail(simulacaoRequest.getEmail()).orElseGet(() -> usuarioService.novoUsuario(simulacaoRequest));

        double valorFinanciamento = simulacaoRequest.getValorFinanciamento();
        double valorEntrada = simulacaoRequest.getValorEntrada();
        int prazo = simulacaoRequest.getPrazo();
        double taxaAplicada = taxaRepository.findVigenteByCodigo("AGIBANK", LocalDateTime.now()).map(Taxa::getValor).orElseThrow();




        Simulacao simulacao = new Simulacao( valorFinanciamento, valorEntrada, prazo, taxaAplicada, usuario);

        simulacaoRepository.save(simulacao);

        return new SimulacaoResponseDto(calculadoraSimulacaoService.sac(simulacao), calculadoraSimulacaoService.price(simulacao), calculadoraSimulacaoService.agibank(simulacao));

    }

    public UsuarioDto listarSimulacoesPorUsuarioId(String email) {
        Usuario usuario = usuarioRepository.findByEmail(email).orElseThrow();

        return usuarioDtoMapper.toDto(usuarioRepository.findByEmail(email).orElseThrow());

    }
}
