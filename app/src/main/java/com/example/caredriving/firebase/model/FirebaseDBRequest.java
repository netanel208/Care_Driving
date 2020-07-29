package com.example.caredriving.firebase.model;

import com.example.caredriving.firebase.model.dataObject.RequestObj;
import com.google.firebase.database.DataSnapshot;

public class FirebaseDBRequest extends FirebaseBaseModel{
    private RequestObj request;

    public FirebaseDBRequest(){}

    public FirebaseDBRequest(String srcId, String dstId){
        this.request = new RequestObj(srcId, dstId);
    }

    public void writeRequestToDB(RequestObj request){
        this.request = request;
        myref.child("Requests").child(request.getRequestId()).setValue(request);
    }

    public RequestObj readRequestFromDB(DataSnapshot dataSnapshot){
        request = dataSnapshot.child("Requests").child(request.getRequestId()).getValue(RequestObj.class);
        return request;
    }

    public void deleteRequestFromDB(){
        myref.child("Requests").child(request.getRequestId()).removeValue();
    }
}
