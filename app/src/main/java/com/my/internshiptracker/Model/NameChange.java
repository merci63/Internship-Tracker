package com.my.internshiptracker.Model;

import com.google.firebase.Timestamp;

public class NameChange {
    private Timestamp timestamp;
    public NameChange(){}
    public NameChange(Timestamp timestamp){
        this.timestamp = timestamp;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }
    public void setTimestamp(Timestamp timestamp){
        this.timestamp = timestamp;
    }
}
