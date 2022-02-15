package com.brewery.controllers;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.nio.charset.StandardCharsets;

@SpringBootTest
@AutoConfigureMockMvc
public class BreweryControllerTest {

    private static final String BREWERIES_LIST_PATH = "/breweries/list",
    BREWERIES_PATH = "/breweries";

    WireMockServer wireMockServer;

    @Value("classpath:fixtures/responses/brewery_list_response.json")
    Resource breweryListResource;

    @BeforeEach
    public void setup() {
        wireMockServer = new WireMockServer();
        wireMockServer.start();
    }

    @AfterEach
    public void tearDown() {
        wireMockServer.stop();
    }

    public void stubForGetBreweryList(String response) {
        wireMockServer.stubFor(WireMock.get(BREWERIES_PATH)
                .willReturn(WireMock.aResponse()
                        .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        .withBody(response)));
    }

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testGetBreweriesSuccess() throws Exception {
        stubForGetBreweryList(FileUtils.readFileToString(breweryListResource.getFile(), StandardCharsets.UTF_8));
        mockMvc.perform(MockMvcRequestBuilders.get(BREWERIES_LIST_PATH))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful());
    }
}
