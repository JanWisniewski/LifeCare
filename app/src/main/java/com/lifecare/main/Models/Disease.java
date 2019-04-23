package com.lifecare.main.Models;

public class Disease {

    private String id;
    private Integer diseaseID;
    private String description;
    private Integer stateID;
    private String uid;

    public Disease() {

    }

    public Disease(String id, Integer diseaseID, String description, Integer stateID, String uid) {
        this.id = id;
        this.diseaseID = diseaseID;
        this.description = description;
        this.stateID = stateID;
        this.uid = uid;
    }

    public Integer getDiseaseID() {
        return diseaseID;
    }

    public String getDescription() {
        return description;
    }

    public Integer getStateID() {
        return stateID;
    }

    public String getId() {
        return id;
    }

    public String getUid() {
        return uid;
    }
}
