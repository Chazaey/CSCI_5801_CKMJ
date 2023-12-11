package com.example.ckmj_se_project.ui.Family;

public class Family {
    private int familyId;
    private String email;
    private String name;

    // Constructors
    public Family(int familyId, String email, String name) {
        this.familyId = familyId;
        this.email = email;
        this.name = name;
    }

    public Family(String email, String name) {
        this.email = email;
        this.name = name;
    }

    // Getters and Setters
    public int getFamilyId() { return familyId; }
    public void setFamilyId(int familyId) { this.familyId = familyId; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
}
