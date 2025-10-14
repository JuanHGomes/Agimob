package com.example.agimob_v1.config; // Ou o pacote de configuração que preferir

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig {

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**") // Aplica a configuração a todos os endpoints
                        .allowedOrigins("http://127.0.0.1:5500", "http://localhost:5500") // Permite as origens do seu frontend
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // Permite os métodos necessários, incluindo OPTIONS
                        .allowedHeaders("*") // Permite todos os cabeçalhos
                        .allowCredentials(true);
            }
        };
    }
}