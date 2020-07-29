package com.example.caredriving.firebase.model.dataObject;

import android.content.Context;
import android.content.Intent;


import java.io.Serializable;
import java.util.HashMap;

public class UserObj implements Serializable {

    private String firstName;
    private String lastName;
    private String age;
    private String city;
    private String email;
    private String phoneNumber;
    private String id;

    public UserObj(){}

    public UserObj(String id, String firstName, String lastName, String age, String city, String email, String phoneNumber){
        this.firstName = firstName;
        this.lastName = lastName;
        this.age = age;
        this.city = city;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.id = id;
    }

    public UserObj(HashMap<String, String> hashMapUser){
        this.id       = hashMapUser.get("id");
        this.firstName = hashMapUser.get("firstName");
        this.lastName  = hashMapUser.get("lastName");
        this.age       = hashMapUser.get("age");
        this.city      = hashMapUser.get("city");
        this.email     = hashMapUser.get("email");
        this.phoneNumber = hashMapUser.get("phoneNumber");
    }

    public void setId(String uid){this.id = uid;}
    public void setFirstName(String firstName) {this.firstName = firstName;}
    public void setLastName(String lastName) {this.lastName = lastName;}
    public void setAge(String age) {this.age = age;}
    public void setCity(String city) {this.city = city;}
    public void setEmail(String email){this.email = email;}
    public void setPhoneNumber(String phoneNumber){this.phoneNumber = phoneNumber;}

    public String getId() {return id;}
    public String getFirstName() {return firstName;}
    public String getLastName() {return lastName;}
    public String getAge() {return age;}
    public String getCity() {return city;}
    public String getEmail() {return email;}
    public String getPhoneNumber() {return phoneNumber;}

    public Intent getIntent(Context from, Class<?> to){
        Intent intent = new Intent(from, to);
        intent.putExtra("firstname", getFirstName());
        intent.putExtra("lastname", getLastName());
        intent.putExtra("age", getAge());
        intent.putExtra("city", getCity());
        return intent;
    }
}