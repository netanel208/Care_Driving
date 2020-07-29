package com.example.caredriving;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toolbar;

import com.example.caredriving.firebase.model.FirebaseDBUser;
import com.example.caredriving.firebase.model.dataObject.StudentObj;
import com.example.caredriving.firebase.model.dataObject.TeacherObj;
import com.example.caredriving.firebase.model.dataObject.UserObj;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class PersonalAreaActivity extends AppCompatActivity implements View.OnClickListener {

    UserObj user;

    EditText fnameEditText;
    EditText lnameEditText;
    EditText ageEditText;
    EditText cityEditText;
    EditText emailEditText;
    EditText phoneNumberEditText;

    // TeacherObj info
    EditText carTypeEditText;
    EditText carYearEditText;
    EditText experienceEditText;
    EditText teacher_transmissionEditText;
    EditText lessonPriceEditText;
    LinearLayout carTypeLayout;
    LinearLayout carYearLayout;
    LinearLayout experienceLayout;
    LinearLayout teacher_transmissionLayout;
    LinearLayout lessonPriceLayout;

    // StudentObj info
    EditText greenFormEditText;
    EditText student_transmissionEditText;
    EditText theoryEditText;
//    EditText teacherIdEditText;
    LinearLayout greenFormLayout;
    LinearLayout student_transmissionLayout;
    LinearLayout theoryLayout;
//    LinearLayout teacherIdLayout;

    ArrayList<View> userViews;
    ArrayList<View> teacherViews;
    ArrayList<View> studentViews;
    ArrayList<View> teacherLayouts;
    ArrayList<View> studentLayouts;

    Button editBtn;
    Button saveBtn;

    boolean savePressed = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_area);


        //take the user from login activity and put it in "user" variable
        user = (UserObj) getIntent().getSerializableExtra("UserObj");


        userViews = new ArrayList<View>();
        teacherViews = new ArrayList<View>();
        studentViews = new ArrayList<View>();
        teacherLayouts = new ArrayList<View>();
        studentLayouts = new ArrayList<View>();


        fnameEditText = findViewById(R.id.fillFirstName);
        lnameEditText = findViewById(R.id.fillLastName);
        ageEditText = findViewById(R.id.fillAge);
        cityEditText = findViewById(R.id.fillCity);
        emailEditText = findViewById(R.id.fillemail);
        phoneNumberEditText = findViewById(R.id.fillphoneNumber);
        carTypeEditText = findViewById(R.id.fillcarType);
        carYearEditText = findViewById(R.id.fillcarYear);
        experienceEditText = findViewById(R.id.fillexperience);
        teacher_transmissionEditText = findViewById(R.id.fillteacher_transmission);
        lessonPriceEditText = findViewById(R.id.filllessonPrice);
        greenFormEditText = findViewById(R.id.fillgreenForm);
        student_transmissionEditText = findViewById(R.id.fillstudent_transmission);
        theoryEditText = findViewById(R.id.filltheory);
//        teacherIdEditText = findViewById(R.id.fillteacherId);
        userViews.add(fnameEditText);
        userViews.add(lnameEditText);
        userViews.add(ageEditText);
        userViews.add(cityEditText);
        userViews.add(emailEditText);
        userViews.add(phoneNumberEditText);
        teacherViews.add(carTypeEditText);
        teacherViews.add(carYearEditText);
        teacherViews.add(experienceEditText);
        teacherViews.add(teacher_transmissionEditText);
        teacherViews.add(lessonPriceEditText);
        studentViews.add(greenFormEditText);
        studentViews.add(student_transmissionEditText);
        studentViews.add(theoryEditText);
//        studentViews.add(teacherIdEditText);


        carTypeLayout = findViewById(R.id.carType);
        carYearLayout = findViewById(R.id.carYear);
        experienceLayout = findViewById(R.id.experience);
        teacher_transmissionLayout = findViewById(R.id.teacher_transmission);
        lessonPriceLayout = findViewById(R.id.lessonPrice);
        greenFormLayout = findViewById(R.id.greenForm);
        student_transmissionLayout = findViewById(R.id.student_transmission);
        theoryLayout = findViewById(R.id.theory);
