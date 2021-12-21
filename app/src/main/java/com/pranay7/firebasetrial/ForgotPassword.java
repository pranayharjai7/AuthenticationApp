package com.pranay7.firebasetrial;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.pranay7.firebasetrial.databinding.ActivityForgotPasswordBinding;

public class ForgotPassword extends AppCompatActivity {

    private ActivityForgotPasswordBinding binding;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityForgotPasswordBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mAuth = FirebaseAuth.getInstance();

    }

    public void resetPasswordClicked(View view) {
        String email = binding.getEmailAddressEditText.getText().toString();

        if(email.isEmpty()){
            binding.getEmailAddressEditText.setError("Email Address is required!");
            binding.getEmailAddressEditText.requestFocus();
            return;
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            binding.getEmailAddressEditText.setError("Please provide valid Email!");
            binding.getEmailAddressEditText.requestFocus();
            return;
        }

        binding.progressBar.setVisibility(View.VISIBLE);
        mAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(ForgotPassword.this, "Reset link has been sent on this email!",Toast.LENGTH_LONG).show();
                }
                else{
                    Toast.makeText(ForgotPassword.this,"Oops, You Entered an Invalid email! Try again..",Toast.LENGTH_LONG).show();
                }
                binding.progressBar.setVisibility(View.GONE);
            }
        });
    }
}