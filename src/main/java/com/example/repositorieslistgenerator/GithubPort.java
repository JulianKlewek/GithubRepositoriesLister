package com.example.repositorieslistgenerator;

import com.example.repositorieslistgenerator.dto.RepositoryDetails;

import java.util.List;

public interface GithubPort {

    List<RepositoryDetails> fetchNotForkedRepositoriesWithDetails(String username);
}
