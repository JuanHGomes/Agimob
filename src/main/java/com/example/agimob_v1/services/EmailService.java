package com.example.agimob_v1.services;

import com.example.agimob_v1.dto.InformacoesAdicionaisDto;
import com.example.agimob_v1.dto.ParcelaDto;
import com.example.agimob_v1.dto.SimulacaoResponseDto;
import com.example.agimob_v1.exceptions.SimulacaoNaoEncontradaException;
import com.example.agimob_v1.exceptions.UsuarioNaoEncontradoException;
import com.example.agimob_v1.model.Simulacao;
import com.example.agimob_v1.model.Usuario;
import com.example.agimob_v1.repository.SimulacaoRepository;
import com.example.agimob_v1.repository.UsuarioRepository;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.util.ByteArrayDataSource;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMailMessage;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.function.EntityResponse;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;


import javax.swing.text.html.parser.Entity;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EmailService {
    private final JavaMailSender mailSender;
    private final TemplateEngine templateEngine;
    private final CalculadoraSimulacaoService calculadoraSimulacao;
    private final UsuarioService usuarioService;
    private final SimulacaoService simulacaoService;
    private final PdfService pdfService;

    public ResponseEntity<Void> enviarEmail(String emailUsuario, Long idSimulacao) throws Exception {

        Usuario usuario = usuarioService.validarUsuario(emailUsuario);

        Simulacao simulacao = simulacaoService.localizarSimulacao(idSimulacao);

        simulacaoService.setUsuarioSimulacao(usuario, idSimulacao);

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        String corpoEmail = gerarCorpoEmail(simulacao);

        helper.setFrom("agimobdasilva@gmail.com");
        helper.setTo(emailUsuario);
        helper.setSubject("Simulação AGIMOB " + formatarData(LocalDateTime.now()));
        helper.setText(corpoEmail, true);
        helper.addAttachment("Simulação AGIMOB", new ByteArrayDataSource(pdfService.gerarRelatorioPdf(simulacao), "application/pdf"));

        mailSender.send(message);

        return ResponseEntity.noContent().build();
    }

    private String gerarCorpoEmail(Simulacao simulacao){

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

        return templateEngine.process("relatorio-simulacao-emailUsuario", context);
    }

    private String gerarHtmlSacEPrice(String
                                              modalidade, List<ParcelaDto> parcelasSac, List<ParcelaDto> parcelasPrice) {

        List<ParcelaDto> dezPrimeirasParcelasSac = parcelasSac.stream().limit(10).toList();
        List<ParcelaDto> dezPrimeirasParcelasPrice = parcelasPrice.stream().limit(10).toList();

        Context context = new Context();
        context.setVariable("assunto", "Relatório de Simulacão");
        context.setVariable("tipoSimulacao", modalidade.toUpperCase());
        context.setVariable("listaParcelasSac", dezPrimeirasParcelasSac);
        context.setVariable("listaParcelasPrice", dezPrimeirasParcelasPrice);

        return templateEngine.process("relatorio-simulacao-emailUsuario-ambos", context);
    }

    private String formatarData(LocalDateTime data) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        return data.format(formatter);
    }


}