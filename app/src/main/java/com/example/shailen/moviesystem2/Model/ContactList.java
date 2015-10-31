package com.example.shailen.moviesystem2.Model;

/**
 * Created by shailen on 10/9/2015.
 */
//model for contact picker details
public class ContactList {

    String DisplayName;
    String MobileNumber;
    String HomeNumber;
    String WorkNumber;
    String emailID;
    String company;
    String jobTitle;
    //, String MobileNumber, String HomeNumber, String WorkNumber
    public ContactList( String DisplayName, String emailID, String MobileNumber, String HomeNumber, String WorkNumber, String company, String jobTitle){
        this.DisplayName = DisplayName;
       this.MobileNumber=MobileNumber;
        this.HomeNumber = HomeNumber;
        this.WorkNumber = WorkNumber;
        this.emailID = emailID;
        this.company = company;
        this.jobTitle = jobTitle;
    }

    public String getDisplayName(){
        return this.DisplayName;
    }
    public String getMobileNumber(){
        return this.MobileNumber;
    }
    public String getHomeNumber(){
        return HomeNumber;
    }
    public String getWorkNumber(){
        return WorkNumber;
    }
    public String getEmailID(){
        return emailID;
    }
    public String getCompany(){
        return company;
    }
    public String getJobTitle(){
        return jobTitle;
    }

}
