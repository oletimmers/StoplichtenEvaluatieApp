package com.example.stoplichtenevaluatieapp.models;


import com.google.firebase.Timestamp;

import java.util.List;

public class Meeting {
    public Timestamp date;
    public String name;
    public String description;
    public List<Comment> comments;

}
