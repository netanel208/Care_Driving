package com.example.caredriving.ui.navigationBar.requests;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.recyclerview.widget.RecyclerView;

import com.example.caredriving.R;
import com.example.caredriving.firebase.model.FirebaseDBRequest;
import com.example.caredriving.firebase.model.FirebaseDBUser;
import com.example.caredriving.firebase.model.dataObject.RequestObj;
import com.example.caredriving.firebase.model.dataObject.StudentObj;
import com.example.caredriving.firebase.model.dataObject.TeacherObj;
import com.example.caredriving.ui.navigationBar.searchTeachers.RecyclerViewAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class RequestsViewAdapterModel extends RecyclerView.Adapter<RequestsViewAdapterModel.ViewHolder> {

    private static final String TAG = "RequestViewAdapterModel";
    private ArrayList<StudentObj> students;
    private Context context;

    public RequestsViewAdapterModel(Context context, ArrayList<StudentObj> students) {
        this.context = context;
        this.students = students;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_request_item, parent, false);
        ViewHolder holder = new RequestsViewAdapterModel.ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Log.d(TAG, "onBindViewHolder: called.");
        holder.tvStudentFirstName.setText(students.get(position).getFirstName());
        holder.tvStudentLastName.setText(students.get(position).getLastName());
        holder.tvCity.setText(students.get(position).getCity());
        holder.tvGreenForm.setText(students.get(position).getGreenForm());
        holder.tvPhoneNumber.setText(students.get(position).getPhoneNumber());
        holder.tvTheory.setText(students.get(position).getTheory());
        holder.tvStudentFirstName.setText(students.get(position).getFirstName());
        holder.setStudent(students.get(position));
    }

    @Override
    public int getItemCount() {
        return students.size();
    }

    public void onClick(View view){

    }




    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tvStudentFirstName;
        TextView tvStudentLastName;
        TextView tvCity;
        TextView tvGreenForm;
        TextView tvTheory;
        TextView tvPhoneNumber;
        TextView tvStatus;
        Button btnAccept;
        Button btnReject;
        private StudentObj student;
        private RequestObj request;
        private FirebaseDBRequest fb_request;
        private FirebaseDBUser fb_teacher;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvStudentFirstName = itemView.findViewById(R.id.tvStudentFirstName);
            tvStudentLastName = itemView.findViewById(R.id.tvStudentLastName);
            tvCity = itemView.findViewById(R.id.tvCityValue);
            tvGreenForm = itemView.findViewById(R.id.tvGreenFormValue);
            tvTheory = itemView.findViewById(R.id.tvTheoryValue);
            tvPhoneNumber = itemView.findViewById(R.id.tvPhoneNumberValue);
            tvStatus = itemView.findViewById(R.id.tvStatus);
            btnAccept = itemView.findViewById(R.id.btnAccept);
            btnReject = itemView.findViewById(R.id.btnReject);

            btnAccept.setOnClickListener(this);
            btnReject.setOnClickListener(this);

            fb_teacher = new FirebaseDBUser();
            readCurrentRequestFromDB();
        }

        public void setStudent(StudentObj student){
            this.student = student;
        }

        @Override
        public void onClick(View view) {
            String teacherId = fb_teacher.getMyUid();
            String studentId = student.getId();
            fb_request = new FirebaseDBRequest(studentId, teacherId);
            if (view == btnAccept){
                //Accept the request and update the request status on DB
                request.acceptRequest();
                fb_request.writeRequestToDB(request);

                //Update teacher id on chosen student
                student.setTeacherId(teacherId);
                FirebaseDatabase.getInstance().getReference().child("users").child(studentId).child("info").child("teacherId").setValue(teacherId);

                //Display appropriate TextView status
                displayStatus();
                System.out.println("Send accept (status 1) to student "+ studentId);
            }
            else if (view == btnReject){
                //Reject the request and update the request status on DB and delete it
                request.rejectRequest();
                fb_request.writeRequestToDB(request);
                fb_request.deleteRequestFromDB();

                //Display appropriate TextView status
                displayStatus();
                System.out.println("Send reject (status -1) to student "+ studentId);
            }
        }

        private void readCurrentRequestFromDB(){
            final FirebaseDBUser fb_user = new FirebaseDBUser();
            FirebaseDBRequest fb_request = new FirebaseDBRequest();
            fb_request.getMyref().addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    String teacherId = fb_user.getMyUid();
                    String studentId = student.getId();
                    FirebaseDBRequest fb_request = new FirebaseDBRequest(studentId, teacherId);
                    request = fb_request.readRequestFromDB(dataSnapshot);
                    System.out.println("current request = "+ request.getRequestId());
                    displayStatus();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }

        private void displayStatus(){
            if (request.getStatus().equals("0")){
                btnAccept.setVisibility(View.VISIBLE);
                btnReject.setVisibility(View.VISIBLE);
            }
            else if(request.getStatus().equals("1")){
                btnAccept.setVisibility(View.GONE);
                btnReject.setVisibility(View.GONE);
                tvStatus.setVisibility(View.VISIBLE);
                tvStatus.setText("Accepted !");
            }
            else if (request.getStatus().equals("-1")){
                btnAccept.setVisibility(View.GONE);
                btnReject.setVisibility(View.GONE);
                tvStatus.setVisibility(View.VISIBLE);
                tvStatus.setTextColor(Color.RED);
                tvStatus.setText("Rejected !");
            }
        }
    }
}