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

    private static final String BREWERIES_LIST_PATH = "/list",
            BREWERIES_PATH = "/breweries",
            BREWERIES_NAME_PATH = "/breweries?by_name=dog",
            BREWERY_PATH = "/brewery",
            BREWERY_NAME_PATH = "/breweries?by_name=2%20Dogz%20and%20A%20Guy%20Brewing";

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

    private void stubForGetBreweriesEmpty() {
        wireMockServer.stubFor(WireMock.get(BREWERIES_PATH)
                .willReturn(WireMock.aResponse()
                        .withBody("")
                        .withStatus(200)));
    }

    private void stubForGetBreweries(String response) {
        wireMockServer.stubFor(WireMock.get(BREWERIES_NAME_PATH)
                .willReturn(WireMock.aResponse()
                        .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        .withBody(response)));
    }

    private void stubForGetBrewery(String response) {
        wireMockServer.stubFor(WireMock.get(BREWERY_NAME_PATH)
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
                .andExpect(MockMvcResultMatchers.status().is5xxServerError()).andReturn();
    }

    @Test
    public void testGetBreweries400() throws Exception {
        stubForGetBreweriesEmpty();
        mockMvc.perform(MockMvcRequestBuilders.get(BREWERIES_PATH))
                .andExpect(MockMvcResultMatchers.status().is4xxClientError()).andReturn();
    }

    @Test
    public void testGetBreweries() throws Exception {
        stubForGetBreweries(FileUtils.readFileToString(breweriesResource.getFile(), StandardCharsets.UTF_8));
        mockMvc.perform(MockMvcRequestBuilders.get(BREWERIES_PATH).queryParam("name", "dog"))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful());
    }

    @Test
    public void testGetBrewery() throws Exception {
        stubForGetBrewery(FileUtils.readFileToString(breweriesResource.getFile(), StandardCharsets.UTF_8));
        mockMvc.perform(MockMvcRequestBuilders.get(BREWERY_PATH).queryParam("name", "2 Dogz and A Guy Brewing"))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful());
    }
}
