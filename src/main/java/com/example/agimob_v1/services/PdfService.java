package com.example.agimob_v1.services;


import com.example.agimob_v1.dto.InformacoesAdicionaisDto;
import com.example.agimob_v1.dto.ParcelaDto;
import com.example.agimob_v1.model.Simulacao;
import com.example.agimob_v1.model.Taxa;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.xhtmlrenderer.pdf.ITextRenderer;

import java.io.ByteArrayOutputStream;
import java.util.List;


@Service
@RequiredArgsConstructor
public class PdfService {

    private final TemplateEngine templateEngine;
    private final CalculadoraSimulacaoService calculadoraSimulacao;
    private final SimulacaoService simulacaoService;
    private final TaxaService taxaService;


    public byte[] gerarRelatorioPdf(Simulacao simulacao){

            String corpoPdf = gerarCorpoRelatorioPdf(simulacao);

            ByteArrayOutputStream os = new ByteArrayOutputStream();
            ITextRenderer renderer = new ITextRenderer();

            renderer.setDocumentFromString(corpoPdf);
            renderer.layout();
            renderer.createPDF(os);

            return os.toByteArray();

    }

    public ResponseEntity<byte[]> baixarPdf(Long idSimulacao){

        Simulacao simulacao = simulacaoService.localizarSimulacao(idSimulacao);

        byte[] relatorioPDf = gerarRelatorioPdf(simulacao);

        HttpHeaders headers = new HttpHeaders();

        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("attachment", "relatorio-"+simulacao.getRenda_participante()+"-"+simulacao.getId()+".pdf");

        return new ResponseEntity<>(relatorioPDf, headers, HttpStatus.OK);


    }

    private String gerarCorpoRelatorioPdf(Simulacao simulacao){

        String modalidade = simulacao.getTipo_modalidade();

        if(modalidade.equalsIgnoreCase("SAC")){
            List<ParcelaDto> parcelasSac = calculadoraSimulacao.calcularParcelas(simulacao).getSac().parcelas();

            return gerarHtmlSacOuPrice(simulacao, parcelasSac);
        }
        else if(modalidade.equalsIgnoreCase("PRICE")){
            List<ParcelaDto> parcelasPrice = calculadoraSimulacao.calcularParcelas(simulacao).getPrice().parcelas();

            return gerarHtmlSacOuPrice(simulacao, parcelasPrice);
        }
        List<ParcelaDto> parcelasSac = calculadoraSimulacao.calcularParcelas(simulacao).getSac().parcelas();
        List<ParcelaDto> parcelasPrice = calculadoraSimulacao.calcularParcelas(simulacao).getPrice().parcelas();

        return gerarHtmlSacEPrice(simulacao, parcelasSac, parcelasPrice);

    }


    private String gerarHtmlSacOuPrice(Simulacao simulacao, List<ParcelaDto> parcelas) {

        List<ParcelaDto> dezPrimeirasParcelas = parcelas.stream().toList();
        Taxa taxa = taxaService.getTaxaPadraoVigente();
        InformacoesAdicionaisDto infosAdicionais =  calculadoraSimulacao.calcularInformacoesAdicionais(simulacao,parcelas, taxa);

        Context context = new Context();
        context.setVariable("assunto", "Relatório de Simulacão");
        context.setVariable("tipoSimulacao", simulacao.getTipo_modalidade().toUpperCase());
        context.setVariable("listaParcelas", dezPrimeirasParcelas);
        context.setVariable("valorFinanciamento", simulacao.getValor_total());
        context.setVariable("taxaJuros", taxa.getValor_taxa());
        context.setVariable("numeroParcelas", simulacao.getPrazo());
        context.setVariable("totalJuros", infosAdicionais.valorTotalJuros());
        context.setVariable("valorTotal", infosAdicionais.valorTotalFinanciamento());



        return templateEngine.process("relatorio-simulacao-pdf", context);
    }

    private String gerarHtmlSacEPrice(Simulacao simulacao, List<ParcelaDto> parcelasSac, List<ParcelaDto> parcelasPrice) {
        Taxa taxa = taxaService.getTaxaPadraoVigente();
        List<ParcelaDto> dezPrimeirasParcelasSac = parcelasSac.stream().toList();
        List<ParcelaDto> dezPrimeirasParcelasPrice = parcelasPrice.stream().toList();
        InformacoesAdicionaisDto infosAdicionaisSac = calculadoraSimulacao.calcularInformacoesAdicionais(simulacao, parcelasSac, taxa);
        InformacoesAdicionaisDto infosAdicionaisPrice = calculadoraSimulacao.calcularInformacoesAdicionais(simulacao, parcelasPrice, taxa);


        Context context = new Context();
        context.setVariable("assunto", "Relatório de Simulacão");
        context.setVariable("tipoSimulacao", simulacao.getTipo_modalidade().toUpperCase());

        context.setVariable("listaParcelasSac", dezPrimeirasParcelasSac);
        context.setVariable("valorFinanciamento", simulacao.getValor_total());
        context.setVariable("taxaJuros", taxa.getValor_taxa());
        context.setVariable("numeroParcelas", simulacao.getPrazo());
        context.setVariable("totalJuros", infosAdicionaisSac.valorTotalJuros());
        context.setVariable("valorTotal", infosAdicionaisSac.valorTotalFinanciamento());
        context.setVariable("informacoesAdicionaisSac", infosAdicionaisSac);

        context.setVariable("listaParcelasPrice", dezPrimeirasParcelasPrice);
        context.setVariable("valorFinanciamento", simulacao.getValor_total());
        context.setVariable("taxaJuros", taxa.getValor_taxa());
        context.setVariable("numeroParcelas", simulacao.getPrazo());
        context.setVariable("totalJuros", infosAdicionaisPrice.valorTotalJuros());
        context.setVariable("valorTotal", infosAdicionaisPrice.valorTotalFinanciamento());
        context.setVariable("informacoesAdicionaisPrice", infosAdicionaisPrice);



        return templateEngine.process("relatorio-simulacao-pdf-ambos", context);
    }

}
