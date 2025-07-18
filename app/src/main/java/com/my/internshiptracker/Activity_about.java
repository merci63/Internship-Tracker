package com.my.internshiptracker;

import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.BuildConfig;

public class Activity_about extends AppCompatActivity {
    private TextView about;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("About");

        about = findViewById(R.id.about);
        String version = BuildConfig.VERSION_NAME;
        String content = "Internship Tracker\nVersion: "+ version + "\n"+
                "Track and manage internship application with Firebase.\n\n"+ "Developed by Mersimoy Bobo\n"+ "GitHub: https://github.com/merci63 ";
        about.setText(content);
        about.setMovementMethod(LinkMovementMethod.getInstance());

    }

   public boolean onSupportNavigateUp(){
        finish();
        return true;
   }
}