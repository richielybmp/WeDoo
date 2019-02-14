package com.espfullstack.wedoo.controllers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.espfullstack.wedoo.helper.DatabaseHelper;
import com.espfullstack.wedoo.pojo.ToDoo;

import java.util.ArrayList;
import java.util.List;

public class ToDooController {
    private DatabaseHelper databaseHelper;

    public ToDooController(Context context) {
        databaseHelper = DatabaseHelper.getInstance(context);
    }

    public void addToDo(ToDoo toDoo) {
        ContentValues values = new ContentValues();
        SQLiteDatabase database = databaseHelper.getDatabase();
        database.beginTransaction();
        try {
            values.put(ToDoo.TITLE, toDoo.getTitle());
            values.put(ToDoo.DESCRIPTION, toDoo.getDescription());
            values.put(ToDoo.TYPE, toDoo.getType());
            values.put(ToDoo.END_DATE, toDoo.getEndDate());

            database.insertWithOnConflict(ToDoo.TABLE, null,
                    values, SQLiteDatabase.CONFLICT_ROLLBACK);
            database.setTransactionSuccessful();
        } finally {
            database.endTransaction();
        }
    }

    public void addAll(List<ToDoo> toDooList) {
        SQLiteDatabase database = databaseHelper.getDatabase();
        database.beginTransaction();
        ContentValues values;
        try {
            for (ToDoo toDoo : toDooList) {
                values = new ContentValues();
                values.put(ToDoo.TITLE, toDoo.getTitle());
                values.put(ToDoo.DESCRIPTION, toDoo.getDescription());
                values.put(ToDoo.TYPE, toDoo.getType());
                values.put(ToDoo.END_DATE, toDoo.getEndDate());

                database.insertWithOnConflict(ToDoo.TABLE, null,
                        values, SQLiteDatabase.CONFLICT_ROLLBACK);
            }
            database.setTransactionSuccessful();
        } finally {
            database.endTransaction();
        }
    }

    public List<ToDoo> getAll() {
        List<ToDoo> todoList = new ArrayList<>();
        Cursor c = databaseHelper.getDatabase().query(ToDoo.TABLE, null, null,
                null, null, null, null);

        if (c.moveToFirst()) {
            do {
                ToDoo toDoo = new ToDoo();
                toDoo.setId(c.getInt(c.getColumnIndex(ToDoo.ID)));
                toDoo.setTitle(c.getString(c.getColumnIndex(ToDoo.TITLE)));
                toDoo.setDescription(c.getString(c.getColumnIndex(ToDoo.DESCRIPTION)));
                toDoo.setType(c.getInt(c.getColumnIndex(ToDoo.TYPE)));
                toDoo.setEndDate(c.getString(c.getColumnIndex(ToDoo.END_DATE)));
                todoList.add(toDoo);
            } while (c.moveToNext());
        }
        c.close();
        return todoList;
    }

    public int delete(int id) {
        int result = 0;
        SQLiteDatabase database = databaseHelper.getDatabase();
        database.beginTransaction();
        try {
            result = database.delete(ToDoo.TABLE, ToDoo.ID + " = ?", new String[] { String.valueOf(id)});
            database.setTransactionSuccessful();
        } finally {
            database.endTransaction();
        }
        return result;
    }
}