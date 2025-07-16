package com.my.internshiptracker;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.my.internshiptracker.Model.Internship;

public class Activity_add_internship extends AppCompatActivity {

    private EditText company, role, status, notes;
    private FirebaseFirestore db;
    private String userId;
    private String internshipId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_internship);
        if (FirebaseAuth.getInstance().getCurrentUser() == null){
            startActivity(new Intent(this, Activity_login.class));
            finish();
            return;
        }

        db = FirebaseFirestore.getInstance();
        userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        company = findViewById(R.id.etCompany);
        role = findViewById(R.id.etRole);
        notes = findViewById(R.id.etNotes);
        status = findViewById(R.id.etStatus);
        Button btnSave = findViewById(R.id.btnSave);
        Button btnCancel = findViewById(R.id.btnCancel);
        internshipId = getIntent().getStringExtra("INTERNSHIP_ID");
        if(internshipId != null){
            company.setText(getIntent().getStringExtra("COMPANY"));
            role.setText(getIntent().getStringExtra("ROLE"));
            status.setText(getIntent().getStringExtra("STATUS"));
            notes.setText(getIntent().getStringExtra("NOTES"));
        }

        btnSave.setOnClickListener(v -> saveInternship());
        btnCancel.setOnClickListener(v -> {
            startActivity(new Intent(this, MainActivity.class));
        });

    }

    private void saveInternship() {

        String nCompany = company.getText().toString().trim();
        String nRole = role.getText().toString().trim();
        String nStatus = status.getText().toString().trim();
        String nNotes = notes.getText().toString().trim();
        if (nCompany.isEmpty() || nRole.isEmpty()){
            Toast.makeText(this, "Fill required fields" ,Toast.LENGTH_SHORT).show();
             return;
        }

        Internship internship = new Internship(nCompany, nRole, nStatus, nNotes);
        if (internshipId == null){
          String docId =  db.collection("users").document(userId).collection("internships").document(userId).collection("internships").document().getId();// set id after adding
                db.collection("users").document(userId).collection("internships").document().getId();
                internship.setId(docId);
                db.collection("users").document(userId).collection("internships").document(docId).set(internship).addOnSuccessListener(aVoid ->{
                    Toast.makeText(this, "Internship added", Toast.LENGTH_SHORT).show();
                    finish();
                }).addOnFailureListener(e -> {
                    Toast.makeText(this, "Error Adding internship: "+ e.getMessage(), Toast.LENGTH_SHORT).show();
            finish();
        });
        } else {

            internship.setId(internshipId);
            db.collection("users").document(userId).collection("internships").document(internshipId).set(internship).addOnSuccessListener(aVoid ->{
                Toast.makeText(this, "Updated", Toast.LENGTH_SHORT).show();
                finish();
            }).addOnFailureListener(e -> {
                Toast.makeText(this,"Error updating internship: "+ e.getMessage(),Toast.LENGTH_SHORT).show();
            });
        }

    }
}