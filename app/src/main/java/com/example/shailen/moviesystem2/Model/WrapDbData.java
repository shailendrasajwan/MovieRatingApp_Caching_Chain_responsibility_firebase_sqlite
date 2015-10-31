package com.example.shailen.moviesystem2.Model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by shailen on 10/7/2015.
 */

//wrapper for data fromm database
    public class WrapDbData implements Serializable {

        private ArrayList<String> data;
        private ArrayList<Movies> flick;

        public WrapDbData(ArrayList<String> data) {
            this.data = data;
        }

        public ArrayList<String> getData() {
            return this.data;
        }



    }



