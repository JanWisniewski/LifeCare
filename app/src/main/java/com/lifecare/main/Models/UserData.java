package com.lifecare.main.Models;

public class UserData {

    private String id;
    private String name;
    private String surname;
    private Integer sexID;

    public UserData() {

    }

    public UserData(String id, String name, String surname, Integer sexID) {
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.sexID = sexID;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    public Integer getSexID() {
        return sexID;
    }
}
