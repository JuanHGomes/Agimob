package com.example.agimob_v1.services;

import com.example.agimob_v1.dto.ParcelaDto;
import com.example.agimob_v1.dto.SimulacaoRequestDTO;
import com.example.agimob_v1.dto.SimulacaoResponseDTO;
import com.example.agimob_v1.model.Simulacao;
import com.example.agimob_v1.model.Taxa;
import com.example.agimob_v1.model.TaxaAgibank;
import com.example.agimob_v1.model.TaxaOutros;
import com.example.agimob_v1.repository.SimulacaoRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SimulacaoService {

    private final SimulacaoRepository repository;

    public SimulacaoService(SimulacaoRepository repository) {
        this.repository = repository;
    }


    public SimulacaoResponseDTO salvar(SimulacaoRequestDTO dto) {
        Simulacao simulacao = new Simulacao();
        simulacao.setValor_total(dto.getValor_total());
        simulacao.setValor_entrada(dto.getValor_entrada());
        simulacao.setPrazo(dto.getPrazo());
        simulacao.setTipo_modalidade(dto.getTipo_modalidade());
        simulacao.setRenda_usuario(dto.getRenda_usuario());
        simulacao.setRenda_participante(dto.getRenda_participante());
        simulacao.setTaxa(new TaxaAgibank()); // sempre Agibank salva no banco
        if(dto.getUsuario().getIdUsuario() != null && dto.getUsuario().getEmail() != null){
            simulacao.setUsuario(dto.getUsuario());
        }
        Simulacao salvo = repository.save(simulacao);

        // retorna apenas dados básicos, sem calcular parcelas
        return toResponseDTOBasico(salvo);
    }


    public List<SimulacaoResponseDTO> listar() {
        return repository.findAll()
                .stream()
                .map(this::toResponseDTOBasico)
                .collect(Collectors.toList());
    }

    public SimulacaoResponseDTO buscar(Long id) {
        Simulacao simulacao = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Simulação não encontrada"));
        return toResponseDTOBasico(simulacao);
    }

    public void deletar(Long id) {
        repository.deleteById(id);
    }

    //CALCULAR RESULTADO DETALHADO

    public SimulacaoResponseDTO calcularResultado(Simulacao simulacao) {
        double valorFinanciado = simulacao.getValor_total() - simulacao.getValor_entrada();

        Taxa taxa = new TaxaAgibank();

        List<ParcelaDto> sac = calcularSac(valorFinanciado, taxa.getValor_taxa(), simulacao.getPrazo());
        List<ParcelaDto> price = calcularPrice(valorFinanciado, taxa.getValor_taxa(), simulacao.getPrazo());

        double totalJurosSac = sac.stream().mapToDouble(ParcelaDto::getValorJurosParcela).sum();
        double totalJurosPrice = price.stream().mapToDouble(ParcelaDto::getValorJurosParcela).sum();

        return toResponseDTOComResultado(simulacao, sac, price, totalJurosSac, totalJurosPrice);
    }

    public SimulacaoResponseDTO calcularResultadoOutrosBancos(Simulacao simulacao) {
        double valorFinanciado = simulacao.getValor_total() - simulacao.getValor_entrada();

        Taxa taxa = new TaxaOutros();

        List<ParcelaDto> sac = calcularSac(valorFinanciado, taxa.getValor_taxa(), simulacao.getPrazo());
        List<ParcelaDto> price = calcularPrice(valorFinanciado, taxa.getValor_taxa(), simulacao.getPrazo());

        double totalJurosSac = sac.stream().mapToDouble(ParcelaDto::getValorJurosParcela).sum();
        double totalJurosPrice = price.stream().mapToDouble(ParcelaDto::getValorJurosParcela).sum();

        return toResponseDTOComResultado(simulacao, sac, price, totalJurosSac, totalJurosPrice);

    }
//SOCORRODEUS

    private SimulacaoResponseDTO toResponseDTOBasico(Simulacao simulacao) {
        SimulacaoResponseDTO dto = new SimulacaoResponseDTO();
        dto.setId(simulacao.getIdSimulacao());
        dto.setData(simulacao.getData());
        dto.setPrazo(simulacao.getPrazo());
        dto.setTipo_modalidade(simulacao.getTipo_modalidade());
        dto.setTaxa(simulacao.getTaxa());
        return dto;
    }

    private SimulacaoResponseDTO toResponseDTOComResultado(Simulacao simulacao,
                                                           List<ParcelaDto> sac,
                                                           List<ParcelaDto> price,
                                                           double totalJurosSac,
                                                           double totalJurosPrice) {
        SimulacaoResponseDTO dto = toResponseDTOBasico(simulacao);

        dto.setSacAgibank(sac);
        dto.setPriceAgibank(price);
        dto.setTotalJurosSacAgibank(totalJurosSac);
        dto.setTotalJurosPriceAgibank(totalJurosPrice);

        return dto;
    }


    private List<ParcelaDto> calcularSac(Double valorFinanciado, Double taxa, Integer prazo) {
        List<ParcelaDto> parcelas = new ArrayList<>();
        double amortizacao = valorFinanciado / prazo;

        for (int i = 1; i <= prazo; i++) {
            double juros = valorFinanciado * taxa;
            double parcela = amortizacao + juros;
            valorFinanciado -= amortizacao;

            ParcelaDto p = new ParcelaDto();
            p.setNumeroParcela(i);
            p.setAmortizacao(amortizacao);
            p.setValorJurosParcela(juros);
            p.setValorTotalParcela(parcela);
            p.setSaldoDevedor(Math.max(valorFinanciado, 0));

            parcelas.add(p);
        }
        return parcelas;
    }

    private List<ParcelaDto> calcularPrice(Double valorFinanciado, Double taxa, Integer prazo) {
        List<ParcelaDto> parcelas = new ArrayList<>();
        double parcelaMes = (valorFinanciado * taxa) / (1 - Math.pow(1 + taxa, -prazo));

        for (int i = 1; i <= prazo; i++) {
            double juros = valorFinanciado * taxa;
            double amortizacao = parcelaMes - juros;
            valorFinanciado -= amortizacao;

            ParcelaDto p = new ParcelaDto();
            p.setNumeroParcela(i);
            p.setAmortizacao(amortizacao);
            p.setValorJurosParcela(juros);
            p.setValorTotalParcela(parcelaMes);
            p.setSaldoDevedor(Math.max(valorFinanciado, 0));

            parcelas.add(p);
        }
        return parcelas;
    }
}
