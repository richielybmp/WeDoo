package com.espfullstack.wedoo.helper;

import com.espfullstack.wedoo.pojo.ToDo;
import com.espfullstack.wedoo.pojo.ToDoItem;

public class SQLiteStrings {
    static final String CREATE_TABLE_TODO =
            "CREATE TABLE " + ToDo.TABLE
            + "("
            + ToDo.ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + ToDo.TITLE + " TEXT,"
            + ToDo.DESCRIPTION + " TEXT,"
            + ToDo.TYPE + " INTEGER,"
            + ToDo.END_DATE + " TEXT"
            + ")";

    static final String CREATE_TABLE_TODO_ITENS =
            "CREATE TABLE " + ToDoItem.TABLE
            + "("
            + ToDoItem.ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + ToDoItem.TITLE + " TEXT,"
            + ToDoItem.DESCRIPTION + " TEXT,"
            + ToDoItem.FK + " INTEGER,"
            + "FOREIGN KEY(" + ToDoItem.FK + ") REFERENCES " + ToDo.TABLE + "(" + ToDo.ID + ") ON DELETE CASCADE"
            + ")";

}
