package com.sahilpc.fezzle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.sahilpc.fezzle.Loaders.llottiedialogfragment;
import com.sahilpc.fezzle.databinding.ActivityForgotPasswordBinding;

import org.jetbrains.annotations.NotNull;

public class ForgotPasswordActivity extends AppCompatActivity {

    ActivityForgotPasswordBinding binding;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityForgotPasswordBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        auth = FirebaseAuth.getInstance();

        final llottiedialogfragment lottie = new llottiedialogfragment(this);

        binding.backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
                finish();
            }
        });

        binding.forgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!validateUserMail()) {
                    return;
                }
                lottie.show();
                auth.sendPasswordResetEmail(binding.maillayout.getEditText().getText().toString().trim()).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull @NotNull Task<Void> task) {

                        lottie.dismiss();

                       if (task.isSuccessful()){

                           Toast.makeText(ForgotPasswordActivity.this, "Forgot Link Send On E-Mail", Toast.LENGTH_SHORT).show();
                           Intent intent = new Intent(ForgotPasswordActivity.this,SignInActivity.class);
                           startActivity(intent);
                           finish();

                       }else {
                           Toast.makeText(ForgotPasswordActivity.this,"Error : "+task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                       }
                    }
                });

            }
        });

    }


    private boolean validateUserMail() {

        String val = binding.usermail.getText().toString().trim();
        String checkEmail = "[a-zA-Z0-9._-]+@[a-z]+.+[a-z]+";

        if (val.isEmpty()) {
            binding.maillayout.setError("Field can not be empty");
            return false;
        } else if (!val.matches(checkEmail)) {
            binding.maillayout.setError("Invalid Email!");
            return false;
        } else {
            binding.maillayout.setError(null);
            binding.maillayout.setErrorEnabled(false);
            return true;
        }
    }

    public void onBackPressed() {
        super.onBackPressed();
        return;
    }
}