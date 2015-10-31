package com.example.shailen.moviesystem2.Model;

/**
 * Created by shailen on 10/7/2015.
 */
public abstract class CheckRequest {
//abstract super class for  implementing chain of responsibility
private CheckRequest source;

    //selects source either database or  API
    public void selectSource(CheckRequest  source){
        this.source = source;
    }

    //  request handler for handling database requests
    public abstract int handleRequest(String request);




}
