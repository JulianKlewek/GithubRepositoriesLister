package com.example.infrastructure.controller;

import com.example.repositorieslistgenerator.RepositoriesListerFacade;
import com.example.repositorieslistgenerator.dto.RepositoryDetails;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequestMapping("/github")
class GithubRestController {

    private final RepositoriesListerFacade repositoriesListerFacade;

    public GithubRestController(RepositoriesListerFacade repositoriesListerFacade) {
        this.repositoriesListerFacade = repositoriesListerFacade;
    }

    @GetMapping(value = "/get-repos/{username}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<List<RepositoryDetails>>> getRepositoriesWithBranches(@PathVariable("username") String username) {
        return repositoriesListerFacade.getUserRepositoriesWithDetails(username)
                .collectList()
                .map(repos -> ResponseEntity.status(HttpStatus.OK)
                        .body(repos))
                .defaultIfEmpty(ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .build());
    }
}
