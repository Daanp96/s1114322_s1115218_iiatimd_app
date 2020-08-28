package com.example.breadofthewild;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class IngredientViewModel extends AndroidViewModel {
    private IngredientRepository repository;
    private LiveData<List<Ingredient>> allIngredients;

    public IngredientViewModel(@NonNull Application application) {
        super(application);
        repository = new IngredientRepository(application);
        allIngredients = repository.getAllIngredients();
    }

    public void insert(Ingredient ingredient){
        repository.insert(ingredient);
    }

    public LiveData<List<Ingredient>> getAllIngredients(){
        return allIngredients;
    }
}
