package com.brewery.models;

import lombok.Data;

import java.util.Date;

@Data
public class Brewery{
    private String id;
    private String name;
    private String brewery_type;
    private String street;
    private String address_2;
    private String address_3;
    private String city;
    private String state;
    private String county_province;
    private String postal_code;
    private String country;
    private String longitude;
    private String latitude;
    private String phone;
    private String website_url;
    private Date updated_at;
    private Date created_at;
}

