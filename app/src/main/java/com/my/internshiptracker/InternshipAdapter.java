package com.my.internshiptracker;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

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

//        holder.itemView.setOnClickListener(v -> {
//            listener.onEditClick(internship);
//            //return true;
//        });
        holder.btnUpdate.setOnClickListener(v -> {
            listener.onEditClick(internship);
        });
        holder.btnDelete.setOnClickListener(v -> {
            listener.onDeleteClick(internship);
           //return true;
        });
    }

    @Override
    public int getItemCount() {
        return internships.size();
    }
    public static class ViewHolder extends RecyclerView.ViewHolder{

        TextView tvCompany, tvRole, tvStatus, tvNotes;
        Button btnDelete, btnUpdate;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvCompany = itemView.findViewById(R.id.tvCompany);
            tvRole = itemView.findViewById(R.id.tvRole);
            tvStatus = itemView.findViewById(R.id.tvStatus);
            tvNotes = itemView.findViewById(R.id.tvNotes);
            btnDelete = itemView.findViewById(R.id.btnDelete);
            btnUpdate = itemView.findViewById(R.id.btnUpdate);
        }
    }
}
