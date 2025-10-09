package com.example.agimob_v1.services;


import com.example.agimob_v1.dto.ParcelaDto;
import com.example.agimob_v1.model.Simulacao;
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
            List<ParcelaDto> parcelasSac = calculadoraSimulacao.calcularParcelas(simulacao).getParcelasSac();

            return gerarHtmlSacOuPrice(simulacao.getTipo_modalidade(), parcelasSac);
        }
        else if(modalidade.equalsIgnoreCase("PRICE")){
            List<ParcelaDto> parcelasPrice = calculadoraSimulacao.calcularParcelas(simulacao).getParcelasPrice();

            return gerarHtmlSacOuPrice(simulacao.getTipo_modalidade(), parcelasPrice);
        }
        List<ParcelaDto> parcelasSac = calculadoraSimulacao.calcularParcelas(simulacao).getParcelasSac();
        List<ParcelaDto> parcelasPrice = calculadoraSimulacao.calcularParcelas(simulacao).getParcelasPrice();

        return gerarHtmlSacEPrice(modalidade, parcelasSac, parcelasPrice);

    }


    private String gerarHtmlSacOuPrice(String modalidade, List<ParcelaDto> parcelas) {

        List<ParcelaDto> dezPrimeirasParcelas = parcelas.stream().limit(10).toList();

        Context context = new Context();
        context.setVariable("assunto", "Relatório de Simulacão");
        context.setVariable("tipoSimulacao", modalidade.toUpperCase());
        context.setVariable("listaParcelas", dezPrimeirasParcelas);

        return templateEngine.process("relatorio-simulacao-pdf", context);
    }

    private String gerarHtmlSacEPrice(String modalidade, List<ParcelaDto> parcelasSac, List<ParcelaDto> parcelasPrice) {

        List<ParcelaDto> dezPrimeirasParcelasSac = parcelasSac.stream().limit(10).toList();
        List<ParcelaDto> dezPrimeirasParcelasPrice = parcelasPrice.stream().limit(10).toList();

        Context context = new Context();
        context.setVariable("assunto", "Relatório de Simulacão");
        context.setVariable("tipoSimulacao", modalidade.toUpperCase());
        context.setVariable("listaParcelasSac", dezPrimeirasParcelasSac);
        context.setVariable("listaParcelasPrice", dezPrimeirasParcelasPrice);

        return templateEngine.process("relatorio-simulacao-pdf-ambos", context);
    }

}
