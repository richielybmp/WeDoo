package com.espfullstack.wedoo.pojo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;

public class ToDoo implements Serializable {
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
    private List<ToDooItem> toDooItemList;

    public ToDoo() {
        this.toDooItemList = new ArrayList<>();
    }

    public ToDoo(String title, String description, int type, String endDate) {
        this.title = title;
        this.description = description;
        this.type = type;
        this.endDate = endDate;
        this.toDooItemList = new ArrayList<>();
    }

    public List<ToDooItem> getToDooItemList() {
        return toDooItemList;
    }

    public void setToDooItemList(final List<ToDooItem> toDooItemList) {
        this.toDooItemList = new ArrayList<>(toDooItemList);
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

    public void setStringType(String type){
        this.setType(type.equals("Tasks") ? TAREFA : COMPRA);
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public int convertTypeToInt(String type){
        return type.equals("Tasks") ? TAREFA : COMPRA;
    }
    public String getConvertedType(){
        return this.type == TAREFA ? "Tasks" : "Shops";
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if(obj == null) return false;
        return this.id == ((ToDoo) obj).getId();
    }
}
