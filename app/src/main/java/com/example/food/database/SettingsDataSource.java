package com.example.food.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;

import com.example.food.utils.Constants;

import org.json.JSONException;
import org.json.JSONObject;

public class SettingsDataSource implements Constants {

    private static SettingsDataSource settingsDataSource;
    private static FoodDB foodDB;
    private SQLiteDatabase sqLiteDatabase;
    private String[] allColumns;

    public static SettingsDataSource sharedInstance(Context context){
        if (settingsDataSource ==null){
            foodDB = new FoodDB(context);
            settingsDataSource = new SettingsDataSource();
            settingsDataSource.allColumns = new String[1];
            settingsDataSource.allColumns[0] = DATA;
            settingsDataSource.open();
        }
        return settingsDataSource;
    }

    private void open() throws SQLiteException {
        sqLiteDatabase = foodDB.getWritableDatabase();
    }

    private void close(){
        sqLiteDatabase.close();
    }

    private void insertSettingsData(String settingData){
        open();
        ContentValues contentValues = new ContentValues();
        contentValues.put(DATA, settingData);

        try {
            sqLiteDatabase.insert(TABLE_SETTINGS,null,contentValues);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    
    public void deleteSettingsData(){
        open();
        sqLiteDatabase.delete(TABLE_SETTINGS,null,null);
    }

    public void insertLoggedInUserData(JSONObject userData) {
        open();
        try {
            JSONObject jsonObject = getSettingsData();
            jsonObject.put("userData",userData);

            ContentValues contentValues = new ContentValues();
            contentValues.put(DATA,jsonObject.toString());

            sqLiteDatabase.update(TABLE_SETTINGS,contentValues,null,null);

        }catch (JSONException e){
            e.printStackTrace();
        }
    }

    public void setUserLoggedIn(){
        open();
        try {
            JSONObject jsonObject = getSettingsData();
            jsonObject.put(USER_LOGGED_IN,true);

            ContentValues contentValues = new ContentValues();
            contentValues.put(DATA, jsonObject.toString());

            sqLiteDatabase.update(TABLE_SETTINGS,contentValues,null,null);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void setUserLoggedOut(){
        open();
        try {
            JSONObject jsonObject = getSettingsData();
            jsonObject.put(USER_LOGGED_IN,false);

            ContentValues contentValues = new ContentValues();
            contentValues.put(DATA, jsonObject.toString());

            sqLiteDatabase.update(TABLE_SETTINGS,contentValues,null,null);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private JSONObject getSettingsData(){
        open();
        try {
            Cursor selected = sqLiteDatabase.query(TABLE_SETTINGS, allColumns, null, null, null, null, null);
            if (selected != null && selected.getCount() > 0) {
                selected.moveToFirst();
                JSONObject jsonObject = new JSONObject(selected.getString(0));
                selected.close();
                return jsonObject;
            } else {
                deleteSettingsData();
                JSONObject jsonObject = new JSONObject();
                jsonObject.put(USER_LOGGED_IN, false);
                insertSettingsData(jsonObject.toString());
                return getSettingsData();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new JSONObject();
        }
    }

    public boolean isUserLoggedIn(){
        try {
            return getSettingsData().getBoolean(USER_LOGGED_IN);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public JSONObject getLoggedInUserData(){
        try {
            return getSettingsData().getJSONObject("userData");
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }



}
