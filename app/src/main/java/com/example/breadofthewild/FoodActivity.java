package com.example.breadofthewild;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class FoodActivity extends AppCompatActivity {

//    private String url = "https://botw-cookbook.herokuapp.com/api/food";
    private String url = "http://10.0.2.2:8000/api/food";

    private RecyclerView mList;

    private LinearLayoutManager linearLayoutManager;
    private DividerItemDecoration dividerItemDecoration;
    private List<Food> foodList;
    private RecyclerView.Adapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getData();
        setContentView(R.layout.activity_list);

        mList = findViewById(R.id.main_list);
        foodList = new ArrayList<>();
        adapter = new FoodAdapter(foodList);
        linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        dividerItemDecoration = new DividerItemDecoration(mList.getContext(), linearLayoutManager.getOrientation());
        mList.setHasFixedSize(true);
        mList.setLayoutManager(linearLayoutManager);
        mList.addItemDecoration(dividerItemDecoration);
        mList.setAdapter(adapter);
    }

    private void getData () {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        final AppDatabase db = AppDatabase.getInstance(getApplicationContext());
        progressDialog.setMessage("Loading...");
        progressDialog.show();

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                new Thread(new InsertFoodItem(db, foodList)).start();
//                foodList = new Gson().fromJson(String.valueOf(response), new TypeToken<List<Food>>() {}.getType());

                for(int i = 0; i < response.length(); i++){
                    try {
                        JSONObject jsonObject = response.getJSONObject(i);
                        Food food = new Food();
                        food.setName(jsonObject.getString("name"));
                        food.setImage(jsonObject.getString("image"));
                        food.setDescription(jsonObject.getString("description"));
                        food.setSubclass(jsonObject.getString("subclass"));
                        food.setEffect(jsonObject.getString("effect"));
                        foodList.add(food);

                    } catch (JSONException e) {
                        Log.e("error", response.toString());
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
                new Thread(new getFoodItem(db, new FoodListener() {
                    @Override
                    public void onResult(List<Food> foodList) {
                        adapter = new FoodAdapter(foodList);
                        mList.setAdapter(adapter);
                    }
                })).start();
                progressDialog.dismiss();
                Log.e("Er ging iets mis", Log.getStackTraceString(error));

            }
        });

        VolleySingleton.getInstance(this).addToRequestQueue(jsonArrayRequest);

    }
}
