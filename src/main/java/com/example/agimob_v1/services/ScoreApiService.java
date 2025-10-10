package com.example.agimob_v1.services;


import com.example.agimob_v1.dto.ScoreDto;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class ScoreApiService {

    public ResponseEntity<ScoreDto> consultarScore(String cpf){

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders header = new HttpHeaders();

        header.set("X-API-KEY", "agireport");
        header.set("X-API-NAME", "agireport");

        int id = 2;

        HttpEntity<String> request = new HttpEntity<>(header);

        String url = "http://35.199.99.173:8080/analytic/{id}";

        ResponseEntity<ScoreDto> response = restTemplate.exchange(
                url,
                HttpMethod.POST,
                request,
                ScoreDto.class,
                id
                );

        return response;

    }

}
