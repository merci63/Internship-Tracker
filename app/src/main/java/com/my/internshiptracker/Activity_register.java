package com.my.internshiptracker;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;

import org.w3c.dom.Text;

public class Activity_register extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private EditText etEmail, etPass, etConfirmPass;
    private TextView signin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();
        etEmail = findViewById(R.id.etEmail);
        etPass = findViewById(R.id.etPassword);
        etConfirmPass = findViewById(R.id.etConfirmPassword);
        signin = findViewById(R.id.signin);
        Button btnSubmit = findViewById(R.id.btnSubmitRegistrer);
        btnSubmit.setOnClickListener(v -> registerUser());
        signin.setOnClickListener(v -> {
            startActivity(new Intent(this, Activity_login.class));
        });

    }

    private void registerUser() {
        String email = etEmail.getText().toString();
        String pass = etPass.getText().toString();
        String confirm = etConfirmPass.getText().toString();

        if (email.isEmpty() || pass.isEmpty() || !pass.equals(confirm)){
            Toast.makeText(this, "Required Field", Toast.LENGTH_SHORT).show();
            return;
        }

        mAuth.createUserWithEmailAndPassword(email,pass).addOnCompleteListener(task -> {
            if (task.isSuccessful()){
                mAuth.signOut();
                Toast.makeText(this,"Registration successful, please log in", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(this, Activity_login.class));
                finish();
            } else {
                Toast.makeText(this, "Registration failed: "+ task.getException().getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}