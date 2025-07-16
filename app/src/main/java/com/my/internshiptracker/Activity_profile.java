package com.my.internshiptracker;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.firebase.auth.FirebaseAuth;

public class Activity_profile extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private TextView tvEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        mAuth = FirebaseAuth.getInstance();
        Log.d("Activity_profile", "Current user: " + (mAuth.getCurrentUser() != null ? mAuth.getCurrentUser().getUid() : "null"));
        if (mAuth.getCurrentUser() == null) {
            startActivity(new Intent(this, Activity_login.class));
            finish();
            return;
        }
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Profile");

        tvEmail = findViewById(R.id.tvEmail);
        tvEmail.setText(mAuth.getCurrentUser().getEmail());
    }:

    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }
}