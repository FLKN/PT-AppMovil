package com.ptappmovil.upiita.pt_appmovil.Items;


import android.graphics.Bitmap;

public class ServiceItem {
    private String dish_name;
    private String dish_cost;
    private Bitmap dish_image;

    public ServiceItem(String dish_name, Bitmap dish_image, String dish_cost) {
        this.dish_name = dish_name;
        this.dish_cost = dish_cost;
        this.dish_image = dish_image;
    }

    public ServiceItem() {
    }

    public String getDish_name() {
        return dish_name;
    }

    public void setDish_name(String dish_name) {
        this.dish_name = dish_name;
    }

    public Bitmap getDish_image() {
        return dish_image;
    }

    public void setDish_image(Bitmap dish_image) {
        this.dish_image = dish_image;
    }

    public String getDish_cost() {
        return dish_cost;
    }

    public void setDish_cost(String dish_cost) {
        this.dish_cost = dish_cost;
    }
}
