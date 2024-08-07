package com.example.repositorieslistgenerator;

import com.example.repositorieslistgenerator.dto.RepositoryDetails;
import reactor.core.publisher.Flux;

public interface GithubPort {

    Flux<RepositoryDetails> fetchNotForkedRepositoriesWithDetails(String username);
}
