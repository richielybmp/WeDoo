package com.espfullstack.wedoo.pojo;

public class ToDoo {
    public static final String TABLE = "todoos";
    public static final String ID = "id";
    public static final String TITLE = "title";
    public static final String DESCRIPTION = "description";
    public static final String TYPE = "type";
    public static final String END_DATE = "end_date";

    public static final int TAREFA = 0;
    public static final int COMPRA = 1;
    //+++++


    private int id;
    private String title;
    private String description;
    private int type;
    private String endDate;

    public ToDoo() {
    }

    public ToDoo(String title, String description, int type, String endDate) {
        this.title = title;
        this.description = description;
        this.type = type;
        this.endDate = endDate;
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

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }
}
