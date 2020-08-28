package com.example.breadofthewild;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class IngredientAdapter extends RecyclerView.Adapter<IngredientAdapter.IngredientViewHolder> {
    private List<Ingredient> list = new ArrayList<>();

//    public IngredientAdapter(List<Ingredient> list) {
//        this.list = list;
//    }

    public static class IngredientViewHolder extends RecyclerView.ViewHolder {
        public TextView viewName;
        public ImageView viewImage;
        public TextView viewDescription;
        public TextView viewSubclass;


        public IngredientViewHolder(@NonNull View itemView) {
            super(itemView);
            viewName = itemView.findViewById(R.id.main_name);
            viewImage = itemView.findViewById(R.id.main_image);
            viewDescription = itemView.findViewById(R.id.main_description);
            viewSubclass = itemView.findViewById(R.id.main_subclass);
        }
    }

    @NonNull
    @Override
    public IngredientAdapter.IngredientViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.ingredient_item, parent, false);
        return new IngredientViewHolder(v);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull IngredientAdapter.IngredientViewHolder holder, int position) {
        Ingredient ingredient = list.get(position);
        holder.viewName.setText(ingredient.getName());
        holder.viewDescription.setText(ingredient.getDescription());
        Picasso.get().load(ingredient.getImage()).into(holder.viewImage);
        holder.viewSubclass.setText(ingredient.getSubclass());
//        Picasso.get().load("https://dl.airtable.com/JtPShAO4TQS3JtMsg87E_40px-BotW_Tough_Elixir_Icon.png%3Fversion%3D53aabd7829fcba3fc04ea19cd05e31ff").into(holder.viewImage);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void setIngredient(List<Ingredient> list){
        this.list = list;
        notifyDataSetChanged();
    }

}
