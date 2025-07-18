package com.my.internshiptracker;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;

import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Activity_settings extends AppCompatActivity {
    private Switch themeSwitch;
    private FirebaseAuth mAuth;
    private EditText oldPass, newPass, confPass;
    private Button passBtn;
    private SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Settings");

        mAuth = FirebaseAuth.getInstance();
        oldPass = findViewById(R.id.oldPass);
        newPass = findViewById(R.id.newPass);
        confPass = findViewById(R.id.confPass);
        passBtn = findViewById(R.id.passBtn);
        themeSwitch = findViewById(R.id.themeSwitch);
        sharedPreferences = getSharedPreferences("AppSetings", MODE_PRIVATE);
        boolean isDarkMode = sharedPreferences.getBoolean("darkMode", false);
        themeSwitch.setChecked(isDarkMode);

        themeSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean("darkMode", isChecked);
            editor.apply();
            AppCompatDelegate.setDefaultNightMode(isChecked ? AppCompatDelegate.MODE_NIGHT_YES : AppCompatDelegate.MODE_NIGHT_NO);
        });
        
        passBtn.setOnClickListener(v -> {
            String currentPass = oldPass.getText().toString().trim();
            String newPassword = newPass.getText().toString().trim();
            String confirmPass = confPass.getText().toString().trim();

            if (currentPass.isEmpty()){
                oldPass.setError("Required Field.");
                return;
            }
            if ( newPassword.isEmpty()){
                newPass.setError("Required Field.");
                return;
            }
            if (confirmPass.isEmpty()){
                confPass.setError("Required Field.");
                return;
            }
            if (newPassword.length()<6){
                newPass.setError("New Password must be at least 6 character");
                return;
            }
            if (!newPassword.equals(confirmPass)){
                confPass.setError("Password doesn't match");
                return;
            }

            FirebaseUser user = mAuth.getCurrentUser();
            if (user != null && user.getEmail() != null){
                AuthCredential authCredential = EmailAuthProvider.getCredential(user.getEmail(), currentPass);
                user.reauthenticate(authCredential);
                user.updatePassword(newPassword).addOnSuccessListener(aVoid ->{
                 Toast.makeText(this, "Password Changed Successufully", Toast.LENGTH_SHORT).show();
                 oldPass.setText("");
                 newPass.setText("");
                 confPass.setText("");
                 mAuth.signOut();
                 startActivity(new Intent(this, Activity_login.class));
                 finish();
                }).addOnFailureListener(e -> Toast.makeText(this, "Error changing password: "+ e.getMessage(),Toast.LENGTH_SHORT).show());
            }

        });


    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}