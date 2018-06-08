package com.miniapp.login.facebook_login.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by mac on 14/12/17.
 */

public class FBPagingCursor {
    @SerializedName("before")
    String before;
    @SerializedName("after")
    String after;

    public String getBefore() {
        return before;
    }

    public void setBefore(String before) {
        this.before = before;
    }

    public String getAfter() {
        return after;
    }

    public void setAfter(String after) {
        this.after = after;
    }
}
