package com.example.caredriving.ui.navigationBar.students;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.caredriving.R;
import com.example.caredriving.firebase.model.dataObject.StudentObj;
import com.example.caredriving.ui.navigationBar.searchTeachers.ContactTeacherActivity;

import java.util.ArrayList;

public class RecyclerViewAdapterStudents extends RecyclerView.Adapter<RecyclerViewAdapterStudents.ViewHolder>{

    private static final String TAG = "RecyclerViewAdapter";
    private ArrayList<StudentObj> students = new ArrayList<>();
    private Context context;

    public RecyclerViewAdapterStudents(Context context, ArrayList<StudentObj> students) {
        this.students = students;
        this.context = context;
    }

    // Recycling the ViewHolder - put things into position
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_listitem_students, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    // Called every single time when a new item is added to the list
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        Log.d(TAG, "onBindViewHolder: called.");
        holder.tvStudentFirstName.setText(students.get(position).getFirstName());
        holder.tvStudentLastName.setText(students.get(position).getLastName());
        holder.tvLocation.setText(students.get(position).getCity());
        holder.tvTheory.setText(students.get(position).getTheory());
        holder.tvGreenForm.setText(students.get(position).getGreenForm());
        holder.tvPhoneNumber.setText(students.get(position).getPhoneNumber());
    }

    // Tells how many items are on the list
    @Override
    public int getItemCount() {
        return students.size();
    }


    //////////////////////////////////////////////////////////////
    // Holds the information in memory of each individual entry //
    //////////////////////////////////////////////////////////////
    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView tvStudentFirstName;
        TextView tvStudentLastName;
        TextView tvLocation;
        TextView tvTheory;
        TextView tvGreenForm;
        TextView tvPhoneNumber;
        ImageView imgPhone;

        public ViewHolder(@NonNull final View itemView) {
            super(itemView);
            tvStudentFirstName = itemView.findViewById(R.id.tvStudentFirstName);
            tvStudentLastName = itemView.findViewById(R.id.tvStudentLastName);
            tvLocation = itemView.findViewById(R.id.tvLocation);
            tvTheory = itemView.findViewById(R.id.tvTheory);
            tvGreenForm = itemView.findViewById(R.id.tvGreenForm);
            tvPhoneNumber = itemView.findViewById(R.id.tvPhoneNumber);
            imgPhone = itemView.findViewById(R.id.imgPhone);

            imgPhone.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(tvPhoneNumber.getText().toString().contains("-")) {
                        tvPhoneNumber.getText().toString().replace("-", "");
                    }

                    String dial = "tel:" + tvPhoneNumber.getText().toString();
                    Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse(dial));
                    context.startActivity(intent);
                }
            });

        }
    }

}
