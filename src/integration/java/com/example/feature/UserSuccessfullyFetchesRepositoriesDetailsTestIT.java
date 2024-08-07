package com.example.feature;

import com.example.BaseIntegrationTest;
import com.example.repositorieslistgenerator.dto.BranchDetails;
import com.example.repositorieslistgenerator.dto.RepositoryDetails;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;

import java.util.List;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

class UserSuccessfullyFetchesRepositoriesDetailsTestIT extends BaseIntegrationTest {

    @Test
    void happyPath_shouldUserProvideValidUsername_andReceiveDetailsAboutRepositories() {
        //given
        wireMockServer.stubFor(get(urlPathTemplate("/users/{username}/repos"))
                .withPathParam("username", equalTo("TestGithubUser"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBodyFile("repositories.json")));
        wireMockServer.stubFor(get(urlPathTemplate("/repos/{ownerName}/{repositoryName}/branches"))
                .withPathParam("ownerName", equalTo("TestGithubUser"))
                .withPathParam("repositoryName", equalTo("repo1"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBodyFile("branches-repo1.json")));
        wireMockServer.stubFor(get(urlPathTemplate("/repos/{ownerName}/{repositoryName}/branches"))
                .withPathParam("ownerName", equalTo("TestGithubUser"))
                .withPathParam("repositoryName", equalTo("repo3"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBodyFile("branches-repo3.json")));
        RepositoryDetails repoDetails1 = new RepositoryDetails("repo1", "TestGithubUser", List.of(
                new BranchDetails("master", "c5b97d5ae6c19d5c5df71a34c7fbeeda2479ccbc")));
        RepositoryDetails repoDetails2 = new RepositoryDetails("repo3", "TestGithubUser", List.of(
                new BranchDetails("master", "mastermastermastermastershashasha"),
                new BranchDetails("dev", "devdevdevdevshashasha")));
        //when&then
        this.webTestClient.get()
                .uri("/github/get-repos/{username}", "TestGithubUser")
                .header(HttpHeaders.ACCEPT, APPLICATION_JSON_VALUE)
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(RepositoryDetails.class)
                .contains(repoDetails1, repoDetails2);
    }
}
