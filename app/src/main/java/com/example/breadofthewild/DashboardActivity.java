package com.example.breadofthewild;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
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
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class DashboardActivity extends AppCompatActivity {

    MediaPlayer mp;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getUser();

        mp = MediaPlayer.create(this, R.raw.korok);
        mp.seekTo(0);
        mp.setVolume(0.5f, 0.5f);

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
                mp.start();
                Toast.makeText(DashboardActivity.this, "Oh no!\nIt's raining, the cookpot can't be lit right now", Toast.LENGTH_LONG).show();
//                toCookbookActivity();
            }
        });
    }

    public void toCookbookActivity() {
//        Intent cookbookIntent = new Intent(this, CookbookActivity.class);
//        startActivity(cookbookIntent);

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
        String url = "https://botw-cookbook.herokuapp.com/api/user";

        final Intent dashboardIntent = getIntent();
        final User user = (User) dashboardIntent.getSerializableExtra("User");

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {

                            JSONObject jsonObject = new JSONObject(response);
                            String username= jsonObject.getString("name");
                            user.setUsername(username);
                            TextView userView = findViewById(R.id.main_user);
                            userView.setText("Hello " + username + "!");

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (error!=null) {
                    error.printStackTrace();
                }
            }
        }){
            @Override
            public Map getHeaders() throws AuthFailureError {
                User user = (User) dashboardIntent.getSerializableExtra("User");
                String token = user.getToken();
                Map headers = new HashMap();
                headers.put("Authorization", "Bearer " + token);
                return headers;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(10000, 3, 1.0f));
        requestQueue.add(stringRequest);


    }
}
