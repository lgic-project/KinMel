package com.example.kinmelsellerapp.response;

public class Category {
    private final int id;
    private final String name;

    public Category(String name, int id) {
        this.name = name;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }
}