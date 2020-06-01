package com.oracle.foodtruck.dto;

import com.google.gson.annotations.SerializedName;

public class FoodTruckData {

    private String applicant;
    private String location;
    private String start24;
    private String end24;

    public String getApplicant() {
        return applicant;
    }

    public void setApplicant(String applicant) {
        this.applicant = applicant;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getStart24() {
        return start24;
    }

    public void setStart24(String start24) {
        this.start24 = start24;
    }

    public String getEnd24() {
        return end24;
    }

    public void setEnd24(String end24) {
        this.end24 = end24;
    }
}
