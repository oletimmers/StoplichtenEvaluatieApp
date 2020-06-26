package com.example.stoplichtenevaluatieapp;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import com.example.stoplichtenevaluatieapp.models.Meeting;
import com.example.stoplichtenevaluatieapp.util.Parser;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class NewMeeting extends AppCompatActivity {
    private static final String TAG = "NewMeetingActivity";


    private int mYear,mMonth,mDay;
    Timestamp newMeetingDate;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_meeting);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        CollapsingToolbarLayout toolBarLayout = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);
        toolBarLayout.setTitle(getString(R.string.new_meeting_title));

        final Button newMeetingDateButton;
        final TextView newMeetingName;
        final TextView newMeetingDescription;

        newMeetingDateButton = findViewById(R.id.new_meeting_date_button);
        newMeetingName = findViewById(R.id.new_meeting_name);
        newMeetingDescription = findViewById(R.id.new_meeting_description);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.save_meeting );
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (newMeetingDate != null && newMeetingName.getText().length() > 0 && newMeetingDescription.getText().length() > 0) {
                    Meeting meetingToPost = new Meeting(FirebaseAuth.getInstance().getCurrentUser().getUid(), newMeetingDate, newMeetingName.getText().toString(), newMeetingDescription.getText().toString());
                    postNewMeeting(meetingToPost);
                } else {
                    Toast.makeText(NewMeeting.this, R.string.incorrect_values_new_meeting, Toast.LENGTH_LONG).show();
                }
            }
        });

        FloatingActionButton backToHome = (FloatingActionButton) findViewById(R.id.back_to_home);
        backToHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(NewMeeting.this, HomeScreen.class);
                startActivity(intent);
            }
        });



        final Calendar myCalendar = Calendar.getInstance();
        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener()
        {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                String myFormat = "dd-MM-yyyy"; //In which you need put here
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat);
                newMeetingDateButton.setText(sdf.format(myCalendar.getTime()));
            }


        };

        newMeetingDateButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dpd = new DatePickerDialog(NewMeeting.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                // Display Selected date in textbox

                                if (year < mYear)
                                    view.updateDate(mYear,mMonth,mDay);

                                if (monthOfYear < mMonth && year == mYear)
                                    view.updateDate(mYear,mMonth,mDay);

                                if (dayOfMonth < mDay && year == mYear && monthOfYear == mMonth)
                                    view.updateDate(mYear,mMonth,mDay);

                                myCalendar.set(Calendar.YEAR, year);
                                myCalendar.set(Calendar.MONTH, monthOfYear);
                                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                                myCalendar.set(Calendar.HOUR_OF_DAY, 0);
                                myCalendar.set(Calendar.MINUTE, 0);
                                myCalendar.set(Calendar.SECOND, 0);
                                myCalendar.set(Calendar.MILLISECOND, 0);
                                Date dt = myCalendar.getTime();
                                newMeetingDate =  new Timestamp(dt);
                                newMeetingDateButton.setText(dayOfMonth + "-"
                                        + (monthOfYear + 1) + "-" + year);

                            }
                        }, mYear, mMonth, mDay);
                dpd.show();

            }
        });
    }

    private void postNewMeeting(final Meeting meetingToPost) {
        db.collection("meetings")
                .add(meetingToPost)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        meetingToPost.ref = documentReference.getId();
                        Bundle b = new Bundle();
                        String meetingJsonString = Parser.getGsonParser().toJson(meetingToPost);
                        b.putString("Meeting", meetingJsonString);
                        Intent intent = new Intent(NewMeeting.this, MeetingActivity.class);
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
}