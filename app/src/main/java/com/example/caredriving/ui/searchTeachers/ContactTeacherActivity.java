package com.example.caredriving.ui.searchTeachers;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.caredriving.MainActivity;
import com.example.caredriving.R;
import com.google.android.material.navigation.NavigationView;

public class ContactTeacherActivity extends AppCompatActivity {

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
    private static final int REQUEST_CALL = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_teacher);
        getSupportActionBar().setTitle("Contact Teacher");
        Log.d(TAG, "ContactTeacherActivity: started.");

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
        //getIncomingIntent();

        // Call to the teacher by clicking the phone image

        imgPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makePhoneCall();
            }
        });
    }


    // Get the incoming intents
    private void getIncomingIntent(){
        Log.d(TAG, "getIncomingIntent: checking for incoming intents.");
        // Checks if the intent have any extras before trying to get the extras
        /*
        if(getIntent().hasExtra("TeachersFirstName") && getIntent().hasExtra("TeachersLastName") && getIntent().hasExtra("TeachersExperience")
                && getIntent().hasExtra("TeachersLocation") && getIntent().hasExtra("TeachersCarType") && getIntent().hasExtra("TeachersCarYear")
                && getIntent().hasExtra("TeachersGearType") && getIntent().hasExtra("TeachersLessonPrice") && getIntent().hasExtra("TeachersPhoneNumber")){
            Log.d(TAG, "getIncomingIntent: get and set incoming intents.");
*/
            String firstName = getIntent().getStringExtra("TeachersFirstName");
            /*
            String lastName = getIntent().getStringExtra("TeachersLastName");
            String experience = getIntent().getStringExtra("TeachersExperience");
            String location = getIntent().getStringExtra("TeachersLocation");
            String carType = getIntent().getStringExtra("TeachersCarType");
            String carYear = getIntent().getStringExtra("TeachersCarYear");
            String gearType = getIntent().getStringExtra("TeachersGearType");
            String lessonPrice = getIntent().getStringExtra("TeachersLessonPrice");
            String phoneNumber = getIntent().getStringExtra("TeachersPhoneNumber");


             */
            tvTeachersFirstName.setText(firstName);
            /*
            tvTeachersLastName.setText(lastName);
            tvTeachersExperience.setText(experience);
            tvTeacherslocations.setText(location);
            tvCarType.setText(carType);
            tvCarYear.setText(carYear);
            tvGearType.setText(gearType);
            tvLessonPrice.setText(lessonPrice);
            tvTeachersPhoneNumber.setText(phoneNumber);


             */
        //}
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


}