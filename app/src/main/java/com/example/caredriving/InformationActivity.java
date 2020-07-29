package com.example.caredriving;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.caredriving.firebase.model.dataObject.StudentObj;
import com.example.caredriving.firebase.model.dataObject.TeacherObj;
import com.example.caredriving.firebase.model.dataObject.UserObj;
import com.example.caredriving.firebase.model.dataObject.validation.Validation;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;

import java.util.HashMap;
import java.util.HashSet;


public class InformationActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText firstName;
    private EditText lastName;
    private EditText age;
    private EditText number;
    private Spinner spnCity;
    private Spinner spnNumber;

    private Button btnNextInfo;
    private Button btnCancelInfo;
    private RadioGroup radioUserGroup;
    private RadioButton radioStudentButton;
    private RadioButton radioTeacherButton;

    private UserObj user;
    private String nextActivity = "student";
    private Intent startIntent;
    private String email;

    private DatabaseReference myRef;
    private FirebaseAuth firebaseAuth;
    //    private String currentId;
    private String cityFromSpinner;
    private String fullNumber;

    private TextView tvError;
    private Validation validation;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_information);

        firebaseAuth = FirebaseAuth.getInstance();

        firstName = findViewById(R.id.etInformationFirstName);
        lastName = findViewById(R.id.etInformationLastName);
        age = findViewById(R.id.etInformationAge);
        number = findViewById(R.id.etInformationNumber);

        spnCity = findViewById(R.id.spnInformationCity);
        spnNumber = findViewById(R.id.spnInformationNumber);

        startIntent = getIntent();
        email = getIntent().getStringExtra("Email");

        validation = new Validation();

        if (startIntent.hasExtra("UserObj")) {
            String type = startIntent.getStringExtra("Type");
            startIntent.removeExtra("Type");
            if (type.equals("student")) {
                user = (StudentObj) startIntent.getSerializableExtra("UserObj");
                setCreatedFields();
            } else {
                nextActivity = "teacher";
                user = (TeacherObj) startIntent.getSerializableExtra("UserObj");
                setCreatedFields();
                radioTeacherButton = findViewById(R.id.radioTeacher);
                radioTeacherButton.setChecked(true);
            }
        }

        btnNextInfo = findViewById(R.id.btnInformationNext);
        btnCancelInfo = findViewById(R.id.btnInformationCancelInfo);
        radioUserGroup = findViewById(R.id.radioUser);

        spinnerCityListener();
        spinnerNumberListener();
        btnNextInfo.setOnClickListener(this);
        btnCancelInfo.setOnClickListener(this);

        radioUserGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                int selectedId = radioUserGroup.getCheckedRadioButtonId();
                radioStudentButton = findViewById(selectedId);
                nextActivity = "student";
                if (radioStudentButton.getText().toString().toLowerCase().equals(nextActivity)) {
                    nextActivity = "student";
                } else {
                    nextActivity = "teacher";
                }
            }
        });
    }

    @Override
    public void onClick(View view) {
        if (view == btnNextInfo) {
            continueRegistration();
        }
        if (view == btnCancelInfo) {
            submitCancelRegistration();
        }
    }

    //if we back to previous activity we already have the information
    private void setCreatedFields() {
        firstName.setText(user.getFirstName());
        lastName.setText(user.getLastName());
        age.setText(user.getAge());

        String fullNum = user.getPhoneNumber();
        String spnNum = fullNum.substring(0, 3);
        String etNum = fullNum.substring(4, fullNum.length());

        number.setText(etNum);
        spnNumber.setSelection(getIndex(spnNumber, spnNum));
        spnCity.setSelection(getIndex(spnCity, user.getCity()));
    }

    private void continueRegistration() {
        clearErrorsFields(validation.getRegistrationErrors());
        validation.clearErrors();
        validation.checkFirstName(firstName.getText().toString().trim());
        validation.checkLastName(lastName.getText().toString().trim());
        validation.checkAge(age.getText().toString().trim());
        validation.checkNumber(number.getText().toString().trim());

        if (validation.hasErrors()) {
            for (String error : validation.getErrors()) {
                Toast.makeText(this, error, Toast.LENGTH_SHORT).show();
                showErrorsFields(validation.getRegistrationErrors());
            }
            return;
        }

        Intent intent;
        if (nextActivity.equals("student")) {
            user = new StudentObj();
            intent = new Intent(InformationActivity.this, InformationStudentActivity.class);
        } else {
            user = new TeacherObj();
            intent = new Intent(InformationActivity.this, InformationTeacherActivity.class);
        }

        fullNumber = fullNumber + "-" + number.getText().toString().trim();

        user.setFirstName(firstName.getText().toString().trim());
        user.setLastName(lastName.getText().toString().trim());
        user.setAge(age.getText().toString().trim());
        user.setCity(cityFromSpinner);
        user.setEmail(email);
        user.setPhoneNumber(fullNumber);

        intent.putExtra("UserObj", user);
        startActivity(intent);
    }

    private void showErrorsFields(HashSet<Integer> registrationErrors) {
        for (int id : registrationErrors) {
            tvError = (TextView) findViewById(id);
            tvError.setVisibility(View.VISIBLE);
        }
    }

    private void clearErrorsFields(HashSet<Integer> registrationErrors) {
        for (int id : registrationErrors) {
            tvError = (TextView) findViewById(id);
            tvError.setVisibility(View.INVISIBLE);
        }
    }

    private void submitCancelRegistration() {
        AlertDialog.Builder cancelRegistration = new AlertDialog.Builder(this);
        cancelRegistration.setTitle(R.string.registration_title);

        cancelRegistration.setMessage(R.string.registration_cancel_question);
        cancelRegistration.setCancelable(false)
                .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        firebaseAuth.getCurrentUser().delete();
                        Intent intent = new Intent(InformationActivity.this, LoginActivity.class);
                        startActivity(intent);
                    }
                })
                .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
//                        dialogInterface.cancel();
                        Toast.makeText(InformationActivity.this, getString(R.string.continue_), Toast.LENGTH_SHORT).show();
                    }
                });
        cancelRegistration.create().show();
    }

    private void spinnerCityListener() {
        spnCity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                cityFromSpinner = adapterView.getItemAtPosition(i).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                cityFromSpinner = adapterView.getItemAtPosition(0).toString();
            }
        });
    }

    private void spinnerNumberListener() {
        spnNumber.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                fullNumber = adapterView.getItemAtPosition(i).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                fullNumber = adapterView.getItemAtPosition(0).toString();
            }
        });
    }

    private int getIndex(Spinner spinner, String myString) {
        for (int i = 0; i < spinner.getCount(); i++) {
            if (spinner.getItemAtPosition(i).toString().equalsIgnoreCase(myString)) {
                return i;
            }
        }
        return 0;
    }
}

