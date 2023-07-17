package com.example.food.view;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.food.R;
import com.example.food.databinding.ActivityHomeBinding;

public class HomeActivity extends AppCompatActivity {

    private ActivityHomeBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        binding.activityHomeMainLL.setOnClickListener(v -> {
            startActivity(new Intent(this,OrderActivity.class));
        });

        binding.activityHomeProfileIV.setOnClickListener(v -> {
            startActivity(new Intent(this,ProfileActivity.class));
        });
    }
}