package com.example.breadofthewild;

import android.os.Process;
import android.util.Log;

import org.json.JSONArray;

import java.util.List;

public class InsertFoodItem implements Runnable {
    AppDatabase db;
    List<Food> food;

    public InsertFoodItem(AppDatabase db, List<Food> food) {
        this.db = db;
        this.food = food;
    }

    @Override
    public void run() {
        for(int i = 0; i < food.size(); i++){
            db.foodDAO().insertFood(this.food.get(i));
        }
    }
}

