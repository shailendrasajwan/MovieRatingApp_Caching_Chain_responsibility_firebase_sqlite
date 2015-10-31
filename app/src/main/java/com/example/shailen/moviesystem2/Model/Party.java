package com.example.shailen.moviesystem2.Model;

import com.example.shailen.moviesystem2.Model.Movies;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by shailen on 10/9/2015.
 */
public class Party {

   // private float current_rating;
    private String date;
    private String time;
    private Movies mov;
    private String loc ;
    private String venue;
     private String  imdbtitle;
    private String  movietitle;
    private String contacts;
    private ArrayList<String> names = new ArrayList<String>();


    public Party(Movies mov) {
        this.mov = mov;
    }
public Party(){

}


    public static HashMap<String,Party> firebaseparty=new HashMap<String,Party>();

    public String getImdbtitle() {
        return imdbtitle;
    }

    public void setImdbtitle(String imdbtitle) {
        this.imdbtitle = imdbtitle;
    }

    public String getMovietitle() {
        return movietitle;
    }

    public void setMovietitle(String movietitle) {
        this.movietitle = movietitle;
    }

    public void setDate(String date){
        this.date= date;
    }
    public void setTime(String time){
        this.time= time;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }

    public String getLoc() {
        return loc;
    }

    public void setLoc(String loc) {
        this.loc = loc;
    }

    public String getVenue() {
        return venue;
    }

    public void setVenue(String venue) {
        this.venue = venue;
    }

    public String getMovieName(){
        return this.mov.getTitle();
    }
    public ArrayList<String> getContacts(){
        return names;
    }
    public void setContactName(String s){
        names.add(s);
    }
    public void  delete(){names.remove(names.size() - 1);}
    public String getInvitee(){
       /* String s="";
        for(String name:names)
            s=name+s;
        s=s.trim();*/

        //return s;
    return contacts;
    }

    public  String  getContactInfo(){
        String s=" ";
        for(String name:names)
            s=s+name+"";
       // s=s.trim();

        return s;

    }

    public ArrayList<String> getContactName(){
        return  names;
    }
    public void setInvitee(String s){
        contacts=s.trim();
    }

    public Movies getMov() {
        return mov;
    }

    public void setMov(Movies mov) {
        this.mov = mov;
    }
}
