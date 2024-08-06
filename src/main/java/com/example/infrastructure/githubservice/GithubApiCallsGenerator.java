package com.example.infrastructure.githubservice;

import com.example.repositorieslistgenerator.GithubPort;
import com.example.infrastructure.controller.error.NotExistingGithubUserException;
import com.example.repositorieslistgenerator.dto.Branch;
import com.example.repositorieslistgenerator.dto.BranchDetails;
import com.example.repositorieslistgenerator.dto.Repository;
import com.example.repositorieslistgenerator.dto.RepositoryDetails;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
class GithubApiCallsGenerator implements GithubPort {

    private static final Logger logger = LoggerFactory.getLogger(GithubApiCallsGenerator.class);
    private final WebClient webClient;

    public GithubApiCallsGenerator(WebClient.Builder webClient, @Value("${github.baseUrl}") String githubBaseUrl) {
        this.webClient = webClient.baseUrl(githubBaseUrl).build();
    }

    public List<Repository> fetchNotForkedUserRepositories(String username) {
        logger.info("Fetching repositories for : {}", username);
        return webClient.get()
                .uri("/users/{username}/repos", username)
                .retrieve()
                .bodyToFlux(Repository.class)
                .onErrorResume(WebClientResponseException.class, exc -> exc.getStatusCode() == HttpStatusCode.valueOf(404)
                        ? Mono.error(new NotExistingGithubUserException("Github username not found: " + username))
                        : Mono.error(new RuntimeException(exc.getMessage())))
                .filter(repository -> !repository.fork())
                .collectList()
                .block();
    }

    public RepositoryDetails fetchRepositoryDetails(Repository repository) {
        String repositoryName = repository.name();
        String ownerName = repository.owner().login();
        logger.info("Fetching repository details for : {}", repositoryName);
        List<BranchDetails> branchDetailsList = this.webClient.get()
                .uri("/repos/{ownerName}/{repositoryName}/branches", ownerName, repositoryName)
                .retrieve()
                .bodyToFlux(Branch.class)
                .map(branch -> new BranchDetails(branch.name(), branch.commit().sha()))
                .collectList()
                .block();
        return new RepositoryDetails(repositoryName, ownerName, branchDetailsList);
    }
}