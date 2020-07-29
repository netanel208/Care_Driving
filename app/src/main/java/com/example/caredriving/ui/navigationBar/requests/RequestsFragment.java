package com.example.caredriving.ui.navigationBar.requests;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.caredriving.MainActivity;
import com.example.caredriving.R;
import com.example.caredriving.firebase.model.FirebaseDBEntity;
import com.example.caredriving.firebase.model.FirebaseDBUser;
import com.example.caredriving.firebase.model.dataObject.RequestObj;
import com.example.caredriving.firebase.model.dataObject.StudentObj;
import com.example.caredriving.firebase.model.dataObject.TeacherObj;
import com.example.caredriving.firebase.model.dataObject.UserObj;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class RequestsFragment extends Fragment {

    private FirebaseDBUser fb_user;
    private TeacherObj user;

//    private static ArrayList<RequestObj> requests = new ArrayList<>();
    private ArrayList<RequestObj> requests = new ArrayList<>();
//    private static ArrayList<StudentObj> students = new ArrayList<>();
    private ArrayList<StudentObj> students = new ArrayList<>();
    private RequestsViewAdapterModel requestsViewAdapterModel;
//    private static RecyclerView recyclerView;
    private RecyclerView recyclerView;

    public static RequestsFragment newInstance() {
        return new RequestsFragment();
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_requests, container, false);
        recyclerView = root.findViewById(R.id.recyclerViewRequests);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        // Set adapter for recycle view
        requestsViewAdapterModel = new RequestsViewAdapterModel(getActivity(), students);
        recyclerView.setAdapter(requestsViewAdapterModel);
        requestsViewAdapterModel.notifyDataSetChanged();


        //find user
        fb_user = new FirebaseDBUser();
        fb_user.getMyref().addListenerForSingleValueEvent(new ValueEventListener(){

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                user = (TeacherObj) fb_user.readUserFromDB(dataSnapshot);
                // Load all requests object to ArrayList
                loadRequestsObj(dataSnapshot, user.getRequests());
                // Load all students object to ArrayList
                loadStudentsObj(dataSnapshot, requests);
                System.out.println("user print requests "+user.getRequests());
                requestsViewAdapterModel.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        // Load all

        return root;
    }

//    @Override
//    public void onDestroy() {
//        super.onDestroy();
//        ((MainActivity)getActivity()).clearBackStackInclusive("tag"); // tag (addToBackStack tag) should be the same which was used while transacting the F2 fragment
//    }

    /**
     *
     * @param dataSnapshot
     * @param requestsIds - Requests id list of current teacher
     */
    private void loadRequestsObj(DataSnapshot dataSnapshot, ArrayList<String> requestsIds){
        for(String id : requestsIds){
            for(DataSnapshot request : dataSnapshot.child("Requests").getChildren()) {
                RequestObj requestObj = request.getValue(RequestObj.class);
                if(id.equals(requestObj.getRequestId()))
                    requests.add(requestObj);
            }
        }
    }

    /**
     *
     * @param dataSnapshot
     * @param requestsObj - List of RequestObj
     */
    private void loadStudentsObj(DataSnapshot dataSnapshot, ArrayList<RequestObj> requestsObj){
        for(RequestObj request : requestsObj){
            String studentId = request.getStudentId();
            String teacherId = request.getTeacherId();
            for(DataSnapshot user : dataSnapshot.child("users").getChildren()){
                // Prefer to use Entity instead FirebaseDBUser
                FirebaseDBEntity entity = user.getValue(FirebaseDBEntity.class);
                UserObj userObj = entity.getUserObj();
                if(studentId.equals(userObj.getId()))
                    students.add((StudentObj)userObj);
            }
        }
    }
}