package com.example.shailen.moviesystem2;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.ContentProviderOperation;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.provider.ContactsContract;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.shailen.moviesystem2.Model.ContactList;
import com.example.shailen.moviesystem2.Model.Movies;
import com.example.shailen.moviesystem2.Model.Party;
import com.example.shailen.moviesystem2.db.PartDetail;
import com.example.shailen.moviesystem2.db.PartySource;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;


public class MyParty extends Activity implements AdapterView.OnItemSelectedListener {
    private TextView movie_title;
    private EditText date;
    private EditText time;
    private EditText loc;
    private EditText venue;
    private TextView rating_see;
    private Movies movieObject;
    private Party movie_party;
    private Button contacts;
    private int firebasecheck=0;
    public static HashMap<String,Party> firebaseparty=new HashMap<String,Party>();
    final Calendar c = Calendar.getInstance();
    private static final int CONTACT_PICKER_RESULT = 1001;
    public final int PICK_CONTACTS = 100;
    private TextView people;
    private Button deleteButton;
    private Button invite;
    private PartySource partySource;
    private static int i=0;
    public static final String TAG = MyParty.class.getSimpleName();
    private static HashMap<String,String> mapparty=new HashMap<String,String>();
    HashMap<String,Object> mapparty1;
    private ArrayList<Party> lstparty= new ArrayList<Party>();
    private static ArrayList<Party> objParty=new ArrayList<Party>();
    private static boolean checkstatus=false;
    public static  HashMap<String,Party> dbparties= new HashMap<String,Party>();
    private static HashMap<String,Party>  currentmap=new HashMap<String,Party>();
    private Party currentdetails=new Party();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_party);
        movieObject =(Movies)getIntent().getParcelableExtra(Moviedetails.PARTY);
        movie_party = new Party(movieObject);
        //   movie_party.setCurrent_rating();
        partySource=new PartySource(MyParty.this);
        //sets layout elements
        initialize();
    Log.v("oncreatecalled", movieObject.getTitle());
        mapparty1=new HashMap<String,Object>();
        //   rating_see.setText(String.valueOf(movie_party.getCurrent_rating()));
        movie_title.setText((CharSequence) movie_party.getMovieName());
        //sets  date
        setCurrentDateOnView();
        //set time on  activity
        setCurrentTimeOnView();
        //adds contacts to contactpicker
        addContacts();
        //loads all the parties
      loadMemory();

        //Add contact listenerr
        addListenerToButton(contacts);
        //invite button listener
        addListenerToInviteButton(invite);
        //delete button listener
        addListenerToDeleteButton();
        /*if(checkNetwork()){
            if(firebasecheck==-1)

        }*/
    }

