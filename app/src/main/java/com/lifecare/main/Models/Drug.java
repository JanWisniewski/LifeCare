package com.lifecare.main.Models;

public class Drug {

    private String id;
    private String uid;
    private String name;
    private Integer dosageID;
    private String conflicts;

    public Drug() {

    }

    public Drug(String id, String name, Integer dosageID, String conflicts, String uid) {
        this.id = id;
        this.name = name;
        this.dosageID = dosageID;
        this.conflicts = conflicts;
        this.uid = uid;
    }

    public String getId() {
        return id;
    }

    public String getUid() {
        return uid;
    }

    public String getName() {
        return name;
    }

    public Integer getDosageID() {
        return dosageID;
    }

    public String getConflicts() {
        return conflicts;
    }
}
