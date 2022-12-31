package com.example.finalproject;

public class Manufacturer {
    private String id;
    private String name;
    private String about;
    private String contactInfo;

    public Manufacturer() {} // Firebase gereği boş bir constructor gerekir.

    public Manufacturer(String id, String name, String about, String contactInfo) {
        this.id = id;
        this.name = name;
        this.about = about;
        this.contactInfo = contactInfo;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }

    public String getContactInfo() {
        return contactInfo;
    }

    public void setContactInfo(String contactInfo) {
        this.contactInfo = contactInfo;
    }
}

