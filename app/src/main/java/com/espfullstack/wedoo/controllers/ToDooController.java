package com.espfullstack.wedoo.controllers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.espfullstack.wedoo.adapters.ToDooAdapter;
import com.espfullstack.wedoo.helper.DatabaseHelper;
import com.espfullstack.wedoo.pojo.ToDoo;
import com.espfullstack.wedoo.pojo.ToDooItem;

import java.util.ArrayList;
import java.util.List;

public class ToDooController {
    private DatabaseHelper databaseHelper;

    public ToDooController(Context context) {
        databaseHelper = DatabaseHelper.getInstance(context);
    }

    public boolean add(ToDoo toDoo) {
        ContentValues values = new ContentValues();
        SQLiteDatabase database = databaseHelper.getDatabase();
        database.beginTransaction();
        long resultado = 0;
        try {
            values.put(ToDoo.TITLE, toDoo.getTitle());
            values.put(ToDoo.DESCRIPTION, toDoo.getDescription());
            values.put(ToDoo.TYPE, toDoo.getType());
            values.put(ToDoo.END_DATE, toDoo.getEndDate());

            resultado = database.insertWithOnConflict(ToDoo.TABLE, null,
                    values, SQLiteDatabase.CONFLICT_REPLACE);
            database.setTransactionSuccessful();
        } finally {
            database.endTransaction();
        }

        if(resultado > -1) {
            toDoo.setId((int) resultado);
            return true;
        }

        return false;
    }

    public boolean update(ToDoo toDoo) {
        ContentValues values = new ContentValues();
        SQLiteDatabase database = databaseHelper.getDatabase();
        database.beginTransaction();
        long resultado = 0;
        try {
            values.put(ToDoo.TITLE, toDoo.getTitle());
            values.put(ToDoo.DESCRIPTION, toDoo.getDescription());
            values.put(ToDoo.TYPE, toDoo.getType());
            values.put(ToDoo.END_DATE, toDoo.getEndDate());

            resultado = database.update(ToDoo.TABLE,
                    values, ToDoo.ID + " = ?", new String[] {String.valueOf(toDoo.getId())});
            database.setTransactionSuccessful();
        } finally {
            database.endTransaction();
        }

        return resultado > 0;
    }

    public void addAll(List<ToDoo> toDooList) {
        SQLiteDatabase database = databaseHelper.getDatabase();
        database.beginTransaction();
        ContentValues values;
        try {
            for (ToDoo toDoo : toDooList) {
                values = new ContentValues();
                values.put(ToDoo.ID, toDoo.getId());
                values.put(ToDoo.TITLE, toDoo.getTitle());
                values.put(ToDoo.DESCRIPTION, toDoo.getDescription());
                values.put(ToDoo.TYPE, toDoo.getType());
                values.put(ToDoo.END_DATE, toDoo.getEndDate());

                database.insertWithOnConflict(ToDoo.TABLE, null,
                        values, SQLiteDatabase.CONFLICT_REPLACE);
            }
            database.setTransactionSuccessful();
        } finally {
            database.endTransaction();
        }
    }

    public List<ToDoo> getAll() {
        List<ToDoo> todoList = new ArrayList<>();
        SQLiteDatabase database = databaseHelper.getDatabase();
        Cursor c = database.query(ToDoo.TABLE, null, null,
                null, null, null, null);

        if (c.moveToFirst()) {
            do {
                ToDoo toDoo = new ToDoo();
                toDoo.setId(c.getInt(c.getColumnIndex(ToDoo.ID)));
                toDoo.setTitle(c.getString(c.getColumnIndex(ToDoo.TITLE)));
                toDoo.setDescription(c.getString(c.getColumnIndex(ToDoo.DESCRIPTION)));
                toDoo.setType(c.getInt(c.getColumnIndex(ToDoo.TYPE)));
                toDoo.setEndDate(c.getString(c.getColumnIndex(ToDoo.END_DATE)));

                List<ToDooItem> toDooItems = new ArrayList<>();
                Cursor cItem = database.query(ToDooItem.TABLE, null, ToDooItem.FK + " = ?",
                        new String[] { String.valueOf(toDoo.getId()) }, null, null, null);

                if (cItem.moveToFirst()) {
                    do {
                        ToDooItem toDooItem = new ToDooItem();
                        toDooItem.setId(cItem.getInt(cItem.getColumnIndex(ToDooItem.ID)));
                        toDooItem.setTitle(cItem.getString(cItem.getColumnIndex(ToDooItem.TITLE)));
                        toDooItem.setDescription(cItem.getString(cItem.getColumnIndex(ToDooItem.DESCRIPTION)));
                        toDooItem.setImageId(cItem.getString(cItem.getColumnIndex(ToDooItem.IMAGE_ID)));
                        toDooItem.setStatus(cItem.getInt(cItem.getColumnIndex(ToDooItem.STATUS)));
                        toDooItems.add(toDooItem);
                    } while (cItem.moveToNext());
                }
                cItem.close();
                toDoo.setToDooItemList(toDooItems);
                todoList.add(toDoo);
            } while (c.moveToNext());
        }
        c.close();
        return todoList;
    }

    public boolean delete(int id) {
        int result = 0;
        SQLiteDatabase database = databaseHelper.getDatabase();
        database.beginTransaction();
        try {
            result = database.delete(ToDoo.TABLE, ToDoo.ID + " = ?", new String[] { String.valueOf(id)});
            database.setTransactionSuccessful();
        } finally {
            database.endTransaction();
        }
        return result > 0;
    }

    public boolean save(ToDoo toDoo) {
        if(toDoo.getId() > -1) {
            return update(toDoo);
        } else {
            return add(toDoo);
        }
    }
}
