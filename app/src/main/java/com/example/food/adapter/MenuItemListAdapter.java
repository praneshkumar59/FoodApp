package com.example.food.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.food.R;
import com.example.food.databinding.ItemListKitchenMenuBinding;
import com.example.food.databinding.ItemListMenuBinding;
import com.example.food.utils.Constants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MenuItemListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements Constants {

    private JSONArray item;
    public MenuItemListAdapter(JSONArray item) {
        this.item = item;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new RecyclerViewHolders(ItemListMenuBinding.inflate(LayoutInflater.from(parent.getContext()),
                parent, false));
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        RecyclerViewHolders holders = (RecyclerViewHolders) holder;

        try {
            JSONObject jsonObject = item.getJSONObject(position);

            holders.binding.itemListMenuNameTV.setText(jsonObject.getString(MENU_COUNT) +" * "+jsonObject.getString(MENU_NAME));
            holders.binding.itemListMenuPriceTV.setText("₹ "+roundOff2Decimal(jsonObject.getInt(MENU_COUNT)*jsonObject.getDouble(MENU_PRICE)));
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public int getItemCount() {
        return item.length();
    }

    private static class RecyclerViewHolders extends RecyclerView.ViewHolder {

        private final ItemListMenuBinding binding;
        RecyclerViewHolders(ItemListMenuBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

        }
    }
}
