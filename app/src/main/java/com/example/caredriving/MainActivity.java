package com.example.caredriving;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.FragmentTransaction;
import android.app.TimePickerDialog;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

import android.os.Build;
import android.os.Bundle;

import com.example.caredriving.firebase.model.FirebaseBaseModel;
import com.example.caredriving.firebase.model.FirebaseDBEntity;
import com.example.caredriving.firebase.model.FirebaseDBLesson;
import com.example.caredriving.firebase.model.FirebaseDBUser;
import com.example.caredriving.firebase.model.dataObject.LessonObj;
import com.example.caredriving.firebase.model.dataObject.StudentObj;
import com.example.caredriving.firebase.model.dataObject.TeacherObj;
import com.example.caredriving.firebase.model.dataObject.UserObj;
//import com.example.caredriving.firebase.model.dataObject.dialogs.ErrorDialog;
import com.example.caredriving.firebase.model.dataObject.validation.Validation;
import com.example.caredriving.ui.navigationBar.requests.RequestsFragment;
import com.example.caredriving.ui.navigationBar.searchTeachers.SearchTeachersFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;


import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.Menu;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;


public class MainActivity extends AppCompatActivity implements
        NavigationView.OnNavigationItemSelectedListener,
        DatePickerDialog.OnDateSetListener,
        TimePickerDialog.OnTimeSetListener,
        View.OnClickListener {

    private AppBarConfiguration mAppBarConfiguration;
    private NavigationView navigationView;
    private DrawerLayout drawer;
    private ImageButton imgButton;
    private View header;
    private TextView nameHeader;
    private TextView emailHeader;

    public UserObj user;
    public FirebaseDBUser fb_user;

    private StudentObj student;
    private int duration = 0;
    private int day, month, year, hour, minute;
    private int dayFinal, monthFinal, yearFinal, hourFinal, minuteFinal;
    boolean dateIsFree = true;
    private FloatingActionButton fab;
    private String[] lessonInfo = new String[3];
    private Validation validation = new Validation();
    private FragmentTransaction fragmentManager;

    private FirebaseDBLesson dbUserLessons;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);

        navigationView = findViewById(R.id.nav_view);
        drawer = findViewById(R.id.drawer_layout);

        fragmentManager = getFragmentManager().beginTransaction();
        fragmentManager.addToBackStack(null);

        dbUserLessons = new FirebaseDBLesson();
        fb_user = new FirebaseDBUser();
        findUser();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_home) {}

        if (id == R.id.nav_search_teachers) {
            MainActivity.this.getSupportFragmentManager().beginTransaction().replace(R.id.containerId, SearchTeachersFragment.newInstance()).commit();
        }

        if (id == R.id.nav_requests) {
            MainActivity.this.getSupportFragmentManager().beginTransaction().replace(R.id.containerId, RequestsFragment.newInstance()).commit();
//            Bundle bundle = new Bundle();
//            bundle.putSerializable("UserObj", user);
//            RequestsFragment fragobj = new RequestsFragment();
//            fragobj.setArguments(bundle);
        }
        DrawerLayout drawerLayout = findViewById(R.id.drawer_layout);
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onClick(View view) {
        if (view == imgButton) {
            headerImageButtonPressed();
        }
        if (view == fab) {
            createNewLesson();
        }
    }

    public void clearBackStackInclusive(String tag) {
        getSupportFragmentManager().popBackStack(tag, FragmentManager.POP_BACK_STACK_INCLUSIVE);
    }

    private void createNewLesson() {

        student = (StudentObj) user;
        String teacherId = student.getTeacherId();

        if (teacherId.equals("null") || teacherId == null) {
            AlertDialog.Builder errorDialog = new AlertDialog.Builder(MainActivity.this);
            errorDialog.setTitle(R.string.information)
                    .setMessage(getString(R.string.error_choose_teacher) + "\n" + getString(R.string.error_go_teacher_search))
                    .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            navigationView.setCheckedItem(R.id.nav_search_teachers);
                            navigationView.getMenu().performIdentifierAction(R.id.nav_search_teachers, 1);
                        }
                    })
                    .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {}
                    });
            errorDialog.create().show();
        } else {
            AlertDialog.Builder createNewLessonDialog = new AlertDialog.Builder(MainActivity.this);
            duration = 0;
            createNewLessonDialog.setTitle(R.string.create_new_lesson_title);
            String[] options = getResources().getStringArray(R.array.lesson_duration);
            createNewLessonDialog.setSingleChoiceItems(options, duration, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    duration = i;
                }
            });

