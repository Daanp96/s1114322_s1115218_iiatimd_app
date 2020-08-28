package com.example.breadofthewild;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface IngredientDAO {

    @Query("SELECT * FROM ingredient")
    LiveData<List<Ingredient>> getAllIngredients();

    @Insert
    void insertIngredient(Ingredient ingredient);
}
