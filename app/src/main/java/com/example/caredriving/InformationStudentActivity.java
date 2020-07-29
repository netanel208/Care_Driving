package com.example.caredriving;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;

import com.example.caredriving.firebase.model.FirebaseBaseModel;
import com.example.caredriving.firebase.model.FirebaseDBUser;
import com.example.caredriving.firebase.model.dataObject.StudentObj;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class InformationStudentActivity extends AppCompatActivity implements View.OnClickListener {

    private Spinner transmission;
    private Spinner greenForm;
    private Spinner theory;

    private Button btnSave;
    private Button btnPrevious;

    private StudentObj student;
    private Intent startIntent;

    private FirebaseDBUser fb_user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_information_student);

        fb_user = new FirebaseDBUser();

        transmission = findViewById(R.id.spnInformationStudentTransmission);
        greenForm = findViewById(R.id.spnInformationStudentGreenForm);
        theory = findViewById(R.id.spnInformationStudentTheory);

        startIntent = getIntent();
        student = (StudentObj) startIntent.getSerializableExtra("UserObj");

        btnSave = findViewById(R.id.btnInformationStudentSaveInfo);
        btnPrevious = findViewById(R.id.btnInformationStudentPreviousInfo);

        spinnerGreenFormListener();
        spinnerTheoryListener();
        spinnerTransmissionListener();

        btnSave.setOnClickListener(this);
        btnPrevious.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view == btnSave) {
            saveUserInformation();
        }
        if (view == btnPrevious) {
            openPreviousPage();
        }
    }

    private void saveUserInformation() {

        student.setTeacherId("null");
        student.setId(fb_user.getMyUid());

        // write to DB
        fb_user.writeUserToDB(student);

        Intent intent = new Intent(InformationStudentActivity.this, MainActivity.class);
        intent.putExtra("UserObj", student);
        startActivity(intent);
    }

    private void openPreviousPage() {
        Intent intent = new Intent(InformationStudentActivity.this, InformationActivity.class);
        intent.putExtra("UserObj", startIntent.getSerializableExtra("UserObj"));
        intent.putExtra("Type", "student");
        startActivity(intent);
    }

    private void spinnerTransmissionListener() {
        transmission.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            String tempTransmission;

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                tempTransmission = adapterView.getItemAtPosition(i).toString();
                student.setTransmission(tempTransmission);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                tempTransmission = adapterView.getItemAtPosition(0).toString();
                student.setTransmission(tempTransmission);
            }
        });
    }

    private void spinnerGreenFormListener() {
        greenForm.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            String tempGreenForm;

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                tempGreenForm = adapterView.getItemAtPosition(i).toString();
                student.setGreenForm(tempGreenForm);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                tempGreenForm = adapterView.getItemAtPosition(0).toString();
                student.setGreenForm(tempGreenForm);
            }
        });
    }

    private void spinnerTheoryListener() {
        theory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            String tempTheory;

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                tempTheory = adapterView.getItemAtPosition(i).toString();
                student.setTheory(tempTheory);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                tempTheory = adapterView.getItemAtPosition(0).toString();
                student.setTheory(tempTheory);
            }
        });
    }
}
