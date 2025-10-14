package com.example.agimob_v1.services;

import java.util.concurrent.CompletableFuture;
import com.example.agimob_v1.dto.ScoreDto;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Random;

@Service
public class ScoreApiService {

    private final UsuarioService usuarioService;
    private final RestTemplate restTemplate;
    private final HttpHeaders headers;

    public ScoreApiService(UsuarioService usuarioService, RestTemplate restTemplate) {
        this.usuarioService = usuarioService;
        this.restTemplate=restTemplate;

        this.headers = new HttpHeaders();
        this.headers.set("X-API-KEY", "agireport");
        this.headers.set("X-API-NAME", "agireport");
    }


    public CompletableFuture<ResponseEntity<ScoreDto>> consultarScore(String cpf){

        int id = usuarioService.localizarUsuarioPorCpf(cpf).getIdScore();

        HttpEntity<String> request = new HttpEntity<>(headers);

        String url = "http://35.199.99.173:8080/analytic/{id}";

        ResponseEntity<ScoreDto> response = restTemplate.exchange(
                url,
                HttpMethod.POST,
                request,
                ScoreDto.class,
                id
                );

        return CompletableFuture.completedFuture(response);

    }

}
