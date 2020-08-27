package com.example.breadofthewild;

import android.app.ProgressDialog;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CookbookActivity extends AppCompatActivity implements FoodAdapter.ItemClickListener {
    private String URL = "http://10.0.2.2:8000/api/favorite";
    private String REMOVE_URL = "http://10.0.2.2:8000/api/removefavorite";

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

        mp = MediaPlayer.create(this, R.raw.delete);
        mp.seekTo(0);
        mp.setVolume(0.5f, 0.5f);

        mList = findViewById(R.id.main_list);
        overviewTitle = findViewById(R.id.title);
        overviewTitle.setText("My Cookbook");
        foodList = new ArrayList<>();
        adapter = new FoodAdapter(getApplicationContext(), foodList, "CookBook");
        ((FoodAdapter) adapter).addClickListener(this);

        linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        dividerItemDecoration = new DividerItemDecoration(mList.getContext(), linearLayoutManager.getOrientation());
        dividerItemDecoration.setDrawable(this.getResources().getDrawable(R.drawable.divider));

        mList.setHasFixedSize(true);
        mList.setLayoutManager(linearLayoutManager);
        mList.addItemDecoration(dividerItemDecoration);
        mList.setAdapter(adapter);
    }

    private void getData() {
        final ProgressDialog progressDialog = new ProgressDialog(this, R.style.ProgressDialog);
        progressDialog.setMessage("Loading Cookbook");
        progressDialog.show();

        //GET ALL FAVORITE RECIPES BELONGING TO LOGGED IN USER
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
                        foodList.add(food);
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

        //DELETE A FAVORITE RECIPE FROM LOGGED IN USER
        JsonObjectRequest jor = new JsonObjectRequest(Request.Method.POST, REMOVE_URL, currentID, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    String deletedFood = response.getString("food_id");
                    Log.d("deleted: ", deletedFood);
                } catch (JSONException e) {
                    Log.d("onReponse: ", String.valueOf(e));
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mp.start();
                Log.d("error", String.valueOf(error));
                adapter.notifyDataSetChanged();
                Toast.makeText(getApplicationContext(), "Recipe has been deleted from the CookBook", Toast.LENGTH_SHORT).show();
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

