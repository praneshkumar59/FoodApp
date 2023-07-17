package com.example.food.utils;

import android.text.TextUtils;
import android.util.Patterns;

import java.text.DecimalFormat;

public interface Constants {

    String DATA = "data";


    //Table Name
    String TABLE_USER = "user";
    String TABLE_SETTINGS = "settings";
    String USER_LOGGED_IN = "userLoggedIn";
    String EMAIL = "email";
    String NAME = "name";

    String MENU_NAME = "menuName";
    String MENU_DESCRIPTION = "menuDescription";
    String MENU_PRICE = "menuPrice";
    String MENU_TYPE = "menuType";
    String MENU_IMAGE = "menuImage";
    String MENU_COUNT = "menuCount";

    default boolean isValidEmail(CharSequence charSequence) {
        return (!TextUtils.isEmpty(charSequence) && Patterns.EMAIL_ADDRESS.matcher(charSequence).matches());
    }

    default String roundOff2Decimal(double value){
        return new DecimalFormat("0.0").format(value);
    }
}
