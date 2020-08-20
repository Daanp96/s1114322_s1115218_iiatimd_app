package com.example.breadofthewild;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FoodActivity extends AppCompatActivity {

//    private String url = "https://botw-cookbook.herokuapp.com/api/food";
    private String url = "http://10.0.2.2:8000/api/food";

    private RecyclerView mList;

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

        mList = findViewById(R.id.main_list);
        overviewTitle = findViewById(R.id.title);
        overviewTitle.setText("Recipes");
        foodList = new ArrayList<>();
        adapter = new FoodAdapter(getApplicationContext(), foodList);
        linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        dividerItemDecoration = new DividerItemDecoration(mList.getContext(), linearLayoutManager.getOrientation());
        dividerItemDecoration.setDrawable(this.getResources().getDrawable(R.drawable.divider));
        mList.setHasFixedSize(true);
        mList.setLayoutManager(linearLayoutManager);
        mList.addItemDecoration(dividerItemDecoration);
        mList.setAdapter(adapter);


    }

    private void getData () {
        final ProgressDialog progressDialog = new ProgressDialog(this, R.style.ProgressDialog);
        progressDialog.setMessage("Loading Recipes");
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
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        jsonArrayRequest.setRetryPolicy(new DefaultRetryPolicy(10000, 3, 1.0f));
        requestQueue.add(jsonArrayRequest);
    }
}
