package com.example.stoplichtenevaluatieapp;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.stoplichtenevaluatieapp.models.Meeting;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Set;

public class ListFragment extends Fragment {
    private static final String TAG = "ListFragment";

    ListView meetingsView;
    ArrayList<Meeting> meetings;

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
        meetings = new ArrayList<Meeting>();
        meetingsView = view.findViewById(R.id.meetingsThisDay);
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Date dt = new Date();
        Calendar yesterday = Calendar.getInstance();
        Calendar tomorrow = Calendar.getInstance();
        yesterday.setTime(dt);
        tomorrow.setTime(dt);
        yesterday.add(Calendar.DATE, -1);
        tomorrow.add(Calendar.DATE, 1);
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
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });

//        view.findViewById(R.id.button_first).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                NavHostFragment.findNavController(ListFragment.this)
//                        .navigate(R.id.action_First2Fragment_to_Second2Fragment);
//            }
//        });
    }
}
