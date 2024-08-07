package com.example.feature;

import com.example.BaseIntegrationTest;
import com.example.repositorieslistgenerator.dto.RepositoryDetails;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;


class UserProvidesUsernameWithZeroNotForkedRepositoriesTestIT extends BaseIntegrationTest {

    @Test
    void shouldUserProvideUsernameWithZeroNotForkedRepos_andReceive200WithEmptyBody() {
        //given
        wireMockServer.stubFor(get(urlPathTemplate("/users/{username}/repos"))
                .withPathParam("username", equalTo("UsernameWithEmptyRepo"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody("[]")));
        //when&then
        this.webTestClient.get()
                .uri("/github/get-repos/{username}", "UsernameWithEmptyRepo")
                .header(HttpHeaders.ACCEPT, APPLICATION_JSON_VALUE)
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(RepositoryDetails.class)
                .contains();
    }
}
