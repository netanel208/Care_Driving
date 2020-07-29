package com.example.caredriving.ui.navigationBar.teacher;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.caredriving.R;
import com.example.caredriving.firebase.model.FirebaseDBEntity;
import com.example.caredriving.firebase.model.FirebaseDBUser;
import com.example.caredriving.firebase.model.dataObject.TeacherObj;
import com.example.caredriving.firebase.model.dataObject.UserObj;
import com.example.caredriving.ui.navigationBar.searchTeachers.ContactTeacherActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class MyTeacherFragment extends Fragment {

    private static final String TAG = "MyTeacherFragment";
    private TextView tvTeachersFirstName;
    private TextView tvTeachersLastName;
    private TextView tvTeachersExperience;
    private TextView tvTeacherslocations;
    private TextView tvCarType;
    private TextView tvCarYear;
    private TextView tvGearType;
    private TextView tvLessonPrice;
    private TextView tvTeachersPhoneNumber;
    private ImageView imgPhone;
    private TextView txtNoTeacher;
    private TextView tvTeachersEmail;
    private ImageView imgEmail;
    RelativeLayout layoutAllTeacherInfo;
    private static final int REQUEST_CALL = 1;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        final View root = inflater.inflate(R.layout.fragment_teacher, container, false);
        tvTeachersFirstName = root.findViewById(R.id.tvTeachersFirstName);
        tvTeachersLastName = root.findViewById(R.id.tvTeachersLastName);
        tvTeachersExperience = root.findViewById(R.id.tvTeachersExperience);
        tvTeacherslocations = root.findViewById(R.id.tvTeachersLoction);
        tvCarType = root.findViewById(R.id.tvTeachersCar);
        tvCarYear = root.findViewById(R.id.tvTeachersCarYear);
        tvGearType = root.findViewById(R.id.tvTeachersGear);
        tvLessonPrice = root.findViewById(R.id.tvLessonPrice);
        tvTeachersPhoneNumber = root.findViewById(R.id.tvTeachersPhone);
        imgPhone = root.findViewById(R.id.imgPhone);
        tvTeachersEmail = root.findViewById(R.id.tvTeachersEmail);
        imgEmail = root.findViewById(R.id.imgEmail);
        txtNoTeacher = root.findViewById(R.id.txtNoTeacher);
        layoutAllTeacherInfo = root.findViewById(R.id.layoutAllTeacherInfo);

        layoutAllTeacherInfo.setVisibility(View.INVISIBLE);
        txtNoTeacher.setVisibility(View.INVISIBLE);

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        String id = currentUser.getUid();
        downloadInfoFromDatabase(root,id);
        // Call to the teacher by clicking the phone image
        imgPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makePhoneCall(root);
            }
        });

        imgEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendEmail();
            }
        });

        return root;
    }

    // Download the teacher's information from the Database
    private void downloadInfoFromDatabase(final View root, final String id){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("users").child(id);
        reference.addValueEventListener(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if ((dataSnapshot.child("info").child("teacherId").getValue()).toString().equals("null")) {
                    txtNoTeacher.setVisibility(View.VISIBLE);
                }
                else {
                    layoutAllTeacherInfo.setVisibility(View.VISIBLE);
                    String teachersId = dataSnapshot.child("info").child("teacherId").getValue().toString();
                    DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("users").child(teachersId);
                    reference.addValueEventListener(new ValueEventListener() {
                        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            FirebaseDBEntity entity = dataSnapshot.getValue(FirebaseDBEntity.class);
                            UserObj user = entity.getUserObj();
                            TeacherObj teacherUser = (TeacherObj) user;
                            tvTeachersFirstName.setText( teacherUser.getFirstName());
                            tvTeachersLastName.setText( teacherUser.getLastName());
                            tvTeachersExperience.setText(teacherUser.getExperience());
                            tvTeacherslocations.setText(teacherUser.getCity());
                            tvCarType.setText(teacherUser.getCarType());
                            tvCarYear.setText(teacherUser.getCarYear());
                            tvGearType.setText(teacherUser.getTransmission());
                            tvLessonPrice.setText(teacherUser.getLessonPrice());
                            tvTeachersPhoneNumber.setText(teacherUser.getPhoneNumber());
                            tvTeachersEmail.setText(teacherUser.getEmail());
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            Toast.makeText(root.getContext(), "Oops, something went wrong", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(root.getContext(), "Oops, something went wrong", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Call to the teacher
    private void makePhoneCall(View root){
        Log.d(TAG, "makePhoneCall: call the teacher " + tvTeachersFirstName + " " + tvTeachersLastName);
        // Checks if the app have permission to make a phone call
        if(ContextCompat.checkSelfPermission(root.getContext(),
                Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(getActivity(),
                    new String[] {Manifest.permission.CALL_PHONE}, REQUEST_CALL);
        }
        else{
            if(tvTeachersPhoneNumber.getText().toString().contains("-")) {
                tvTeachersPhoneNumber.getText().toString().replace("-", "");
            }

            String dial = "tel:" + tvTeachersPhoneNumber.getText().toString();
            Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse(dial));
            startActivity(intent);
        }
    }

    // Ask for permission to make the phone call
    public void onRequestPermissionsResult(View root, int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_CALL){
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                makePhoneCall(root);
            }
            else{
                Toast.makeText(root.getContext(), "Permission Denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    // Send Email to teacher
    private void sendEmail(){
        String teachersEmail = tvTeachersEmail.getText().toString();
        String[] receiver = new String[1];
        receiver[0] = teachersEmail;
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_EMAIL, receiver);

        // opening only email clients - force sending with email
        intent.setType("message/rfc822");
        startActivity(Intent.createChooser(intent, getString(R.string.open_email_clients)));
    }
}