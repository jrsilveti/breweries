package com.brewery.controllers;

import com.brewery.models.Brewery;
import com.brewery.services.BreweryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
    public List<Brewery> getBreweryByName(@RequestParam String name) {
        return breweryService.getBrewery(name);
    }
}
