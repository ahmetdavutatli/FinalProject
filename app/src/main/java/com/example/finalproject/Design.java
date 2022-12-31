package com.example.finalproject;

public class Design {
    private String id;
    private String imageUrl;
    private String description;
    private Designer designer;

    public Design() {} // Firebase için boş bir constructor

    public Design(String id, String imageUrl, String description, Designer designer) {
        this.id = id;
        this.imageUrl = imageUrl;
        this.description = description;
        this.designer = designer;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Designer getDesigner() {
        return designer;
    }

    public void setDesigner(Designer designer) {
        this.designer = designer;
    }
}

