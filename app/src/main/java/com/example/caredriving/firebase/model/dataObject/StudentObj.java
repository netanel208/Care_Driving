package com.example.caredriving.firebase.model.dataObject;

import android.content.Context;
import android.content.Intent;

import java.util.ArrayList;
import java.util.HashMap;

public class StudentObj extends UserObj {

    private String greenForm;
    private String transmission;
    private String theory;
    private String teacherId;

    private ArrayList<LessonObj> lessons;

    public StudentObj(){
        super();
        this.lessons = new ArrayList<>();
    }

    public StudentObj(String id, String firstName, String lastName, String age, String city, String email, String phoneNumber,
                      String greenForm, String transmission, String theory, String teacherId) {
        super(id, firstName, lastName, age, city, email, phoneNumber);
        this.greenForm = greenForm;
        this.transmission = transmission;
        this.theory = theory;
        this.teacherId = teacherId;
        this.lessons = new ArrayList<>();
    }

    public StudentObj(HashMap<String, String> hashMapStudent){
        super(hashMapStudent);
        this.greenForm = hashMapStudent.get("greenForm");
        this.transmission = hashMapStudent.get("transmission");
        this.theory = hashMapStudent.get("theory");
        this.teacherId = hashMapStudent.get("teacherId");
        this.lessons = new ArrayList<>();
    }

    public Intent getIntent(Context from, Class<?> to){
        return super.getIntent(from, to);
    }

    public void setGreenForm(String greenForm) {
        this.greenForm = greenForm;
    }

    public void setTransmission(String transmission) {
        this.transmission = transmission;
    }

    public void setTheory(String theory) {
        this.theory = theory;
    }

    public String getGreenForm() {
        return greenForm;
    }

    public String getTransmission() {
        return transmission;
    }

    public String getTheory() {
        return theory;
    }

    public void setTeacherId(String teacherId) {
        this.teacherId = teacherId;
    }

    public String getTeacherId() {
        return teacherId;
    }

}
