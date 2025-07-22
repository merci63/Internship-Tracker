package com.my.internshiptracker;


import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;

import androidx.appcompat.app.AppCompatActivity;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.my.internshiptracker.Model.Internship;
import com.my.internshiptracker.Model.JobListing;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Activity_browse extends AppCompatActivity implements JobAdapter.OnTrackClickListener {
    private RecyclerView recyclerView;
    private JobAdapter adapter;
    private List<JobListing> jobs = new ArrayList<>();
    private ProgressBar progressBar;
    private RequestQueue queue;
    private TextView emptyView;
    private int currentPage = 1;
    private boolean isLoading = false;
    private FirebaseFirestore db;
    private String userId;
    private final String API_URL="https://api.adzuna.com/v1/api/jobs/ca/search/%d?app_id=%s&app_key=%s&what=internship&results_per_page=50";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browse);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Browse Job");

        db = FirebaseFirestore.getInstance();
        userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        recyclerView = findViewById(R.id.recyclerViewBrowse);
        emptyView = findViewById(R.id.emptyView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new JobAdapter(jobs, this);
        recyclerView.setAdapter(adapter);
        progressBar = findViewById(R.id.progressBar);
        queue = Volley.newRequestQueue(this);

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                int visableItemCount = layoutManager.getChildCount();
                int totalItemCount = layoutManager.getItemCount();
                int firstVisableItemPosition = layoutManager.findFirstVisibleItemPosition();

                if(!isLoading && (visableItemCount + firstVisableItemPosition) >= totalItemCount -5 && firstVisableItemPosition >= 0){
                    currentPage++;
                    fetchJobs();
                }
            }
        });
        fetchJobs();
        toolbar.setNavigationOnClickListener(v -> finish());

    }

    private void fetchJobs() {
        isLoading = true;
        progressBar.setVisibility(View.VISIBLE);
        String url = String.format(API_URL, currentPage, BuildConfig.ADZUNA_APP_ID, BuildConfig.ADZUNA_API_KEY);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, response -> {
            progressBar.setVisibility(View.GONE);
            isLoading = false;
            try {
                JSONArray results = response.getJSONArray("results");
                if(currentPage == 1){
                    jobs.clear();
                }
                for (int i = 0; i < results.length(); i++) {
                    JSONObject job = results.getJSONObject(i);
                    String title = job.getString("title");
                    String company = job.getJSONObject("company").getString("display_name");
                    String location = job.getJSONObject("location").getString("display_name");
                    String url1 = job.getString("redirect_url");
                    jobs.add(new JobListing(title, company, location, url1));
                }

                adapter.notifyDataSetChanged();
                if(jobs.isEmpty()){
                    recyclerView.setVisibility(View.GONE);
                    emptyView.setVisibility(View.VISIBLE);
                } else{
                    recyclerView.setVisibility(View.VISIBLE);
                    emptyView.setVisibility(View.GONE);
                }
            } catch (JSONException e) {
                Toast.makeText(this, "Error parsing data", Toast.LENGTH_SHORT).show();
            }
        },error -> {
            progressBar.setVisibility(View.GONE);
            isLoading = false;
            Toast.makeText(this, "Error fetching jobs: " + error.getMessage(), Toast.LENGTH_SHORT).show();
        });
        queue.add(request);
    }

    @Override
    public void onTrackClick(JobListing job) {
        Internship internship = new Internship(job.getCompany(),job.getTitle(),"Applied", "From Adzuna API - Location: "+job.getLocation());
        db.collection("users").document(userId).collection("internships").add(internship).addOnSuccessListener(documentReference -> Toast.makeText(this,"Added to tracker", Toast.LENGTH_SHORT).show()).addOnFailureListener(e -> Toast.makeText(this,"Error adding to tracker: "+ e.getMessage(),Toast.LENGTH_SHORT).show());

    }
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }
}