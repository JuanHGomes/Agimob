package com.example.agimob_v1.services;

import com.example.agimob_v1.dto.ParcelaDto;
import com.example.agimob_v1.dto.SimulacaoRequestDTO;
import com.example.agimob_v1.dto.SimulacaoOutrosResponseDTO;
import com.example.agimob_v1.model.Simulacao;
import com.example.agimob_v1.model.Taxa;
import com.example.agimob_v1.model.TaxaAgibank;
import com.example.agimob_v1.model.TaxaOutros;
import com.example.agimob_v1.repository.SimulacaoRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
// COMO FAZER O SERVICE ENVIAR SOMENTE A SIMULACAO AGIBANK PRO BANCO?
//VALE A PENA MANTER O CALCULO DE OUTROS BANCOS NO BACKEND?


@Service
public class SimulacaoService {

    private final SimulacaoRepository repository;

    public SimulacaoService(SimulacaoRepository repository) {
        this.repository = repository;
    }

    public List<SimulacaoOutrosResponseDTO> listar() {
        return repository.findAll()
                .stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    public SimulacaoOutrosResponseDTO buscar(Long id) {
        Simulacao simulacao = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Simulacao nao encontrada"));
        return toResponseDTO(simulacao);
    }

    public SimulacaoOutrosResponseDTO salvar(SimulacaoRequestDTO dto) {
        Simulacao simulacao = new Simulacao();
        simulacao.setData(dto.getData());
        simulacao.setValor_total(dto.getValor_total());
        simulacao.setValor_entrada(dto.getValor_entrada());
        simulacao.setPrazo(dto.getPrazo());
        simulacao.setRenda_usuario(dto.getRenda_usuario());
        simulacao.setRenda_participante(dto.getRenda_participante());
        simulacao.setTipo_modalidade(dto.getTipo_modalidade());
        simulacao.setTaxa(dto.getTaxa());
        simulacao.setUsuario(dto.getUsuario());

        Simulacao add = repository.save(simulacao);
        return toResponseDTO(add);

    }

    public void deletar(Long id) {
        repository.deleteById(id);
    }


    //conversao para dto de resposta
    private SimulacaoOutrosResponseDTO toResponseDTO(Simulacao simulacao, List<ParcelaDto> sacAgibank,
                                                     List<ParcelaDto> priceAgibank,
                                                     List<ParcelaDto> sacOutros,
                                                     List<ParcelaDto> priceOutros,
                                                     Double totalJurosSacAgibank,
                                                     Double totalJurosPriceAgibank,
                                                     Double totalJurosSacOutros,
                                                     Double totalJurosPriceOutros) {

        SimulacaoOutrosResponseDTO dto = new SimulacaoOutrosResponseDTO();

        //resopsta pro frontend apos os calculos/
        dto.setSacAgibank(sacAgibank);
        dto.setPriceAgibank(priceAgibank);
        dto.setSacOutros(sacOutros);
        dto.setPriceOutros(priceOutros);

        dto.setTotalJurosSacAgibank(totalJurosSacAgibank);
        dto.setTotalJurosPriceAgibank(totalJurosPriceAgibank);
        dto.setTotalJurosSacOutros(totalJurosSacOutros);
        dto.setTotalJurosPriceOutros(totalJurosPriceOutros);

        return dto;
    }


//lógica dos calculos de simulacoes!!

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

        double parcelaMes = ((valorFinanciado * taxa) / (1 - Math.pow((1 + taxa), -prazo)));

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

//aqui esta a lógica do response que é o que vai ser devolvido para o front

    public SimulacaoOutrosResponseDTO simularComAgibank(Simulacao simulacao) {
        Taxa taxa = new TaxaAgibank();
        Double valorFinanciado = simulacao.getValor_total() - simulacao.getValor_entrada();

        //calcula a lista de parcelas de cada modalidade
        List<ParcelaDto> sac = calcularSac(valorFinanciado, taxa.getValor_taxa(), simulacao.getPrazo());
        List<ParcelaDto> price = calcularPrice(valorFinanciado, taxa.getValor_taxa(), simulacao.getPrazo());

        //calcula o total de juros de cada modalidade

        double totalJurosSac = sac.stream().mapToDouble(ParcelaDto::getValorJurosParcela).sum();
        double totalJurosPrice = price.stream().mapToDouble(ParcelaDto::getValorJurosParcela).sum();

        //retornando a resposta em dto com os calculos:

        return toResponseDTO(simulacao, sac, price, null, null, totalJurosSac, totalJurosPrice, null, null);

    }

    public SimulacaoOutrosResponseDTO simularComOutrosBancos(Simulacao simulacao) {
        Taxa taxa = new TaxaOutros();
        Double valorFinanciado = simulacao.getValor_total() - simulacao.getValor_entrada();

        //calcula a lista de parcelas em price com taxa de outrosbancos
        List<ParcelaDto> sac = calcularSac(valorFinanciado, taxa.getValor_taxa(), simulacao.getPrazo());
        List<ParcelaDto> price = calcularPrice(valorFinanciado, taxa.getValor_taxa(), simulacao.getPrazo());

        double totalJurosSac = sac.stream().mapToDouble(ParcelaDto::getValorJurosParcela).sum();
        double totalJurosPrice = price.stream().mapToDouble(ParcelaDto::getValorJurosParcela).sum();


        return toResponseDTO(simulacao, null, null, sac, price, null, null, totalJurosSac, totalJurosPrice);

    }

}
/*  //SE A TAXA FOR AGIBANK FAÇA:
        if (simulacao.getTaxa().getValor_taxa().equals(new TaxaAgibank().getValor_taxa())){
            dto = simularComAgibank(simulacao);
        }

        //SE A TAXA FOR OUTROS BANCOS FACA
        if(simulacao.getTaxa().getValor_taxa().equals(new TaxaOutros().getValor_taxa())){
            dto=simularComOutrosBancos(simulacao);
        }

        return dto;*/