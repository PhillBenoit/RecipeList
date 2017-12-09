package com.example.user1.recipelist.Objects;

/**
 *
 */

public class RecipeObject {
    private int id;
    private String name;

    public RecipeObject(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public  RecipeObject() {
        id = 0;
        name = "";
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
