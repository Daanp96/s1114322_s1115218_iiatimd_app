package com.example.breadofthewild;

public class Food {
    public String name;
    public String image;
    public String description;
    public String subclass;

    public Food() {
    }

    public Food(String name, String image, String description, String subclass) {
        this.name = name;
        this.image = image;
        this.description = description;
        this.subclass = subclass;
    }

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
