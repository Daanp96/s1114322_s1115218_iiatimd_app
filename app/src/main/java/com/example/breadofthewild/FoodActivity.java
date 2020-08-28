package com.example.breadofthewild;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class FoodActivity extends AppCompatActivity {
    private FoodViewModel foodViewModel;

//    private String url = "https://botw-cookbook.herokuapp.com/api/food";
    private String url = "http://10.0.2.2:8000/api/food";
    private Context context;
    private RecyclerView mList;

    //    private List<Food> foodList;
//    private RecyclerView.Adapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        mList = findViewById(R.id.main_list);
//        foodList = new ArrayList<>();
//        adapter = new FoodAdapter(foodList);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(mList.getContext(), linearLayoutManager.getOrientation());
        mList.setHasFixedSize(true);
        mList.setLayoutManager(linearLayoutManager);
        mList.addItemDecoration(dividerItemDecoration);

        final FoodAdapter adapter = new FoodAdapter();
        mList.setAdapter(adapter);

        foodViewModel = ViewModelProviders.of(this).get(FoodViewModel.class);
        foodViewModel.getAllFood().observe(this, new Observer<List<Food>>() {
            @Override
            public void onChanged(List<Food> foodList) {
                adapter.setFood(foodList);
            }
        });

        getData();

    }

    private void getData () {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        final FoodAdapter adapter = new FoodAdapter();
        progressDialog.setMessage("Loading...");
        progressDialog.show();

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(url, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                for (int i = 0; i < response.length(); i++) {
                    try {
                        JSONObject jsonObject = response.getJSONObject(i);
                        Food food = new Food();
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
        });

        VolleySingleton.getInstance(this).addToRequestQueue(jsonArrayRequest);

    }
}
