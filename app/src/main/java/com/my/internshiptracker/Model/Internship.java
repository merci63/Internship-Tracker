package com.my.internshiptracker.Model;

public class Internship {
    private String id;
    private String company;
    private String role;
    private String status;
    private String notes;

    public Internship(String company, String role, String status, String notes){
           this.company = company;
           this.role = role;
           this.status = status;
           this.notes = notes;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public Internship() {
    }


}
