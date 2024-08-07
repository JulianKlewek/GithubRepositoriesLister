package com.example.infrastructure.githubservice;

import com.example.repositorieslistgenerator.GithubPort;
import com.example.infrastructure.githubservice.dto.Repository;
import com.example.repositorieslistgenerator.dto.RepositoryDetails;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.util.List;

@Service
class GithubApiPortHttpService implements GithubPort {

    private final GithubApiCallsGenerator githubApiCallsGenerator;

    public GithubApiPortHttpService(GithubApiCallsGenerator githubApiCallsGenerator) {
        this.githubApiCallsGenerator = githubApiCallsGenerator;
    }

    @Override
    public List<RepositoryDetails> fetchNotForkedRepositoriesWithDetails(String username) {
        List<Repository> repositories = githubApiCallsGenerator.fetchNotForkedUserRepositories(username);
        return Flux.fromIterable(repositories)
                .flatMap(githubApiCallsGenerator::fetchRepositoryDetails)
                .collectList()
                .block();
    }

}
