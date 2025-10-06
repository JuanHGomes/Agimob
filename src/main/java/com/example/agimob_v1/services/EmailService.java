package com.example.agimob_v1.services;

import com.example.agimob_v1.dto.InformacoesAdicionaisDto;
import com.example.agimob_v1.dto.ParcelaDto;
import com.example.agimob_v1.dto.SimulacaoResponseDto;
import com.example.agimob_v1.model.Simulacao;
import com.example.agimob_v1.repository.SimulacaoRepository;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
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
import org.xml.sax.EntityResolver;

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
    private final SimulacaoRepository simulacaoRepository;
    private final CalculadoraSimulacaoService calculadoraSimulacao;

    public ResponseEntity<Void> enviarEmail(String to, Long idSimulacao) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();

        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        Simulacao simulacao = simulacaoRepository.findById(idSimulacao).orElseThrow();

        String corpoEmail = gerarHtml(simulacao);

        helper.setFrom("agimobdasilva@gmail.com");
        helper.setTo(to);
        helper.setSubject("Simulacao AGIMOB "+ formatarData(LocalDateTime.now()));
        helper.setText(corpoEmail, true);
        mailSender.send(message);

        return ResponseEntity.noContent().build();
    }

    private String gerarHtml(Simulacao simulacao){

        List<ParcelaDto> dezPrimeirasParcelas = calculadoraSimulacao.sac(simulacao).stream().limit(10).toList();


        Context context = new Context();
        context.setVariable("assunto", "Relatório de Simulacão");
        context.setVariable("tipoSimulacao", simulacao.getTipo_modalidade().toUpperCase());
        context.setVariable("listaParcelas", dezPrimeirasParcelas);

        return templateEngine.process("relatorio-simulacao", context);
    }

    private String formatarData(LocalDateTime data){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        return data.format(formatter);
    }

}
