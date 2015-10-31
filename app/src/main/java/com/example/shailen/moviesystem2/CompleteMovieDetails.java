package com.example.shailen.moviesystem2;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.shailen.moviesystem2.Fragment.PopupDialogFragment;
import com.example.shailen.moviesystem2.Model.Movies;
import com.example.shailen.moviesystem2.Controller.MovieAdapter;
import com.example.shailen.moviesystem2.db.CheckSource;
import com.example.shailen.moviesystem2.db.MovieDetail;
import com.example.shailen.moviesystem2.service.AppController;


import org.json.JSONException;
import org.json.JSONObject;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;

public class CompleteMovieDetails extends Activity {


    ListView movielistview;
    JSONObject json_data;
    Movies movie;
    public static final String TAG = CompleteMovieDetails.class.getSimpleName();
    ArrayList<String> Movielist = new ArrayList<String>();
    private CheckSource mDataSource;
    protected ArrayList<String> mTitles;
    private ArrayList<Movies> movieDatabase;
    public static final String MOVIE_OBJECT = "MOVIE_OBJECT";
    private static String TAG2=CompleteMovieDetails.class.getSimpleName();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complete_movie_details);
        movielistview = (ListView) findViewById(R.id.lstComplete);
        mDataSource = new CheckSource(CompleteMovieDetails.this);
        movieDatabase = new ArrayList<Movies>();
        mTitles = new ArrayList<String>();

        String movietitle = getIntent().getStringExtra(MatchedActivity.SelectMovie);
        if(movietitle != null){
            Log.v("tag", movietitle);
            use_json_object("http://www.omdbapi.com/?t=" + movietitle.trim().replace(" ", "+") + "&y=&plot=full&r=json");
            setListenerOnListView();
        }

        else {


            Bundle bdl = getIntent().getExtras();
            ArrayList<Movies> mArraylist1 = bdl.getParcelableArrayList("moviedata");
                      Iterator<Movies> itr = mArraylist1.iterator();
            while(itr.hasNext()){
                Movies Movies = itr.next();
                System.out.println(Movies.getTitle());
            }
            MovieAdapter movie_adapter = new MovieAdapter(CompleteMovieDetails.this,mArraylist1);
            movielistview.setAdapter(movie_adapter);



            setListenerOnListView();
        }
    }
    private void setListenerOnListView() {
        movielistview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent myIntent = new Intent(view.getContext(), Moviedetails.class);

                Movies movies = (Movies) parent.getItemAtPosition(position);

                myIntent.putExtra(MOVIE_OBJECT, movies);

                startActivity(myIntent);
            }
        });
    }


    private void updateList() {
        CheckSource my_CheckSource = new CheckSource(CompleteMovieDetails.this);
        try {
            my_CheckSource.open();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        Cursor cursor = my_CheckSource.selectAllMovies();
        ArrayList<Movies> Database = new ArrayList<Movies>();
        cursor.moveToFirst();
        while(!cursor.isAfterLast()){
            Movies movi = new Movies();
            movi.setTitle(cursor.getString(cursor.getColumnIndex(MovieDetail.COLUMN_TITLE)));
            movi.setRated(cursor.getString(cursor.getColumnIndex(MovieDetail.COLUMN_CONTENT)));
            movi.setReleased(cursor.getString(cursor.getColumnIndex(MovieDetail.COLUMN_RELEASE)));
            movi.setGenre(cursor.getString(cursor.getColumnIndex(MovieDetail.COLUMN_GENRE)));
            movi.setDirector(cursor.getString(cursor.getColumnIndex(MovieDetail.COLUMN_DIRECTOR)));
            movi.setActors(cursor.getString(cursor.getColumnIndex(MovieDetail.COLUMN_CAST)));
            movi.setPlot(cursor.getString(cursor.getColumnIndex(MovieDetail.COLUMN_PLOT)));
            movi.setPoster(cursor.getString(cursor.getColumnIndex(MovieDetail.COLUMN_IMAGE)));
            movi.setImdbRating(cursor.getString(cursor.getColumnIndex(MovieDetail.COLUMN_RATING)));
            movi.setImdbId(cursor.getString(cursor.getColumnIndex(MovieDetail.COLUMN_IMDB)));
            movi.setShort_plot();
            Database.add(movi);
            cursor.moveToNext();
        }
        my_CheckSource.close();
        MovieAdapter movie_adapter = new MovieAdapter(CompleteMovieDetails.this,Database);
        movielistview.setAdapter(movie_adapter);
    }

    private void use_json_object(String url) {

             if(isNetworkAvailable()) {
                 JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.GET,
                         url, null,
                         new Response.Listener<JSONObject>() {

                             @Override
                             public void onResponse(JSONObject response) {
                                 try {
                                     Log.v("json", response.getString("Director"));
                                     movie = getDetails(response);
                                     insertMovieData();
                                     Movielist.add(movie.getDirector());
                                     Cursor cursor = mDataSource.selectAllMovies();
                                     mTitles.clear();
                                     cursor.moveToFirst();
                                     while (!cursor.isAfterLast()) {
                                         Movies Movies = new Movies();
                                         //int i = cursor.getColumnIndex(MovieDetail.COLUMN_TITLE);
                                         //mTitles.add(cursor.getString(i));
                                         Movies.setTitle(cursor.getString(cursor.getColumnIndex(MovieDetail.COLUMN_TITLE)));
                                         Movies.setRated(cursor.getString(cursor.getColumnIndex(MovieDetail.COLUMN_CONTENT)));
                                         Movies.setReleased(cursor.getString(cursor.getColumnIndex(MovieDetail.COLUMN_RELEASE)));
                                         Movies.setGenre(cursor.getString(cursor.getColumnIndex(MovieDetail.COLUMN_GENRE)));
                                         Movies.setDirector(cursor.getString(cursor.getColumnIndex(MovieDetail.COLUMN_DIRECTOR)));
                                         Movies.setActors(cursor.getString(cursor.getColumnIndex(MovieDetail.COLUMN_CAST)));
                                         Movies.setPlot(cursor.getString(cursor.getColumnIndex(MovieDetail.COLUMN_PLOT)));
                                         Movies.setPoster(cursor.getString(cursor.getColumnIndex(MovieDetail.COLUMN_IMAGE)));
                                         Movies.setImdbRating(cursor.getString(cursor.getColumnIndex(MovieDetail.COLUMN_RATING)));
                                         Movies.setImdbId(cursor.getString(cursor.getColumnIndex(MovieDetail.COLUMN_IMDB)));
                                         Movies.setShort_plot();
                                         movieDatabase.add(Movies);
                                         cursor.moveToNext();
                                     }

                                     runOnUiThread(new Runnable() {
                                         @Override
                                         public void run() {

                                             MovieAdapter movie_adapter = new MovieAdapter(CompleteMovieDetails.this, movieDatabase);
                                             movielistview.setAdapter(movie_adapter);

                                         }
                                     });


                                 } catch (JSONException e) {
                                     e.printStackTrace();
                                 }
                             }
                         }, new Response.ErrorListener() {

                     @Override
                     public void onErrorResponse(VolleyError error) {
                         VolleyLog.v(TAG2, "Error: " + error.getMessage());


                     }
                 });


                 AppController.getInstance().addToRequestQueue(jsObjRequest, "completemoviedetails");


             }
  else{
                 alertApp();
             }


   }






    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_complete_movie_details, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();


        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    private Movies getDetails(JSONObject omdb) throws JSONException {
        //JSONObject omdb = new JSONObject(data);
        Movies Movies = new Movies();
        Movies.setTitle(omdb.getString("Title"));
        Movies.setImdbRating(omdb.getString("imdbRating"));
        Movies.setReleased(omdb.getString("Released"));
        Movies.setRated(omdb.getString("Rated"));
        Movies.setGenre(omdb.getString("Genre"));
        Movies.setYear(omdb.getString("Year"));
        Movies.setPlot(omdb.getString("Plot"));
        Movies.setPoster(omdb.getString("Poster"));
        Movies.setResponse(omdb.getString("Response"));
        Movies.setImdbId(omdb.getString("imdbID"));
        Movies.setType(omdb.getString("Type"));
        Movies.setLanguage(omdb.getString("Language"));
        Movies.setCountry(omdb.getString("Country"));
        Movies.setAwards(omdb.getString("Awards"));
        Movies.setActors(omdb.getString("Actors"));
        Movies.setDirector(omdb.getString("Director"));
        Movies.setWriter(omdb.getString("Writer"));
        Movies.setMetascore(omdb.getString("Metascore"));
        return Movies;
    }
    private boolean isNetworkAvailable() {
        ConnectivityManager manager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();
        if(networkInfo != null && networkInfo.isConnected()){
            return true;
        }
        else{

            return false;
        }
    }


    private void alertApp() {
        PopupDialogFragment dialog = new PopupDialogFragment();
        dialog.show(getFragmentManager(), "error_dialog");

    }
    @Override
    protected void onResume(){

        super.onResume();

        try {
            mDataSource.open();
            Log.v(TAG, "Database created");

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
    private void insertMovieData() {
        Log.v(TAG,"-------Trying----------");
        mDataSource.insertMovie(movie);
        Log.v(TAG, "-------Trying----------");
    }

    @Override
    protected void onPause(){
        super.onPause();

        mDataSource.close();
    }

}
