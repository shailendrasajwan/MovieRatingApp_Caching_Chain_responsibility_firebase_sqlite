package com.example.shailen.moviesystem2.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.shailen.moviesystem2.Model.Movies;
import com.example.shailen.moviesystem2.Moviedetails;

import java.sql.SQLException;

/**
 * Created by shailen on 10/4/2015.
 */
public class CheckSource {
    private SQLiteDatabase mDatabase;
    private MovieDetail movieDetail;
    private Context mContext;

    public CheckSource(Context context){
        mContext = context;
        movieDetail = new MovieDetail(mContext);
     //  mContext.deleteDatabase("finalmovieslst1thurdb");
   //      mContext.deleteDatabase("moviesystem18.db");
     //   mContext.deleteDatabase("finialmovdb");
      //  mContext.deleteDatabase("finalmovieslistdb");
      //  mContext.deleteDatabase("finalmovieslst1db");
    }



    public void setContext(Context context){
        this.mContext = context;
        this.movieDetail = new MovieDetail(context);

       // mContext.deleteDatabase("movies.db");
      //  mContext.deleteDatabase("moviesystem18.db");
    }

    public CheckSource(){
     //  mContext.deleteDatabase("movies1.db");
    }

    //open
    public void open() throws SQLException {
        mDatabase = movieDetail.getWritableDatabase();
    }

    public void close() {
        mDatabase.close();
    }
  //inserts movie in movie database
    public void insertMovie(Movies mov){
        mDatabase.beginTransaction();
        try {

            ContentValues values = new ContentValues();
            values.put(MovieDetail.COLUMN_IMAGE,mov.getPoster());
            values.put(MovieDetail.COLUMN_TITLE,mov.getTitle());
            values.put(MovieDetail.COLUMN_CONTENT,mov.getRated());
            values.put(MovieDetail.COLUMN_PLOT,mov.getPlot());
            values.put(MovieDetail.COLUMN_GENRE,mov.getGenre());
            values.put(MovieDetail.COLUMN_RELEASE,mov.getReleased());
            values.put(MovieDetail.COLUMN_RATING,mov.getImdbRating());
            values.put(MovieDetail.COLUMN_CAST,mov.getActors());
            values.put(MovieDetail.COLUMN_DIRECTOR,mov.getDirector());
            values.put(MovieDetail.COLUMN_IMDB,mov.getImdbId());

            mDatabase.insert(MovieDetail.TABLE_MOVIES, null, values);
            mDatabase.setTransactionSuccessful();
        }
        finally {
            mDatabase.endTransaction();
        }
    }


    /*
     * UPDATE  rating
     */
    public int updateRating(String rating,String id) {
    try {

        Log.v("updateratingfn",rating+"");
        String whereClause = MovieDetail.COLUMN_IMDB + "=?";
        ContentValues values = new ContentValues();
        values.put(MovieDetail.COLUMN_RATING, rating);
        int i = mDatabase.update(MovieDetail.TABLE_MOVIES, values, whereClause, new String[]{id.trim()});
        Log.v("updatrating", i + "");
        return i;
    }
    catch(Exception ex){
        Log.v("updateparty",ex.toString());
        return -2;
    }
    }


//selects all movies from database
    public Cursor selectAllMovies(){
        Cursor cursor = mDatabase.query(
                MovieDetail.TABLE_MOVIES,
                null, // column names
                null, // where clause
                null, // where params
                null, // groupby
                null, // having
                null  // orderby
        ) ;
        return cursor;
    }
}