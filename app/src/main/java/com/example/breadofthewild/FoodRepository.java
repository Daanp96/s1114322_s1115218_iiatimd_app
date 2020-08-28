package com.example.breadofthewild;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.List;

public class FoodRepository {
    private FoodDAO foodDAO;
    private LiveData<List<Food>> allFood;

    public FoodRepository(Application application){
        AppDatabase database = AppDatabase.getInstance(application);
        foodDAO = database.foodDAO();
        allFood = foodDAO.getAllFood();
    }

    public void insert(Food food){
        new InsertFoodAsyncTask(foodDAO).execute(food);
    }

    public LiveData<List<Food>> getAllFood(){ return allFood; }

    private static class InsertFoodAsyncTask extends AsyncTask<Food, Void, Void> {
        private FoodDAO foodDAO;

        private InsertFoodAsyncTask(FoodDAO foodDAO){
            this.foodDAO = foodDAO;
        }

        @Override
        protected Void doInBackground(Food... foods) {
            foodDAO.insertFood(foods[0]);
            return null;
        }
    }
}
