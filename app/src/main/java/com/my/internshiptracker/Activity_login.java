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

public class Activity_login extends AppCompatActivity {
private FirebaseAuth mAuth;
private EditText etEmail, etPassword;
private TextView reset;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mAuth = FirebaseAuth.getInstance();
        mAuth.signOut();
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        reset = findViewById(R.id.reset);

        Button btnLogin = findViewById(R.id.btnLogin);
        Button btnRegister = findViewById(R.id.btnRegister);

        btnLogin.setOnClickListener(v -> loginUser());
        btnRegister.setOnClickListener(v -> startActivity(new Intent(this, Activity_register.class)));

        reset.setOnClickListener(v -> {
            startActivity(new Intent(this, Activity_reset.class));
        });
    }

    private void loginUser() {
        String email = etEmail.getText().toString();
        String pass = etPassword.getText().toString();
        if (email.isEmpty() || pass.isEmpty()){
            Toast.makeText(this, "Field cannot be empty", Toast.LENGTH_SHORT).show();
            return;
        }
        mAuth.signInWithEmailAndPassword(email, pass).addOnCompleteListener(task ->{
            if(task.isSuccessful()){
                startActivity(new Intent(this, MainActivity.class));
                finish();
            } else {
                Toast.makeText(this, "Login failed. "+ task.getException().getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}