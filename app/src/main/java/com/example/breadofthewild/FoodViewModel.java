package com.example.breadofthewild;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class FoodViewModel extends AndroidViewModel {
    private FoodRepository repository;
    private LiveData<List<Food>> allFood;

    public FoodViewModel(@NonNull Application application){
        super(application);
        repository = new FoodRepository(application);
        allFood = repository.getAllFood();
    }

    public void insert(Food food){
        repository.insert(food);
    }

    public LiveData<List<Food>> getAllFood(){
        return allFood;
    }
}
