package com.example.stoplichtenevaluatieapp;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.stoplichtenevaluatieapp.adapters.MeetingAdapter;
import com.example.stoplichtenevaluatieapp.models.Meeting;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.lang.reflect.Array;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Set;

public class ListFragment extends Fragment {
    private static final String TAG = "ListFragment";

    ArrayList<Meeting> meetings;
    ListView meetingsView;
    private static MeetingAdapter adapter;
    public static Date dt = new Date();

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

        meetingsView = (ListView)view.findViewById(R.id.meetingsThisDay);
        meetings = new ArrayList<Meeting>();
        this.getMeetings();
    }

    public void getMeetings() {
        // Gisteren en morgen bepalen
        Snackbar.make(this.getView(), "Get meetings", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
        Calendar yesterday = Calendar.getInstance();
        Calendar tomorrow = Calendar.getInstance();
        yesterday.setTime(dt);
        tomorrow.setTime(dt);
        yesterday.add(Calendar.DATE, -1);
        tomorrow.add(Calendar.DATE, 1);
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

                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
    }
}
