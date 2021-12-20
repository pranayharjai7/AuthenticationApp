package com.pranay7.firebasetrial;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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
        Toast.makeText(this,"profile menu option clicked",Toast.LENGTH_LONG).show();
        Intent intent = new Intent(this, UserProfile.class);
        startActivity(intent);
    }

    public void logoutOptionClicked(@NonNull MenuItem item){
        FirebaseAuth.getInstance().signOut();
        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);
    }
}