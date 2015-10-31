package com.example.shailen.moviesystem2.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by shailen on 9/25/2015.
 */
public class MovieDetail  extends  SQLiteOpenHelper  {

    protected static final String DB_NAME = "Jammovies";
    public static final String TABLE_MOVIES = "Collmov";
    public static final String COLUMN_ID = "_ID";
    public static final String COLUMN_IMDB = "IMDB";
    public static final String COLUMN_TITLE = "TITLE";
    public static final String COLUMN_IMAGE = "IMAGE";
    public static final String COLUMN_CONTENT = "CONTENT";
    public static final String COLUMN_PLOT = "PLOT";
    public static final String COLUMN_GENRE = "GENRE";
    public static final String COLUMN_RELEASE = "RELEASE";
    public static final String COLUMN_RATING = "RATING";
    public static final String COLUMN_CAST = "CAST";
    public static final String COLUMN_DIRECTOR = "DIRECTOR";
    protected static final int DB_VERSION = 1;

    protected static final String DB_CREATE =
            "CREATE TABLE " + TABLE_MOVIES + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_TITLE + " TEXT, " +
                    COLUMN_IMAGE + " TEXT, " +
                    COLUMN_CONTENT + " TEXT, " +
                    COLUMN_PLOT + " TEXT, " +
                    COLUMN_GENRE + " TEXT, " +
                    COLUMN_RELEASE + " TEXT, " +
                    COLUMN_RATING + " TEXT, " +
                    COLUMN_CAST + " TEXT, " +
                    COLUMN_IMDB + " TEXT, " +
                    COLUMN_DIRECTOR + " TEXT)";

    public MovieDetail(Context context) {
        super(context,DB_NAME,null,DB_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DB_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

}
