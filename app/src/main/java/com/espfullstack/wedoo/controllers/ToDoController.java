package com.espfullstack.wedoo.controllers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.espfullstack.wedoo.helper.DatabaseHelper;
import com.espfullstack.wedoo.helper.SQLiteStrings;
import com.espfullstack.wedoo.pojo.ToDo;

import java.util.ArrayList;
import java.util.List;

public class ToDoController {
    private DatabaseHelper databaseHelper;

    public ToDoController(Context context) {
        databaseHelper = DatabaseHelper.getInstance(context);
    }

    public void addToDo(ToDo toDo) {
        ContentValues values = new ContentValues();
        SQLiteDatabase database = databaseHelper.getDatabase();
        database.beginTransaction();
        try {
            values.put(ToDo.TITLE, toDo.getTitle());
            values.put(ToDo.DESCRIPTION, toDo.getDescription());
            values.put(ToDo.TYPE, toDo.getType());
            values.put(ToDo.END_DATE, toDo.getEndDate());

            database.insertWithOnConflict(ToDo.TABLE, null,
                    values, SQLiteDatabase.CONFLICT_ROLLBACK);
            database.setTransactionSuccessful();
        } finally {
            database.endTransaction();
        }
    }

    public void addAll(List<ToDo> toDoList) {
        SQLiteDatabase database = databaseHelper.getDatabase();
        database.beginTransaction();
        ContentValues values;
        try {
            for (ToDo toDo: toDoList) {
                values = new ContentValues();
                values.put(ToDo.TITLE, toDo.getTitle());
                values.put(ToDo.DESCRIPTION, toDo.getDescription());
                values.put(ToDo.TYPE, toDo.getType());
                values.put(ToDo.END_DATE, toDo.getEndDate());

                database.insertWithOnConflict(ToDo.TABLE, null,
                        values, SQLiteDatabase.CONFLICT_ROLLBACK);
            }
            database.setTransactionSuccessful();
        } finally {
            database.endTransaction();
        }
    }

    public List<ToDo> getAll() {
        List<ToDo> todoList = new ArrayList<>();
        Cursor c = databaseHelper.getDatabase().query(ToDo.TABLE, null, null,
                null, null, null, null);

        if (c.moveToFirst()) {
            do {
                ToDo toDo = new ToDo();
                toDo.setTitle(c.getString(c.getColumnIndex(ToDo.TITLE)));
                toDo.setDescription(c.getString(c.getColumnIndex(ToDo.DESCRIPTION)));
                toDo.setType(c.getInt(c.getColumnIndex(ToDo.TYPE)));
                toDo.setEndDate(c.getString(c.getColumnIndex(ToDo.END_DATE)));
                todoList.add(toDo);
            } while (c.moveToNext());
        }
        c.close();
        return todoList;
    }
}
