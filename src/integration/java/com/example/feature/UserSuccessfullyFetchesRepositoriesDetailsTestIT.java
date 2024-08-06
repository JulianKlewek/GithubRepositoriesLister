package com.example.feature;

import com.example.BaseIntegrationTest;
import com.example.repositorieslistgenerator.dto.BranchDetails;
import com.example.repositorieslistgenerator.dto.RepositoryDetails;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

import java.util.Arrays;
import java.util.List;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.equalTo;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.urlPathTemplate;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class UserSuccessfullyFetchesRepositoriesDetailsTestIT extends BaseIntegrationTest {

    @Test
    void happyPath_shouldUserProvideValidUsername_andReceiveDetailsAboutRepositories() throws Exception {
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
        List<RepositoryDetails> expectedRepositories = List.of(
                new RepositoryDetails("repo1", "TestGithubUser", List.of(
                        new BranchDetails("master", "c5b97d5ae6c19d5c5df71a34c7fbeeda2479ccbc"))),
                new RepositoryDetails("repo3", "TestGithubUser", List.of(
                        new BranchDetails("master", "mastermastermastermastershashasha"),
                        new BranchDetails("dev", "devdevdevdevshashasha"))));
        ObjectMapper mapper = new ObjectMapper();
        //when
        ResultActions perform = mockMvc.perform(get("/github/get-repos/{username}", "TestGithubUser")
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andDo(print());
        List<RepositoryDetails> result = Arrays.asList(mapper.readValue(perform.andReturn().getResponse().getContentAsString(), RepositoryDetails[].class));
        //then
        perform.andExpectAll(
                        status().isOk(),
                        jsonPath("$", hasSize(2)),
                        jsonPath("$[*].repositoryName", containsInAnyOrder("repo1", "repo3")),
                        jsonPath("$[*].branches[*]", hasSize(3)))
                .andReturn();
        assertEquals(expectedRepositories, result);
    }
}
