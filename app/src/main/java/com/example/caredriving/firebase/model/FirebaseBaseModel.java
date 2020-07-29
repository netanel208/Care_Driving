package com.example.caredriving.firebase.model;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class FirebaseBaseModel {
    protected DatabaseReference myref;

    public FirebaseBaseModel(){
        myref = FirebaseDatabase.getInstance().getReference();
    }

    public DatabaseReference getMyref(){return myref;}
}
