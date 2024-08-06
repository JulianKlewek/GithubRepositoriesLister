package com.example.repositorieslistgenerator;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class GithubListerConfiguration {

    @Bean
    public RepositoriesListerFacade createGithubRepositoriesListerFacade(GithubPort githubPort) {
        return new GithubRepositoriesListerFacade(githubPort);
    }

}
