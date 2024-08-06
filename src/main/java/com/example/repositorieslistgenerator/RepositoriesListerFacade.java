package com.example.repositorieslistgenerator;

import com.example.repositorieslistgenerator.dto.RepositoryDetails;

import java.util.List;

public interface RepositoriesListerFacade {

    List<RepositoryDetails> getUserRepositoriesWithDetails(String username);
}
