package com.example.caredriving;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.caredriving.firebase.model.FirebaseDBUser;
import com.example.caredriving.firebase.model.dataObject.TeacherObj;
import com.example.caredriving.firebase.model.dataObject.validation.Validation;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class InformationTeacherActivity extends AppCompatActivity implements View.OnClickListener {

    private Spinner carBrand;
    private Spinner carYear;
    private Spinner experience;
    private Spinner transmission;
    private EditText lessonPrice;

    private Button btnSave;
    private Button btnPrevious;

    private TeacherObj teacher;
    private Intent startIntent;

    private FirebaseDBUser fb_user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_information_teacher);

        fb_user = new FirebaseDBUser();

        carYear = findViewById(R.id.spnInformationTeacherCarYears);
        experience = findViewById(R.id.spnInformationTeacherExperience);
        carBrand = findViewById(R.id.spnInformationTeacherCarBrands);
        transmission = findViewById(R.id.spnInformationTeacherTransmission);
        lessonPrice = findViewById(R.id.etInformationTeacherLessonPrice);

        startIntent = getIntent();
        teacher = (TeacherObj) startIntent.getSerializableExtra("UserObj");

        btnSave = findViewById(R.id.btnInformationTeacherSaveInfo);
        btnPrevious = findViewById(R.id.btnInformationTeacherPreviousInfo);

        spinnerTransmissionListener();
        spinnerCarBrandsListener();
        spinnerExperienceListener();
        spinnerCarYearListener();

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
        String price = lessonPrice.getText().toString();

        Validation validation = new Validation();
        validation.checkPrice(price);

        if(validation.hasErrors()){
            for(String error : validation.getErrors()){
                Toast.makeText(this, error, Toast.LENGTH_SHORT).show();
            }
            return;
        }

        teacher.setLessonPrice(price);
        teacher.setId(fb_user.getMyUid());

        fb_user.writeUserToDB(teacher);

        Intent intent = new Intent(InformationTeacherActivity.this, MainActivity.class);
        intent.putExtra("UserObj", teacher);
        startActivity(intent);
    }

    private void openPreviousPage() {
        Intent intent = new Intent(InformationTeacherActivity.this, InformationActivity.class);
        intent.putExtra("UserObj", startIntent.getSerializableExtra("UserObj"));
        intent.putExtra("Type", "teacher");
        startActivity(intent);
    }

    private void spinnerCarBrandsListener() {
        carBrand.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            String tempCarBrand;

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                tempCarBrand = adapterView.getItemAtPosition(i).toString();
                teacher.setCarBrand(tempCarBrand);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                tempCarBrand = adapterView.getItemAtPosition(0).toString();
                teacher.setCarBrand(tempCarBrand);
            }
        });
    }

    private void spinnerTransmissionListener() {
        transmission.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            String tempTransmission;

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                tempTransmission = adapterView.getItemAtPosition(i).toString();
                teacher.setTransmission(tempTransmission);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                tempTransmission = adapterView.getItemAtPosition(0).toString();
                teacher.setTransmission(tempTransmission);
            }
        });
    }

    private void spinnerCarYearListener() {
        int startFrom = 2000;
        List<String> list = new ArrayList<>();
        int year = Calendar.getInstance().get(Calendar.YEAR);
        for (; startFrom < year; startFrom++) {
            list.add(startFrom + "");
        }

        ArrayAdapter<String> yearAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, list);
        yearAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        carYear.setAdapter(yearAdapter);
        carYear.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            String tempCarYear;

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                tempCarYear = adapterView.getItemAtPosition(i).toString();
                teacher.setCarYear(tempCarYear);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                tempCarYear = adapterView.getItemAtPosition(0).toString();
                teacher.setCarYear(tempCarYear);
            }
        });
    }


    private void spinnerExperienceListener() {
        List<String> list = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            list.add(i * 5 + " - " + (i + 1) * 5);
        }
        list.add("25+");

        ArrayAdapter<String> yearAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, list);
        yearAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        experience.setAdapter(yearAdapter);
        experience.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            String tempExperience;

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                tempExperience = adapterView.getItemAtPosition(i).toString();
                teacher.setExperience(tempExperience);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                tempExperience = adapterView.getItemAtPosition(0).toString();
                teacher.setExperience(tempExperience);
            }
        });
        teacher.setExperience(experience.toString());
    }
}
