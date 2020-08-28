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

public class IngredientsActivity extends AppCompatActivity {
    private IngredientViewModel ingredientViewModel;

//    private List<Ingredient> ingredientList;
//    private RecyclerView.Adapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        RecyclerView mList = findViewById(R.id.main_list);
//        ingredientList = new ArrayList<>();
//        adapter = new IngredientAdapter(ingredientList);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(mList.getContext(), linearLayoutManager.getOrientation());
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
        progressDialog.setMessage("Loading...");
        progressDialog.show();

        String url = "http://10.0.2.2:8000/api/ingredient";
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
        });

        VolleySingleton.getInstance(this).addToRequestQueue(jsonArrayRequest);
    }
}
