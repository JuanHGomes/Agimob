package com.example.agimob_v1.services;

import com.example.agimob_v1.dto.*;
import com.example.agimob_v1.exceptions.FormatoIndevidoException;
import com.example.agimob_v1.exceptions.ValorMenorIgualZeroException;
import com.example.agimob_v1.model.Simulacao;
import com.example.agimob_v1.model.Taxa;
import com.example.agimob_v1.model.Usuario;
import com.example.agimob_v1.repository.SimulacaoRepository;
import com.example.agimob_v1.repository.TaxaRepository;
import com.example.agimob_v1.repository.UsuarioRepository;
import com.example.agimob_v1.services.mappers.SimulacaoResponseMapper;
import com.example.agimob_v1.services.mappers.UsuarioDtoMapper;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SimulacaoService {

    private final SimulacaoRepository simulacaoRepository;
    private final UsuarioRepository usuarioRepository;
    private final UsuarioService usuarioService;
    private final CalculadoraSimulacaoService calculadoraSimulacaoService;
    private final TaxaRepository taxaRepository;
    private final UsuarioDtoMapper usuarioDtoMapper;
    private final SimulacaoResponseMapper simulacaoResponseMapper;
    private final EmailService emailService;
    private final String tipoTaxa = "AGIBANK";

    public int prazoConvertido(SimulacaoRequestDto simulacaoRequest) {
        return simulacaoRequest.prazo()*12;
    }

    public List<Simulacao> listarSimulacoes() {
        return simulacaoRepository.findAll();
    }

    public SimulacaoResponseDto novaSimulacao(SimulacaoRequestDto simulacaoRequest) throws RuntimeException {

        validarRequest(simulacaoRequest);

        Usuario usuario = usuarioRepository.findByEmail(simulacaoRequest.email()).orElseGet(() -> usuarioService.novoUsuario(simulacaoRequest));

        double valorFinanciamento = simulacaoRequest.valorTotal();
        double valorEntrada = simulacaoRequest.valorEntrada();
        int prazo = prazoConvertido(simulacaoRequest);
        double rendaUsuario = simulacaoRequest.rendaUsuario();
        double rendaParticipante = simulacaoRequest.rendaParticipante();
        Taxa taxa = taxaRepository.findVigenteByCodigo(tipoTaxa, LocalDateTime.now()).orElseThrow();
        String tipoSimulacao = simulacaoRequest.tipo();

        Simulacao simulacao = new Simulacao(valorFinanciamento, valorEntrada, prazo, rendaUsuario, rendaParticipante, taxa, usuario, tipoSimulacao);

        simulacaoRepository.save(simulacao);


        if (simulacaoRequest.tipo().equalsIgnoreCase("SAC")) {

           List<ParcelaDto> parcelas = calculadoraSimulacaoService.sac(simulacao);
           InformacoesAdicionaisDto informacoesAdicionais = calculadoraSimulacaoService.calcularInformacoesAdicionais(simulacao, parcelas);

          return simulacaoResponseMapper.toSacResponseDto(simulacao.getId(), tipoSimulacao, parcelas,informacoesAdicionais);

        } else if (simulacaoRequest.tipo().equalsIgnoreCase("PRICE")) {

            List<ParcelaDto> parcelas = calculadoraSimulacaoService.price(simulacao);
            InformacoesAdicionaisDto informacoesAdicionais = calculadoraSimulacaoService.calcularInformacoesAdicionais(simulacao, parcelas);

            return simulacaoResponseMapper.toPriceResponseDto(simulacao.getId(), tipoSimulacao, parcelas,informacoesAdicionais);

        } else{

            List<ParcelaDto> parcelasSac = calculadoraSimulacaoService.sac(simulacao);
            List<ParcelaDto> parcelasPrice = calculadoraSimulacaoService.price(simulacao);

            InformacoesAdicionaisDto informacoesAdicionaisSac = calculadoraSimulacaoService.calcularInformacoesAdicionais(simulacao,parcelasSac);
            InformacoesAdicionaisDto informacoesAdicionaisPrice = calculadoraSimulacaoService.calcularInformacoesAdicionais(simulacao,parcelasPrice);

            return simulacaoResponseMapper.toAmbosResponseDto(simulacao.getId(), tipoSimulacao, parcelasSac, parcelasPrice, informacoesAdicionaisSac, informacoesAdicionaisPrice);

        }


    }


    public UsuarioDto listarSimulacoesPorUsuarioId(String email) {

    Usuario usuario = usuarioRepository.findByEmail(email).orElseThrow();

    return usuarioDtoMapper.toDto(usuario);

    }

    private void validarRequest(SimulacaoRequestDto request){
        // INSERIR TAMBÉM EXEÇÕES PARA VALORES EXDRULOS COMO 100 ANOS... PARA NÃO QUEBRAR A MINHA API

        if(request.valorTotal() <=0){
            throw new ValorMenorIgualZeroException("O valor do financiamento não pode ser menor ou igual a ZERO!");
        }
        if(request.valorEntrada() <=0){
            throw new ValorMenorIgualZeroException("O valor do Valor de Entrada não pode ser menor ou igual a ZERO!");
        }
        if(request.prazo() <=0){
            throw new ValorMenorIgualZeroException("O Prazo não pode ser menor ou igual a ZERO!");
        }
        if(!request.tipo().equalsIgnoreCase("SAC") && !request.tipo().equalsIgnoreCase("PRICE") && !request.tipo().equalsIgnoreCase("AMBOS")){
            throw new FormatoIndevidoException("Modalidade inserida inválida, as modadaliades válidas são: SAC, PRICE e AMBOS");
        }

    }

}


