package com.my.internshiptracker;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.my.internshiptracker.Model.Internship;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements InternshipAdapter.OnItemClickListener{

    private RecyclerView recyclerView;
    private InternshipAdapter adapter;
    private List<Internship> internships = new ArrayList<>();
    private FirebaseFirestore db;
    private String userId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FirebaseAuth auth = FirebaseAuth.getInstance();
        if (auth.getCurrentUser() == null){
            startActivity(new Intent(this, Activity_login.class));
            finish();
            return;
        }

        db = FirebaseFirestore.getInstance();
        userId = auth.getCurrentUser().getUid();
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new InternshipAdapter(internships,this);
        recyclerView.setAdapter(adapter);

        FloatingActionButton fabAdd = findViewById(R.id.fabAdd);
        fabAdd.setOnClickListener(v -> startActivity(new Intent(this, Activity_add_internship.class)));

    }
    protected void onResume(){
        super.onResume();
        loadInternships();
    }

    private void loadInternships() {

        db.collection("users").document(userId).collection("internships").get().addOnCompleteListener(task -> {
            if(task.isSuccessful()){
                internships.clear();
                for(QueryDocumentSnapshot documentSnapshot: task.getResult()){
                    Internship internship = documentSnapshot.toObject(Internship.class);
                    internship.setId(documentSnapshot.getId());
                    internships.add(internship);
                }
                adapter.notifyDataSetChanged();
            } else {
                Toast.makeText(this, "Error loading data", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onEditClick(Internship internship) {
        if (internship.getId() == null){
            Toast.makeText(this, "Error: Internship ID is null",Toast.LENGTH_SHORT).show();
            return;
        }

        Intent intent = new Intent(this, Activity_add_internship.class);
        intent.putExtra("INTERNSHIP_ID", internship.getId());
        intent.putExtra("COMPANY", internship.getCompany());
        intent.putExtra("ROLE", internship.getRole());
        intent.putExtra("STATUS", internship.getStatus());
        intent.putExtra("NOTES", internship.getNotes());
        startActivity(intent);
    }

    @Override
    public void onDeleteClick(Internship internship) {

        if (internship.getId() == null){
            Toast.makeText(this, "Error: Internship ID is null",Toast.LENGTH_SHORT).show();
            return;
        }
        db.collection("users").document(userId).collection("internships").document(internship.getId()).delete().addOnSuccessListener(aVoid ->{
            loadInternships();
            Toast.makeText(this, "Deleted", Toast.LENGTH_SHORT).show();
        }).addOnFailureListener(e -> {
            Toast.makeText(this,"Error deleting: "+ e.getMessage(),Toast.LENGTH_SHORT).show();
        });
    }
}