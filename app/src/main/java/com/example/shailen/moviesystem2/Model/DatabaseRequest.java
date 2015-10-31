package com.example.shailen.moviesystem2.Model;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.example.shailen.moviesystem2.db.CheckSource;
import com.example.shailen.moviesystem2.db.MovieDetail;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

/**
 * Created by shailen on 10/7/2015.
 */
// class for handling datbase part of chain of responsibility
public class DatabaseRequest extends CheckRequest{

    private CheckSource dbsource = new CheckSource();
    private HashMap<String, Movies> dbmovies= new HashMap<String, Movies>();
    private ArrayList<Movies> listmovies = new ArrayList<Movies>();


    public DatabaseRequest(Context context) {
        this.dbsource.setContext(context);
    }



    @Override
    public int handleRequest(String request) {

        try {
            dbsource.open();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        Cursor cursor = dbsource.selectAllMovies();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            //stores  all the movies from  database into  arraylist for  memory purposes
            Movies Movies = new Movies();

            Movies.setTitle(cursor.getString(cursor.getColumnIndex( MovieDetail.COLUMN_TITLE)));
            System.out.println(cursor.getString(cursor.getColumnIndex( MovieDetail.COLUMN_TITLE)));
            Movies.setRated(cursor.getString(cursor.getColumnIndex( MovieDetail.COLUMN_CONTENT)));
            Movies.setReleased(cursor.getString(cursor.getColumnIndex( MovieDetail.COLUMN_RELEASE)));
            Movies.setGenre(cursor.getString(cursor.getColumnIndex( MovieDetail.COLUMN_GENRE)));
            Movies.setDirector(cursor.getString(cursor.getColumnIndex( MovieDetail.COLUMN_DIRECTOR)));
            Movies.setActors(cursor.getString(cursor.getColumnIndex( MovieDetail.COLUMN_CAST)));
            Movies.setPlot(cursor.getString(cursor.getColumnIndex( MovieDetail.COLUMN_PLOT)));
            Movies.setPoster(cursor.getString(cursor.getColumnIndex( MovieDetail.COLUMN_IMAGE)));
            Movies.setImdbRating(cursor.getString(cursor.getColumnIndex( MovieDetail.COLUMN_RATING)));
            Movies.setImdbId(cursor.getString(cursor.getColumnIndex( MovieDetail.COLUMN_IMDB)));
            Movies.setShort_plot();
            dbmovies.put(cursor.getString(cursor.getColumnIndex(MovieDetail.COLUMN_TITLE)), Movies);
            listmovies.add(Movies);
            cursor.moveToNext();
        }
        dbsource.close();

        if (dbmovies.containsKey(request)) {
            System.out.println("Movie found");
            Log.v("Databasehandler", "moviefound");
            return 1;
        } else {
            System.out.println("Movie Not found");
           JsonRequest objjsonrequest = new JsonRequest();
            objjsonrequest.handleRequest(request);
            Log.v("Databasehandler", "movienotfound");
            return 0;
        }

    }

    public Movies getMovie(String request) {

        return this.dbmovies.get(request);
    }
    public ArrayList<Movies> getlistmovies(String request){

        Movies movie = new Movies();
        for(int i =0; i< listmovies.size();i++){
            Movies mov = listmovies.get(i);
            if(mov.getTitle().equalsIgnoreCase(request)){
                listmovies.remove(i);
                movie = mov;
                listmovies.add(movie);
                Collections.reverse(listmovies);
                break;
            }

        }




        return listmovies;
    }



}
