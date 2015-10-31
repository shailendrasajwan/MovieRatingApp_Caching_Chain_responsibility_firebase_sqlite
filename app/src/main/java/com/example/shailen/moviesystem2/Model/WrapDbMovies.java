package com.example.shailen.moviesystem2.Model;

import java.util.ArrayList;

/**
 * Created by shailen on 10/7/2015.
 */
//wrapper for data from movie  database
public class WrapDbMovies {

    private ArrayList<Movies> mov;

    public WrapDbMovies(ArrayList<Movies> data) {
        this.mov = data;
    }


    public ArrayList<Movies> getData() {
        return this.mov;
    }



}
