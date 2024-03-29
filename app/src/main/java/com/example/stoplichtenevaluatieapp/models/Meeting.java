package com.example.stoplichtenevaluatieapp.models;


import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class Meeting {
    public String ref;
    private String creator;
    private Timestamp date;
    private String name;
    private String description;
    private ArrayList<String> comments;
    private long red = 0;
    private long orange = 0;
    private long green = 0;
    private long totalVotes = 0;

    public Meeting() {}

    public Meeting(String creator, Timestamp date, String name, String description) {
        this.creator = creator;
        this.date = date;
        this.name = name;
        this.description = description;
        this.comments = new ArrayList<String>();
    }

    public Timestamp getDate() {
        return date;
    }

    public void setDate(Timestamp date) {
        this.date = date;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ArrayList<String> getComments() {
        return comments;
    }

    public void setComments(ArrayList<String> comments) {
        this.comments = comments;
    }

    public String getDateString() {
        SimpleDateFormat sfd = new SimpleDateFormat("dd-MM-yyyy");
        return sfd.format(this.date.toDate()).toString();
    }

    public long getRed() {
        return red;
    }

    public void setRed(long red) {
        this.red = red;
    }

    public long increaseRed() {
        this.red ++;
        this.totalVotes ++;
        return this.red;
    }

    public long getOrange() {
        return orange;
    }

    public void setOrange(long orange) {
        this.orange = orange;
    }

    public long increaseOrange() {
        this.orange ++;
        this.totalVotes ++;
        return this.orange;
    }

    public long getGreen() {
        return green;
    }

    public void setGreen(long green) {
        this.green = green;
    }

    public long increaseGreen() {
        this.green ++;
        this.totalVotes ++;
        return this.green;
    }

    public long getTotalVotes() {
        return totalVotes;
    }

    public void setTotalVotes(long totalVotes) {
        this.totalVotes = totalVotes;
    }

    public String getCreator() {
        return creator != null ? creator : "";
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }
}
