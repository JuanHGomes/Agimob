package com.example.agimob_v1.services;

import com.example.agimob_v1.dto.InformacoesAdicionaisDto;
import com.example.agimob_v1.dto.ParcelaDto;
import com.example.agimob_v1.dto.SimulacaoRequestDto;
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
    private final CalculadoraSimulacaoService calculadoraSimulacao;
    private final UsuarioService usuarioService;
    private final SimulacaoService simulacaoService;
    private final PdfService pdfService;

    public ResponseEntity<Void> enviarEmail(String emailUsuario, Long idSimulacao) throws Exception {

        Usuario usuario = usuarioService.validarUsuarioPeloEmail(emailUsuario);

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

        List<ParcelaDto> dezPrimeirasParcelas = parcelas.stream().limit(10).toList();

        Context context = new Context();
        context.setVariable("assunto", "Relatório de Simulacão");
        context.setVariable("tipoSimulacao", simulacao.getTipo_modalidade().toUpperCase());
        context.setVariable("listaParcelas", dezPrimeirasParcelas);

        if(simulacao.getTipoUsuario().equalsIgnoreCase("CLIENTE")){
            InformacoesAdicionaisDto infosClienteAgi = calculadoraSimulacao.calcularInformacoesClienteAgi(simulacao, simulacao.getTipo_modalidade());

            context.setVariable("dadosClienteAgi", infosClienteAgi);
        }

        return templateEngine.process("relatorio-simulacao-email", context);
    }

    private String gerarHtmlSacEPrice(Simulacao simulacao, List<ParcelaDto> parcelasSac, List<ParcelaDto> parcelasPrice) {

        List<ParcelaDto> dezPrimeirasParcelasSac = parcelasSac.stream().limit(10).toList();
        List<ParcelaDto> dezPrimeirasParcelasPrice = parcelasPrice.stream().limit(10).toList();

        Context context = new Context();
        context.setVariable("assunto", "Relatório de Simulacão");
        context.setVariable("tipoSimulacao", simulacao.getTipo_modalidade().toUpperCase());
        context.setVariable("listaParcelasSac", dezPrimeirasParcelasSac);
        context.setVariable("listaParcelasPrice", dezPrimeirasParcelasPrice);

        if (simulacao.getTipoUsuario().equalsIgnoreCase("CLIENTE")){
            InformacoesAdicionaisDto infosAdicionaisClienteAgiSac = calculadoraSimulacao.calcularInformacoesClienteAgi(simulacao, "SAC");
            InformacoesAdicionaisDto infosAdicionaisClienteAgiPrice = calculadoraSimulacao.calcularInformacoesClienteAgi(simulacao, "PRICE");

            context.setVariable("dadosClienteAgiSac", infosAdicionaisClienteAgiSac);
            context.setVariable("dadosClienteAgiPrice", infosAdicionaisClienteAgiPrice);
        }

        return templateEngine.process("relatorio-simulacao-email-ambos", context);
    }

    private String formatarData(LocalDateTime data) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        return data.format(formatter);
    }


}