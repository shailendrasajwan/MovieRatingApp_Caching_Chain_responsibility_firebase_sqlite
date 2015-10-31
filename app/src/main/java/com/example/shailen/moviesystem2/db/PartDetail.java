package com.example.shailen.moviesystem2.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by shailen on 10/14/2015.
 */
public class PartDetail extends SQLiteOpenHelper {
    public static final String TABLE_PARTY = "SINGHMoss";
    public static final String MOVIE_ID = "MOVIEID";
    public static final String PARTY_DATE= "DATE";
    public static final String MOVIE_TITLE = "TITLE";
    public static final String PARTY_CONTACTS = "CONTACTS";
    public static final String PARTY_TIME = "TIME";
    public static final String PARTY_VENUE = "VENUE";
    public static final String PARTY_LOCATION = "LOCATION";
    public static final String PARTY_ID = "PARTYID";
    protected static final String DB_NAME = "ruckbase";
    protected static final int DB_VERSION = 1;
    protected static final String DB_CREATE =
            "CREATE TABLE " + TABLE_PARTY + " (" +
                    PARTY_ID+ " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    MOVIE_ID + " TEXT, " +
                    PARTY_DATE + " TEXT, " +
                    MOVIE_TITLE + " TEXT, " +
                    PARTY_CONTACTS + " TEXT, " +
                    PARTY_TIME + " TEXT, " +
                    PARTY_VENUE + " TEXT, " +
                    PARTY_LOCATION + " TEXT)";




    public PartDetail(Context context) {
        super(context,PartDetail.DB_NAME,null,PartDetail.DB_VERSION);
    }



    @Override
    public void onCreate(SQLiteDatabase db)  {
        db.execSQL(PartDetail.DB_CREATE);
        Log.v("partoncreate", "db created");
    }



    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
