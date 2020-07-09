package com.example.breadofthewild;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class DashboardActivity extends AppCompatActivity {
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        Button toFoodScreen = findViewById(R.id.toRecipes);
        Button toIngredientsScreen = findViewById(R.id.toIngredients);
        toFoodScreen.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                toRecipesActivity();
            }
        });

        toIngredientsScreen.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                toIngredientsActivity();
            }
        });
    }

    public void toRecipesActivity(){
        Intent foodIntent = new Intent(this, FoodActivity.class);
        startActivity(foodIntent);
    }

    public void toIngredientsActivity(){
        Intent ingredientIntent = new Intent(this, IngredientsActivity.class);
        startActivity(ingredientIntent);
    }
}
