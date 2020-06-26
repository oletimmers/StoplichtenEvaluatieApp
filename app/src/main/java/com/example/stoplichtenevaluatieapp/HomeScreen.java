package com.example.stoplichtenevaluatieapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;

import androidx.appcompat.app.AppCompatActivity;

import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

public class HomeScreen extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        final HomeScreen that = this;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);

        FloatingActionButton fab = findViewById(R.id.new_meeting_button);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(that, NewMeeting.class);
                startActivity(intent);
            }
        });


        FloatingActionButton logout = findViewById(R.id.fab_logout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new MaterialAlertDialogBuilder(that, R.style.Theme_MaterialComponents_MaterialAlertDialog)
                        .setTitle("Uitloggen")
                        .setMessage("Weet u zeker dat u wilt uitloggen?")
                        .setPositiveButton("Ja", new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int whichButton) {
                                FirebaseAuth.getInstance().signOut();
                                Toast.makeText(HomeScreen.this, R.string.signed_out, Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(that, MainActivity.class);
                                startActivity(intent);
                            }})
                        .setNegativeButton("Nee", null)
                        .show();
            }
        });
    }


}
