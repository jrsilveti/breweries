package com.brewery.services;

import com.brewery.models.Brewery;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;

@Service
public class BreweryService {

    @Value("${breweryDBUrl}")
    private String breweryDBUrl;

    WebClient breweryDBClient = WebClient.builder()
            .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            .build();

    public List<Brewery> getBreweryList() {
        return breweryDBClient.get()
                .uri(breweryDBUrl)
                .accept(MediaType.APPLICATION_JSON)
                .exchangeToMono(clientResponse -> {
                    if (clientResponse.statusCode().equals(HttpStatus.OK)) {
                        return clientResponse.bodyToMono(new ParameterizedTypeReference<List<Brewery>>() {
                        });
                    } else {
                        throw new ResponseStatusException(clientResponse.statusCode());
                    }
                }).block();
    }

    public List<Brewery> searchBreweries(String name) {
        UriComponents breweryUriBuilder = UriComponentsBuilder.fromUri(URI.create(breweryDBUrl)).queryParam("by_name", name).build();
        List<Brewery> breweryList = breweryDBClient.get()
                .uri(breweryUriBuilder.toString())
                .accept(MediaType.APPLICATION_JSON)
                .exchangeToMono(clientResponse -> {
                    if (clientResponse.statusCode().equals(HttpStatus.OK)) {
                        return clientResponse.bodyToMono(new ParameterizedTypeReference<List<Brewery>>() {
                        });
                    } else {
                        throw new ResponseStatusException(clientResponse.statusCode());
                    }
                }).block();

        if(breweryList != null && !breweryList.isEmpty()) {
            return breweryList;
        } else {
            throw new ResponseStatusException(HttpStatus.NO_CONTENT);
        }
    }

    public Brewery getBrewery(String name) {
        List<Brewery> resultList = searchBreweries(name);
        for(Brewery brewery: resultList) {
            if(brewery.getName().equals(name)) {
                return brewery;
            }
        }
        throw new ResponseStatusException(HttpStatus.NO_CONTENT);
    }
}
