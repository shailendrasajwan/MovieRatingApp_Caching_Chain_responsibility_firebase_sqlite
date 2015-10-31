package com.example.shailen.moviesystem2.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.shailen.moviesystem2.Model.Movies;
import com.example.shailen.moviesystem2.Model.Party;
import com.example.shailen.moviesystem2.Moviedetails;

import java.sql.SQLException;


/**
 * Created by shailen on 10/14/2015.
 */

public class PartySource {
    private SQLiteDatabase pdbase;
    private  PartDetail partDetail;
    private Context mContext;
static int count=0;
//initializes party with current party
    public PartySource(Context context){
        mContext = context;
        partDetail = new PartDetail(mContext);
       // mContext.deleteDatabase("movieexampartyfinal2.db");
        // mContext.deleteDatabase("fPARTY21");
        //mContext.deleteDatabase("finalpartydb1");
        //mContext.deleteDatabase("finalpartydb");
       // mContext.deleteDatabase("movieexamparty.db");
    }



    public void setContext(Context context){
        this.mContext = context;
        this.partDetail = new PartDetail(context);
    //   mContext.deleteDatabase("newpartydb");
        // mContext.deleteDatabase("movies1.db");
        // mContext.deleteDatabase("movies.db");
    }

//initializes  party
    public PartySource(){
        //  mContext.deleteDatabase("movies1.db");
    }

    //open
    public void open() throws SQLException {
       pdbase =partDetail.getWritableDatabase();
    }

    public void close() {
        pdbase.close();
    }
//inserts a party into sqlite
    public void insertParty(Party party,Movies mov ){
    pdbase.beginTransaction();
        try {
            ContentValues values = new ContentValues();

            values.put(partDetail.MOVIE_ID,mov.getImdbId());
            values.put(partDetail.PARTY_DATE ,party.getDate());
            values.put(partDetail.MOVIE_TITLE,mov.getTitle());
            values.put( partDetail.PARTY_CONTACTS,party.getContactInfo());
            values.put(partDetail.PARTY_TIME,party.getTime());
            values.put(partDetail.PARTY_VENUE,party.getVenue());
            values.put(partDetail.PARTY_LOCATION,party.getLoc());
            pdbase.insert(partDetail.TABLE_PARTY, null, values);
            pdbase.setTransactionSuccessful();
            Log.v("insertparty","party   inserted");

        }
        catch(Exception ex){

            Log.v("insertparty","party cannot beinserted");
        }
        finally {
            pdbase.endTransaction();
        }
    }


    /*
     * UPDATE party into sqlite
     */
    public int updateParty(String date,String time,String loc,String venue,String contacts, String id) {
        try {
            String whereClause = PartDetail.MOVIE_ID + " =?";
            ContentValues values = new ContentValues();
            values.put(partDetail.PARTY_DATE, date);
            values.put(partDetail.PARTY_TIME, time);
            values.put(partDetail.PARTY_LOCATION, loc);
            values.put(partDetail.PARTY_VENUE, venue);
            values.put(partDetail.PARTY_CONTACTS, contacts.trim());
            Log.v("updatepartysqlite", "party updated");
           int i= pdbase.update(partDetail.TABLE_PARTY, values, whereClause, new String[]{id});
            Log.v("updateparty",i+"");
            return i;

        }
        catch(Exception ex){
            Log.v("updateparty","party cannot be updated");
            return -2;
        }

        }

//selects complete   party

    public Cursor selectparty(){
        Cursor cursor = pdbase.query(
             partDetail.TABLE_PARTY,
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