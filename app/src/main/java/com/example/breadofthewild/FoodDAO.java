package com.example.breadofthewild;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.ArrayList;
import java.util.List;

@Dao
public interface FoodDAO {

    @Query("SELECT * FROM food")
    LiveData<List<Food>> getAllFood();

    @Insert
    void insertFood(Food food);

}