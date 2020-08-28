package com.example.breadofthewild;

import android.util.Log;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "ingredient")
public class Ingredient {

    @PrimaryKey(autoGenerate = true)
    public int id;

    @ColumnInfo(name = "name")
    public String name;

    @ColumnInfo(name = "image")
    public String image;

    @ColumnInfo(name = "description")
    public String description;

    @ColumnInfo(name = "subclass")
    public String subclass;

    public Ingredient(){}

    // Getters
    public int getId() {
        return id;
    }

    public String getName() { return name; }

    public String getImage() { return image; }

    public String getDescription() { return description; }

    public String getSubclass() { return subclass; }

    // Setters
    public void setId(int id){
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setImage(String image) {
        image = image.substring(image.indexOf("(")+ 1);
        image = image.substring(0, image.indexOf(")"));
        this.image = image;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setSubclass(String subclass) { this.subclass =subclass;}
}
