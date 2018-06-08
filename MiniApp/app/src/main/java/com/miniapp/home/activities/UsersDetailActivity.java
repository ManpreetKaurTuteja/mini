package com.miniapp.home.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.miniapp.R;
import com.miniapp.application.MiniApp;
import com.miniapp.commonUtils.Constants;
import com.miniapp.commonUtils.JsonUtils;
import com.miniapp.home.adapters.ImagesAdaptar;
import com.miniapp.home.adapters.UsersImagesAdaptar;
import com.miniapp.home.models.OtherImages;
import com.miniapp.home.models.UserOtherImages;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.App;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.List;

@EActivity(R.layout.activity_users_detail)
public class UsersDetailActivity extends AppCompatActivity {

    @App
    MiniApp miniApp;

    @ViewById(R.id.other_images_recycler)
    public RecyclerView content_listview;

    @ViewById(R.id.name_text)
    TextView name_textview;
    @ViewById(R.id.description_text)
    TextView description_textview;
    @ViewById(R.id.category_textview)
    TextView category_textview;
    @ViewById(R.id.email_text)
    TextView email_textview;
    @ViewById(R.id.usertype_textview)
    TextView usertype_textview;
    @ViewById(R.id.phone_text)
    TextView phone_textview;
    @ViewById(R.id.gender_text)
    TextView gender_textview;
    @ViewById(R.id.dp_image)
    ImageView dp_image;
    private RecyclerView.LayoutManager layoutManager;

    @AfterViews
    void init() {
        miniApp.setActivity(this);

        miniApp.setActivity(this);
        layoutManager = new LinearLayoutManager(this.getApplicationContext());
        content_listview.setLayoutManager(layoutManager);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            Glide.with(this).load(bundle.getString(Constants.kKeyForImageUrl)).diskCacheStrategy(DiskCacheStrategy.NONE).
                    placeholder(R.drawable.placeholder).
                    skipMemoryCache(true).into(dp_image);
            name_textview.setText(bundle.getString(Constants.kKeyForUsername));
            description_textview.setText(bundle.getString(Constants.kKeyForDescription));
            category_textview.setText(bundle.getString(Constants.kKeyForCategoryName));
            gender_textview.setText(bundle.getString(Constants.kKeyForUserGender));
            phone_textview.setText(bundle.getString(Constants.kKeyForPhoneNum));
            email_textview.setText(bundle.getString(Constants.kKeyForEmail));
            usertype_textview.setText(bundle.getString(Constants.kKeyForUserType));
            if (bundle.containsKey(com.miniapp.home.Utils.Constants.kKeyForUserOtherImage)) {
                String string = bundle.getString(com.miniapp.home.Utils.Constants.kKeyForUserOtherImage);
                OtherImages otherImages = (OtherImages) JsonUtils.toObject(string, OtherImages.class);
                List<UserOtherImages> userOtherImages = otherImages.getUserOtherImages();
                UsersImagesAdaptar adaptar = new UsersImagesAdaptar(userOtherImages, this, content_listview);
                content_listview.setAdapter(adaptar);
            }
        }
    }
}
