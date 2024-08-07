package com.example.infrastructure.githubservice;

import com.example.repositorieslistgenerator.GithubPort;
import com.example.repositorieslistgenerator.dto.RepositoryDetails;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@Service
class GithubApiPortHttpService implements GithubPort {

    private final GithubApiCallsGenerator githubApiCallsGenerator;

    public GithubApiPortHttpService(GithubApiCallsGenerator githubApiCallsGenerator) {
        this.githubApiCallsGenerator = githubApiCallsGenerator;
    }

    @Override
    public Flux<RepositoryDetails> fetchNotForkedRepositoriesWithDetails(String username) {
        return githubApiCallsGenerator.fetchNotForkedUserRepositories(username);
    }

}
