package com.example.agimob_v1.services;

import com.example.agimob_v1.dto.InformacoesAdicionaisDto;
import com.example.agimob_v1.dto.ParcelaDto;
import com.example.agimob_v1.dto.SimulacaoResponseDto;
import com.example.agimob_v1.model.Simulacao;
import com.example.agimob_v1.repository.SimulacaoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.function.EntityResponse;
import org.xml.sax.EntityResolver;

import javax.swing.text.html.parser.Entity;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EmailService {
    private final JavaMailSender mailSender;
    private final SimulacaoRepository simulacaoRepository;
    private final CalculadoraSimulacaoService calculadoraSimulacao;

    public ResponseEntity<Void> enviarEmail(String to, Long idSimulacao){
        SimpleMailMessage message = new SimpleMailMessage();
        Simulacao simulacao = simulacaoRepository.findById(idSimulacao).orElseThrow();

        message.setFrom("agimobdasilva@gmail.com");
        message.setTo(to);
        message.setSubject("Simulacao "+ LocalDateTime.now());
        message.setText(simulacao.toString());
        mailSender.send(message);

        return ResponseEntity.noContent().build();
    }

    public String gerarCorpoEmail(Simulacao simulacao){
        StringBuilder sb = new StringBuilder();
        List<ParcelaDto> pacelas = new ArrayList<>();

        if(simulacao.getTipo_modalidade().equalsIgnoreCase("sac")){
            pacelas = calculadoraSimulacao.sac(simulacao);
        } else if (simulacao.getTipo_modalidade().equalsIgnoreCase("price")) {
            pacelas = calculadoraSimulacao.sac(simulacao);
        }

        InformacoesAdicionaisDto infosAdicionais = calculadoraSimulacao.calcularInformacoesAdicionais(simulacao, pacelas);

        sb.append("Modalidade da simulação: "+simulacao.getTipo_modalidade());
        sb.append()

    }
}
