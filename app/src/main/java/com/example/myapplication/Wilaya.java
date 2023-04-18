package com.example.myapplication;

import java.util.List;

public class Wilaya {
    private int id;
    private String name;
    private List<String> communes;

    // getters and setters

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getCommunes() {
        return communes;
    }

    public void setCommunes(List<String> communes) {
        this.communes = communes;
    }
}
