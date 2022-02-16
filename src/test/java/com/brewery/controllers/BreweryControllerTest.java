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
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.nio.charset.StandardCharsets;

@SpringBootTest
@AutoConfigureMockMvc
public class BreweryControllerTest {

    private static final String BREWERIES_LIST_PATH = "/list",
            BREWERIES_PATH = "/breweries",
            BREWERY_PATH = "/breweries?by_name=dog";

    WireMockServer wireMockServer;

    @Value("classpath:fixtures/responses/brewery_list_response.json")
    Resource breweryListResource;

    @Value("classpath:fixtures/responses/breweries_response.json")
    Resource breweriesResource;

    @BeforeEach
    public void setup() {
        wireMockServer = new WireMockServer();
        wireMockServer.start();
    }

    @AfterEach
    public void tearDown() {
        wireMockServer.stop();
    }

    private void stubForGetBreweryList(String response) {
        wireMockServer.stubFor(WireMock.get(BREWERIES_PATH)
                .willReturn(WireMock.aResponse()
                        .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        .withBody(response)));
    }

    private void stubForGetBreweryList500() {
        wireMockServer.stubFor(WireMock.get(BREWERIES_PATH)
                .willReturn(WireMock.aResponse()
                        .withStatus(500)));
    }

    private void stubForGetBreweries(String response) {
        wireMockServer.stubFor(WireMock.get(BREWERY_PATH)
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

    @Test
    public void testGetBreweries500() throws Exception {
        stubForGetBreweryList500();
        mockMvc.perform(MockMvcRequestBuilders.get(BREWERIES_LIST_PATH))
                .andExpect(MockMvcResultMatchers.status().is5xxServerError());
    }

    @Test
    public void testGetBrewery() throws Exception {
        stubForGetBreweries(FileUtils.readFileToString(breweriesResource.getFile(), StandardCharsets.UTF_8));
        mockMvc.perform(MockMvcRequestBuilders.get(BREWERIES_PATH).queryParam("name", "dog"))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful());
    }
}
