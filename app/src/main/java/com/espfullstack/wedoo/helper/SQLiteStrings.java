package com.espfullstack.wedoo.helper;

import com.espfullstack.wedoo.pojo.ToDoo;
import com.espfullstack.wedoo.pojo.ToDooItem;

public class SQLiteStrings {
    static final String CREATE_TABLE_TODO =
            "CREATE TABLE " + ToDoo.TABLE
            + "("
            + ToDoo.ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + ToDoo.TITLE + " TEXT,"
            + ToDoo.DESCRIPTION + " TEXT,"
            + ToDoo.TYPE + " INTEGER,"
            + ToDoo.END_DATE + " TEXT"
            + ")";

    static final String CREATE_TABLE_TODO_ITENS =
            "CREATE TABLE " + ToDooItem.TABLE
            + "("
            + ToDooItem.ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + ToDooItem.TITLE + " TEXT,"
            + ToDooItem.DESCRIPTION + " TEXT,"
            + ToDooItem.FK + " INTEGER,"
            + "FOREIGN KEY(" + ToDooItem.FK + ") REFERENCES " + ToDoo.TABLE + "(" + ToDoo.ID + ") ON DELETE CASCADE"
            + ")";

}
