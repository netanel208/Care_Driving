package com.example.caredriving.ui.navigationBar.searchTeachers;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.example.caredriving.R;
import com.example.caredriving.firebase.model.FirebaseBaseModel;
import com.example.caredriving.firebase.model.FirebaseDBEntity;
import com.example.caredriving.firebase.model.FirebaseDBRequest;
import com.example.caredriving.firebase.model.FirebaseDBUser;
import com.example.caredriving.firebase.model.dataObject.RequestObj;
import com.example.caredriving.firebase.model.dataObject.StudentObj;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

public class ContactTeacherActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "ContactTeacherActivity";
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
    private TextView tvTeachersEmail;
    private ImageView imgEmail;
    private Button btnConnectToTeacher;
    private static final int REQUEST_CALL = 1;

    private FirebaseDBUser fb_user;
    private StudentObj user;
    private String teacherId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "ContactTeacherActivity: started.");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_teacher);
        getSupportActionBar().setTitle("Contact Teacher");

        tvTeachersFirstName = findViewById(R.id.tvDetailsTeachersFirstName);
        tvTeachersLastName = findViewById(R.id.tvDetailsTeachersLastName);
        tvTeachersExperience = findViewById(R.id.tvDetailsTeachersExperience);
        tvTeacherslocations = findViewById(R.id.tvDetailsTeachersLoction);
        tvCarType = findViewById(R.id.tvDetailsTeachersCar);
        tvCarYear = findViewById(R.id.tvDetailsTeachersCarYear);
        tvGearType = findViewById(R.id.tvDetailsTeachersGear);
        tvLessonPrice = findViewById(R.id.tvDetailsLessonPrice);
        tvTeachersPhoneNumber = findViewById(R.id.tvDetailsTeachersPhone);
        imgPhone = findViewById(R.id.imgPhone);
        tvTeachersEmail = findViewById(R.id.tvDetailsTeachersEmail);
        imgEmail = findViewById(R.id.imgEmail);
        btnConnectToTeacher = findViewById(R.id.tvDetailsConnectToTeacher);

        //find user
        fb_user = new FirebaseDBUser();
        fb_user.getUserRefFromDB().addListenerForSingleValueEvent(new ValueEventListener(){
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                FirebaseDBEntity entity = dataSnapshot.getValue(FirebaseDBEntity.class);
                user = (StudentObj) entity.getUserObj();
                boolean hasTeacher = checkIfHasTeacher();
                btnConnectToTeacher.setEnabled(!hasTeacher);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        // Get the incoming intents
        getIncomingIntent();

        // Call to the teacher by clicking the phone image
        imgPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makePhoneCall();
            }
        });

        //Send email to teacher by clicking the envelope image
        imgEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendEmail();
            }
        });

        btnConnectToTeacher.setOnClickListener(this);
    }


    // Get the incoming intents
    private void getIncomingIntent(){
        Log.d(TAG, "getIncomingIntent: checking for incoming intents.");
        // Checks if the intent have any extras before trying to get the extras
        Bundle extras = getIntent().getExtras();
        if(extras!=null){
            // Get the extras
            teacherId = getIntent().getStringExtra("TeachersUid");
            String firstName = getIntent().getStringExtra("TeachersFirstName");
            String lastName = getIntent().getStringExtra("TeachersLastName");
            String experience = getIntent().getStringExtra("TeachersExperience");
            String location = getIntent().getStringExtra("TeachersLocation");
            String carType = getIntent().getStringExtra("TeachersCarType");
            String carYear = getIntent().getStringExtra("TeachersCarYear");
            String gearType = getIntent().getStringExtra("TeachersGearType");
            String lessonPrice = getIntent().getStringExtra("TeachersLessonPrice");
            String phoneNumber = getIntent().getStringExtra("TeachersPhoneNumber");
            String email = getIntent().getStringExtra("TeachersEmail");
            // Set the extras
            tvTeachersFirstName.setText(firstName);
            tvTeachersLastName.setText(lastName);
            tvTeachersExperience.setText(experience);
            tvTeacherslocations.setText(location);
            tvCarType.setText(carType);
            tvCarYear.setText(carYear);
            tvGearType.setText(gearType);
            tvLessonPrice.setText(lessonPrice);
            tvTeachersPhoneNumber.setText(phoneNumber);
            tvTeachersEmail.setText(email);
        }
    }

    // Call to the teacher
    private void makePhoneCall(){
        Log.d(TAG, "makePhoneCall: call the teacher " + tvTeachersFirstName + " " + tvTeachersLastName);
        // Checks if the app have permission to make a phone call
        if(ContextCompat.checkSelfPermission(ContactTeacherActivity.this,
                Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(ContactTeacherActivity.this,
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
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_CALL){
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                makePhoneCall();
            }
            else{
                Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
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



    @Override
    public void onClick(View view) {
        if(view == btnConnectToTeacher){
//            FirebaseDBRequest request = new FirebaseDBRequest(user.getId(), teacherId);
            RequestObj requestObj = new RequestObj(user.getId(), teacherId);
            FirebaseDBRequest request = new FirebaseDBRequest();
            request.writeRequestToDB(requestObj);
            Toast.makeText(this, "Request successfully sent", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean checkIfHasTeacher(){
        return !user.getTeacherId().equals("null");
    }
}