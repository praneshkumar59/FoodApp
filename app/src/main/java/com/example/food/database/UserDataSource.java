package com.example.food.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;

import com.example.food.utils.Constants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class UserDataSource implements Constants {

    private static UserDataSource userDataSource;
    private static FoodDB foodDB;
    private SQLiteDatabase sqLiteDatabase;
    private String[] allColumns;

    public static UserDataSource sharedInstance(Context context){
        if (userDataSource ==null){
            foodDB = new FoodDB(context);
            userDataSource = new UserDataSource();
            userDataSource.allColumns = new String[2];
            userDataSource.allColumns[0] = EMAIL;
            userDataSource.allColumns[1] = DATA;
            userDataSource.open();
        }
        return userDataSource;
    }

    private void open() throws SQLiteException {
        sqLiteDatabase = foodDB.getWritableDatabase();
    }

    private void close(){
        sqLiteDatabase.close();
    }

    public void deleteUserData(){
        open();
        sqLiteDatabase.delete(TABLE_USER,null,null);
    }

    public void insertOrUpdateUserData(JSONObject userData){
        open();
        try {
            ContentValues contentValues = new ContentValues();
            contentValues.put(EMAIL, userData.getString(EMAIL));
            contentValues.put(DATA, userData.toString());

            if(isUserAvailable(contentValues.getAsString(EMAIL))){
                String where = EMAIL + " ='"+contentValues.getAsString(EMAIL)+"'";
                sqLiteDatabase.update(TABLE_USER, contentValues, where,null);
            }else {
                sqLiteDatabase.insert(TABLE_USER, null, contentValues);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }


    public void insertBulkUserData(JSONArray userDataList){
        open();
        sqLiteDatabase.beginTransaction();
        try{
            ContentValues contentValues;
            for (int i=0;i<userDataList.length();i++){
                try {
                    JSONObject jsonObject = userDataList.getJSONObject(i);
                    contentValues = new ContentValues();
                    contentValues.put(EMAIL, jsonObject.getString(EMAIL));
                    contentValues.put(DATA, jsonObject.toString());

                    if(isUserAvailable(contentValues.getAsString(EMAIL))){
                        String where = EMAIL + " ='"+contentValues.getAsString(EMAIL)+"'";
                        sqLiteDatabase.update(TABLE_USER, contentValues, where,null);
                    }else {
                        sqLiteDatabase.insert(TABLE_USER, null, contentValues);
                    }
                }catch (JSONException e){
                    e.printStackTrace();
                }
            }
            sqLiteDatabase.setTransactionSuccessful();
        }catch (SQLiteException e){
            e.printStackTrace();
        }finally {
            sqLiteDatabase.endTransaction();
            close();
        }
    }

    public boolean isUserAvailable(String email){
        open();
        String where = EMAIL + " ='"+email+"'";
        try (Cursor selected = sqLiteDatabase.query(TABLE_USER, allColumns, where, null, null, null, null)) {
            return selected != null && selected.getCount() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public JSONObject getUserData(String email){
        open();
        JSONObject userData = null;
        String where = EMAIL + " ='"+email+"'";
        try (Cursor selected = sqLiteDatabase.query(TABLE_USER, allColumns, where, null, null, null, null)) {
            if (selected != null && selected.getCount() > 0) {
                selected.moveToFirst();
                userData = new JSONObject(selected.getString(1));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return userData;
    }

    public boolean isValidCredentials(String email, String password){
        open();
        boolean isValid = false;
        JSONObject userData = null;
        String where = EMAIL + " ='"+email+"'";
        try (Cursor selected = sqLiteDatabase.query(TABLE_USER, allColumns, where, null, null, null, null)) {
            if (selected != null && selected.getCount() > 0) {
                selected.moveToFirst();
                userData = new JSONObject(selected.getString(1));
                isValid = email.trim().equals(userData.getString(EMAIL).trim())
                        && password.trim().equals(userData.getString("password").trim());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return isValid;
    }


}
