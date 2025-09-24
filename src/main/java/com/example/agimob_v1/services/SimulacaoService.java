package com.example.agimob_v1.services;

import com.example.agimob_v1.dto.*;
import com.example.agimob_v1.exceptions.OpcaoInvalidaException;
import com.example.agimob_v1.model.Simulacao;
import com.example.agimob_v1.model.Taxa;
import com.example.agimob_v1.model.Usuario;
import com.example.agimob_v1.repository.SimulacaoRepository;
import com.example.agimob_v1.repository.TaxaRepository;
import com.example.agimob_v1.repository.UsuarioRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SimulacaoService {
    private final SimulacaoRepository simulacaoRepository;
    private final UsuarioRepository usuarioRepository;
    private final UsuarioService usuarioService;
    private final CalculadoraSimulacaoService calculadoraSimulacaoService;
    private final TaxaRepository taxaRepository;

    public SimulacaoService(SimulacaoRepository simulacaoRepository, UsuarioRepository usuarioRepository, UsuarioService usuarioService, CalculadoraSimulacaoService calculadoraSimulacaoService, TaxaRepository taxaRepository) {
        this.simulacaoRepository = simulacaoRepository;
        this.usuarioRepository = usuarioRepository;
        this.usuarioService = usuarioService;
        this.calculadoraSimulacaoService = calculadoraSimulacaoService;
        this.taxaRepository = taxaRepository;
    }

    public List<Simulacao> listarSimulacoes(){
        return simulacaoRepository.findAll();
    }

    public SimulacaoResponseDto novaSimulacao(SimulacaoRequestDto simulacaoRequestDto) throws Exception {
        Usuario usuario = usuarioRepository.findByEmail(simulacaoRequestDto.email_usuario()).orElseGet(() -> usuarioService.novoUsuario(simulacaoRequestDto));

        double valorFinanciamento = simulacaoRequestDto.valorFinanciamento();
        double valorEntrada = simulacaoRequestDto.valorEntrada();
        int prazo = simulacaoRequestDto.prazo();
        double taxaAplicada = taxaRepository.findByCodigo("AGIBANK").map(Taxa::getValor).orElseThrow(() -> new Exception("vixi"));
        //CRIAR EXCEÇÃO PARA A TAXA INVALIDA
        String tipo = simulacaoRequestDto.tipo();

        Simulacao simulacao = new Simulacao(valorFinanciamento, valorEntrada, prazo, usuario, taxaAplicada, tipo);

        simulacaoRepository.save(simulacao);

        //UTILIZAR ENUM PARA SAC-PRICE-AMBOS
        if(simulacao.getTipo().equals("SAC")){
            return new SimulacaoSacResponseDto(calculadoraSimulacaoService.sac(simulacao));
        }
        else if (simulacao.getTipo().equals("PRICE")) {
            return new SimulacaoPriceResponseDto(calculadoraSimulacaoService.price(simulacao));
        }
        else if(simulacao.getTipo().equals("AMBOS")){
           return new SimulacaoResponseDto(calculadoraSimulacaoService.sac(simulacao), calculadoraSimulacaoService.price(simulacao), calculadoraSimulacaoService.agibank(simulacao));
        }
        else{
            throw  new OpcaoInvalidaException("Opção invalida");
        }

    }

}
