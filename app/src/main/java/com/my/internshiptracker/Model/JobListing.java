package com.my.internshiptracker.Model;

public class JobListing {
    private String title;
    private String company;
    private String location;
    private String url;

    public String getTitle() {
        return title;
    }


    public String getCompany() {
        return company;
    }


    public String getLocation() {
        return location;
    }

    public String getUrl() {
        return url;
    }


    public JobListing(String title, String company, String location, String url){
        this.title = title;
        this.company = company;
        this.location = location;
        this.url = url;
    }
}
