package com.espfullstack.wedoo.controllers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.espfullstack.wedoo.helper.DatabaseHelper;
import com.espfullstack.wedoo.pojo.ToDoo;
import com.espfullstack.wedoo.pojo.ToDooItem;

import java.util.ArrayList;
import java.util.List;

public class ToDooItemController {
    DatabaseHelper databaseHelper;

    public ToDooItemController(Context context) {
        databaseHelper = DatabaseHelper.getInstance(context);
    }

    public void addToDo(int toDooId, ToDooItem toDooItem) {
        ContentValues values = new ContentValues();
        SQLiteDatabase database = databaseHelper.getDatabase();
        database.beginTransaction();
        try {
            values.put(ToDooItem.FK, toDooId);
            values.put(ToDooItem.TITLE, toDooItem.getTitle());
            values.put(ToDooItem.DESCRIPTION, toDooItem.getDescription());

            database.insertWithOnConflict(ToDooItem.TABLE, null,
                    values, SQLiteDatabase.CONFLICT_REPLACE);
            database.setTransactionSuccessful();
        } finally {
            database.endTransaction();
        }
    }

    public void addAll(int toDooId, List<ToDooItem> toDooItems) {
        SQLiteDatabase database = databaseHelper.getDatabase();
        database.beginTransaction();
        ContentValues values;
        try {
            for (ToDooItem toDooItem : toDooItems) {
                values = new ContentValues();
                values.put(ToDooItem.FK, toDooId);
                values.put(ToDooItem.TITLE, toDooItem.getTitle());
                values.put(ToDooItem.DESCRIPTION, toDooItem.getDescription());

                database.insertWithOnConflict(ToDooItem.TABLE, null,
                        values, SQLiteDatabase.CONFLICT_REPLACE);
            }
            database.setTransactionSuccessful();
        } finally {
            database.endTransaction();
        }
    }

    public List<ToDooItem> getAll(int toDooId) {
        List<ToDooItem> toDooItems = new ArrayList<>();
        Cursor c = databaseHelper.getDatabase().query(ToDooItem.TABLE, null, ToDooItem.FK + " = ?",
                new String[] { String.valueOf(toDooId) }, null, null, null);

        if (c.moveToFirst()) {
            do {
                ToDooItem toDooItem = new ToDooItem();
                toDooItem.setId(c.getInt(c.getColumnIndex(ToDooItem.ID)));
                toDooItem.setTitle(c.getString(c.getColumnIndex(ToDooItem.TITLE)));
                toDooItem.setDescription(c.getString(c.getColumnIndex(ToDooItem.DESCRIPTION)));
                toDooItem.setStatus(c.getInt(c.getColumnIndex(ToDooItem.STATUS)));
                toDooItems.add(toDooItem);
            } while (c.moveToNext());
        }
        c.close();
        return toDooItems;
    }

    public int delete(int toDooItemId) {
        int result = 0;
        SQLiteDatabase database = databaseHelper.getDatabase();
        database.beginTransaction();
        try {
            result = database.delete(ToDooItem.TABLE, ToDooItem.ID + " = ?", new String[] { String.valueOf(toDooItemId)});
            database.setTransactionSuccessful();
        } finally {
            database.endTransaction();
        }
        return result;
    }

    public int deleteAll(int toDooId) {
        int result = 0;
        SQLiteDatabase database = databaseHelper.getDatabase();
        database.beginTransaction();
        try {
            result = database.delete(ToDooItem.TABLE, ToDooItem.FK + " = ?", new String[] { String.valueOf(toDooId)});
            database.setTransactionSuccessful();
        } finally {
            database.endTransaction();
        }
        return result;
    }
}
