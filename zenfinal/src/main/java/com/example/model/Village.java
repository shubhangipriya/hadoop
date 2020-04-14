package com.example.model;

public class Village {

    int id;
    String village_name;

    public Village(int id, String village_name) {
        this.id = id;
        this.village_name = village_name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getVillage_name() {
        return village_name;
    }

    public void setVillage_name(String village_name) {
        this.village_name = village_name;
    }
}
