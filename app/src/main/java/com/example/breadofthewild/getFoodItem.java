package com.example.breadofthewild;

import android.util.Log;

import java.util.List;

public class getFoodItem implements Runnable {
    private AppDatabase db;
    private FoodListener foodListener;

    public getFoodItem(AppDatabase db, FoodListener foodListener) {
        this.db = db;
        this.foodListener = foodListener;
    }

    @Override
    public void run() {
        foodListener.onResult(db.foodDAO().getAllFood());
//        String name = db.foodDAO().getAllFood().get(0).getName();
//        Log.d("this be working?", name);
    }
}

interface FoodListener {
    void onResult(List<Food> foodList);
}