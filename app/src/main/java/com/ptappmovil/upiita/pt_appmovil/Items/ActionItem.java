package com.ptappmovil.upiita.pt_appmovil.Items;

public class ActionItem {
    private String action_icon;
    private String action_name;

    public ActionItem(String action_icon, String action_name) {
        this.action_icon = action_icon;
        this.action_name = action_name;
    }

    public ActionItem() {
    }

    public String getAction_icon() {
        return action_icon;
    }

    public void setAction_icon(String action_icon) {
        this.action_icon = action_icon;
    }

    public String getAction_name() {
        return action_name;
    }

    public void setAction_name(String action_name) {
        this.action_name = action_name;
    }
}

