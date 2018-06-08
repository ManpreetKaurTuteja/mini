package com.miniapp.home.models;

/**
 * Created by mac on 18/04/18.
 */

public class UserJobSubCategory {
    int subcategory_id;
    String subcategory_title;
    String sub_category_description;

    public int getSubcategory_id() {
        return subcategory_id;
    }

    public void setSubcategory_id(int subcategory_id) {
        this.subcategory_id = subcategory_id;
    }

    public String getSubcategory_title() {
        return subcategory_title;
    }

    public void setSubcategory_title(String subcategory_title) {
        this.subcategory_title = subcategory_title;
    }

    public String getSub_category_description() {
        return sub_category_description;
    }

    public void setSub_category_description(String sub_category_description) {
        this.sub_category_description = sub_category_description;
    }
}
