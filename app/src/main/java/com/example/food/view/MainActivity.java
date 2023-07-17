package com.example.food.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.splashscreen.SplashScreen;

import android.content.Intent;
import android.os.Bundle;

import com.example.food.R;
import com.example.food.database.SettingsDataSource;

public class MainActivity extends AppCompatActivity {

    SettingsDataSource settingsDataSource;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SplashScreen.installSplashScreen(this);


        settingsDataSource = SettingsDataSource.sharedInstance(this);

        if (settingsDataSource.isUserLoggedIn()){
            startActivity(new Intent(this, HomeActivity.class));
        }else {
            startActivity(new Intent(this, LoginActivity.class));
        }
        finish();
    }
}