//        createNewLessonDialog.setCancelable(false)
            createNewLessonDialog
                    .setPositiveButton(R.string.set_date, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            duration += 1;
                            lessonInfo[0] = duration + "";
                            continueCreateLessonChooseDate();
                        }
                    })
                    .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.cancel();
                        }
                    });
            createNewLessonDialog.create().show();
        }
    }

    private void continueCreateLessonChooseDate() {
        Calendar calendar = Calendar.getInstance();

        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new
                DatePickerDialog(MainActivity.this, MainActivity.this, year, month, day);
        datePickerDialog.setTitle(R.string.set_date);
        datePickerDialog.setButton(DatePickerDialog.BUTTON_POSITIVE, getString(R.string.set_time), datePickerDialog);
        datePickerDialog.show();
    }

    private void continueCreateLessonChooseTime() {
        Calendar calendar = Calendar.getInstance();
        hour = calendar.get(Calendar.HOUR_OF_DAY);
        minute = calendar.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(MainActivity.this, MainActivity.this,
                hour, minute, true);
        timePickerDialog.setTitle(R.string.set_time);
        timePickerDialog.setButton(timePickerDialog.BUTTON_POSITIVE, getString(R.string.lesson_create), timePickerDialog);
        timePickerDialog.show();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
        yearFinal = i;
        monthFinal = i1 + 1;
        dayFinal = i2;

        lessonInfo[1] = dayFinal + "/" + monthFinal + "/" + yearFinal;

        validation.checkLessonDate(lessonInfo[1]);
        if (validation.hasErrors()) {
            AlertDialog.Builder errorDialog = new AlertDialog.Builder(MainActivity.this);
            errorDialog.setTitle(getString(R.string.error))
                    .setMessage(validation.getErrors().get(0))
                    .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            validation.clearErrors();
                            continueCreateLessonChooseDate();
                        }
                    });
            errorDialog.show();
        } else {
            continueCreateLessonChooseTime();
        }
    }

    @Override
    public void onTimeSet(TimePicker timePicker, int i, int i1) {
        hourFinal = i;
        minuteFinal = i1;

        lessonInfo[2] = hourFinal + ":" + minuteFinal;
        validation.checkTime(lessonInfo[1], lessonInfo[2]);
        if (validation.hasErrors()) {
            AlertDialog.Builder errorDialog = new AlertDialog.Builder(MainActivity.this);
            errorDialog.setTitle(getString(R.string.error))
                    .setMessage(validation.getErrors().get(0))
                    .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            validation.clearErrors();
                            continueCreateLessonChooseTime();
                        }
                    });
            errorDialog.show();
        } else {
            final String studentId = student.getId();
            final String teacherId = student.getTeacherId();

            final LessonObj lessonObj = new LessonObj(studentId, teacherId);
            lessonObj.setDate(lessonInfo[1], lessonInfo[2]);
            lessonObj.setDuration(lessonInfo[0]);

            dateIsFree = true;

            dbUserLessons.getLessonsRefFromDB().child("check").setValue("check");
            dbUserLessons.getLessonsRefFromDB().child("check").removeValue();

            dbUserLessons.getLessonsRefFromDB().addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    String year = lessonObj.getDate().getYear() + "";
                    String month = lessonObj.getDate().getMonth() + "";
                    String day = lessonObj.getDate().getDay() + "";
                    String time = lessonObj.getLessonTime();

                    for (DataSnapshot ds : dataSnapshot.child("students").child(studentId).getChildren()) {
                        if (ds.child(month).child(day).exists()) {
                            for (DataSnapshot ds1 : ds.child(month).child(day).getChildren()) {
                                String key = ds1.getKey();
                                dateIsFree = lessonObj.isAvailableTime(key);
                                if (!dateIsFree) {
                                    Toast.makeText(getApplicationContext(), getString(R.string.error_lesson_created) + "\n" + getString(R.string.error_student_is_busy), Toast.LENGTH_SHORT).show();
                                    return;
                                }
                            }
                        }
                    }

                    for (DataSnapshot ds : dataSnapshot.child("teachers").child(teacherId).getChildren()) {
                        if (ds.child(month).child(day).exists()) {
                            for (DataSnapshot ds1 : ds.child(month).child(day).getChildren()) {
                                String key = ds1.getKey();
                                dateIsFree = lessonObj.isAvailableTime(key);
                                if (!dateIsFree) {
                                    Toast.makeText(getApplicationContext(), getString(R.string.error_lesson_created) + "\n" + getString(R.string.error_teacher_is_busy), Toast.LENGTH_SHORT).show();
                                    return;
                                }
                            }
                        }
                    }

                    if (dateIsFree) {
                        new FirebaseDBLesson().addLessonToDB(lessonObj);
                        Toast.makeText(getApplicationContext(), R.string.lesson_created_successfully, Toast.LENGTH_SHORT).show();
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {}
            });
        }
    }

    private void findUser() {
        fb_user.getMyref().addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // Create local instance of UserObj(TeacherObj/StudentObj) from Entity object(HashMap, String)
//                FirebaseDBEntity entity = dataSnapshot.getValue(FirebaseDBEntity.class);
//                assert entity != null;
//                user = entity.getUserObj();
                user = fb_user.readUserFromDB(dataSnapshot);
                if (user instanceof TeacherObj) {
                    setTeachersNavigationView();
                } else if (user instanceof StudentObj) {
                    setStudentsNavigationView();
                }
                displayHeaderDetailsToUser();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    private void headerImageButtonPressed() {
        //What happen when click on image view in navigation bar
        Intent intent = new Intent(MainActivity.this, PersonalAreaActivity.class);
        intent.putExtra("UserObj", user);
        startActivity(intent);
    }

    private void displayHeaderDetailsToUser() {
        String hello = "Welcome, " + user.getFirstName() + " !";
        nameHeader.setText(hello);
        emailHeader.setText(user.getEmail());
    }

    // Add to the NavigationView restrictions according to the Teacher object
    private void setTeachersNavigationView() {
        navigationView.getMenu().findItem(R.id.nav_search_teachers).setVisible(false);
        navigationView.getMenu().findItem(R.id.nav_teacher).setVisible(false);
        setNavigationView();
    }

    // Add to the NavigationView restrictions according to the Student object
    private void setStudentsNavigationView() {
        createNewLessonButton();
        navigationView.getMenu().findItem(R.id.nav_requests).setVisible(false);
        navigationView.getMenu().findItem(R.id.nav_students).setVisible(false);
        setNavigationView();
    }

    @SuppressLint("RestrictedApi")
    private void createNewLessonButton() {
        fab = findViewById(R.id.fab);
        fab.setVisibility(View.VISIBLE);
        fab.setOnClickListener(this);
    }

    // Set the navigation view
    private void setNavigationView() {
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_search_teachers, R.id.nav_requests,
                R.id.nav_students, R.id.nav_teacher, R.id.imageButton)
                .setDrawerLayout(drawer)
                .build();

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);


        header = navigationView.getHeaderView(0);
        nameHeader = header.findViewById(R.id.nameHeader);
        emailHeader = header.findViewById(R.id.emailHeader);
        imgButton = header.findViewById(R.id.imageButton);
        imgButton.setOnClickListener(this);
    }
}