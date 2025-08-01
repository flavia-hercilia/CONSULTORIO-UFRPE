package br.ufrpe.consultorio.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    @Bean
    public WebClient professionalServiceWebClient() {
        return WebClient.builder()
                .baseUrl("http://professional-service-app:8082") 
                .build();
    }

    @Bean
    public WebClient patientServiceWebClient() {
        return WebClient.builder()
                .baseUrl("http://patient-service-app:8083") 
                .build();
    }
}
