package com.example.caredriving.firebase.model.dataObject.validation;

public class LessonDate {
    private int year, month, day, hour, minutes;

    public LessonDate(String date, String time){
        String [] dateFields = date.split("/");
        String [] timeFields = time.split(":");
        this.day = Integer.parseInt(dateFields[0]);
        this.month = Integer.parseInt(dateFields[1]);
        this.year = Integer.parseInt(dateFields[2]);
        this.hour = Integer.parseInt(timeFields[0]);
        this.minutes = Integer.parseInt(timeFields[1]);
    }

    public String getFullDate(){
        return day+"/"+month+"/"+year;
    }

    public int getYear() {
        return year;
    }

    public int getMonth() {
        return month;
    }

    public int getDay() {
        return day;
    }

    public int getHour() {
        return hour;
    }

    public int getMinutes() {
        return minutes;
    }
}
