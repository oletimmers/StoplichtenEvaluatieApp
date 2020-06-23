package com.example.stoplichtenevaluatieapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.stoplichtenevaluatieapp.R;
import com.example.stoplichtenevaluatieapp.models.Meeting;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

public class MeetingAdapter extends ArrayAdapter<Meeting> {
    private ArrayList<Meeting> meetings;
    Context mContext;

    private static class MeetingItemHolder {
        TextView txtTitle;
        TextView txtDescription;
    }

    public MeetingAdapter(ArrayList<Meeting> meetings, Context context) {
        super(context, R.layout.meetingview_row, meetings);
        this.meetings = meetings;
        this.mContext = context;
    }

//    @Override
//    public void onClick(View v) {
//        int position=(Integer) v.getTag();
//        Object object= getItem(position);
//        Meeting meeting=(Meeting)object;
//
//        switch (v.getId())
//        {
//            case R.id.meeting:
//                Snackbar.make(v, "Open " +meeting.getName(), Snackbar.LENGTH_LONG)
//                        .setAction("No action", null).show();
//                break;
//        }
//    }



    private int lastPosition = -1;

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        Meeting meeting = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        MeetingItemHolder viewHolder; // view lookup cache stored in tag

        final View result;

        if (convertView == null) {

            viewHolder = new MeetingItemHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.meetingview_row, parent, false);
            viewHolder.txtTitle = (TextView) convertView.findViewById(R.id.meeting_title);
            viewHolder.txtDescription = (TextView) convertView.findViewById(R.id.meeting_subtitle);

            result=convertView;

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (MeetingItemHolder) convertView.getTag();
            result=convertView;
        }

        Animation animation = AnimationUtils.loadAnimation(mContext, (position > lastPosition) ? R.anim.up_from_bottom : R.anim.down_from_top);
        result.startAnimation(animation);
        lastPosition = position;

        viewHolder.txtTitle.setText(meeting.getName());
        viewHolder.txtDescription.setText(meeting.getDescription());
//        viewHolder.info.setOnClickListener(this);
//        viewHolder.info.setTag(position);
        // Return the completed view to render on screen
        return convertView;
    }

}
