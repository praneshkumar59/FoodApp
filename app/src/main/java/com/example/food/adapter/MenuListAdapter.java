package com.example.food.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.food.R;
import com.example.food.databinding.ItemListKitchenMenuBinding;
import com.example.food.utils.Constants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MenuListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements Constants {

    private JSONArray item;
    private Context context;

    private OnSelectListener onSelectListener;
    public MenuListAdapter(JSONArray item, Context context) {
        this.context = context;
        this.item = item;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new RecyclerViewHolders(ItemListKitchenMenuBinding.inflate(LayoutInflater.from(parent.getContext()),
                parent, false));
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        RecyclerViewHolders holders = (RecyclerViewHolders) holder;

        try {
            JSONObject jsonObject = item.getJSONObject(position);

            holders.binding.itemListKitchenMenuNameTV.setText(jsonObject.getString(MENU_NAME));
            holders.binding.itemListKitchenMenuDescriptionTV.setText(jsonObject.getString(MENU_DESCRIPTION));
            holders.binding.itemListKitchenMenuPriceTV.setText("₹ "+jsonObject.getDouble(MENU_PRICE));

            if (position == 0){
                holders.binding.itemListKitchenMenuImageIV.setImageResource(R.drawable.image_menu_one);
            }else {
                holders.binding.itemListKitchenMenuImageIV.setImageResource(R.drawable.image_menu_two);
            }

            if (jsonObject.getInt(MENU_COUNT) == 0){
                holders.binding.itemListKitchenMenuCountLL.setVisibility(View.GONE);
                holders.binding.itemListKitchenMenuAddTV.setVisibility(View.VISIBLE);
                holders.binding.itemListKitchenMenuPlusTV.setVisibility(View.VISIBLE);
                holders.binding.itemListKitchenMenuAvailableTV.setVisibility(View.GONE);
            }else if (jsonObject.getInt(MENU_COUNT) == 2){
                holders.binding.itemListKitchenMenuAddTV.setVisibility(View.GONE);
                holders.binding.itemListKitchenMenuCountLL.setVisibility(View.VISIBLE);
                holders.binding.itemListKitchenMenuPlusTV.setVisibility(View.INVISIBLE);
                holders.binding.itemListKitchenMenuCountTV.setText(String.valueOf(jsonObject.getInt(MENU_COUNT)));
                holders.binding.itemListKitchenMenuAvailableTV.setVisibility(View.VISIBLE);
            }else {
                holders.binding.itemListKitchenMenuAddTV.setVisibility(View.GONE);
                holders.binding.itemListKitchenMenuCountLL.setVisibility(View.VISIBLE);
                holders.binding.itemListKitchenMenuPlusTV.setVisibility(View.VISIBLE);
                holders.binding.itemListKitchenMenuCountTV.setText(String.valueOf(jsonObject.getInt(MENU_COUNT)));
                holders.binding.itemListKitchenMenuAvailableTV.setVisibility(View.GONE);
            }

            holders.binding.itemListKitchenMenuAddTV.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onSelectListener.onAddClick(position,jsonObject);
                }
            });

            holders.binding.itemListKitchenMenuPlusTV.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onSelectListener.onAddClick(position,jsonObject);
                }
            });

            holders.binding.itemListKitchenMenuMinusTV.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onSelectListener.onRemoveClick(position,jsonObject);
                }
            });


        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public int getItemCount() {
        return item.length();
    }

    private static class RecyclerViewHolders extends RecyclerView.ViewHolder {

        private final ItemListKitchenMenuBinding binding;
        RecyclerViewHolders(ItemListKitchenMenuBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

        }
    }

    @SuppressLint("NotifyDataSetChanged")
    public void notifyList(JSONArray item) {
        this.item = item;
        notifyDataSetChanged();
    }

    public void notifyAddClick(int position){
        try {
            JSONObject jsonObject = this.item.getJSONObject(position);
            jsonObject.put(MENU_COUNT,jsonObject.getInt(MENU_COUNT)+1);
            this.item.put(position,jsonObject);
            notifyItemChanged(position);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void notifyRemoveClick(int position){
        try {
            JSONObject jsonObject = this.item.getJSONObject(position);
            if (jsonObject.getInt(MENU_COUNT)>=0) {
                jsonObject.put(MENU_COUNT, jsonObject.getInt(MENU_COUNT) - 1);
                this.item.put(position, jsonObject);
                notifyItemChanged(position);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public interface OnSelectListener {
        void onAddClick(int position, JSONObject jsonObject);
        void onRemoveClick(int position, JSONObject jsonObject);
    }

    public void setOnSelectListener(OnSelectListener onSelectListener){
        this.onSelectListener = onSelectListener;
    }

}
