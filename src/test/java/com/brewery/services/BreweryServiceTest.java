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

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

@SpringBootTest(classes = {BreweryService.class})
public class BreweryServiceTest {

    private static final String BREWERIES_PATH = "/breweries";

    WireMockServer wireMockServer;

    @Autowired
    BreweryService breweryService;

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

    @Test
    public void testGetBreweryList() throws IOException {
        stubForGetBreweryList(FileUtils.readFileToString(breweryListResource.getFile(), StandardCharsets.UTF_8));
        List<Brewery> resultList = breweryService.getBreweryList();
        System.out.println(resultList.get(0).name);
        Assertions.assertNotNull(resultList);
    }
}