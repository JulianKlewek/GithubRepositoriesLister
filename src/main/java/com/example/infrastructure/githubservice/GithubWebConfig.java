package com.example.infrastructure.githubservice;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
class GithubWebConfig {

    @Bean
    public WebClient.Builder webClient() {
        return WebClient.builder();
    }
}
