package com.example.stoplichtenevaluatieapp.models;

public class Comment {
    public String comment;
    public User user;

    public Comment(String comment, User user) {
        this.comment = comment;
        this.user = user;
    }
}
