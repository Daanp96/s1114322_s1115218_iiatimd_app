package com.example.breadofthewild;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class FoodAdapter extends RecyclerView.Adapter<FoodAdapter.FoodViewHolder> {
    private Context context;
    private List<Food> list;

    public FoodAdapter(Context context, List<Food> list) {
        this.context = context;
        this.list = list;
    }

    public static class FoodViewHolder extends RecyclerView.ViewHolder {
        public TextView viewName;
        public ImageView viewImage;
        public TextView viewDescription;


        public FoodViewHolder(@NonNull View itemView) {
            super(itemView);
            viewName = itemView.findViewById(R.id.main_name);
            viewImage = itemView.findViewById(R.id.main_image);
            viewDescription = itemView.findViewById(R.id.main_description);
        }
    }

    @NonNull
    @Override
    public FoodAdapter.FoodViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.single_item, parent, false);
        FoodViewHolder foodViewHolder = new FoodViewHolder(v);
        return foodViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull FoodAdapter.FoodViewHolder holder, int position) {
        Food food = list.get(position);
        holder.viewName.setText(food.getName());
        holder.viewDescription.setText(food.getDescription());
        Picasso.get().load(food.getImage()).into(holder.viewImage);
//        Picasso.get().load("https://dl.airtable.com/JtPShAO4TQS3JtMsg87E_40px-BotW_Tough_Elixir_Icon.png%3Fversion%3D53aabd7829fcba3fc04ea19cd05e31ff").into(holder.viewImage);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

}

