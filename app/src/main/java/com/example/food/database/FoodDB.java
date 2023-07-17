package com.example.food.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.food.utils.Constants;

public class FoodDB extends SQLiteOpenHelper implements Constants {

    private final String user, settings;

    FoodDB(Context context){
        super(context, "FA_DB_01", null, 1);

        user = "CREATE TABLE "+ TABLE_USER +"("+ EMAIL +" TEXT PRIMARY KEY,"
                + DATA +" TEXT);";

        settings = "CREATE TABLE "+ TABLE_SETTINGS +"("+ DATA +" TEXT);";

    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(user);
        sqLiteDatabase.execSQL(settings);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+ TABLE_USER);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+ TABLE_SETTINGS);
        onCreate(sqLiteDatabase);
    }
}
