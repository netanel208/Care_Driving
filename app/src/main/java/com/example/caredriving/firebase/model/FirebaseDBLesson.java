package com.example.caredriving.firebase.model;

import com.example.caredriving.firebase.model.dataObject.LessonObj;
import com.google.firebase.database.DatabaseReference;

public class FirebaseDBLesson extends FirebaseBaseModel{
    private LessonObj lessonObj;

    public void addLessonToDB(LessonObj lessonObj){
        this.lessonObj = lessonObj;

        String lessonId = myref.push().getKey();
        int lessonHour = lessonObj.getDate().getHour();
        String lessonHours = lessonHour + "-" + (lessonHour+Integer.parseInt(lessonObj.getDuration()));

        //add to lessons tree
        myref.child("lessons").child(lessonId).setValue(lessonObj);

        //add to lessons per user tree student side
        myref.child("lessonsPerUser").child("students").child(lessonObj.getStudentId())
                .child(lessonObj.getDate().getYear()+"")
                .child(lessonObj.getDate().getMonth()+"")
                .child(lessonObj.getDate().getDay()+"")
                .child(lessonHours)
                .setValue(lessonId);

        //add to lessons per user tree teacher side
        myref.child("lessonsPerUser").child("teachers").child(lessonObj.getTeacherId())
                .child(lessonObj.getDate().getYear()+"")
                .child(lessonObj.getDate().getMonth()+"")
                .child(lessonObj.getDate().getDay()+"")
                .child(lessonHours)
                .setValue(lessonId);
    }

    public void readLessonFromDB(String lessonId){

    }

    public DatabaseReference getLessonsRefFromDB(){
        return myref.child("lessonsPerUser");
    }
}
