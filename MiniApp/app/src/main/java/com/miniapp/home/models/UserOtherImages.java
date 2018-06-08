package com.miniapp.home.models;

import android.graphics.Bitmap;

/**
 * Created by mac on 18/04/18.
 */

public class UserOtherImages {
    String image;
    String thumb;
    Bitmap bitmap;

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getThumb() {
        return thumb;
    }

    public void setThumb(String thumb) {
        this.thumb = thumb;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }
}
