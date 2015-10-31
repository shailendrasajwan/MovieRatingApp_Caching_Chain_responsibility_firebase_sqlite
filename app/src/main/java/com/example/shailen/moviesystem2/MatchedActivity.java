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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.shailen.moviesystem2.Fragment.PopupDialogFragment;
import com.example.shailen.moviesystem2.service.AppController;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MatchedActivity extends Activity {
    private ListView lstView;
    public static final String TAG1 = MatchedActivity.class.getSimpleName();;
    String tag_json_obj1 = "json_obj_req";
    ProgressDialog pDialog;
  public  static String SelectMovie="movie";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_matched);
        lstView=(ListView)findViewById(R.id.lstmatched);

        titleInfo();
        setOnClickListener();
    }


    private void setOnClickListener(){
        lstView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent myIntent = new Intent(view.getContext(), CompleteMovieDetails.class);
                myIntent.putExtra(SelectMovie, parent.getItemAtPosition(position).toString());
                startActivity(myIntent);

            }
        });
    }
  //fetches all the titles from  by json call
    private void titleInfo(){


        String srchitem=getIntent().getStringExtra(MainActivity.MOVIE_TITLE);
        String url="http://www.omdbapi.com/?s="+srchitem.trim().replace(" ", "+")+"&t&r=json";
        Log.v("fetchedurl", "url: " + url);
       // pDialog = new ProgressDialog(this);
       // pDialog.setMessage("Loading...");
       // pDialog.show();
        final ArrayList<String> Movielist = new ArrayList<String>();
        if(checkNetwork()) {
            JsonObjectRequest jsonObjReq1 = new JsonObjectRequest(Request.Method.GET,
                    url, null,
                    new Response.Listener<JSONObject>() {

                        @Override
                        public void onResponse(JSONObject response) {
                            Log.d(TAG1, response.toString());
                            try {
                                JSONArray jArray = response.getJSONArray("Search");
                                for (int i = 0; i < jArray.length(); i++) {
                                    JSONObject json_data = jArray.getJSONObject(i);
                                    //Log.v(TAG, "Title: " + json_data.getString("Title"));
                                    Movielist.add(json_data.getString("Title"));
                                    Log.v(TAG1, "Title: " + json_data.getString("Title"));
                                 //   pDialog.hide();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                              // pDialog.hide();
                            }
                            runOnUiThread(new Runnable() {

                                @Override
                                public void run() {

                                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(MatchedActivity.this,
                                            android.R.layout.simple_list_item_1,
                                            Movielist);
                                    lstView.setAdapter(adapter);
                                }
                            });

                            //  pDialog.hide();
                        }
                    }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    VolleyLog.d(TAG1, "Error: " + error.getMessage());
                    // hide the progress dialog
                    //pDialog.hide();
                }
            });

// Adding request to request queue
            AppController.getInstance().addToRequestQueue(jsonObjReq1, tag_json_obj1);

        }


        else {
            alertApp();
            Log.v("alertapp","fragmentcalled");
        }



    }

  //checks the network connectivity
    private boolean checkNetwork() {
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
        PopupDialogFragment dialogFragment=new PopupDialogFragment();
        dialogFragment.show(getFragmentManager(),"error_dialog");

    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_matched, menu);
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
