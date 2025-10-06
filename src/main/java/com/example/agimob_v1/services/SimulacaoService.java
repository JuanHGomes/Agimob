package com.example.agimob_v1.services;

import com.example.agimob_v1.dto.*;
import com.example.agimob_v1.exceptions.ValorMenorIgualZeroException;
import com.example.agimob_v1.model.Simulacao;
import com.example.agimob_v1.model.Taxa;
import com.example.agimob_v1.model.Usuario;
import com.example.agimob_v1.repository.SimulacaoRepository;
import com.example.agimob_v1.repository.TaxaRepository;
import com.example.agimob_v1.repository.UsuarioRepository;
import com.example.agimob_v1.services.mappers.SimulacaoResponseMapper;
import com.example.agimob_v1.services.mappers.UsuarioDtoMapper;
import lombok.AllArgsConstructor;

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
        simulacaoRequest.setPrazo(simulacaoRequest.getPrazo() * 12);

        return simulacaoRequest.getPrazo();
    }

    public List<Simulacao> listarSimulacoes() {
        return simulacaoRepository.findAll();
    }

    public SimulacaoResponseDto novaSimulacao(SimulacaoRequestDto simulacaoRequest) throws Exception {

        Usuario usuario = usuarioRepository.findByEmail(simulacaoRequest.getEmail()).orElseGet(() -> usuarioService.novoUsuario(simulacaoRequest));

        double valorFinanciamento = simulacaoRequest.getValorTotal();
        if(valorFinanciamento <= 0){
            throw new ValorMenorIgualZeroException("O valor do financiamento não pode ser menor ou igual a zero!");
        }
        double valorEntrada = simulacaoRequest.getValorEntrada();
        int prazo = prazoConvertido(simulacaoRequest);
        double rendaUsuario = simulacaoRequest.getRendaUsuario();
        double rendaParticipante = simulacaoRequest.getRendaParticipante();
        Taxa taxa = taxaRepository.findVigenteByCodigo(tipoTaxa, LocalDateTime.now()).orElseThrow();
        String tipoSimulacao = simulacaoRequest.getTipo();

        Simulacao simulacao = new Simulacao(valorFinanciamento, valorEntrada, prazo, rendaUsuario, rendaParticipante, taxa, usuario, tipoSimulacao);

        simulacaoRepository.save(simulacao);


        if (simulacaoRequest.getTipo().equalsIgnoreCase("SAC")) {

           List<ParcelaDto> parcelas = calculadoraSimulacaoService.sac(simulacao);
           InformacoesAdicionaisDto informacoesAdicionais = calculadoraSimulacaoService.calcularInformacoesAdicionais(simulacao, parcelas);

          return simulacaoResponseMapper.toSacResponseDto(simulacao.getId(), tipoSimulacao, parcelas,informacoesAdicionais);

        } else if (simulacaoRequest.getTipo().equalsIgnoreCase("PRICE")) {

            List<ParcelaDto> parcelas = calculadoraSimulacaoService.price(simulacao);
            InformacoesAdicionaisDto informacoesAdicionais = calculadoraSimulacaoService.calcularInformacoesAdicionais(simulacao, parcelas);

            return simulacaoResponseMapper.toPriceResponseDto(simulacao.getId(), tipoSimulacao, parcelas,informacoesAdicionais);

        } else if (simulacaoRequest.getTipo().equalsIgnoreCase("AMBOS")) {

            List<ParcelaDto> parcelasSac = calculadoraSimulacaoService.sac(simulacao);
            List<ParcelaDto> parcelasPrice = calculadoraSimulacaoService.price(simulacao);

            InformacoesAdicionaisDto informacoesAdicionaisSac = calculadoraSimulacaoService.calcularInformacoesAdicionais(simulacao,parcelasSac);
            InformacoesAdicionaisDto informacoesAdicionaisPrice = calculadoraSimulacaoService.calcularInformacoesAdicionais(simulacao,parcelasPrice);

            return simulacaoResponseMapper.toAmbosResponseDto(simulacao.getId(), tipoSimulacao, parcelasSac, parcelasPrice, informacoesAdicionaisSac, informacoesAdicionaisPrice);

        }

        throw new Exception();
    }


    public UsuarioDto listarSimulacoesPorUsuarioId(String email) {

    Usuario usuario = usuarioRepository.findByEmail(email).orElseThrow();

    return usuarioDtoMapper.toDto(usuario);

    }

}


