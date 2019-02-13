package com.espfullstack.wedoo.pojo;

public class ToDooItem {
    public static final String TABLE = "ITENS";
    public static final String ID = "id";
    public static final String FK = "fk_todoo";
    public static final String TITLE = "title";
    public static final String DESCRIPTION = "description";

    private int id;
    private String title;
    private String description;

    public ToDooItem() {
    }

    public ToDooItem(String title, String description) {
        this.title = title;
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
