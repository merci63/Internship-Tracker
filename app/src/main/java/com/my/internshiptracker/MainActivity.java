package com.my.internshiptracker;

import android.content.BroadcastReceiver;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.widget.Toolbar;


import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;

import androidx.core.view.GravityCompat;

import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.my.internshiptracker.Model.Internship;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements InternshipAdapter.OnItemClickListener, NavigationView.OnNavigationItemSelectedListener{

    private RecyclerView recyclerView;
    private InternshipAdapter adapter;
    private List<Internship> internships = new ArrayList<>();
    private FirebaseFirestore db;
    private String userId;
    private DrawerLayout drawerLayout;
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

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Button btnBrowse = findViewById(R.id.btnBrowse);

        drawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        TextView navHeaderEmail = navigationView.getHeaderView(0).findViewById(R.id.nav_header_email);
        navHeaderEmail.setText(auth.getCurrentUser().getEmail());
        TextView navHeaderUserName = navigationView.getHeaderView(0).findViewById(R.id.nav_header_name);
        navHeaderUserName.setText(auth.getCurrentUser().getDisplayName());

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.nav_dashboard){
                return true;//already on dashboard, do nothing
            } else if (itemId == R.id.nav_add_internship) {
                startActivity(new Intent(this, Activity_add_internship.class));
                return true;
            } else if (itemId == R.id.nav_profile) {
                startActivity(new Intent(this, Activity_profile.class));
                return true;
            }
            return false;
        });
        btnBrowse.setOnClickListener(v -> {
            startActivity(new Intent(this, Activity_browse.class));
        });
        bottomNavigationView.setSelectedItemId(R.id.nav_dashboard);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new InternshipAdapter(internships,this);
        recyclerView.setAdapter(adapter);
       loadInternships();
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

    public boolean onNavigationItemSelected(MenuItem item){
        int itemId = item.getItemId();
        if(itemId == R.id.nav_settings){
            //Toast.makeText(this,"Setting selected", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, Activity_settings.class));
        } else if (itemId == R.id.nav_logout) {
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(this, Activity_login.class));
            finish();
        } else if (itemId == R.id.nav_about) {
            startActivity(new Intent(this, Activity_about.class));
            //Toast.makeText(this,"About selected", Toast.LENGTH_SHORT).show();
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
    public void onBackPressed(){
        if(drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
}