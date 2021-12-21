package com.pranay7.firebasetrial;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.pranay7.firebasetrial.databinding.ActivityRegisterUserBinding;

public class RegisterUser extends AppCompatActivity {

    private ActivityRegisterUserBinding binding;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegisterUserBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mAuth = FirebaseAuth.getInstance();
    }


    public void bannerTextViewClicked(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void registerUserButtonClicked(View view) {
        String fullName = binding.fullNameEditText.getText().toString();
        String emailAddress = binding.emailAddressEditText.getText().toString();
        String password = binding.passwordEditText.getText().toString();

        if(fullName.isEmpty()){
            binding.fullNameEditText.setError("Full Name is required!");
            binding.fullNameEditText.requestFocus();
            return;
        }
        if(emailAddress.isEmpty()){
            binding.emailAddressEditText.setError("Email Address is required!");
            binding.emailAddressEditText.requestFocus();
            return;
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(emailAddress).matches()){
            binding.emailAddressEditText.setError("Please provide valid Email!");
            binding.emailAddressEditText.requestFocus();
            return;
        }
        if(password.isEmpty()){
            binding.passwordEditText.setError("Password is required!");
            binding.passwordEditText.requestFocus();
            return;
        }
        if(password.length() < 6){
            binding.passwordEditText.setError("Password length should be min 6 characters!");
            binding.passwordEditText.requestFocus();
            return;
        }

        binding.progressBar.setVisibility(View.VISIBLE);
        mAuth.createUserWithEmailAndPassword(emailAddress,password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            User user = new User(fullName,emailAddress);
                            realTime(user);
                            Intent intent = new Intent(RegisterUser.this, MainActivity.class);
                            startActivity(intent);
                        }
                        else{
                            Toast.makeText(RegisterUser.this,"Failed to register, try again!",Toast.LENGTH_LONG).show();
                            binding.progressBar.setVisibility(View.GONE);
                        }
                    }
                });

    }

    private void realTime(User user) {
        FirebaseDatabase.getInstance()
                .getReference("Users")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .setValue(user)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        if(task.isSuccessful()){
                            Toast.makeText(RegisterUser.this,"User has been registered Successfully!",Toast.LENGTH_LONG).show();
                        }
                        else{
                            Toast.makeText(RegisterUser.this,"Failed to register, try again!",Toast.LENGTH_LONG).show();
                        }
                        binding.progressBar.setVisibility(View.GONE);
                    }
                });
    }
}