package com.my.internshiptracker;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;

public class Activity_reset extends AppCompatActivity {

    private EditText resetEmail;
    private Button btnReset, resetLoginBtn;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset);

        mAuth = FirebaseAuth.getInstance();

        resetEmail = findViewById(R.id.resetEmail);
        btnReset = findViewById(R.id.btnReset);
        resetLoginBtn = findViewById(R.id.resetLoginBtn);

        resetLoginBtn.setOnClickListener(v -> {
            startActivity(new Intent(this, Activity_login.class));
        });

        btnReset.setOnClickListener(v -> resetPassword());

    }

    private void resetPassword() {
        String rEmail = resetEmail.getText().toString().trim();
        if(rEmail.isEmpty()){
            resetEmail.setError("Email is required");
            return;
        }
        mAuth.sendPasswordResetEmail(rEmail).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(Activity_reset.this, "Password reset email sent", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(Activity_reset.this, "Failed to send reset email: "+task.getException().getMessage(),Toast.LENGTH_SHORT).show();
            }
        });
    }
}