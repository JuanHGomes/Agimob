package com.example.agimob_v1.services;


import com.example.agimob_v1.dto.InformacoesAdicionaisDto;
import com.example.agimob_v1.dto.ParcelaDto;
import com.example.agimob_v1.dto.SimulacaoResponseDto;
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
    private final TaxaService taxaService;


    public byte[] gerarRelatorioPdf(SimulacaoResponseDto response){

            String corpoPdf = gerarCorpoRelatorioPdf(response);

            ByteArrayOutputStream os = new ByteArrayOutputStream();
            ITextRenderer renderer = new ITextRenderer();

            renderer.setDocumentFromString(corpoPdf);
            renderer.layout();
            renderer.createPDF(os);

            return os.toByteArray();

    }

    public ResponseEntity<byte[]> baixarPdf(SimulacaoResponseDto response){

        Simulacao simulacao = simulacaoService.localizarSimulacao(response.getId());

        byte[] relatorioPDf = gerarRelatorioPdf(response);

        HttpHeaders headers = new HttpHeaders();

        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("attachment", "relatorio-"+response.getTipo()+"-"+simulacao.getId()+".pdf");

        return new ResponseEntity<>(relatorioPDf, headers, HttpStatus.OK);


    }

    private String gerarCorpoRelatorioPdf(SimulacaoResponseDto response){

        String modalidade = response.getTipo();

        if(modalidade.equalsIgnoreCase("SAC")){

            return gerarHtmlSacOuPrice(response);
        }
        else if(modalidade.equalsIgnoreCase("PRICE")){

            return gerarHtmlSacOuPrice(response);
        }

        return gerarHtmlAmbos(response);

    }


    private String gerarHtmlSacOuPrice(SimulacaoResponseDto response) {


        List<ParcelaDto> parcelas;
        InformacoesAdicionaisDto informacoesAdicionais;

        if(response.getTipo().equalsIgnoreCase("SAC")){
            parcelas = response.getSac().parcelas();
            informacoesAdicionais = response.getSac().informacoesAdicionais();
        }
        else {
            parcelas = response.getPrice().parcelas();
            informacoesAdicionais = response.getPrice().informacoesAdicionais();
        }

        Context context = new Context();
        context.setVariable("resposne", response);
        context.setVariable("tipoUsuario", response.getTipoUsuario());
        context.setVariable("assunto", "Relatório de Simulacão");
        context.setVariable("tipoSimulacao", response.getTipo().toUpperCase());
        context.setVariable("listaParcelas", parcelas);
        context.setVariable("informacoesAdicionais", informacoesAdicionais);
        context.setVariable("numeroParcelas",  parcelas.getLast().getNumeroParcela());
        context.setVariable("valorFinanciamento", informacoesAdicionais.valorTotalFinanciamento()-informacoesAdicionais.valorTotalJuros());

        return templateEngine.process("relatorio-simulacao-pdf", context);
    }

    private String gerarHtmlAmbos(SimulacaoResponseDto response) {

        List<ParcelaDto> parcelasSac = response.getSac().parcelas();
        List<ParcelaDto> parcelasPrice = response.getPrice().parcelas();
        InformacoesAdicionaisDto informacoesAdicionaisSac = response.getSac().informacoesAdicionais();
        InformacoesAdicionaisDto informacoesAdicionaisPrice = response.getPrice().informacoesAdicionais();

        Context context = new Context();

        context.setVariable("response", response);
        context.setVariable("assunto", "Relatório de Simulacão");
        context.setVariable("tipoUsuario", response.getTipoUsuario());
        context.setVariable("tipoSimulacao", response.getTipo().toUpperCase());
        context.setVariable("listaParcelasSac", parcelasSac);
        context.setVariable("listaParcelasPrice", parcelasPrice);
        context.setVariable("informacoesAdicionaisSac", informacoesAdicionaisSac);
        context.setVariable("informacoesAdicionaisPrice", informacoesAdicionaisPrice);
        context.setVariable("numeroParcelas",  parcelasSac.getLast().getNumeroParcela());
        double valorFinanciamento = informacoesAdicionaisSac.valorTotalFinanciamento()-informacoesAdicionaisSac.valorTotalJuros();
        context.setVariable("valorFinanciamento", valorFinanciamento);

        return templateEngine.process("relatorio-simulacao-pdf-ambos", context);
    }

}
