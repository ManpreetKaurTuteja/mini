package com.miniapp.home.activities;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.miniapp.R;
import com.miniapp.application.MiniApp;
import com.miniapp.commonUtils.CommonUtility;
import com.miniapp.commonUtils.JsonUtils;
import com.miniapp.home.Utils.Constants;
import com.miniapp.home.models.UserOtherImages;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.App;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

@EActivity(R.layout.activity_full_image)
public class FullImageActivity extends AppCompatActivity {
    @App
    MiniApp miniApp;

    @ViewById(R.id.full_image)
    ImageView imageView;

    @AfterViews
    void init() {
        miniApp.setActivity(this);
//        CommonUtility.setStatusBarColor(this);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            String url = "";
            if (bundle.containsKey(Constants.kKeyForUserOtherImage)) {
                url = bundle.getString(Constants.kKeyForUserOtherImage);
                UserOtherImages data = (UserOtherImages) JsonUtils.toObject(url, UserOtherImages.class);
                final String thumb = data.getThumb();
                final Bitmap bitmap = data.getBitmap();
                if (thumb != null)
                    Glide.with(this).load(url).diskCacheStrategy(DiskCacheStrategy.NONE).
                            placeholder(R.drawable.placeholder).
                            skipMemoryCache(true).into(imageView);
                else if (bitmap != null) {
                    imageView.setImageBitmap(bitmap);
                }

            } else if (bundle.containsKey("image_bitmap")) {
                byte[] byteArray = getIntent().getByteArrayExtra("image_bitmap");
                Bitmap bmp = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
                imageView.setImageBitmap(bmp);
            }
        }
    }
}
