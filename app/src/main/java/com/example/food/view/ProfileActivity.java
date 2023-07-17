package com.example.food.view;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.food.R;
import com.example.food.database.SettingsDataSource;
import com.example.food.databinding.ActivityProfileBinding;
import com.example.food.utils.Constants;

import org.json.JSONException;

import java.util.EnumMap;

public class ProfileActivity extends AppCompatActivity implements Constants {

    private ActivityProfileBinding binding;
    private SettingsDataSource settingsDataSource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProfileBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        settingsDataSource = SettingsDataSource.sharedInstance(this);

        binding.activityProfileBackIV.setOnClickListener(view1 -> finish());

        try {
            binding.activityProfileEmailTV.setText(settingsDataSource.getLoggedInUserData().getString(EMAIL));
            binding.activityProfileNameTV.setText(settingsDataSource.getLoggedInUserData().getString(NAME));
            binding.activityProfileInitialTV.setText(settingsDataSource.getLoggedInUserData().getString(NAME).substring(0,1));

            binding.activityProfileLogoutCV.setOnClickListener(view12 -> {
                settingsDataSource.deleteSettingsData();
                Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            });

        }catch (JSONException e){
            e.printStackTrace();
        }


    }
}