package com.example.agimob_v1.services;

import com.example.agimob_v1.dto.*;
import com.example.agimob_v1.exceptions.FormatoIndevidoException;
import com.example.agimob_v1.exceptions.SimulacaoNaoEncontradaException;
import com.example.agimob_v1.exceptions.ValorForaDaFaixaException;
import com.example.agimob_v1.exceptions.ValorMenorIgualZeroException;
import com.example.agimob_v1.model.Simulacao;
import com.example.agimob_v1.model.Taxa;
import com.example.agimob_v1.model.Usuario;
import com.example.agimob_v1.repository.SimulacaoRepository;
import com.example.agimob_v1.repository.TaxaRepository;
import com.example.agimob_v1.services.mappers.SimulacaoResponseMapper;


import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SimulacaoService {

    private final SimulacaoRepository simulacaoRepository;
    private final CalculadoraSimulacaoService calculadoraSimulacaoService;
    private final TaxaRepository taxaRepository;
    private final SimulacaoResponseMapper simulacaoResponseMapper;
    private final UsuarioService usuarioService;
    private final TaxaService taxaService;
    private final String taxaPadrao = "AGIBANK";

    public int prazoConvertido(SimulacaoRequestDto simulacaoRequest) {
        return simulacaoRequest.prazo()*12;
    }

    public List<Simulacao> listarSimulacoes() {
        return simulacaoRepository.findAll();
    }

    public SimulacaoResponseDto novaSimulacao(SimulacaoRequestDto simulacaoRequest) throws RuntimeException {

        validarRequest(simulacaoRequest);

        boolean clienteAgi =  usuarioService.validarSeClienteAgi(simulacaoRequest.emailUsuario(), simulacaoRequest.cpfUsuario());

        Taxa taxa;

        if(clienteAgi){
          taxa = taxaService.determinarTaxaPorScore(simulacaoRequest.cpfUsuario());
        }
        else{
            taxa = taxaRepository.findVigenteByCodigo(taxaPadrao, LocalDateTime.now()).orElseThrow();
        }

        double valorFinanciamento = simulacaoRequest.valorTotal();
        double valorEntrada = simulacaoRequest.valorEntrada();
        int prazo = prazoConvertido(simulacaoRequest);
        double rendaUsuario = simulacaoRequest.rendaUsuario();
        double rendaParticipante = simulacaoRequest.rendaParticipante();




        String tipoSimulacao = simulacaoRequest.tipo();

        Simulacao simulacao = new Simulacao(valorFinanciamento, valorEntrada, prazo, rendaUsuario, rendaParticipante, taxa, tipoSimulacao);

        simulacaoRepository.save(simulacao);

        return calculadoraSimulacaoService.calcularParcelas(simulacao);

    }


    private void validarRequest(SimulacaoRequestDto request){

        if(request.valorTotal() <=0){
            throw new ValorMenorIgualZeroException("O valor do financiamento não pode ser menor ou igual a ZERO!");
        }
        if(request.valorEntrada() <=0){
            throw new ValorMenorIgualZeroException("O valor do Valor de Entrada não pode ser menor ou igual a ZERO!");
        }
        if(!request.tipo().equalsIgnoreCase("SAC") && !request.tipo().equalsIgnoreCase("PRICE") && !request.tipo().equalsIgnoreCase("AMBOS")){
            throw new FormatoIndevidoException("Modalidade inserida inválida, as modadaliades válidas são: SAC, PRICE e AMBOS");
        }
        if(request.prazo() > 35 || request.prazo() < 1){
            throw new ValorForaDaFaixaException("O Prazo precisa estar entre 1 e 35 anos!");
        }

    }

    public Simulacao localizarSimulacao(Long idSimulacao){
       return simulacaoRepository.findById(idSimulacao).orElseThrow(() -> new SimulacaoNaoEncontradaException("A simulação com o ID "+idSimulacao+" não foi localizada..."));
    }

    public void setUsuarioSimulacao(Usuario usuario, Long idSimulacao){

        Simulacao simulacao = localizarSimulacao(idSimulacao);

        simulacao.setUsuario(usuario);

       simulacaoRepository.save(simulacao);
    }

}


