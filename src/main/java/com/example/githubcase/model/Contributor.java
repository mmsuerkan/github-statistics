package com.example.githubcase.model;

public class Contributor {

    //username,location,contributions,company

    private String username;
    private String location;
    private int contributions;
    private String company;

    public Contributor(String username, String location, int contributions, String company) {
        this.username = username;
        this.location = location;
        this.contributions = contributions;
        this.company = company;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public int getContributions() {
        return contributions;
    }

    public void setContributions(int contributions) {
        this.contributions = contributions;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    @Override
    public String toString() {
        return "repo: " + username + ", user: " + username + ", location: " + location + ", company: " + company + ", contributions: " + contributions;
    }
}
