package com.example.repositorieslistgenerator;

import com.example.repositorieslistgenerator.dto.Repository;
import com.example.repositorieslistgenerator.dto.RepositoryDetails;

import java.util.List;

class GithubRepositoriesListerFacade implements RepositoriesListerFacade {

    private final GithubPort githubPort;

    GithubRepositoriesListerFacade(GithubPort githubPort) {
        this.githubPort = githubPort;
    }

    @Override
    public List<RepositoryDetails> getUserRepositoriesWithDetails(String username) {
        List<Repository> repositories = githubPort.fetchNotForkedUserRepositories(username);
        return repositories.stream()
                .map(githubPort::fetchRepositoryDetails)
                .toList();
    }
}
