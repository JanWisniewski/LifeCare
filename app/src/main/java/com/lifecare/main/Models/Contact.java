package com.lifecare.main.Models;

/**
 * Created by Johnny on 01.04.2019.
 */

public class Contact {

    private String id;
    private String name;
    private Integer relation;
    private String address;
    private String phone;
    private String uid;

    public Contact() {

    }

    public Contact(String id, String name, Integer relation, String address, String phone, String uid) {
        this.id = id;
        this.name = name;
        this.relation = relation;
        this.address = address;
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

    public Integer getRelation() {
        return relation;
    }

    public String getAddress() {
        return address;
    }


}
