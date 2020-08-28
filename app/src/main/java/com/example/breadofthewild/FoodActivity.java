package com.example.breadofthewild;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.media.MediaPlayer;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FoodActivity extends AppCompatActivity implements FoodAdapter.ItemClickListener{
    private FoodViewModel foodViewModel;
    private String URL = "http://10.0.2.2:8000/api/food";
    private String ADD_URL = "http://10.0.2.2:8000/api/addfavorite";

    private RecyclerView mList;
    private MediaPlayer mp;
    private LinearLayoutManager linearLayoutManager;
    private DividerItemDecoration dividerItemDecoration;
    private List<Food> foodList;
    private RecyclerView.Adapter adapter;
    private TextView overviewTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getData();
        setContentView(R.layout.activity_list);

        mp = MediaPlayer.create(this, R.raw.korok);
        mp.seekTo(0);
        mp.setVolume(0.5f, 0.5f);

        mList = findViewById(R.id.main_list);
        overviewTitle = findViewById(R.id.title);
        overviewTitle.setText("Recipes");
        // foodList = new ArrayList<>();
        // adapter = new FoodAdapter(getApplicationContext(), foodList, "Food");

        linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        dividerItemDecoration = new DividerItemDecoration(mList.getContext(), linearLayoutManager.getOrientation());
        dividerItemDecoration.setDrawable(this.getResources().getDrawable(R.drawable.divider));

        mList.setHasFixedSize(true);
        mList.setLayoutManager(linearLayoutManager);
        mList.addItemDecoration(dividerItemDecoration);

        final FoodAdapter adapter = new FoodAdapter();
        mList.setAdapter(adapter);

        foodViewModel = ViewModelProviders.of(this).get(FoodViewModel.class);
        foodViewModel.getAllFood().observe(this, new Observer<List<Food>>() {
            @Override
            public void onChanged(List<Food> foodList) {
                adapter.setFood(foodList, "Food");
            }
        });

        ((FoodAdapter) adapter).addClickListener(this);




    }

    private void getData () {
        final ProgressDialog progressDialog = new ProgressDialog(this, R.style.ProgressDialog);
        final FoodAdapter adapter = new FoodAdapter();
        progressDialog.setMessage("Loading Recipes");
        progressDialog.show();

        //GET ALL RECIPES
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(URL, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                for (int i = 0; i < response.length(); i++) {
                    try {
                        JSONObject jsonObject = response.getJSONObject(i);
                        Food food = new Food();
                        food.setId(jsonObject.getInt("id"));
                        food.setName(jsonObject.getString("name"));
                        food.setImage(jsonObject.getString("image"));
                        food.setDescription(jsonObject.getString("description"));
                        food.setSubclass(jsonObject.getString("subclass"));
                        food.setEffect(jsonObject.getString("effect"));
                        foodViewModel.insert(food);
                    } catch (JSONException e) {
                        e.printStackTrace();
                        progressDialog.dismiss();
                    }
                }
                adapter.notifyDataSetChanged();
                progressDialog.dismiss();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Volley", error.toString());
                progressDialog.dismiss();
            }
        }) {
            @Override
            public Map getHeaders() throws AuthFailureError {
                String token = UserAuth.getInstance(getApplicationContext()).getJwt();
                Map headers = new HashMap();
                headers.put("Authorization", "Bearer " + token);
                return headers;
            }
        };
        jsonArrayRequest.setRetryPolicy(new DefaultRetryPolicy(10000, 3, 1.0f));
        VolleySingleton.getInstance(this).addToRequestQueue(jsonArrayRequest);
    }

    @Override
    public void onItemClick(int foodID) {
        String foodString =  String.valueOf(foodID);
        Map<String, String> params = new HashMap<>();
        params.put("food_id", foodString);
        JSONObject currentID = new JSONObject(params);

        //ADD A RECIPE TO COOKBOOK OF LOGGED IN USER
        JsonObjectRequest jor = new JsonObjectRequest(Request.Method.POST, ADD_URL, currentID, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    String addedFood = response.getString("food_id");
                    mp.start();
                    Log.d("added: ", addedFood);
                    Toast.makeText(getApplicationContext(), "Recipe has been added to the CookBook", Toast.LENGTH_SHORT).show();
                } catch (JSONException e) {
                    Log.d("onReponse: ", String.valueOf(e));
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("error", String.valueOf(error));
            }
        }) {
            @Override
            public Map getHeaders() throws AuthFailureError {
                String token = UserAuth.getInstance(getApplicationContext()).getJwt();
                Map headers = new HashMap();
                headers.put("Authorization", "Bearer " + token);
                return headers;
            }
        };
        jor.setRetryPolicy(new DefaultRetryPolicy(10000, 3, 1.0f));
        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(jor);
    }
}

