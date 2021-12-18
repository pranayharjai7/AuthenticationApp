package com.pranay7.firebasetrial;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.pranay7.firebasetrial.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        mAuth = FirebaseAuth.getInstance();
    }

    public void registerTextViewClicked(View view) {
        Intent intent = new Intent(this, RegisterUser.class);
        startActivity(intent);
    }

    public void loginButtonClicked(View view) {
        String emailAddress = binding.emailAddressEditText.getText().toString();
        String password = binding.passwordEditText.getText().toString();

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

        binding.progressBar.setVisibility(View.VISIBLE);

        mAuth.signInWithEmailAndPassword(emailAddress,password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            //redirect to UserProfile
                            userProfile();
                        }
                        else{
                            Toast.makeText(MainActivity.this,"Failed to login! Please check your credentials",Toast.LENGTH_LONG).show();
                            binding.progressBar.setVisibility(View.GONE);
                        }
                    }
                });
    }

    private void userProfile() {
        Intent intent = new Intent(this, UserProfile.class);
        startActivity(intent);
    }


}