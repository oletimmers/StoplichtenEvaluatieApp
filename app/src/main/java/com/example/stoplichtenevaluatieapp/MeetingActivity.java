package com.example.stoplichtenevaluatieapp;

import android.content.Intent;
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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
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

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.Map;

public class MeetingActivity extends AppCompatActivity {
    private static final String TAG = "MeetingActivity";
    MeetingActivity that = this;

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    DocumentReference meetingRef;

    Meeting meeting;
    TextView explainButtons;
    TextView meetingNameDisplay;
    TextView meetingDateDisplay;
    TextView meetingDescriptionDisplay;

    ImageView red;
    ImageView orange;
    ImageView green;

    TextView rateRed;
    TextView rateOrange;
    TextView rateGreen;

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
        meetingRef = db.collection("meetings").document(meeting.ref);


        /*
        Vanaf hier is de meeting in de activity geladen
         */
        hasAlreadyVoted();

        explainButtons = findViewById(R.id.explain_buttons);
        meetingNameDisplay = findViewById(R.id.meeting_name);
        meetingDateDisplay = findViewById(R.id.meeting_date);
        meetingDescriptionDisplay = findViewById(R.id.meeting_description);

        red = findViewById(R.id.red_circle);
        orange = findViewById(R.id.orange_circle);
        green = findViewById(R.id.green_circle);

        rateRed = findViewById(R.id.rateRed);
        rateOrange = findViewById(R.id.rateOrange);
        rateGreen = findViewById(R.id.rateGreen);

        rateRed.setVisibility(View.GONE);
        rateOrange.setVisibility(View.GONE);
        rateGreen.setVisibility(View.GONE);

        meetingNameDisplay.setText(meeting.getName());
        meetingDateDisplay.setText(meeting.getDateString());
        meetingDescriptionDisplay.setText(meeting.getDescription());

        toolBarLayout.setTitle(getString(R.string.meeting));

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.delete_meeting_button);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

    }



    private void clickRed() {
        meetingRef
                .update(
                        "red", FieldValue.increment(1),
                        "totalVotes", FieldValue.increment(1)
                )
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "DocumentSnapshot successfully updated!");
                        meeting.increaseRed();
                        registerVote();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error updating document", e);
                    }
                });

    }
    private void clickOrange() {
        meetingRef
                .update("orange", FieldValue.increment(1),
                        "totalVotes", FieldValue.increment(1))
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "DocumentSnapshot successfully updated!");
                        meeting.increaseOrange();
                        registerVote();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error updating document", e);
                    }
                });
    }
    private void clickGreen() {
        meetingRef
                .update("green", FieldValue.increment(1),
                        "totalVotes", FieldValue.increment(1))
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "DocumentSnapshot successfully updated!");
                        meeting.increaseGreen();
                        registerVote();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error updating document", e);
                    }
                });
    }

    private void registerVote() {
        Map<String, Object> data = new HashMap<>();
        data.put("userID", FirebaseAuth.getInstance().getCurrentUser().getUid());
        data.put("meetingID", meeting.ref);
        db.collection("meetingVotes").add(data)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d(TAG, "DocumentSnapshot written with ID: " + documentReference.getId());
                        finish();
                        Bundle b = new Bundle();
                        String meetingJsonString = Parser.getGsonParser().toJson(meeting);
                        b.putString("Meeting", meetingJsonString);
                        Intent intent = getIntent();
                        intent.putExtra("data", b);
                        startActivity(intent);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error adding document", e);
                    }
                });
    }

    private void hasAlreadyVoted() {
        db.collection("meetingVotes")
                .whereEqualTo("userID", FirebaseAuth.getInstance().getCurrentUser().getUid())
                .whereEqualTo("meetingID", meeting.ref)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            if(task.getResult().isEmpty()){
                                mayVote();
                            } else {
                                mayNotVote();
                            }
                        }
                    }
                });
    }

    private void mayVote() {
        explainButtons.setText(getText(R.string.explainer_traffic_lights));
        red.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickRed();
            }
        });
        orange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickOrange();
            }
        });
        green.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickGreen();
            }
        });
    }

    private void mayNotVote() {
        explainButtons.setText(getText(R.string.explainer_already_voted));
        red.setOnClickListener(null);
        orange.setOnClickListener(null);
        green.setOnClickListener(null);
        rateRed.setVisibility(View.VISIBLE);
        rateOrange.setVisibility(View.VISIBLE);
        rateGreen.setVisibility(View.VISIBLE);
        setPercentages();
    }

    private void setPercentages() {
        Double percentageRed = 0D;
        Double percentageOrange = 0D;
        Double percentageGreen = 0D;
        if (meeting.getRed() > 0) {
            percentageRed = ((double) meeting.getRed())/meeting.getTotalVotes() * 100;
        }
        if (meeting.getOrange() > 0) {
            percentageOrange = ((double) meeting.getOrange())/meeting.getTotalVotes() * 100;
        }
        if (meeting.getGreen() > 0) {
            percentageGreen = ((double) meeting.getGreen())/meeting.getTotalVotes() * 100;
        }
        BigDecimal bgRed = new BigDecimal(percentageRed).setScale(1, RoundingMode.CEILING);
        BigDecimal bgOrange = new BigDecimal(percentageOrange).setScale(1, RoundingMode.CEILING);
        BigDecimal bgGreen = new BigDecimal(percentageGreen).setScale(1
                , RoundingMode.CEILING);
        rateRed.setText(bgRed.toString() + "%");
        rateOrange.setText(bgOrange.toString() + "%");
        rateGreen.setText(bgGreen.toString() + "%");
    }

}