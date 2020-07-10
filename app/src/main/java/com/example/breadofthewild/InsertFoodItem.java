package com.example.breadofthewild;

import java.util.List;

public class InsertFoodItem implements Runnable {
    AppDatabase db;
    Food[] food;

    public InsertFoodItem(AppDatabase db, Food[] food) {
        this.db = db;
        this.food = food;
    }

    @Override
    public void run() {
        List<Food> snapshot = db.foodDAO().getAllFood();
        for (Food foodItem : food) {
            boolean blockInsert = false;
            for (Food snapShotVillager : snapshot) {
                if (snapShotVillager.getId() == foodItem.getId()) blockInsert = true;
            }
            if (!blockInsert) db.foodDAO().insertFood(foodItem);
        }
    }
}
