package com.example.shailen.moviesystem2.Model;

import android.util.Log;

/**
 * Created by shailen on 10/7/2015.
 */
public class JsonRequest extends CheckRequest{


    @Override
    public int handleRequest(String request) {
        System.out.println("In api call ");
        Log.v("Jsonrequest","In api csll");
        return 0;
    }


}
