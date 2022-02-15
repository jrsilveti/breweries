package com.brewery.models;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class BreweryError {
    private int status;
    private String message;
}
