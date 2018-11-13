package com.group.assignment;

public class MyQuery {


    public MyQuery(){
        this.Id = id+"";
        id++;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getNarriative() {
        return narriative;
    }

    public void setNarriative(String narriative) {
        this.narriative = narriative;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    private static int id = 1;

    private String Id;
    private String description;
    private String narriative;
    private String title;
}