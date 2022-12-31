package com.example.finalproject;

public class Offer {
    private String id;
    private Design design;
    private Manufacturer manufacturer;
    private double amount;

    public Offer() {} // Firebase için boş bir constructor

    public Offer(String id, Design design, Manufacturer manufacturer, double amount) {
        this.id = id;
        this.design = design;
        this.manufacturer = manufacturer;
        this.amount = amount;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Design getDesign() {
        return design;
    }

    public void setDesign(Design design) {
        this.design = design;
    }

    public Manufacturer getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(Manufacturer manufacturer) {
        this.manufacturer = manufacturer;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }
}

