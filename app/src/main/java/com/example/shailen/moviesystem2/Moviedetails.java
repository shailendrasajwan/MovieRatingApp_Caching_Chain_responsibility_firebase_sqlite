package com.example.shailen.moviesystem2;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.NetworkImageView;
import com.example.shailen.moviesystem2.Model.Movies;
import com.example.shailen.moviesystem2.db.CheckSource;
import com.example.shailen.moviesystem2.service.AppController;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

public class Moviedetails extends Activity {


    TextView title ;
    TextView  plot ;
    TextView ContentType;
    TextView Genre;
    TextView release_date;
    TextView Cast ;
    TextView director;
    RatingBar ratingBar;
    Button PartyButton;
    NetworkImageView poster;
    private String TAG="MovieDetails";
    ProgressDialog pDialog;
    Movies movie;
    private static boolean status=false;
    private static  float initialrating=0;
    private static  float newrating=0;
    private CheckSource source;
    private float rating ;
    private Movies objmovies;
    private static String changedrating="";
    public static final String PARTY= "PARTY";
    public static  HashMap<String,Float> rating_map=new HashMap<String,Float>();
    private static ArrayList<Movies> movielist= new ArrayList<Movies>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_moviedetails);
       title= (TextView)findViewById(R.id.MovieTitle) ;
     plot=(TextView)findViewById(R.id.LongPlot)  ;
        ContentType=(TextView)findViewById(R.id.ContentType);
       Genre=(TextView)findViewById(R.id.genre);
    release_date=(TextView)findViewById(R.id.release_date) ;
      Cast=(TextView)findViewById(R.id.Cast) ;
    director=(TextView)findViewById(R.id.Director);
      ratingBar=(RatingBar)findViewById(R.id.ratingBar);
      PartyButton=(Button)findViewById(R.id.PartyButton);
       poster=(NetworkImageView)findViewById(R.id.netimageView);

        source= new  CheckSource( Moviedetails.this);
        objmovies = (Movies) getIntent().getParcelableExtra(CompleteMovieDetails.MOVIE_OBJECT);
        if( objmovies!=null)
        {title.setText((CharSequence)objmovies.getTitle());}
        updateDisplay(objmovies);
        ListenerOnPartyButton();

      // setListeneronRatingchange();
        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {

                newrating=ratingBar.getRating();
               rating_map.put(objmovies.getImdbId(),newrating);
              status=true;
                Log.v("moviedetails new rating",""+newrating);
            }
        });
    }

    private void ListenerOnPartyButton() {
        PartyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Moviedetails.this, MyParty.class);
                i.putExtra(PARTY,objmovies);
                startActivity(i);
           Log.v("createpartyclicked","true");
            }
        });
    }





//sets layout elemnts value
    private void updateDisplay(Movies movie) {

      ImageLoader img= AppController.getInstance().getImageLoader();

        poster.setImageUrl(movie.getPoster(), img);

        title.setText(movie.getTitle());

        if (movie.getPlot().length() >= 375) {
            plot.setText("Plot: " + movie.getPlot().substring(0,275)+"...");
        }
        else {
            plot.setText("Plot: " + movie.getPlot());
        }
        String[] arr=movie.getActors().split(",");
        String casts=arr[0]+","+arr[1];
        Cast.setText("Cast: " +casts);
        director.setText("Director: " + movie.getDirector());
        release_date.setText("Released: " + movie.getReleased());
        Genre.setText("Genre: " + movie.getGenre());
        if(rating_map.containsKey(movie.getImdbId()))
        ratingBar.setRating(rating_map.get(movie.getImdbId()));
        else
            ratingBar.setRating(movie.getImdbRating());
        ContentType.setText(movie.getRated());
        title.setText(movie.getTitle());

    }

    @Override
    protected void onResume(){

        super.onResume();


        try {
            source.open();
            Log.v(TAG, " Database created ");

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
    @Override
    protected void onPause(){


        super.onPause();
        try {
            changedrating = String.valueOf(newrating);
            Log.v("onpause rating", changedrating);
        }
        catch(Exception ex){
            Log.v("onpauseratingerror",ex.toString());
        }
        if(status) {
            Log.v("onpause rating", changedrating + objmovies.getImdbId());
            String s=objmovies.getImdbId();
            objmovies.setRated(s);
            source.updateRating(changedrating, s);
            status=false;
            Log.v("updatedratingindb",newrating+"");
        }

     source.close();


        Log.v(TAG, " Database closed ");
    }



        @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_moviedetails, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
