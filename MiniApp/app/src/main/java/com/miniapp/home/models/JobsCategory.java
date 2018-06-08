package com.miniapp.home.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by mac on 07/04/18.
 */

public class JobsCategory {
    @SerializedName("id")
    int category_id;
    @SerializedName("title")
    String category_name;
    String description;

    public int getCategory_id() {
        return category_id;
    }

    public void setCategory_id(int category_id) {
        this.category_id = category_id;
    }

    public String getCategory_name() {
        return category_name;
    }

    public void setCategory_name(String category_name) {
        this.category_name = category_name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
