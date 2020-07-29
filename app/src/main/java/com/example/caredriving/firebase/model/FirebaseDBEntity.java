package com.example.caredriving.firebase.model;

import com.example.caredriving.firebase.model.dataObject.StudentObj;
import com.example.caredriving.firebase.model.dataObject.TeacherObj;
import com.example.caredriving.firebase.model.dataObject.UserObj;

import java.io.Serializable;
import java.util.HashMap;

public class FirebaseDBEntity extends FirebaseBaseModel implements Serializable {
    HashMap<String, String> info;
    String type;

    public UserObj getUserObj(){
        try {
            if (type.equals("teacher"))
                return new TeacherObj(info);
            else if (type.equals("student"))
                return new StudentObj(info);
        } catch (Exception e){
            return null;
        }


        return null;
    }

    public void setType(String type){this.type = type;}
    public void setInfo(HashMap<String, String> user){this.info = user;}
    public String getType(){return type;}
    public HashMap<String, String> getInfo(){return info;}
}
