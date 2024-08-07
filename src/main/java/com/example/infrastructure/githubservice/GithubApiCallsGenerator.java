package com.example.infrastructure.githubservice;

import com.example.infrastructure.controller.error.NotExistingGithubUserException;
import com.example.infrastructure.githubservice.dto.Branch;
import com.example.repositorieslistgenerator.dto.BranchDetails;
import com.example.infrastructure.githubservice.dto.Repository;
import com.example.repositorieslistgenerator.dto.RepositoryDetails;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
class GithubApiCallsGenerator {

    private static final Logger logger = LoggerFactory.getLogger(GithubApiCallsGenerator.class);
    private final WebClient webClient;

    public GithubApiCallsGenerator(WebClient.Builder webClient, @Value("${github.baseUrl}") String githubBaseUrl) {
        this.webClient = webClient.baseUrl(githubBaseUrl).build();
    }

    public Flux<RepositoryDetails> fetchNotForkedUserRepositories(String username) {
        logger.info("Fetching repositories for : {}", username);
        return this.webClient.get()
                .uri("/users/{username}/repos", username)
                .retrieve()
                .bodyToFlux(Repository.class)
                .onErrorResume(WebClientResponseException.class, exc -> exc.getStatusCode() == HttpStatusCode.valueOf(404)
                        ? Mono.error(new NotExistingGithubUserException("Github username not found: " + username))
                        : Mono.error(new RuntimeException(exc.getMessage())))
                .filter(repository -> !repository.fork())
                .flatMap(this::fetchRepositoryDetails);
    }

    public Mono<RepositoryDetails> fetchRepositoryDetails(Repository repository) {
        String repositoryName = repository.name();
        String ownerName = repository.owner().login();
        logger.info("Fetching repository details for : {}", repositoryName);
        return this.webClient.get()
                .uri("/repos/{ownerName}/{repositoryName}/branches", ownerName, repositoryName)
                .retrieve()
                .bodyToFlux(Branch.class)
                .map(branch -> new BranchDetails(branch.name(), branch.commit().sha()))
                .collectList()
                .map(branches -> new RepositoryDetails(repositoryName, ownerName, branches));
    }
}