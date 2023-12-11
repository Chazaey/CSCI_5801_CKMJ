package com.example.ckmj_se_project.ui.Backend;

import com.google.gson.annotations.SerializedName;

public class Item { //if serialised, lists work & input breaks
    @SerializedName("itemId")
    private int itemId;
    @SerializedName("familyId")
    private int familyId;
    @SerializedName("name")
    private String name;
    @SerializedName("amount")
    private int amount;
    @SerializedName("itemStatus")
    private String itemStatus;

    // Constructor
    public Item(int itemId, int familyId, String name, int amount, String itemStatus) {
        this.itemId = itemId;
        this.familyId = familyId;
        this.name = name;
        this.amount = amount;
        this.itemStatus = itemStatus;
    }

    public Item(String name, int amount, String itemStatus, int familyId) {
        this.familyId = familyId;
        this.name = name;
        this.amount = amount;
        this.itemStatus = itemStatus;
    }

    // Getters and Setters
    public int getItemId() { return itemId; }
    public void setItemId(int itemId) { this.itemId = itemId; }

    public int getFamilyId() { return familyId; }
    public void setFamilyId(int familyId) { this.familyId = familyId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public int getAmount() { return amount; }
    public void setAmount(int amount) { this.amount = amount; }

    public String getItemStatus() { return itemStatus; }
    public void setItemStatus(String itemStatus) { this.itemStatus = itemStatus; }
}

