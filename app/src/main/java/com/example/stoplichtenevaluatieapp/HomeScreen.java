package com.example.stoplichtenevaluatieapp;

import android.app.Activity;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.ContentView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class HomeScreen extends AppCompatActivity {

    TextView dateToShow;
    private ListFragment listFragment;
    HomeScreen that;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        that = this;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);
        dateToShow = findViewById(R.id.dateToShow);
        SimpleDateFormat sfd = new SimpleDateFormat("dd-MM-yyyy");
        dateToShow.setText(sfd.format(new Date()));

        ImageView left = findViewById(R.id.button_left);
        ImageView right = findViewById(R.id.button_right);
        listFragment = (ListFragment)getSupportFragmentManager().findFragmentById(R.id.list_fragment);

        left.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                Calendar calendar = Calendar.getInstance();
                calendar.setTime(ListFragment.dt);
                calendar.add(Calendar.DATE, -1);
                ListFragment.dt = calendar.getTime();
                getSupportFragmentManager().beginTransaction();

            }
        });


        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Nieuwe bijeenkomst toevoegen", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }


}