public void  updateofflineparty(){
    if(checkNetwork())
        offlineParty();
    Log.v("offlineparty updated","automatically in onresume");
    Log.v(TAG, "---------- Database succesfully created -------------");
}


    private void addListenerToDeleteButton() {
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(people.getText()!=null){
                    String[] arr=people.getText().toString().split(" ");
                    String ppl=" ";
                    for(int i=0;i<arr.length-1;i++) {
                        ppl=ppl+arr[i]+" ";
                    }
                    Log.v("peopleupdated",ppl);
                    people.setText(ppl);
                }


                  try{
               if(!movie_party.getContacts().isEmpty()){
                movie_party.delete();
                ArrayList<String> contact_list = movie_party.getContacts();
                Iterator<String> itr = contact_list.iterator();
                String con = "";
                while(itr.hasNext()){
                    con = con + itr.next()+ " ";
                }
                people.setText(con);
            }}
            catch(Exception ex){
                Log.v("peopleupdated",ex.toString());
            }}
        };

        deleteButton.setOnClickListener(listener);
    }

    private void addContacts() {

        Resources res = getResources();
        String[] cont= res.getStringArray(R.array.contactarray);
        ArrayList<ContactList> my_contacts = new ArrayList<ContactList>();

        for(int i=0;i<cont.length;i++)
        {
            String[] details=cont[i].toString().split(",");

            ContactList contact = new ContactList(details[0].toString(),details[1].toString(),details[2].toString(),details[3].toString(),details[4].toString(),details[5].toString(),details[6].toString());
            my_contacts.add(contact);
        }


        Iterator<ContactList> itr = my_contacts.iterator();
        while (itr.hasNext()) {
            ContactList element = itr.next();
            ArrayList<ContentProviderOperation> ops = new ArrayList<>();
            ops.add(ContentProviderOperation.newInsert(
                    ContactsContract.RawContacts.CONTENT_URI)
                    .withValue(ContactsContract.RawContacts.ACCOUNT_TYPE, null)
                    .withValue(ContactsContract.RawContacts.ACCOUNT_NAME, null)
                    .build());

            //------------------------------------------------------ Names
            if (element.getDisplayName() != null) {
                ops.add(ContentProviderOperation.newInsert(
                        ContactsContract.Data.CONTENT_URI)
                        .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                        .withValue(ContactsContract.Data.MIMETYPE,
                                ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE)
                        .withValue(
                                ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME,
                                element.getDisplayName()).build());
            }

            //------------------------------------------------------ Mobile Number
            if (element.getMobileNumber()!= null) {
                ops.add(ContentProviderOperation.
                        newInsert(ContactsContract.Data.CONTENT_URI)
                        .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                        .withValue(ContactsContract.Data.MIMETYPE,
                                ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
                        .withValue(ContactsContract.CommonDataKinds.Phone.NUMBER,element.getMobileNumber())
                        .withValue(ContactsContract.CommonDataKinds.Phone.TYPE,
                                ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE)
                        .build());
            }

            //------------------------------------------------------ Home Numbers
            if (element.getHomeNumber()!= null) {
                ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                        .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                        .withValue(ContactsContract.Data.MIMETYPE,
                                ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
                        .withValue(ContactsContract.CommonDataKinds.Phone.NUMBER,element.getHomeNumber())
                        .withValue(ContactsContract.CommonDataKinds.Phone.TYPE,
                                ContactsContract.CommonDataKinds.Phone.TYPE_HOME)
                        .build());
            }

            //------------------------------------------------------ Work Numbers
            if (element.getWorkNumber()!= null) {
                ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                        .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                        .withValue(ContactsContract.Data.MIMETYPE,
                                ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
                        .withValue(ContactsContract.CommonDataKinds.Phone.NUMBER, element.getWorkNumber())
                        .withValue(ContactsContract.CommonDataKinds.Phone.TYPE,
                                ContactsContract.CommonDataKinds.Phone.TYPE_WORK)
                        .build());
            }

            //------------------------------------------------------ Email
            if (element.getEmailID()!= null) {
                ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                        .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                        .withValue(ContactsContract.Data.MIMETYPE,
                                ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE)
                        .withValue(ContactsContract.CommonDataKinds.Email.DATA, element.getEmailID())
                        .withValue(ContactsContract.CommonDataKinds.Email.TYPE, ContactsContract.CommonDataKinds.Email.TYPE_WORK)
                        .build());
            }

            //------------------------------------------------------ Organization
            if (!element.getCompany().equals("") && !element.getJobTitle().equals("")) {
                ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                        .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                        .withValue(ContactsContract.Data.MIMETYPE,
                                ContactsContract.CommonDataKinds.Organization.CONTENT_ITEM_TYPE)
                        .withValue(ContactsContract.CommonDataKinds.Organization.COMPANY, element.getCompany())
                        .withValue(ContactsContract.CommonDataKinds.Organization.TYPE, ContactsContract.CommonDataKinds.Organization.TYPE_WORK)
                        .withValue(ContactsContract.CommonDataKinds.Organization.TITLE, element.getJobTitle())
                        .withValue(ContactsContract.CommonDataKinds.Organization.TYPE, ContactsContract.CommonDataKinds.Organization.TYPE_WORK)
                        .build());
            }

            // Asking the Contact provider to create a new contact
            try {
                getContentResolver().applyBatch(ContactsContract.AUTHORITY, ops);

            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(MyParty.this, "Exception: " + e.getMessage(), Toast.LENGTH_LONG).show();
            }
            ops.clear();
        }
    }
    private void initialize() {
        date = (EditText) findViewById(R.id.date);
        time = (EditText) findViewById(R.id.time);
        loc = (EditText) findViewById(R.id.address);
        venue = (EditText) findViewById(R.id.venue);
        // rating_see = (TextView)findViewById(R.id.rating);
        contacts = (Button) findViewById(R.id.contacts);
        movie_title = (TextView) findViewById(R.id.PartyMovie);
        people = (TextView) findViewById(R.id.people);
        deleteButton = (Button) findViewById(R.id.delete);
        invite=(Button) findViewById(R.id.buttonInvitation);
    }


    private void loadMemory() {

        try {
            partySource.open();
        } catch (SQLException e) {
            e.printStackTrace();
        }
      // if(i==0) {
           try {

               Cursor cursor = partySource.selectparty();
               if (cursor != null) {
                   cursor.moveToFirst();

                   while (!cursor.isAfterLast()) {
                       Party party = new Party();
                       party.setDate(cursor.getString(cursor.getColumnIndex(PartDetail.PARTY_DATE)));
                       party.setTime(cursor.getString(cursor.getColumnIndex(PartDetail.PARTY_TIME)));
                       party.setContactName(cursor.getString(cursor.getColumnIndex(PartDetail.PARTY_CONTACTS)));
                       party.setInvitee(cursor.getString(cursor.getColumnIndex(PartDetail.PARTY_CONTACTS)));
                       party.setLoc(cursor.getString(cursor.getColumnIndex(PartDetail.PARTY_LOCATION)));
                       party.setVenue(cursor.getString(cursor.getColumnIndex(PartDetail.PARTY_VENUE)));
                       party.setMovietitle(cursor.getString(cursor.getColumnIndex(PartDetail.MOVIE_TITLE)));
                       party.setImdbtitle(cursor.getString(cursor.getColumnIndex(PartDetail.MOVIE_ID)));
                       lstparty.add(party);
                       dbparties.put(movieObject.getTitle(), party);
                       cursor.moveToNext();

                   }

                 //  i++;
                   Log.v("loadmemory", "successful" + i);

               }
           } catch (Exception ex) {
               Log.v("loadmemory", "nullset");
           }

      // }
        if(dbparties .containsKey(movieObject.getTitle())){
            Party objparty=new Party();
           objparty=dbparties.get(movieObject.getTitle());
            date.setText((CharSequence)objparty.getDate());
            time.setText((CharSequence)objparty.getTime());
            loc.setText((CharSequence)objparty.getLoc());
            venue.setText((CharSequence)objparty.getVenue());
            people.setText((CharSequence)objparty.getContactInfo());
            //Log.v("partymap",objparty.get);
            //people.setText((CharSequence)objparty.getContacts().get(0));

        }
/*        if(currentmap.containsKey(movieObject.getImdbId().trim())){

            date.setText((CharSequence) currentmap.get(movieObject.getImdbId()).getDate());
            time.setText((CharSequence)currentmap.get(movieObject.getImdbId()).getTime());
            loc.setText((CharSequence)currentmap.get(movieObject.getImdbId()).getLoc());
            venue.setText((CharSequence)currentmap.get(movieObject.getImdbId()).getVenue());
            people.setText((CharSequence)currentmap.get(movieObject.getImdbId()).getInvitee());
            Log.v("loadmemory", "successful" + i);

        }*/


    }


    //invitee  button listener
    public void addListenerToInviteButton(Button invite){
        View.OnClickListener listner= new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                updateFirebase();

               checkParty();

            }
        };
        invite.setOnClickListener(listner);
    }



    public void   updatepartyContent(){
        movie_party.setImdbtitle(movieObject.getImdbId().trim());
        movie_party.setMovietitle(movieObject.getTitle());
        movie_party.setLoc(loc.getText().toString());
        movie_party.setVenue(venue.getText().toString());
        movie_party.setDate(date.getText().toString());
        movie_party.setTime(time.getText().toString());
        movie_party.setInvitee(people.getText().toString());
Log.v("updatepartycontent", movieObject.getImdbId() + movieObject.getTitle() + venue.getText().toString() + date.getText().toString() + time.getText().toString() + people.getText().toString());


    }
    //update data to firebase
    public void updateFirebase(){
  updatepartyContent();

        if(checkNetwork()){
            Firebase.setAndroidContext(getApplicationContext());
            Firebase cloud = new Firebase("https://blistering-torch-2418.firebaseio.com/");
            Firebase alanRef = cloud.child("party").child(movieObject.getTitle());
            alanRef.setValue(movie_party);
                Log.v("firebaseparty", "parrty existing updated in firebase");
                if(checkstatus==true) {
                    offlineParty();
                }



        }

        else{

            checkstatus=true;
            Party p=new Party();
            p.setDate(movie_party.getDate());
            p.setInvitee(movie_party.getContactInfo());
            p.setMov(movie_party.getMov());
            p.setLoc(movie_party.getLoc());
            p.setTime(movie_party.getTime());
            p.setVenue(movie_party.getVenue());
            p.setImdbtitle(movie_party.getImdbtitle());
            p.setMovietitle(movie_party.getMovietitle());
            objParty.add(p);
               // firebaseparty.put(movieObject.getTitle(),movie_party);
            Log.v("firebaseparty", "static map party insertion error");
        }

    }


    //offline party

    public void offlineParty(){
          if(!objParty.isEmpty()) {
              Firebase.setAndroidContext(getApplicationContext());
              Party pp = objParty.get(0);
              Firebase cloud1 = new Firebase("https://blistering-torch-2418.firebaseio.com/");
              Firebase alanRef1 = cloud1.child("party").child(pp.getMovietitle());
              alanRef1.setValue(pp);
              checkstatus = false;
              objParty.clear();
              Log.v("offlinefirebaseparty", "parrty updated in firebase");

          }

    }

    //checkks on adding invite whether party  exists
    private void  checkParty(){
        Log.v("checkparty", "checkpartycalled");
        String s=date.getText().toString().trim()+time.getText().toString().trim()+people.getText().toString()+loc.getText().toString().trim() + venue.getText().toString().trim();
        if (dbparties.containsKey(movieObject.getTitle())) {
            Party p = dbparties.get(movieObject.getTitle());
            String myparty = p.getDate().trim()+p.getTime().trim()+p.getContactInfo().trim() + p.getLoc().trim()+p.getVenue().trim();
            if (myparty.toLowerCase().equals(s.toLowerCase())){
                Log.v("party", "already exists");
            } else {
                movie_party.setImdbtitle(movieObject.getImdbId());
                movie_party.setMovietitle(movieObject.getTitle());
                movie_party.setLoc(loc.getText().toString());
                movie_party.setVenue(venue.getText().toString());
                movie_party.setDate(date.getText().toString());
                movie_party.setTime(time.getText().toString());
                movie_party.setInvitee(people.getText().toString());

             //   partySource.insertParty(movie_party, movieObject);
                dbparties.put(movieObject.getTitle(), movie_party);
                Log.v("hashmapupdate",movie_party.getMovietitle());
                partySource.updateParty(date.getText().toString(), time.getText().toString(), loc.getText().toString(), venue.getText().toString(), people.getText().toString(), movieObject.getImdbId());


                lstparty.add(movie_party);
                Log.v("party", "parrty updated");

            }

        }

        else{
          movie_party.setImdbtitle(movieObject.getImdbId());
            movie_party.setMovietitle(movieObject.getTitle());
            movie_party.setLoc(loc.getText().toString());
            movie_party.setVenue(venue.getText().toString());
            movie_party.setDate(date.getText().toString());
            movie_party.setTime(time.getText().toString());
            movie_party.setInvitee(people.getText().toString());
            partySource.insertParty(movie_party, movieObject);
            dbparties.put(movieObject.getTitle(), movie_party);
            lstparty.add(movie_party);
                Log.v("partinserted", movie_party.getImdbtitle());
        }
    }






    public void addListenerToButton(Button contacts){
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    movie_party.setInvitee(people.getText().toString());

                Log.v("added","people.getText().toString()");
                Intent i = new Intent(Intent.ACTION_PICK, ContactsContract.CommonDataKinds.Phone.CONTENT_URI);
                startActivityForResult(i,101);
            }
        };
        contacts.setOnClickListener(listener);
    }

    public void setCurrentDateOnView() {
        String dateFormat = "MM/dd/yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat, Locale.US);
        date.setText(sdf.format(c.getTime()));
        movie_party.setDate(sdf.format(c.getTime()));
    }

    public void setCurrentTimeOnView() {
        String timeFormat = "hh:mm a";
        SimpleDateFormat stf = new SimpleDateFormat(timeFormat, Locale.US );
        time.setText(stf.format(c.getTime()));
        movie_party.setTime(stf.format(c.getTime()));
    }

    DatePickerDialog.OnDateSetListener my_date = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet( DatePicker view, int year, int monthOfYear, int dayOfMonth ) {
            c.set( Calendar.YEAR, year );
            c.set( Calendar.MONTH, monthOfYear );
            c.set( Calendar.DAY_OF_MONTH, dayOfMonth );
            setCurrentDateOnView();
        }
    };
    public void dateOnClick(View view){
        new DatePickerDialog( MyParty.this, my_date,
                c.get( Calendar.YEAR ), c.get( Calendar.MONTH ), c.get( Calendar.DAY_OF_MONTH ) ).show();
    }
    TimePickerDialog.OnTimeSetListener my_time = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            c.set(Calendar.HOUR_OF_DAY,hourOfDay);
            c.set(Calendar.MINUTE,minute);
            setCurrentTimeOnView();
        }
    };
    public void timeOnClick(View view){
        new TimePickerDialog(MyParty.this,my_time,c.get(Calendar.HOUR),c.get(Calendar.MINUTE),false).show();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String selectedItem = parent.getItemAtPosition(position).toString();
        Toast.makeText(MyParty.this,
                String.valueOf(selectedItem),
                Toast.LENGTH_LONG).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            Uri uri = data.getData();
            Cursor cursor = getContentResolver().query(uri, null, null, null, null);
            cursor.moveToFirst();
               try {
                   int contactIndex = cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME);
                   if(movie_party.getContacts().isEmpty())
                       movie_party.setContactName(cursor.getString(contactIndex));
                   if(!movie_party.getContacts().contains(cursor.getString(contactIndex)))
                       movie_party.setContactName(cursor.getString(contactIndex));
                   ArrayList<String> contact_list = movie_party.getContacts();
                   Iterator<String> itr = contact_list.iterator();
                   String con = "";
                   while (itr.hasNext()) {
                   //   if (!itr.next().equals("Invitee"))
                           con = con + itr.next() + " ";

                       Log.v("contacts", con);
                   }

                   people.setText(con);
                   Log.v("datacontactpicker", con);
               }
               catch(Exception ex){
                   Log.v("contacts",ex.toString());
               }
        }

    }



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
    @Override
    protected void onResume(){

        super.onResume();

        try {
            partySource.open();
            if(checkNetwork()){
            offlineParty();
            Log.v("offlineparty updated","automatically in onresume");
            Log.v(TAG, "---------- Database succesfully created -------------");}

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void onPause() {
        super.onPause();

       /* currentdetails.setDate(date.getText().toString());
        currentdetails.setTime(time.getText().toString());
        currentdetails.setLoc(loc.getText().toString());
        currentdetails.setVenue(venue.getText().toString());

        currentdetails.setInvitee(people.getText().toString());
           currentmap.put(movieObject.getTitle(), currentdetails);*/



        partySource.close();
    }
}
