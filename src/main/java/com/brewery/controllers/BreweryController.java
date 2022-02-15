package com.brewery.controllers;

import com.brewery.models.Brewery;
import com.brewery.services.BreweryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/breweries")
public class BreweryController {

    @Autowired
    BreweryService breweryService;

    @GetMapping("/list")
    public List<Brewery> getBreweryList() {
        List<Brewery> breweryList;

        breweryList = breweryService.getBreweryList();

        //add error handling for this endpoint

        return breweryList;
    }
}
