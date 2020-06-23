package com.example.stoplichtenevaluatieapp;

import android.os.Bundle;

import com.example.stoplichtenevaluatieapp.adapters.MeetingAdapter;
import com.example.stoplichtenevaluatieapp.models.Meeting;
import com.example.stoplichtenevaluatieapp.util.Parser;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class MeetingActivity extends AppCompatActivity {
    private static final String TAG = "MeetingActivity";

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    DocumentReference meetingRef;

    Meeting meeting;
    TextView meetingNameDisplay;
    TextView meetingDateDisplay;
    TextView meetingDescriptionDisplay;
    ImageView red;
    ImageView orange;
    ImageView green;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meeting);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        CollapsingToolbarLayout toolBarLayout = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);

        Bundle data = getIntent().getBundleExtra("data");
        String meetingJsonString = (String)data.get("Meeting");
        meeting = Parser.getGsonParser().fromJson(meetingJsonString, Meeting.class);

        meetingNameDisplay = findViewById(R.id.meeting_name);
        meetingDateDisplay = findViewById(R.id.meeting_date);
        meetingDescriptionDisplay = findViewById(R.id.meeting_description);

        red = findViewById(R.id.red_circle);
        red.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickRed();
            }
        });



        meetingNameDisplay.setText(meeting.getName());
        meetingDateDisplay.setText(meeting.getDateString());
        meetingDescriptionDisplay.setText(meeting.getDescription());

        toolBarLayout.setTitle(getString(R.string.meeting));

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

    }

    private void clickRed() {
        meetingRef = db.collection("meetings").document(meeting.ref);
        meetingRef
                .update("red", meeting.getRed() + 1)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "DocumentSnapshot successfully updated!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error updating document", e);
                    }
                });
    }
}