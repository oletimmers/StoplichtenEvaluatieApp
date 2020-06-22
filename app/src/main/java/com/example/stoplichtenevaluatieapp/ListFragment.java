package com.example.stoplichtenevaluatieapp;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.stoplichtenevaluatieapp.adapters.MeetingAdapter;
import com.example.stoplichtenevaluatieapp.models.Meeting;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class ListFragment extends Fragment {
    private static final String TAG = "ListFragment";

    TextView dateToShow;

    ArrayList<Meeting> meetings;
    ListView meetingsView;
    private static MeetingAdapter adapter;
    public static Date dt = new Date();

    RelativeLayout loadingPanel;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_list, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        dateToShow = view.findViewById(R.id.dateToShow);
        updateTime();

        meetingsView = (ListView)view.findViewById(R.id.meetingsThisDay);
        meetings = new ArrayList<Meeting>();

        loadingPanel = view.findViewById(R.id.loadingPanel);

        ImageView left = view.findViewById(R.id.button_left);
        ImageView right = view.findViewById(R.id.button_right);

        left.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(dt);
                calendar.add(Calendar.DATE, -1);
                dt = calendar.getTime();
                updateTime();

                meetings = new ArrayList<Meeting>();
                getMeetings();
            }
        });

        right.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(dt);
                calendar.add(Calendar.DATE, 1);
                dt = calendar.getTime();
                updateTime();

                meetings = new ArrayList<Meeting>();
                getMeetings();
            }
        });

        this.getMeetings();
    }

    public void getMeetings() {
        loadingPanel.setVisibility(View.VISIBLE);

        // Gisteren en morgen bepalen
        Calendar yesterday = Calendar.getInstance();
        Calendar tomorrow = Calendar.getInstance();
        yesterday.setTime(dt);
        tomorrow.setTime(dt);
        yesterday.add(Calendar.DATE, -1);
        tomorrow.add(Calendar.DATE, 0);
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("meetings")
                .whereGreaterThan("date", yesterday.getTime())
                .whereLessThan("date", tomorrow.getTime())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Meeting meetingToAdd = document.toObject(Meeting.class);
                                meetings.add(meetingToAdd);
//                                Log.d(TAG, document.getId() + " => " + document.getData());
//                                Log.d(TAG,"MEETING NAAM: "+ meetingToAdd.name);
                            }
                            adapter = new MeetingAdapter(meetings, getContext());
                            meetingsView.setAdapter(adapter);
                            loadingPanel.setVisibility(View.GONE);
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
    }

    public void updateTime() {
        SimpleDateFormat sfd = new SimpleDateFormat("dd-MM-yyyy");
        dateToShow.setText(sfd.format(dt));
    }
}
