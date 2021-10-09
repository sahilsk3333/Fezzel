package com.sahilpc.fezzle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import com.sahilpc.fezzle.Loaders.llottiedialogfragment;
import com.sahilpc.fezzle.Models.Users;


public class SignUpActivity extends AppCompatActivity {
    private FirebaseAuth auth;
    private FirebaseDatabase database;

    TextInputLayout username, usermail, password;
    ImageView backbtn;
    Button signup;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);


        backbtn = findViewById(R.id.backbtn);
        signup = findViewById(R.id.signup);

        username = findViewById(R.id.usernamelayout);
        usermail = findViewById(R.id.userMailLayout);
        password = findViewById(R.id.passwordLayout);

        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
                finish();
            }
        });

        //loading dialog
        final llottiedialogfragment lottie = new llottiedialogfragment(this);


        // Initialize Firebase Auth
        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!validateUserName() | !validateUserMail() | !validatePassword()) {
                    return;
                }
                lottie.show();

                auth.createUserWithEmailAndPassword(usermail.getEditText().getText().toString().trim(), password.getEditText().getText().toString().trim()).
                        addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull @org.jetbrains.annotations.NotNull Task<AuthResult> task) {
                                lottie.dismiss();
                                if (task.isSuccessful()) {
                                    Users user = new Users(username.getEditText().getText().toString().trim(), usermail.getEditText().getText().toString().trim(), password.getEditText().getText().toString().trim(),"online");
                                    String id = task.getResult().getUser().getUid();
                                    database.getReference().child("Users").child(id).setValue(user);

                                    Toast.makeText(SignUpActivity.this, "Welcome", Toast.LENGTH_SHORT).show();


                                    Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
                                    startActivity(intent);
                                    finish();
                                } else {
                                    Toast.makeText(SignUpActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                }

                            }
                        });

            }


        });


    }

    public void onBackPressed() {
        super.onBackPressed();
        return;
    }

    private boolean validateUserName() {

        String val = username.getEditText().getText().toString().trim();

        if (val.isEmpty()) {
            username.setError("Field Can not be empty");
            return false;
        } else {
            username.setError(null);
            username.setErrorEnabled(false);
            return true;
        }
    }

    private boolean validateUserMail() {

        String val = usermail.getEditText().getText().toString().trim();
        String checkEmail = "[a-zA-Z0-9._-]+@[a-z]+.+[a-z]+";

        if (val.isEmpty()) {
            usermail.setError("Field can not be empty");
            return false;
        } else if (!val.matches(checkEmail)) {
            usermail.setError("Invalid Email!");
            return false;
        } else {
            usermail.setError(null);
            usermail.setErrorEnabled(false);
            return true;
        }
    }

    private boolean validatePassword() {

        String val = password.getEditText().getText().toString().trim();

        if (val.isEmpty()) {
            password.setError("Field can not be empty");
            return false;
        } else {
            password.setError(null);
            password.setErrorEnabled(false);
            return true;
        }
    }


}