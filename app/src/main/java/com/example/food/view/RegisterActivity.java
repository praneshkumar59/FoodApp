package com.example.food.view;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.food.database.SettingsDataSource;
import com.example.food.database.UserDataSource;
import com.example.food.databinding.ActivityRegisterBinding;
import com.example.food.utils.Constants;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONException;
import org.json.JSONObject;

public class RegisterActivity extends AppCompatActivity implements Constants {

    private ActivityRegisterBinding binding;

    private final JSONObject userObject = new JSONObject();

    private UserDataSource userDataSource;
    private SettingsDataSource settingsDataSource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        userDataSource = UserDataSource.sharedInstance(this);
        settingsDataSource = SettingsDataSource.sharedInstance(this);

        binding.activityRegisterLoginTV.setOnClickListener(view1 -> {
            startActivity(new Intent(this,LoginActivity.class));
            finish();
        });

        binding.activityRegisterCV.setOnClickListener(view12 -> {
            try {
                if (!binding.activityRegisterNameET.getText().toString().trim().isEmpty()) {
                    userObject.put("name",binding.activityRegisterNameET.getText().toString().trim());
                    if (!binding.activityRegisterEmailET.getText().toString().trim().isEmpty() && isValidEmail(binding.activityRegisterEmailET.getText())){
                        userObject.put(EMAIL,binding.activityRegisterEmailET.getText().toString().trim());
                        if (!binding.activityRegisterPasswordET.getText().toString().trim().isEmpty() && binding.activityRegisterPasswordET.getText().toString().trim().length()>4){
                            userObject.put("password",binding.activityRegisterPasswordET.getText().toString().trim());
                            if (!userDataSource.isUserAvailable(binding.activityRegisterEmailET.getText().toString().trim())){
                                settingsDataSource.deleteSettingsData();
                                userDataSource.insertOrUpdateUserData(userObject);
                                settingsDataSource.insertLoggedInUserData(userObject);
                                settingsDataSource.setUserLoggedIn();
                                startActivity(new Intent(this, HomeActivity.class));
                                finish();
                            }else {
                                Snackbar.make(view, "User already exists", BaseTransientBottomBar.LENGTH_SHORT).show();
                            }
                        }else {
                            Snackbar.make(view, "Password length must be more than 4 characters", BaseTransientBottomBar.LENGTH_SHORT).show();
                        }
                    }else {
                        Snackbar.make(view, "Enter valid email", BaseTransientBottomBar.LENGTH_SHORT).show();
                    }
                } else {
                    Snackbar.make(view, "Enter name", BaseTransientBottomBar.LENGTH_SHORT).show();
                }
            }catch (JSONException e){
                e.printStackTrace();
            }
        });
    }
}