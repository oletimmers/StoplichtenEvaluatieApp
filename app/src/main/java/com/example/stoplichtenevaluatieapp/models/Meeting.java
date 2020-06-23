package com.example.stoplichtenevaluatieapp.models;


import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;

import java.text.SimpleDateFormat;
import java.util.List;

public class Meeting {
    public String ref;
    private String id;
    private Timestamp date;
    private String name;
    private String description;
    private List<Comment> comments;
    private long red;
    private long orange;
    private long green;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
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

    public long getOrange() {
        return orange;
    }

    public void setOrange(long orange) {
        this.orange = orange;
    }

    public long getGreen() {
        return green;
    }

    public void setGreen(long green) {
        this.green = green;
    }
}
