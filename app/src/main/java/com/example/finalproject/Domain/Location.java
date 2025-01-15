package com.example.finalproject.Domain;

import androidx.annotation.NonNull;

public class Location {

    private int id;

    private String Loc;

    @NonNull
    @Override
    public String toString() {
        return  Loc ;
    }

    public Location(int id, String loc) {
        this.id = id;
        Loc = loc;
    }

    public Location() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLoc() {
        return Loc;
    }

    public void setLoc(String loc) {
        Loc = loc;
    }
}
