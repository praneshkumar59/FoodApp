package com.example.food.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;

import com.example.food.R;
import com.example.food.adapter.MenuItemListAdapter;
import com.example.food.database.SettingsDataSource;
import com.example.food.database.UserDataSource;
import com.example.food.databinding.ActivityOrderDetailsBinding;
import com.example.food.utils.Constants;

import org.json.JSONArray;
import org.json.JSONException;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class OrderDetailsActivity extends AppCompatActivity implements Constants {

    ActivityOrderDetailsBinding binding;

    MenuItemListAdapter menuItemListAdapter;

    @SuppressLint({"SetTextI18n","SimpleDateFormat"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityOrderDetailsBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        SettingsDataSource settingsDataSource = SettingsDataSource.sharedInstance(this);

        try {
            JSONArray itemArray = new JSONArray(getIntent().getExtras().getString(DATA));


            binding.activityOrderDetailsNameTV.setText(settingsDataSource.getLoggedInUserData().getString(NAME));
            binding.activityOrderDetailsEmailTV.setText(settingsDataSource.getLoggedInUserData().getString(EMAIL));

            SimpleDateFormat formatter = new SimpleDateFormat("dd MMM hh:mm a");
            Date date = new Date();
            binding.activityOrderDetailsTimeTV.setText(formatter.format(date));

            double menuPrice = 0;
            for (int i = 0; i< itemArray.length(); i++) {
                menuPrice = menuPrice + itemArray.getJSONObject(i).getInt(MENU_COUNT) *
                        itemArray.getJSONObject(i).getDouble(MENU_PRICE);
            }
            binding.activityOrderDetailTotalTV.setText("₹ "+roundOff2Decimal(menuPrice));

            menuItemListAdapter = new MenuItemListAdapter(itemArray);
            binding.activityOrderDetailsRV.setLayoutManager(new LinearLayoutManager(getApplicationContext(),LinearLayoutManager.VERTICAL,false));
            binding.activityOrderDetailsRV.setAdapter(menuItemListAdapter);

            binding.activityOrderDetailsBackIV.setOnClickListener(view1 -> {
                finish();
            });

            binding.activityOrderDetailsBackHomeCV.setOnClickListener(view12 -> finish());


        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }
}