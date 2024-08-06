package com.example.repositorieslistgenerator.dto;

import java.util.List;

public record RepositoryDetails(String repositoryName, String ownerLogin, List<BranchDetails> branches) {
}
