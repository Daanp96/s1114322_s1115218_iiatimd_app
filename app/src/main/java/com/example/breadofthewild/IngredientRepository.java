package com.example.breadofthewild;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.List;

public class IngredientRepository {
    private IngredientDAO ingredientDAO;
    private LiveData<List<Ingredient>> allIngredients;

    public IngredientRepository(Application application){
        AppDatabase database = AppDatabase.getInstance(application);
        ingredientDAO = database.ingredientDAO();
        allIngredients = ingredientDAO.getAllIngredients();
    }

    public void insert(Ingredient ingredient){
        new InsertIngredientAsyncTask(ingredientDAO).execute(ingredient);
    }

    public LiveData<List<Ingredient>> getAllIngredients(){
        return allIngredients;
    }

    private static class InsertIngredientAsyncTask extends AsyncTask<Ingredient, Void, Void> {
        private IngredientDAO ingredientDAO;

        private InsertIngredientAsyncTask(IngredientDAO ingredientDAO){
            this.ingredientDAO = ingredientDAO;
        }

        @Override
        protected Void doInBackground(Ingredient... ingredients){
            ingredientDAO.insertIngredient(ingredients[0]);
            return null;
        }
    }
}
