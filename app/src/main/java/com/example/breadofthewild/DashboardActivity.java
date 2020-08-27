package com.example.breadofthewild;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class DashboardActivity extends AppCompatActivity {

    private final String URL = "http://10.0.2.2:8000/api/user";

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent mainIntent = new Intent(this, MainActivity.class);
        startActivity(mainIntent);
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences sp = getPreferences(MODE_PRIVATE);
        UserAuth.getInstance(this).setPreferences(sp);
        getUser();
        setContentView(R.layout.activity_dashboard);
        Button toFoodScreen = findViewById(R.id.toRecipes);
        Button toIngredientsScreen = findViewById(R.id.toIngredients);
        Button toCookbookScreen = findViewById(R.id.toCookpot);
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

        toCookbookScreen.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

//                Toast.makeText(DashboardActivity.this, "Oh no!\nIt's raining, the cookpot can't be lit right now", Toast.LENGTH_LONG).show();
                toCookbookActivity();
            }
        });
    }

    public void toCookbookActivity() {
        Intent cookbookIntent = new Intent(this, CookbookActivity.class);
        startActivity(cookbookIntent);

    }

    public void toRecipesActivity(){
        Intent foodIntent = new Intent(this, FoodActivity.class);
        startActivity(foodIntent);
    }

    public void toIngredientsActivity(){
        Intent ingredientIntent = new Intent(this, IngredientActivity.class);
        startActivity(ingredientIntent);
    }

    public void getUser() {

        JsonObjectRequest jor = new JsonObjectRequest(Request.Method.GET, URL, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    String username = response.getString("name");
                    TextView userView = findViewById(R.id.main_user);
                    userView.setText("Hello " + username + "!");

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
               Log.e("Volley", error.toString());
            }
        }){
            @Override
            public Map getHeaders() throws AuthFailureError {
                String token = UserAuth.getInstance(getApplicationContext()).getJwt();
                Map headers = new HashMap();
                headers.put("Authorization", "Bearer " + token);
                return headers;
            }
        };
        VolleySingleton.getInstance(this).addToRequestQueue(jor);

    }
}
