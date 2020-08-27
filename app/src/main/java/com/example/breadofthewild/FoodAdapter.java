package com.example.breadofthewild;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FoodAdapter extends RecyclerView.Adapter<FoodAdapter.FoodViewHolder> {
    private Context context;
    private List<Food> list;
    private String type;
    private ItemClickListener mListener;



    public FoodAdapter(Context context, List<Food> list, String type) {
        this.context = context;
        this.list = list;
        this.type = type;
    }

    public static class FoodViewHolder extends RecyclerView.ViewHolder {
        public TextView viewName;
        public ImageView viewImage;
        public TextView viewDescription;
        public TextView viewSubclass;
        public TextView viewEffect;
        public ImageButton viewButton;



        public FoodViewHolder(@NonNull View itemView) {
            super(itemView);
            viewName = itemView.findViewById(R.id.main_name);
            viewImage = itemView.findViewById(R.id.main_image);
            viewDescription = itemView.findViewById(R.id.main_description);
            viewSubclass = itemView.findViewById(R.id.main_subclass);
            viewEffect = itemView.findViewById(R.id.main_effect);
            viewButton = itemView.findViewById(R.id.main_button);
        }
    }

    @NonNull
    @Override
    public FoodAdapter.FoodViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.food_item, parent, false);
        FoodViewHolder foodViewHolder = new FoodViewHolder(v);
        return foodViewHolder;
    }

    public void addClickListener(ItemClickListener listener) {
        mListener = listener;

    }

    @Override
    public void onBindViewHolder(@NonNull FoodAdapter.FoodViewHolder holder, final int position) {
        final Food food = list.get(position);
        holder.viewName.setText(food.getName());
        holder.viewDescription.setText(food.getDescription());
        Picasso.get().load(food.getImage()).into(holder.viewImage);
        holder.viewSubclass.setText(food.getSubclass());
        holder.viewEffect.setText(food.getEffect());
//        Picasso.get().load("https://dl.airtable.com/JtPShAO4TQS3JtMsg87E_40px-BotW_Tough_Elixir_Icon.png%3Fversion%3D53aabd7829fcba3fc04ea19cd05e31ff").into(holder.viewImage);

        holder.viewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int foodID = food.getId();
                if(mListener != null) {
                        mListener.onItemClick(foodID);
                        list.remove(list.get(position));

                }
            }
        });

        switch(type) {
            case "Food":
                holder.viewButton.setImageResource(R.drawable.add);
                break;

            case "CookBook":
                holder.viewButton.setImageResource(R.drawable.delete);
                break;

            default:
                break;
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public interface ItemClickListener{
        void onItemClick(int currentID);
    }

}


