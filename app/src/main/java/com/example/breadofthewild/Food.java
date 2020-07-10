package com.example.breadofthewild;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "food")
public class Food {

    @PrimaryKey(autoGenerate = true)
    public int id;

    @ColumnInfo(name = "name")
    public String name;

    @ColumnInfo(name = "image")
    public String image;

    @ColumnInfo(name = "description")
    public String description;

    @ColumnInfo(name = "subclass")
    private String subclass;

    @ColumnInfo(name = "effect")
    private String effect;

    public Food(){}

//    public Food(String name, String image, String description, String subclass, String effect) {
//        this.name = name;
//        this.image = image;
//        this.description = description;
//        this.subclass = subclass;
//        this.effect = effect;
//    }
    public int getId() { return id; }

    public String getName() {
        return name;
    }

    public String getImage() {
        return image;
    }

    public String getDescription() {
        return description;
    }

    public String getSubclass() { return subclass; }

    public String getEffect() { return effect; }

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

    public void setSubclass(String subclass) { this.subclass = subclass;}

    public void setEffect(String effect) { this.effect = effect;}
}
