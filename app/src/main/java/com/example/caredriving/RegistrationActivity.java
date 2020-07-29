package com.example.caredriving;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class RegistrationActivity extends AppCompatActivity implements View.OnClickListener {

    private FirebaseAuth firebaseAuth;
    //    private FirebaseAuth.AuthStateListener authListener;
    private EditText etRegistrationPassword;
    private EditText etRegistrationEmail;
    private Button btnRegister;

    private TextView tvLogin;

    private ProgressDialog progressDialog;

    private String email;
    private String password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        etRegistrationPassword = findViewById(R.id.etRegistrationPassword);
        etRegistrationEmail = findViewById(R.id.etRegistrationEmail);
        btnRegister = findViewById(R.id.btnRegister);
        tvLogin = findViewById(R.id.tvLogin);

        progressDialog = new ProgressDialog(this);

        // get the login status of the app - user loged in or not
        firebaseAuth = FirebaseAuth.getInstance();


        btnRegister.setOnClickListener(this);
        tvLogin.setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {
        if (view == btnRegister) {
            registerUser();
        }

        if (view == tvLogin) {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        }
    }

    private void openInformationActivity() {
        Intent intent = new Intent(RegistrationActivity.this, InformationActivity.class);
        intent.putExtra("Email", email);
        startActivity(intent);
    }

    private void registerUser() {
        email = etRegistrationEmail.getText().toString().trim();
        password = etRegistrationPassword.getText().toString().trim();

        if (TextUtils.isEmpty(email)) {
            Toast.makeText(this, "Please enter email", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Please enter password", Toast.LENGTH_SHORT).show();
            return;
        }
        hideKeyboard();
        progressDialog.setMessage("Registering user...");
        progressDialog.show();

        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            progressDialog.dismiss();
                            progressDialog.cancel();
//                            Toast.makeText(RegistrationActivity.this,"Open information activity...", Toast.LENGTH_SHORT).show();
                            openInformationActivity();

                        } else {
//                            Toast.makeText(RegistrationActivity.this,"Failed, please try again...", Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                            progressDialog.cancel();
                        }
                    }
                });
    }

    private void hideKeyboard() {
        etRegistrationEmail.onEditorAction(EditorInfo.IME_ACTION_DONE);
        etRegistrationPassword.onEditorAction(EditorInfo.IME_ACTION_DONE);
    }
}
