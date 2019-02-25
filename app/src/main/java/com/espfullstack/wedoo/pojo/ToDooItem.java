package com.espfullstack.wedoo.pojo;

import java.io.Serializable;

public class ToDooItem implements Serializable {
    public static final String TABLE = "todoos_items";
    public static final String ID = "id";
    public static final String FK = "fk_todoo";
    public static final String TITLE = "title";
    public static final String DESCRIPTION = "description";
    public static final String STATUS = "status";

    private int id = -1;
    private String title;
    private String description;
    private int status;

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

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        if(status == 0 | status == 1)
            this.status = status;
    }
}
