package com.example.caredriving.firebase.model.dataObject;

import android.content.Context;
import android.content.Intent;

import java.util.ArrayList;
import java.util.HashMap;

public class TeacherObj extends UserObj {

    private String carType;
    private String carYear;
    private String experience;
    private String transmission; //גיר
    private String lessonPrice;

    private ArrayList<String> students;
    private ArrayList<String> requests;
    private ArrayList<String> lessons;

    public TeacherObj(){
        super();
        this.students = new ArrayList<>();
        this.requests = new ArrayList<>();
        this.lessons =  new ArrayList<>();
    }

    public TeacherObj(String id, String firstName, String lastName,
                      String age, String city, String email, String phoneNumber,
                      String carType, String carYear, String experience,
                      String transmission, String lessonPrice){
        super(id, firstName, lastName, age, city, email, phoneNumber);
        this.carType = carType;
        this.carYear = carYear;
        this.experience = experience;
        this.transmission = transmission;
        this.lessonPrice = lessonPrice;
        this.students = new ArrayList<>();
        this.requests = new ArrayList<>();
        this.lessons =  new ArrayList<>();
    }

    public TeacherObj(HashMap<String, String> hashMapTeacher){
        super(hashMapTeacher);
        this.carType = hashMapTeacher.get("carType");
        this.carYear = hashMapTeacher.get("carYear");
        this.experience = hashMapTeacher.get("experience");
        this.transmission = hashMapTeacher.get("transmission");
        this.lessonPrice = hashMapTeacher.get("lessonPrice");
        this.students = new ArrayList<>();
        this.requests = new ArrayList<>();
        this.lessons =  new ArrayList<>();
    }

    public void setCarBrand(String carBrand) {this.carType = carBrand;}
    public void setCarYear(String carYear) {this.carYear = carYear;}
    public void setExperience(String experience) {this.experience = experience;}
    public void setTransmission(String transmission) {this.transmission = transmission;}
    public void setLessonPrice(String price) {this.lessonPrice = price;}

    public void loadStudents(ArrayList<String> studentsId){
        this.students = studentsId;
    }

    public void loadRequests(ArrayList<String> requestsId){
        this.requests = requestsId;
    }

    public Intent getIntent(Context from, Class<?> to) {
        return super.getIntent(from, to);
    }

    public String getCarType() {
        return carType;
    }

    public String getCarYear() {
        return carYear;
    }

    public String getExperience() {
        return experience;
    }

    public String getTransmission() {
        return transmission;
    }

    public String getLessonPrice() {
        return lessonPrice;
    }

    public ArrayList<String> getStudents(){ return students;}

    public ArrayList<String> getRequests(){ return requests;}

    public ArrayList<String> getLessons() { return lessons; }

    //    public void addStudent(String studentId){}

    @Override
    public String toString(){
        return getFirstName() + " " + getLastName();
    }
}
