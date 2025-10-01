package com.example.agimob_v1.services;

import com.example.agimob_v1.dto.*;
import com.example.agimob_v1.model.Simulacao;
import com.example.agimob_v1.model.Taxa;
import com.example.agimob_v1.model.Usuario;
import com.example.agimob_v1.repository.SimulacaoRepository;
import com.example.agimob_v1.repository.TaxaRepository;
import com.example.agimob_v1.repository.UsuarioRepository;
import com.example.agimob_v1.services.mappers.SimulacaoResponseMapper;
import com.example.agimob_v1.services.mappers.UsuarioDtoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
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
    @Autowired
    private final SimulacaoResponseMapper simulacaoResponseMapper;

    public SimulacaoService(SimulacaoRepository simulacaoRepository, UsuarioRepository usuarioRepository, UsuarioService usuarioService, CalculadoraSimulacaoService calculadoraSimulacaoService, TaxaRepository taxaRepository, UsuarioDtoMapper usuarioDtoMapper, SimulacaoResponseMapper simulacaoResponseMapper) {
        this.simulacaoRepository = simulacaoRepository;
        this.usuarioRepository = usuarioRepository;
        this.usuarioService = usuarioService;
        this.calculadoraSimulacaoService = calculadoraSimulacaoService;
        this.taxaRepository = taxaRepository;
        this.usuarioDtoMapper = usuarioDtoMapper;
        this.simulacaoResponseMapper = simulacaoResponseMapper;
    }


    public int prazoConvertido(SimulacaoRequestDto simulacaoRequest) {
        simulacaoRequest.setPrazo(simulacaoRequest.getPrazo() * 12);

        return simulacaoRequest.getPrazo();
    }

    public List<Simulacao> listarSimulacoes() {
        return simulacaoRepository.findAll();
    }

    public SimulacaoResponseDto novaSimulacao(SimulacaoRequestDto simulacaoRequest) throws Exception {

        Usuario usuario = usuarioRepository.findByEmail(simulacaoRequest.getEmail()).orElseGet(() -> usuarioService.novoUsuario(simulacaoRequest));

        double valorFinanciamento = simulacaoRequest.getValorFinanciamento();
        double valorEntrada = simulacaoRequest.getValorEntrada();
        int prazo = prazoConvertido(simulacaoRequest);
        double rendaUsuario = simulacaoRequest.getRendaUsuario();
        double rendaPartcipante = simulacaoRequest.getRendaParticipante();
        Taxa taxa = taxaRepository.findVigenteByCodigo("AGIBANK", LocalDateTime.now()).orElseThrow();
        String tipoSimulacao = simulacaoRequest.getTipo();

        Simulacao simulacao = new Simulacao(valorFinanciamento, valorEntrada, prazo, rendaUsuario, rendaPartcipante, taxa, usuario);

        simulacaoRepository.save(simulacao);

        List<ParcelaDto> parcelas;
        InformacoesAdicionaisDto informacoesAdicionais;

        if (simulacaoRequest.getTipo().equalsIgnoreCase("SAC")) {

            parcelas = calculadoraSimulacaoService.sac(simulacao);
            informacoesAdicionais = calculadoraSimulacaoService.calcularInformacoesAdicionais(simulacao, parcelas);

            return simulacaoResponseMapper.toSacResponseDto(tipoSimulacao, parcelas,informacoesAdicionais);

        } else if (simulacaoRequest.getTipo().equalsIgnoreCase("PRICE")) {

            return simulacaoResponseMapper.toPriceResponseDto(tipoSimulacao, calculadoraSimulacaoService.price(simulacao));

        } else if (simulacaoRequest.getTipo().equalsIgnoreCase("AMBOS")) {

            return simulacaoResponseMapper.toAmbosResponseDto(simulacaoRequest.getTipo(), calculadoraSimulacaoService.sac(simulacao), calculadoraSimulacaoService.price(simulacao));
        }

        throw new Exception();
    }


    public UsuarioDto listarSimulacoesPorUsuarioId(String email) {

    Usuario usuario = usuarioRepository.findByEmail(email).orElseThrow();

    return usuarioDtoMapper.toDto(usuarioRepository.findByEmail(email).orElseThrow());

    }

}


