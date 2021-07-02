package com.kgec.yourthoughtsapp;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ThoughtDetails implements Serializable {

    private int id;
    private String name;
    private String date;
    private String thought;
    private int likes;
    private int commentCount;
    private int comments;
    private int photo;
    private String uid;



    public ThoughtDetails() {

        //name= "Paul";
        SimpleDateFormat sdf=new SimpleDateFormat("MMM d , yyyy", Locale.getDefault());
        date=sdf.format(new java.util.Date());
        likes=0;
        comments=0;
        id++;
    }

    public ThoughtDetails(String date, String thought) {
        SimpleDateFormat sdf=new SimpleDateFormat("MMM d , yyyy", Locale.getDefault());
        date=sdf.format(new java.util.Date());
        this.date = date;
        this.thought = thought;
    }

    public ThoughtDetails(String name, Date ccdate, String thought, int commentCount) {
        SimpleDateFormat sdf=new SimpleDateFormat("MMM d , yyyy", Locale.getDefault());
        date=sdf.format(ccdate).toString();
        this.name = name;
        this.date = date;
        this.thought = thought;
        this.commentCount=commentCount;
    }

    public ThoughtDetails(String uid) {
        this.uid = uid;
        SimpleDateFormat sdf=new SimpleDateFormat("MMM d , yyyy", Locale.getDefault());
        date=sdf.format(new java.util.Date());
        likes=0;
        comments=0;
        id++;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String  uid) {
        this.uid = uid;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDate() {

        return date;

    }

    public int getPhoto() {
        return photo;
    }

    public void setPhoto(int photo) {
        this.photo = photo;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getThought() {
        return thought;
    }

    public int getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(int commentCount) {
        this.commentCount = commentCount;
    }

    public void setThought(String thought) {
        this.thought = thought;
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public int getComments() {
        return comments;
    }

    public void setComments(int comments) {
        this.comments = comments;
    }
}



