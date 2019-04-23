package com.lifecare.main.Models;

import java.util.Date;

public class Doctor {

    private String id;
    private String name;
    private String phoneNumber;
    private Integer specializationID;
    private Date lastVisit;
    private String uid;

    public Doctor() {

    }

    public Doctor(String id, String name, String phoneNumber, Integer specializationID, Date lastVisit, String uid) {
        this.id = id;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.specializationID = specializationID;
        this.lastVisit = lastVisit;
        this.uid = uid;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public Integer getSpecializationID() {
        return specializationID;
    }

    public Date getLastVisit() {
        return lastVisit;
    }

    public String getUid() {
        return uid;
    }
}
