package com.espfullstack.wedoo.helper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.espfullstack.wedoo.pojo.ToDoo;
import com.espfullstack.wedoo.pojo.ToDooItem;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static DatabaseHelper dInstance = null;
    private static final int DATABASE_VERSION = 3;
    private static final String DATABASE_NAME = "WeDooDB";
    private static final String LOG = "DatabaseHelper";

    private DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public static synchronized DatabaseHelper getInstance(Context context) {

        if (dInstance == null) {
            dInstance = new DatabaseHelper(context);
        }
        return dInstance;
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        db.execSQL("PRAGMA foreign_keys=ON");
        super.onOpen(db);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.e(LOG, "Create Tables");
        db.execSQL(SQLiteStrings.CREATE_TABLE_TODO);
        db.execSQL(SQLiteStrings.CREATE_TABLE_TODO_ITENS);
        //Teste - remover
//        for(int i = 0; i < 10; i ++)
//            db.execSQL("INSERT INTO " + ToDoo.TABLE + " VALUES " + "("+ i +", 'Tarefa', ' Descricao', 0, '13/02/2019')");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + ToDoo.TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + ToDooItem.TABLE);
        onCreate(db);
    }

    public synchronized SQLiteDatabase getDatabase() {
        return dInstance.getWritableDatabase();
    }
}
