package com.example.repositorieslistgenerator;

import com.example.repositorieslistgenerator.dto.Repository;
import com.example.repositorieslistgenerator.dto.RepositoryDetails;

import java.util.List;

public interface GithubPort {

    List<Repository> fetchNotForkedUserRepositories(String username);

    RepositoryDetails fetchRepositoryDetails(Repository repository);
}
