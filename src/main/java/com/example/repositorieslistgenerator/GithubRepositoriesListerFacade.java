package com.example.repositorieslistgenerator;

import com.example.repositorieslistgenerator.dto.RepositoryDetails;
import reactor.core.publisher.Flux;

class GithubRepositoriesListerFacade implements RepositoriesListerFacade {

    private final GithubPort githubPort;

    GithubRepositoriesListerFacade(GithubPort githubPort) {
        this.githubPort = githubPort;
    }

    @Override
    public Flux<RepositoryDetails> getUserRepositoriesWithDetails(String username) {
        return githubPort.fetchNotForkedRepositoriesWithDetails(username);
    }
}
