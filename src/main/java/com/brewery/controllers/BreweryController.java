package com.brewery.controllers;

import com.brewery.models.Brewery;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/breweries")
public class BreweryController {

    @GetMapping("/list")
    public List<Brewery> getBreweryList() {
        List<Brewery> breweryList = new ArrayList<>();

        return breweryList;
    }
}
