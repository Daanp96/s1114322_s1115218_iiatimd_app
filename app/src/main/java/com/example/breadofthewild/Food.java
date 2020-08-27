package com.example.breadofthewild;

public class Food {

    public int id;
    public String name;
    public String image;
    public String description;
    public String subclass;
    public String effect;

    public Food() {
    }

    public Food(int id, String name, String image, String description, String subclass, String effect) {
        this.id = id;
        this.name = name;
        this.image = image;
        this.description = description;
        this.subclass = subclass;
        this.effect = effect;
    }

    public int getId() {return id;}

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

    public void setId(int id) { this.id = id;}

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
