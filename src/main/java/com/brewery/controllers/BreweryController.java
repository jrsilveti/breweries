package com.brewery.controllers;

import com.brewery.models.Brewery;
import com.brewery.services.BreweryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
public class BreweryController {

    @Autowired
    BreweryService breweryService;

    @GetMapping("/list")
    public List<Brewery> getBreweryList() {
        List<Brewery> breweryList;
        breweryList = breweryService.getBreweryList();

        return breweryList;
    }

    @GetMapping("/breweries")
    public List<Brewery> getBreweries(@RequestParam String name) {
        return breweryService.getBrewery(name);
    }

    @GetMapping("/brewery")
    public Brewery getBrewery(@RequestParam String name) {
        List<Brewery> resultList = breweryService.getBrewery(name);
        for(Brewery brewery: resultList) {
            if(brewery.getName().equals(name)) {
                return brewery;
            }
        }

        throw new ResponseStatusException(HttpStatus.NO_CONTENT);
    }
}
