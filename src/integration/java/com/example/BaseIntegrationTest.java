package com.example;

import com.example.repositorieslistgenerator.GithubPort;
import com.example.repositorieslistgenerator.RepositoriesListerFacade;
import com.github.tomakehurst.wiremock.junit5.WireMockExtension;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;

@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("integration")
public class BaseIntegrationTest {
    protected static final String WIREMOCK_SERVER_HOST = "http://localhost";

    @Autowired
    protected GithubPort githubPort;
    @Autowired
    RepositoriesListerFacade repositoriesListerFacade;
    @Autowired
    protected MockMvc mockMvc;

    @RegisterExtension
    protected static WireMockExtension wireMockServer = WireMockExtension.newInstance()
            .options(wireMockConfig().dynamicPort())
            .options(wireMockConfig().usingFilesUnderDirectory("src/integration/resources"))
            .build();

    @DynamicPropertySource
    private static void propertyOverride(DynamicPropertyRegistry registry) {
        registry.add("github.baseUrl", () -> WIREMOCK_SERVER_HOST + ":" + wireMockServer.getPort());
    }

    @AfterEach
    void afterEach() {
        wireMockServer.resetAll();
    }
}
