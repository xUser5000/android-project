package com.example.bookapp.model;

public class Category {

    String id , category , uid;
    long timestamp;

    //constructor for firebase
    public Category(){

    }
    //parametrized constructor


    public Category(String id, String category, String uid, long timestamp) {
        this.id = id;
        this.category=category;
        this.uid=uid;
        this.timestamp = timestamp;
    }
    //setter & getter

    public String getId() {

        return id;
    }

    public String getCategory() {

        return category;
    }

    public String getUid() {

        return uid;
    }

    public long getTimestamp() {

        return timestamp;
    }

    public void setId(String id) {

        this.id = id;
    }

    public void setCategory(String category) {

        this.category = category;
    }

    public void setUid(String uid) {

        this.uid = uid;
    }

    public void setTimestamp(long timestamp) {

        this.timestamp = timestamp;
    }
}
