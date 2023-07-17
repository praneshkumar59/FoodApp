package com.example.food.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SimpleItemAnimator;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import com.example.food.R;
import com.example.food.adapter.MenuListAdapter;
import com.example.food.databinding.ActivityOrderBinding;
import com.example.food.databinding.DialogOrderSuccessBinding;
import com.example.food.utils.Constants;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import nl.dionsegijn.konfetti.core.PartyFactory;
import nl.dionsegijn.konfetti.core.Position;
import nl.dionsegijn.konfetti.core.emitter.Emitter;
import nl.dionsegijn.konfetti.core.emitter.EmitterConfig;
import nl.dionsegijn.konfetti.core.models.Shape;

public class OrderActivity extends AppCompatActivity implements Constants {

    private ActivityOrderBinding binding;
    private MenuListAdapter menuListAdapter;

    List<JSONObject> selectedMenuList = new ArrayList<>();

    private Shape.DrawableShape drawableShape = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityOrderBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        final Drawable drawable = ContextCompat.getDrawable(this, R.drawable.image_food);
        drawableShape = new Shape.DrawableShape(drawable, true);

        JSONArray itemArray = new JSONArray();
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(MENU_NAME, "Crab Masala");
            jsonObject.put(MENU_DESCRIPTION,"Crabs cooked in hot and spicy masala infused with red chillies and freshly ground whole spices.");
            jsonObject.put(MENU_COUNT,0);
            jsonObject.put(MENU_PRICE,350);
            itemArray.put(jsonObject);

            jsonObject = new JSONObject();
            jsonObject.put(MENU_NAME, "Biryani Combo");
            jsonObject.put(MENU_DESCRIPTION,"Chicken Biryani [1 Pc] + Grill Chicken [2 Pcs] + Soft Drink [Coke].");
            jsonObject.put(MENU_COUNT,0);
            jsonObject.put(MENU_IMAGE, R.drawable.image_menu_two);
            jsonObject.put(MENU_PRICE,295);
            itemArray.put(jsonObject);

            RecyclerView.ItemAnimator itemAnimator = binding.activityOrderRV.getItemAnimator();
            if(itemAnimator instanceof SimpleItemAnimator){
                ((SimpleItemAnimator) itemAnimator).setSupportsChangeAnimations(false);
            }
            menuListAdapter = new MenuListAdapter(itemArray,getApplicationContext());
            binding.activityOrderRV.setLayoutManager(new LinearLayoutManager(getApplicationContext(),LinearLayoutManager.VERTICAL,false));
            binding.activityOrderRV.setAdapter(menuListAdapter);

            menuListAdapter.setOnSelectListener(new MenuListAdapter.OnSelectListener() {
                @Override
                public void onAddClick(int position, JSONObject jsonObject) {
                    menuListAdapter.notifyAddClick(position);
                    selectedMenuList.remove(jsonObject);
                    selectedMenuList.add(jsonObject);
                    setItemCountCard();
                }

                @Override
                public void onRemoveClick(int position, JSONObject jsonObject) {
                    menuListAdapter.notifyRemoveClick(position);
                    try {
                        selectedMenuList.remove(jsonObject);
                        if (jsonObject.getInt(MENU_COUNT)>0){
                            selectedMenuList.add(jsonObject);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    setItemCountCard();
                }
            });

        }catch (JSONException e){
            e.printStackTrace();
        }

        binding.activityOrderBackIV.setOnClickListener(view12 -> finish());

        binding.activityOrderCV.setOnClickListener(view1 -> {
            if (selectedMenuList.size()>0){
                showOrderSuccessDialog();
            }else {
                Snackbar.make(view,"Add items to place an order", BaseTransientBottomBar.LENGTH_SHORT).show();
            }
        });
    }

    private void showOrderSuccessDialog() {
        Dialog dialog = new Dialog(this);
        DialogOrderSuccessBinding dialogBinding = DialogOrderSuccessBinding.inflate(LayoutInflater.from(this));
        dialog.setContentView(dialogBinding.getRoot());
        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCancelable(false);
        dialog.show();

        dialogBinding.dialogOrderSuccessViewCV.setOnClickListener(view -> {
            dialog.dismiss();
            startActivity(new Intent(this, OrderDetailsActivity.class)
                   .putExtra(DATA,selectedMenuList.toString()));
            finish();
        });

        explode();
    }

    @SuppressLint("SetTextI18n")
    private void setItemCountCard(){
        try {

            double menuPrice = 0;
            for (int i = 0; i< selectedMenuList.size(); i++) {
                menuPrice = menuPrice + selectedMenuList.get(i).getInt(MENU_COUNT) *
                        selectedMenuList.get(i).getDouble(MENU_PRICE);
            }
            binding.activityOrderTV.setText("Total : ₹ "+roundOff2Decimal(menuPrice));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void explode() {
        EmitterConfig emitterConfig = new Emitter(100L, TimeUnit.MILLISECONDS).max(100);
        binding.activityOrderKonfettiView.start(
                new PartyFactory(emitterConfig)
                        .spread(360)
                        .shapes(Arrays.asList(Shape.Square.INSTANCE, Shape.Circle.INSTANCE, drawableShape))
                        .colors(Arrays.asList(0xfce18a, 0xff726d, 0xf4306d, 0xb48def))
                        .setSpeedBetween(0f, 30f)
                        .position(new Position.Relative(0.5, 0.1))
                        .build()
        );
    }

}