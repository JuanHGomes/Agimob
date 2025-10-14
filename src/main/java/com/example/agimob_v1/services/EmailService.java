package com.example.agimob_v1.services;

import com.example.agimob_v1.dto.InformacoesAdicionaisDto;
import com.example.agimob_v1.dto.ParcelaDto;
import com.example.agimob_v1.dto.SimulacaoRequestDto;
import com.example.agimob_v1.dto.SimulacaoResponseDto;
import com.example.agimob_v1.model.Simulacao;
import com.example.agimob_v1.model.Usuario;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.util.ByteArrayDataSource;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;


import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EmailService {
    private final JavaMailSender mailSender;
    private final TemplateEngine templateEngine;
    private final UsuarioService usuarioService;
    private final SimulacaoService simulacaoService;
    private final PdfService pdfService;

    public ResponseEntity<Void> enviarEmail(String  emailUsuario, SimulacaoResponseDto response) throws Exception {

        Usuario usuario = usuarioService.validarUsuarioPeloEmail(emailUsuario);

        Simulacao simulacao = simulacaoService.localizarSimulacao(response.getId());

        simulacaoService.setUsuarioSimulacao(usuario, response.getId());

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        String corpoEmail = gerarCorpoEmail(response, simulacao.getTipoUsuario());

        helper.setFrom("agimobdasilva@gmail.com");
        helper.setTo(emailUsuario);
        helper.setSubject("Simulação AGIMOB " + formatarData(LocalDateTime.now()));
        helper.setText(corpoEmail, true);
        helper.addAttachment("Simulação AGIMOB", new ByteArrayDataSource(pdfService.gerarRelatorioPdf(response), "application/pdf"));

        mailSender.send(message);

        return ResponseEntity.noContent().build();
    }

    private String gerarCorpoEmail(SimulacaoResponseDto response, String tipoUsuario){

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

        List<ParcelaDto> dezPrimeirasParcelas;
        InformacoesAdicionaisDto informacoesAdicionais;

        if(response.getTipo().equalsIgnoreCase("SAC")){
            dezPrimeirasParcelas = response.getSac().parcelas().stream().limit(10).toList();
            informacoesAdicionais = response.getSac().informacoesAdicionais();
        }
        else {
            dezPrimeirasParcelas = response.getPrice().parcelas().stream().limit(10).toList();
            informacoesAdicionais = response.getPrice().informacoesAdicionais();
        }

        Context context = new Context();
        context.setVariable("resposne", response);
        context.setVariable("tipoUsuario", response.getTipoUsuario());
        context.setVariable("assunto", "Relatório de Simulacão");
        context.setVariable("tipoSimulacao", response.getTipo().toUpperCase());
        context.setVariable("listaParcelas", dezPrimeirasParcelas);
        context.setVariable("informacoesAdicionais", informacoesAdicionais);

        return templateEngine.process("relatorio-simulacao-email", context);
    }

    private String gerarHtmlAmbos(SimulacaoResponseDto response) {

        List<ParcelaDto> dezPrimeirasParcelasSac = response.getSac().parcelas().stream().limit(10).toList();
        List<ParcelaDto> dezPrimeirasParcelasPrice = response.getPrice().parcelas().stream().limit(10).toList();
        InformacoesAdicionaisDto infosAdicionaisSac = response.getSac().informacoesAdicionais();
        InformacoesAdicionaisDto infosAdicionaisPrice = response.getPrice().informacoesAdicionais();

        Context context = new Context();

        context.setVariable("response", response);
        context.setVariable("assunto", "Relatório de Simulacão");
        context.setVariable("tipoUsuario", response.getTipoUsuario());
        context.setVariable("tipoSimulacao", response.getTipo().toUpperCase());
        context.setVariable("listaParcelasSac", dezPrimeirasParcelasSac);
        context.setVariable("listaParcelasPrice", dezPrimeirasParcelasPrice);
        context.setVariable("informacoesAdicionaisSac", infosAdicionaisSac);
        context.setVariable("informacoesAdicionaisPrice", infosAdicionaisPrice);

        return templateEngine.process("relatorio-simulacao-email-ambos", context);
    }

    private String formatarData(LocalDateTime data) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        return data.format(formatter);
    }


}