package com.my.internshiptracker;
import com.my.internshiptracker.Model;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.my.internshiptracker.Model.Internship;

import java.util.List;

public class InternshipAdapter extends RecyclerView.Adapter<InternshipAdapter.ViewHolder> {
    private List<Internship> internships;
    private OnItemClickListener listener;
    public interface OnItemClickListener{
        void onEditClick(Internship internship);
        void onDeleteClick(Internship internship);
    }

    public InternshipAdapter(List<Internship> internships, OnItemClickListener listener){
        this.internships = internships;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_internship,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Internship internship = internships.get(position);
        holder.tvCompany.setText(internship.getCompany());
        holder.tvRole.setText(internship.getRole());
        holder.tvStatus.setText(internship.getStatus());
        holder.tvNotes.setText(internship.getNotes());
    }




    @Override
    public int getItemCount() {
        return 0;
    }
}
