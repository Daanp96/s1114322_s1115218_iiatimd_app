package com.example.breadofthewild;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.ArrayList;
import java.util.List;

@Dao
public interface FoodDAO {

    @OnConflictStrategy
    @Query("SELECT * FROM food")
    LiveData<List<Food>> getAllFood();

    @OnConflictStrategy
    @Insert
    void insertFood(Food food);
}