package com.brewery.services;

import com.brewery.models.Brewery;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

@SpringBootTest(classes = {BreweryService.class})
public class BreweryServiceTest {

    private static final String BREWERIES_PATH = "/breweries",
            BREWERY_PATH = "/breweries?by_name=dog",
            SPECIFIC_BREWERY_PATH = "/breweries?by_name=2%20Dogz%20and%20A%20Guy%20Brewing";

    WireMockServer wireMockServer;

    @Autowired
    BreweryService breweryService;

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

    private void stubOpenBreweryDBCall(String response, String path) {
        wireMockServer.stubFor(WireMock.get(path)
                .willReturn(WireMock.aResponse()
                        .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        .withBody(response)));
    }

    @Test
    public void testGetBreweryList() throws IOException {
        stubOpenBreweryDBCall(FileUtils.readFileToString(breweryListResource.getFile(), StandardCharsets.UTF_8),
                BREWERIES_PATH);
        List<Brewery> resultList = breweryService.getBreweryList();
        Assertions.assertNotNull(resultList);
    }

    @Test
    public void testSearchBreweries() throws IOException {
        stubOpenBreweryDBCall(FileUtils.readFileToString(breweriesResource.getFile(), StandardCharsets.UTF_8),
                BREWERY_PATH);
        List<Brewery> resultList = breweryService.searchBreweries("dog");
        Assertions.assertNotNull(resultList);
    }

    @Test
    public void testSearchBreweries204() {
        stubOpenBreweryDBCall("", BREWERY_PATH);
        Assertions.assertThrows(ResponseStatusException.class, () -> breweryService.searchBreweries("dog"));
    }

    @Test
    public void testGetBrewery() throws IOException {
        stubOpenBreweryDBCall(FileUtils.readFileToString(breweriesResource.getFile(), StandardCharsets.UTF_8),
                SPECIFIC_BREWERY_PATH);
        Brewery resultBrewery = breweryService.getBrewery("2 Dogz and A Guy Brewing");
        Assertions.assertNotNull(resultBrewery);
        Assertions.assertEquals("2 Dogz and A Guy Brewing", resultBrewery.getName());
    }

    @Test
    public void testGetBrewery204() {
        stubOpenBreweryDBCall("", SPECIFIC_BREWERY_PATH);
        Assertions.assertThrows(ResponseStatusException.class,
                () -> breweryService.searchBreweries("2 Dogz and A Guy Brewing"));
    }

}
