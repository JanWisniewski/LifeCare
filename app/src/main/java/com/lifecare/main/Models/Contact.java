package com.lifecare.main.Models;

/**
 * Created by Johnny on 01.04.2019.
 */

public class Contact {

    String id;
    String name;
    String phone;
    String uid;

    public Contact() {

    }

    public Contact(String id, String name, String phone, String uid) {
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.uid = uid;
    }

    public String getUid() {
        return uid;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }
}
