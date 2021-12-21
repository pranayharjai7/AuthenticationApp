package com.pranay7.firebasetrial;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class AfterLoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_after_login);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.after_login_options_menu,menu);
        return true;
    }

    public void profileOptionClicked(@NonNull MenuItem item){
        Intent intent = new Intent(this, UserProfile.class);
        startActivity(intent);
    }

    public void logoutOptionClicked(@NonNull MenuItem item){
        FirebaseAuth.getInstance().signOut();
        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);
    }

    public void startButtonClicked(View view) {
    }

    public void pauseButtonClicked(View view) {
    }

    public void resetButtonClicked(View view) {
    }

    public void stopButtonClicked(View view) {
    }
}