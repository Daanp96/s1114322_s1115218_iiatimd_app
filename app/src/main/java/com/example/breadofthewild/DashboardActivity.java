package com.example.breadofthewild;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getUser();

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
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(20000, 3, 1.0f));
        requestQueue.add(stringRequest);


    }
}