//        teacherIdLayout = findViewById(R.id.teacherId);
        teacherLayouts.add(carTypeLayout);
        teacherLayouts.add(carYearLayout);
        teacherLayouts.add(experienceLayout);
        teacherLayouts.add(teacher_transmissionLayout);
        teacherLayouts.add(lessonPriceLayout);
        studentLayouts.add(greenFormLayout);
        studentLayouts.add(student_transmissionLayout);
        studentLayouts.add(theoryLayout);
//        studentLayouts.add(teacherIdLayout);


        editBtn = findViewById(R.id.editButton);
        saveBtn = findViewById(R.id.saveButton);


        //Fill hint details in all fields according the "user" and "type" from intent
        fillHintDetails();


        editBtn.setOnClickListener(this);
        saveBtn.setOnClickListener(this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        intent.putExtra("UserObj", user);
        startActivityForResult(intent, 0);
        return true;
    }

    @Override
    public void onClick(View view) {
        if(view == editBtn) {
            editButtonClicked();
            saveBtn.setEnabled(true);
        }
        if(view == saveBtn) {
            saveButtonClicked();
            saveBtn.setEnabled(false);
        }
    }

    private void setTextViewsNotEditable(){
        for(View view: userViews){
            view.setEnabled(false);
        }
        if(user instanceof TeacherObj){
            for(View view: teacherViews){
                view.setEnabled(false);
            }
        }
        if(user instanceof StudentObj){
            for(View view: studentViews){
                view.setEnabled(false);
            }
        }
    }

    private void setEditTextsEditable(){
        for(View view: userViews){
            if(view != emailEditText) {
                view.setEnabled(true);
            }
        }
        if(user instanceof TeacherObj){
            for(View view: teacherViews){
                view.setEnabled(true);
            }
        }
        if(user instanceof StudentObj){
            for(View view: studentViews){
                view.setEnabled(true);
            }
        }
    }

    private void fillHintDetails(){
        // UserObj need basic hint details
        fnameEditText.setHint(user.getFirstName());
        lnameEditText.setHint(user.getLastName());
        ageEditText.setHint(user.getAge());
        cityEditText.setHint(user.getCity());
        emailEditText.setHint(user.getEmail());
        phoneNumberEditText.setHint(user.getPhoneNumber());

        // TeacherObj hint details
        if(user instanceof TeacherObj){
            setStudentViewsGone();
            fillHintTeacherViews();
        }
        // StudentObj hint details
        if(user instanceof StudentObj){
            setTeacherViewsGone();
            fillHintStudentViews();
        }
    }

    private void setStudentViewsGone(){
        for(View view : studentLayouts){
            view.setVisibility(View.GONE);
        }
    }

    private void setTeacherViewsGone(){
        for(View view : teacherLayouts){
            view.setVisibility(View.GONE);
        }
    }

    private void fillHintTeacherViews(){
        TeacherObj teacher = (TeacherObj) user;
        carTypeEditText.setHint(teacher.getCarType());
        carYearEditText.setHint(teacher.getCarYear());
        experienceEditText.setHint(teacher.getExperience());
        teacher_transmissionEditText.setHint(teacher.getTransmission());
        lessonPriceEditText.setHint(teacher.getLessonPrice());
    }

    private void fillHintStudentViews(){
        StudentObj student = (StudentObj) user;
        greenFormEditText.setHint(student.getGreenForm());
        student_transmissionEditText.setHint(student.getTransmission());
        theoryEditText.setHint(student.getTheory());
//        teacherIdEditText.setHint(student.getTeacherId());
    }

    private void fillTextDetails(){
        // UserObj need basic text details
        fnameEditText.setText(user.getFirstName());
        lnameEditText.setText(user.getLastName());
        ageEditText.setText(user.getAge());
        cityEditText.setText(user.getCity());
        emailEditText.setText(user.getEmail());
        phoneNumberEditText.setText(user.getPhoneNumber());


        // TeacherObj text details
        if(user instanceof TeacherObj){
            fillTextTeacherViews();
        }
        // StudentObj text details
        if(user instanceof StudentObj){
            fillTextStudentViews();
        }
    }

    private void fillTextTeacherViews(){
        TeacherObj teacher = (TeacherObj) user;
        carTypeEditText.setText(teacher.getCarType());
        carYearEditText.setText(teacher.getCarYear());
        experienceEditText.setText(teacher.getExperience());
        teacher_transmissionEditText.setText(teacher.getTransmission());
        lessonPriceEditText.setText(teacher.getLessonPrice());
    }

    private void fillTextStudentViews(){
        StudentObj student = (StudentObj) user;
        greenFormEditText.setText(student.getGreenForm());
        student_transmissionEditText.setText(student.getTransmission());
        theoryEditText.setText(student.getTheory());
//        teacherIdEditText.setText(student.getTeacherId());
    }

    private void editButtonClicked(){
        setEditTextsEditable();
        fillTextDetails();
    }

    private void saveButtonClicked(){
        FirebaseDBUser fb_user = new FirebaseDBUser();
        DatabaseReference userReference = fb_user.getUserRefFromDB().child("info");
        if(user instanceof TeacherObj){
            TeacherObj teacher = (TeacherObj) user;
            // Remove old information about this teacher from DB
            removeOldInformationAboutThisTeacher();
            // Update new information in this teacherObj
            teacher.setFirstName(fnameEditText.getText().toString());
            teacher.setLastName(lnameEditText.getText().toString());
            teacher.setAge(ageEditText.getText().toString());
            teacher.setCity(cityEditText.getText().toString());
            teacher.setPhoneNumber(phoneNumberEditText.getText().toString());
            teacher.setCarBrand(carTypeEditText.getText().toString());
            teacher.setCarYear(carYearEditText.getText().toString());
            teacher.setExperience(experienceEditText.getText().toString());
            teacher.setLessonPrice(lessonPriceEditText.getText().toString());
            teacher.setTransmission(teacher_transmissionEditText.getText().toString());
            // Update new information about this teacher on DB
            fb_user.writeUserToDB(teacher);
            user = teacher;
        }
        if(user instanceof StudentObj){
            StudentObj student = (StudentObj) user;
            student.setFirstName(fnameEditText.getText().toString());
            student.setLastName(lnameEditText.getText().toString());
            student.setAge(ageEditText.getText().toString());
            student.setCity(cityEditText.getText().toString());
            student.setPhoneNumber(phoneNumberEditText.getText().toString());
            student.setGreenForm(greenFormEditText.getText().toString());
            student.setTransmission(student_transmissionEditText.getText().toString());
            student.setTheory(theoryEditText.getText().toString());
//            student.setTeacherId(teacherIdEditText.getText().toString());
            fb_user.writeUserToDB(student);
            user = student;
        }
        setTextViewsNotEditable();
        savePressed = true;
    }

    @Override
    public void onBackPressed(){
        super.onBackPressed();
        if(savePressed){
            Intent intent = new Intent(PersonalAreaActivity.this, MainActivity.class);
            intent.putExtra("UserObj", user);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }
        else {
            Intent intent = new Intent();
            Bundle data = new Bundle();
            data.putSerializable("UserObj", user);
            intent.putExtras(data);
            setResult(RESULT_OK, intent);
            finish();
        }
    }

    public void removeOldInformationAboutThisTeacher(){
        FirebaseDatabase.getInstance().getReference().child("Search Teachers").child("Location").child(user.getCity()).child(user.getId()).removeValue();
        FirebaseDatabase.getInstance().getReference().child("Search Teachers").child("Transmission").child(((TeacherObj) user).getTransmission()).child(user.getId()).removeValue();
        FirebaseDatabase.getInstance().getReference().child("Search Teachers").child("Car Type").child(((TeacherObj) user).getCarType()).child(user.getId()).removeValue();
        FirebaseDatabase.getInstance().getReference().child("Search Teachers").child("Price").child(((TeacherObj) user).getLessonPrice()).child(user.getId()).removeValue();
    }
}
