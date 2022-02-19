package com.pranay7.firebasetrial;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.pranay7.firebasetrial.databinding.ActivityAfterLoginBinding;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.TemporalAmount;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class AfterLoginActivity extends AppCompatActivity {

    private ActivityAfterLoginBinding binding;
    private FirebaseAuth mAuth;
    private FirebaseDatabase mReal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAfterLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mAuth = FirebaseAuth.getInstance();
        mReal = FirebaseDatabase.getInstance();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.after_login_options_menu, menu);
        return true;
    }

    public void profileOptionClicked(@NonNull MenuItem item) {
        Intent intent = new Intent(this, UserProfile.class);
        startActivity(intent);
    }

    public void logoutOptionClicked(@NonNull MenuItem item) {
        FirebaseAuth.getInstance().signOut();
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void startButtonClicked(View view) {

        binding.progressBar.setVisibility(View.VISIBLE);

        String timeAtStart = LocalTime.now().toString();
        long t = LocalTime.now().minusNanos(LocalTime.parse(timeAtStart).toNanoOfDay()).toNanoOfDay();
        String averageTime = LocalTime.ofNanoOfDay(t).toString();
        Queue queue = new Queue(0, 2, 2, averageTime, timeAtStart, timeAtStart);

        mReal.getReference("Queue")
                .child(mAuth.getCurrentUser().getUid())
                .setValue(queue)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(AfterLoginActivity.this, "Queue has been started!", Toast.LENGTH_LONG).show();
                            updateScreen();

                        } else {
                            Toast.makeText(AfterLoginActivity.this, "Can't Start at the moment, Please try again!", Toast.LENGTH_LONG).show();
                        }
                        binding.progressBar.setVisibility(View.GONE);
                    }
                });


    }

    private void updateScreen() {

        mReal.getReference("Queue")
                .child(mAuth.getCurrentUser().getUid())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @RequiresApi(api = Build.VERSION_CODES.O)
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        Queue queue = dataSnapshot.getValue(Queue.class);
                        if (queue != null) {
                            binding.currentNumberData.setText("" + queue.currentNumber);
                            binding.lastNumberData.setText("" + queue.lastNumber);
                            binding.customersLeftData.setText("" + queue.customersLeft);

                            LocalTime avgTime = LocalTime.parse(queue.averageTime);
                            int hour = avgTime.getHour();
                            int min = avgTime.getMinute();
                            int sec = avgTime.getSecond();
                            binding.averageTimeData.setText(""+hour+" h, "+min+ " m, "+sec+" s");
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

    }


    public void nextButtonClicked(View view) {
        binding.progressBar.setVisibility(View.VISIBLE);
        mReal.getReference("Queue")
                .child(mAuth.getCurrentUser().getUid())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @RequiresApi(api = Build.VERSION_CODES.O)
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        Queue queue = dataSnapshot.getValue(Queue.class);
                        if (queue != null) {
                            updateDatabase(queue);
                        }
                        binding.progressBar.setVisibility(View.GONE);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void updateDatabase(Queue queue) {
        int currentNumber = queue.currentNumber;
        int lastNumber = queue.lastNumber;
        int customersLeft = queue.customersLeft;
        String averageTime = queue.averageTime;
        String timeOfStart = queue.timeOfStart;
        String timeOfNext = queue.timeOfNext;

        if (currentNumber < lastNumber) {
            currentNumber++;
        } else {
            Toast.makeText(this, "This was the last Customer!", Toast.LENGTH_LONG).show();
            return;
        }

        customersLeft = lastNumber - currentNumber;

        LocalTime next = LocalTime.now();
        timeOfNext = next.toString();
        long t = next.minusNanos(LocalTime.parse(timeOfStart).toNanoOfDay()).toNanoOfDay();
        if (currentNumber != 0) {
            t = t / currentNumber;
        }
        LocalTime avgTime = LocalTime.ofNanoOfDay(t);
        averageTime = avgTime.toString();

        Queue newQueue = new Queue(currentNumber, lastNumber, customersLeft, averageTime, timeOfStart, timeOfNext);
        mReal.getReference("Queue")
                .child(mAuth.getCurrentUser().getUid())
                .setValue(newQueue)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(AfterLoginActivity.this,"Queue is updated", Toast.LENGTH_LONG).show();
                        }
                        else {
                            Toast.makeText(AfterLoginActivity.this, "Can't update at the moment, Please try again!", Toast.LENGTH_LONG).show();
                        }
                    }
                });

        updateScreen();



    }

    public void pauseButtonClicked(View view) {
        //Stop assigning Numbers
    }

    public void resetButtonClicked(View view) {

        binding.progressBar.setVisibility(View.VISIBLE);
        mReal.getReference("Queue")
                .child(mAuth.getCurrentUser().getUid())
                .removeValue()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            resetQueue();
                            Toast.makeText(AfterLoginActivity.this, "Reset Complete", Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(AfterLoginActivity.this, "Can't Reset at the moment, Please try again!", Toast.LENGTH_LONG).show();
                        }
                        binding.progressBar.setVisibility(View.GONE);
                    }
                });
    }

    private void resetQueue() {
        binding.currentNumberData.setText("");
        binding.lastNumberData.setText("");
        binding.customersLeftData.setText("");
        binding.averageTimeData.setText("");
    }


}