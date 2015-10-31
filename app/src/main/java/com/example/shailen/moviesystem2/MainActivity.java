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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.shailen.moviesystem2.Model.CheckRequest;
import com.example.shailen.moviesystem2.Model.DatabaseRequest;
import com.example.shailen.moviesystem2.Model.JsonRequest;
import com.example.shailen.moviesystem2.Model.Movies;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class MainActivity extends Activity {

    public static final  String MOVIE_TITLE = "TITLE" ;
    public static final String Tagm="MainActivity";
    public static final String SEARCH = "SEARCH";
 //   EditText  t=(EditText)findViewById(R.id.editText);
 EditText  movietitle;
    String srch;
    Button  search;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
      movietitle=(EditText)findViewById(R.id.movie_name);
       search=(Button)findViewById(R.id.searchButton);


        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               /* String selection = movietitle.getText().toString();
                Intent myintent = new Intent(v.getContext(),MatchedActivity.class);
                myintent.putExtra(MOVIE_TITLE, selection);
                startActivity(myintent);*/
                srch = movietitle.getText().toString();

               if(srch.toLowerCase().equals("Ant Man".toLowerCase()))
                    srch="Ant-Man";
                DatabaseRequest chkrequest1=new DatabaseRequest(getApplicationContext());
                CheckRequest chkrequest2=new JsonRequest();
                chkrequest1.selectSource(chkrequest2);
                int i= chkrequest1.handleRequest(srch);
                if(i == 1){
                    Movies m = chkrequest1.getMovie(srch);
                    ArrayList< Movies> lstMovies = new ArrayList<Movies>();
                    lstMovies  =chkrequest1.getlistmovies(srch);
               Log.v("movies","found");
                    Intent myintent = new Intent(MainActivity.this, CompleteMovieDetails.class);
                    myintent .putParcelableArrayListExtra("moviedata", lstMovies);
                    startActivity(myintent);

                }
                else if (i==0) {
                    Log.v(Tagm,""+i);
                    Intent iIntent;
                    iIntent = new Intent(v.getContext(),MatchedActivity.class);
                    iIntent.putExtra(MOVIE_TITLE,srch);
                    startActivity(iIntent);
                }

            }
        });

    }

    public void search(){





    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
