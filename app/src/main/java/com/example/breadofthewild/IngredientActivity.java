package com.example.breadofthewild;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
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

public class IngredientActivity extends AppCompatActivity {

    private IngredientViewModel ingredientViewModel;
//    private String url = "https://botw-cookbook.herokuapp.com/api/ingredient";
    private String url = "http://10.0.2.2:8000/api/ingredient";

    private RecyclerView mList;

    private LinearLayoutManager linearLayoutManager;
    private DividerItemDecoration dividerItemDecoration;
    private List<Ingredient> ingredientList;
    private RecyclerView.Adapter adapter;
    private TextView overviewTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getData();
        setContentView(R.layout.activity_list);

        mList = findViewById(R.id.main_list);
        overviewTitle = findViewById(R.id.title);
        overviewTitle.setText("Ingredients");
        linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        dividerItemDecoration = new DividerItemDecoration(mList.getContext(), linearLayoutManager.getOrientation());
        dividerItemDecoration.setDrawable(this.getResources().getDrawable(R.drawable.divider));
        mList.setHasFixedSize(true);
        mList.setLayoutManager(linearLayoutManager);
        mList.addItemDecoration(dividerItemDecoration);

        final IngredientAdapter adapter = new IngredientAdapter();
        mList.setAdapter(adapter);

        ingredientViewModel = ViewModelProviders.of(this).get(IngredientViewModel.class);
        ingredientViewModel.getAllIngredients().observe(this, new Observer<List<Ingredient>>() {
            @Override
            public void onChanged(List<Ingredient> ingredients) {
                adapter.setIngredient(ingredients);
            }
        });

        getData();
    }

    private void getData () {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        final IngredientAdapter adapter = new IngredientAdapter();
        progressDialog.setMessage("Loading ingredients");
        progressDialog.show();

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(url, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                for (int i = 0; i < response.length(); i++) {
                    try {
                        JSONObject jsonObject = response.getJSONObject(i);
                        Ingredient ingredient = new Ingredient();
                        ingredient.setName(jsonObject.getString("name"));
                        ingredient.setImage(jsonObject.getString("image"));
                        ingredient.setDescription(jsonObject.getString("description"));
                        ingredient.setSubclass(jsonObject.getString("subclass"));
                        ingredientViewModel.insert(ingredient);
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
}
