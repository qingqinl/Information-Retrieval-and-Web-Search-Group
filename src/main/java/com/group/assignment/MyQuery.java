package com.group.assignment;

public class MyQuery {


    MyQuery(){
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

    void setDescription(String description) {
        this.description = description;
    }

    public String getNarriative() {
        return narriative;
    }

    void setNarriative(String narriative) {
        this.narriative = narriative;
    }

    public String getTitle() {
        return title;
    }

    void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        return "ID " + Id + "\nTitle " + title + "\nDesc " + description + "\nnarr " + narriative;
    }

    private static int id = 1;

    private String Id;
    private String description;
    private String narriative;
    private String title;
}