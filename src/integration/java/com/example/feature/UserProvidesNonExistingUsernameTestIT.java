package com.example.feature;

import com.example.BaseIntegrationTest;
import com.example.infrastructure.controller.error.ApiError;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

class UserProvidesNonExistingUsernameTestIT extends BaseIntegrationTest {

    @Test
    void userProvidedNonExistingGithubUsername_andReceive404() {
        //step1 user gives not existing username
        //given
        ApiError expectedError = new ApiError(404, "Github username not found: nonExistingUsername");
        wireMockServer.stubFor(get(urlPathTemplate("/users/{username}/repos"))
                .withPathParam("username", equalTo("nonExistingUsername"))
                .willReturn(aResponse()
                        .withStatus(404)
                        .withHeader("Content-Type", "application/json")));
        //when&then
        this.webTestClient.get()
                .uri("/github/get-repos/{username}", "nonExistingUsername")
                .header(HttpHeaders.ACCEPT, APPLICATION_JSON_VALUE)
                .exchange()
                .expectStatus().isNotFound()
                .expectBody(ApiError.class)
                .isEqualTo(expectedError);
    }
}
