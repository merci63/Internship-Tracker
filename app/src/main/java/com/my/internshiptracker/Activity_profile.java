package com.my.internshiptracker;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.FirebaseFirestore;
import com.my.internshiptracker.Model.NameChange;

import java.util.Calendar;
import java.util.Date;

public class Activity_profile extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private  final int NAME_CHANGE_LIMIT = 2;
    private TextView tvEmail, userName;
    private EditText name;
    private Button updateBtn, verifyEmailBtn;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);


        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        if (user == null) {
            Toast.makeText(this,"User not logged in", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, Activity_login.class));
            finish();
            return;
        }
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Profile");

        userName = findViewById(R.id.userName);
        name = findViewById(R.id.name);
        updateBtn = findViewById(R.id.updateBtn);
        verifyEmailBtn = findViewById(R.id.verifyEmail);
        tvEmail = findViewById(R.id.tvEmail);
//        tvEmail.setText(mAuth.getCurrentUser().getEmail());

            tvEmail.setText("Email: "+user.getEmail());
            userName.setText("Name: "+(user.getDisplayName() != null ? user.getDisplayName() : "Not set"));
            name.setText(user.getDisplayName() != null ? user.getDisplayName() : "");
            verifyEmailBtn.setVisibility(user.isEmailVerified()? View.GONE : View.VISIBLE);

        toolbar.setNavigationOnClickListener(v -> finish());

        updateBtn.setOnClickListener(v -> {
            if(!user.isEmailVerified()){
                Toast.makeText(this,"Please verify your email to update your name",Toast.LENGTH_SHORT).show();
                return;
            }
            String newName = name.getText().toString().trim();
            if (newName.isEmpty()){
                name.setError("Enter name");
                Toast.makeText(this, "Enter a display name", Toast.LENGTH_SHORT).show();
                return;
            }
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.MONTH, -1);
            Date oneMonthAgo = calendar.getTime();
            db.collection("users").document(user.getUid()).collection("nameChanges").whereGreaterThanOrEqualTo("timestamp", new Timestamp(oneMonthAgo)).get().addOnSuccessListener(queryDocumentSnapshots -> {
                if (queryDocumentSnapshots.size() >= NAME_CHANGE_LIMIT){
                    Toast.makeText(this,"You can only change two times within one month",Toast.LENGTH_SHORT).show();
                    return;
                }
                UserProfileChangeRequest profileChangeRequest = new UserProfileChangeRequest.Builder().setDisplayName(newName).build();
                user.updateProfile(profileChangeRequest).addOnSuccessListener(aVoid ->{
                    userName.setText("Name: "+ newName);
                    Toast.makeText(this,"User Name updated", Toast.LENGTH_SHORT).show();
                    db.collection("users").document(user.getUid()).collection("nameChanges").add(new NameChange(Timestamp.now()));
                }).addOnFailureListener(e -> {
                    Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
        }).addOnFailureListener(e -> {
            Toast.makeText(this,"Error updating name: "+e.getMessage(),Toast.LENGTH_SHORT).show();
            });
            });


        verifyEmailBtn.setOnClickListener(v -> {
            if (user != null){
                user.sendEmailVerification().addOnSuccessListener(aVoid ->{
                    Toast.makeText(this, "Verification email sent", Toast.LENGTH_SHORT).show();
                }).addOnFailureListener(e -> Toast.makeText(this, "Failed to send verification email: "+e.getMessage(), Toast.LENGTH_SHORT).show());
            }
        });

    }

    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }

}
