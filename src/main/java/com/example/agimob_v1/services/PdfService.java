package com.example.agimob_v1.services;


import com.example.agimob_v1.dto.ParcelaDto;
import com.example.agimob_v1.model.Simulacao;
import lombok.RequiredArgsConstructor;
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
    private final CalculadoraSimulacaoService calculadoraSimulacaoService;


    public byte[] gerarRelatorioPdf(Simulacao simulacao){

            String corpoPdf = gerarHtml(simulacao);

            ByteArrayOutputStream os = new ByteArrayOutputStream();
            ITextRenderer renderer = new ITextRenderer();

            renderer.setDocumentFromString(corpoPdf);
            renderer.layout();
            renderer.createPDF(os);

            return os.toByteArray();

    }

    private String gerarHtml(Simulacao simulacao){

        List<ParcelaDto> dezPrimeirasParcelas = calculadoraSimulacaoService.sac(simulacao).stream().limit(10).toList();


        Context context = new Context();
        context.setVariable("assunto", "Relatório de Simulacão");
        context.setVariable("tipoSimulacao", simulacao.getTipo_modalidade().toUpperCase());
        context.setVariable("listaParcelas", dezPrimeirasParcelas);

        return templateEngine.process("relatorio-simulacao-pdf", context);
    }

}
