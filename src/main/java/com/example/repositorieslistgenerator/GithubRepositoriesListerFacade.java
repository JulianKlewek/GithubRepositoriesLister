package com.example.repositorieslistgenerator;

import com.example.repositorieslistgenerator.dto.RepositoryDetails;

import java.util.List;

class GithubRepositoriesListerFacade implements RepositoriesListerFacade {

    private final GithubPort githubPort;

    GithubRepositoriesListerFacade(GithubPort githubPort) {
        this.githubPort = githubPort;
    }

    @Override
    public List<RepositoryDetails> getUserRepositoriesWithDetails(String username) {
        return githubPort.fetchNotForkedRepositoriesWithDetails(username);
    }
}
