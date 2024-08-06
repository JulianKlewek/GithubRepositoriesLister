package com.example.feature;

import com.example.BaseIntegrationTest;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.equalTo;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.urlPathTemplate;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class UserProvidesNonExistingUsernameTestIT extends BaseIntegrationTest {

    @Test
    void userProvidedNonExistingGithubUsername_andReceive404() throws Exception {
        //step1 user gives not existing username
        //given
        wireMockServer.stubFor(get(urlPathTemplate("/users/{username}/repos"))
                .withPathParam("username", equalTo("nonExistingUsername"))
                .willReturn(aResponse()
                        .withStatus(404)
                        .withHeader("Content-Type", "application/json")));
        //when
        ResultActions perform = mockMvc.perform(get("/github/get-repos/{username}", "nonExistingUsername")
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andDo(print());
        //then
        perform.andExpectAll(
                        status().isNotFound(),
                        jsonPath("$.status", Matchers.equalTo(404)),
                        jsonPath("$.message", Matchers.equalTo("Github username not found: nonExistingUsername")))
                .andReturn();
    }
}
