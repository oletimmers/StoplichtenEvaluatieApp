package com.example.stoplichtenevaluatieapp.models;


import com.google.firebase.Timestamp;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class Meeting {
    private Timestamp date;
    private String name;
    private String description;
    private List<Comment> comments;


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

}
