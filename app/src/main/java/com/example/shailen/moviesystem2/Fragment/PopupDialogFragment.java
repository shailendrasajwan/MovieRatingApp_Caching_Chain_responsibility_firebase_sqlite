package com.example.shailen.moviesystem2.Fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import java.util.logging.Logger;

/**
 * Created by shailen on 10/12/2015.
 */
//class for  creating dialog
public class PopupDialogFragment extends DialogFragment  {

@Override
public Dialog onCreateDialog(Bundle savedInstanceState){
   Context  context=getActivity();
    AlertDialog.Builder  builder=new AlertDialog.Builder(context)
             .setTitle("Network down")
             .setMessage("Please try when network resumes")
            .setPositiveButton("OK", null);
    AlertDialog dialog =builder.create();
    Log.v("Alertdialog","created");
    return dialog;
}






}
