package com.example.food.view;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.food.database.SettingsDataSource;
import com.example.food.database.UserDataSource;
import com.example.food.databinding.ActivityLoginBinding;
import com.example.food.utils.Constants;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

public class LoginActivity extends AppCompatActivity implements Constants {

    private ActivityLoginBinding binding;

    private UserDataSource userDataSource;
    private SettingsDataSource settingsDataSource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        userDataSource = UserDataSource.sharedInstance(this);
        settingsDataSource = SettingsDataSource.sharedInstance(this);

        binding.activityLoginRegisterTV.setOnClickListener(view12 -> {
            startActivity(new Intent(this,RegisterActivity.class));
            finish();
        });

        binding.activityLoginLoginCV.setOnClickListener(view1 -> {
            if (isValidEmail(binding.activityLoginEmailET.getText())){
                if (userDataSource.isUserAvailable(binding.activityLoginEmailET.getText().toString().trim())){
                    if (!binding.activityLoginPasswordET.getText().toString().trim().isEmpty()){
                        if (userDataSource.isValidCredentials(binding.activityLoginEmailET.getText().toString().trim(),
                                binding.activityLoginPasswordET.getText().toString().trim())){
                            settingsDataSource.deleteSettingsData();
                            userDataSource.insertOrUpdateUserData(userDataSource.getUserData(binding.activityLoginEmailET.getText().toString().trim()));
                            settingsDataSource.insertLoggedInUserData(userDataSource.getUserData(binding.activityLoginEmailET.getText().toString().trim()));
                            settingsDataSource.setUserLoggedIn();
                            startActivity(new Intent(this, HomeActivity.class));
                            finish();
                        }else {
                            Snackbar.make(view, "Invalid credentials", BaseTransientBottomBar.LENGTH_SHORT).show();
                        }
                    }else {
                        Snackbar.make(view, "Password length must be more than 4 characters", BaseTransientBottomBar.LENGTH_SHORT).show();
                    }
                }else {
                    Snackbar.make(view,"User doesn't exists", BaseTransientBottomBar.LENGTH_SHORT).show();
                }
            }else {
                Snackbar.make(view,"Enter valid email", BaseTransientBottomBar.LENGTH_SHORT).show();
            }
        });
    }

}