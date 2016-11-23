package com.ptappmovil.upiita.pt_appmovil.Items;


import android.graphics.Bitmap;

public class ServiceItem {
    private String dish_id;
    private String dish_name;
    private String dish_cost;

    public ServiceItem(String dish_id, String dish_name, String dish_cost) {
        this.dish_id = dish_id;
        this.dish_name = dish_name;
        this.dish_cost = dish_cost;
    }

    public ServiceItem() {
    }

    public String getDish_name() {
        return dish_name;
    }

    public void setDish_name(String dish_name) {
        this.dish_name = dish_name;
    }

    public String getDish_cost() {
        return dish_cost;
    }

    public void setDish_cost(String dish_cost) {
        this.dish_cost = dish_cost;
    }

    public String getDish_id() { return dish_id; }

    public void setDish_id(String dish_id) { this.dish_id = dish_id; }
}
