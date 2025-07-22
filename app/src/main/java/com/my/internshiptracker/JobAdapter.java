package com.my.internshiptracker;

import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.my.internshiptracker.Model.JobListing;

import java.util.List;

public class JobAdapter extends RecyclerView.Adapter<JobAdapter.ViewHolder> {
    private List<JobListing> jobs;
    private OnTrackClickListener trackClickListener;

    public interface OnTrackClickListener  {
        void onTrackClick(JobListing job);
    }

    public JobAdapter(List<JobListing>jobs, OnTrackClickListener trackClickListener){
        this.jobs = jobs;
        this.trackClickListener = trackClickListener;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_job_listing,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        JobListing job = jobs.get(position);
        holder.title.setText(job.getTitle());
        holder.company.setText(job.getCompany());
        holder.location.setText(job.getLocation());
        holder.btnApply.setOnClickListener(v ->{
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(job.getUrl()));
            holder.itemView.getContext().startActivity(intent);
        });

        holder.btnTrack.setOnClickListener(v -> trackClickListener.onTrackClick(job));
    }

    @Override
    public int getItemCount() {
        return jobs.size();
    }
    public static class ViewHolder extends RecyclerView.ViewHolder{
        TextView title, company, location;
        Button btnApply, btnTrack;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            company = itemView.findViewById(R.id.company);
            location = itemView.findViewById(R.id.location);
            btnApply = itemView.findViewById(R.id.btnApply);
            btnTrack = itemView.findViewById(R.id.btnTrack);
        }
    }
}
