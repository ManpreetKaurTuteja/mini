package com.miniapp.home.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.miniapp.R;
import com.miniapp.application.MiniApp;
import com.miniapp.commonUtils.CommonUtility;
import com.miniapp.commonUtils.JsonUtils;
import com.miniapp.home.Utils.Constants;
import com.miniapp.home.adapters.ImagesAdaptar;
import com.miniapp.home.models.AddJobResponse;
import com.miniapp.home.models.OtherImages;
import com.miniapp.home.models.UserOtherImages;
import com.miniapp.login.activities.LoginActivity_;
import com.miniapp.volley.AppHelper;
import com.miniapp.volley.VolleyMultipartRequest;
import com.miniapp.volley.VolleySingleton;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.App;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@EActivity(R.layout.activity_user_images)
public class UserImagesActivity extends AppCompatActivity {

    @App
    MiniApp miniApp;

    @ViewById(R.id.other_images_recycler)
    public RecyclerView content_listview;
    private RecyclerView.LayoutManager layoutManager;
    public List<UserOtherImages> userOtherImages = new ArrayList<>();
    public List<UserOtherImages> newImages = new ArrayList<>();
    public List<UserOtherImages> removedImages = new ArrayList<>();
    ImagesAdaptar adaptar;
    @ViewById(R.id.add_image)
    ImageView addImage;
    @ViewById(R.id.save_button)
    Button saveButton;

    @AfterViews
    void init() {
        miniApp.setActivity(this);
        layoutManager = new LinearLayoutManager(this.getApplicationContext());
//        Bundle bundle = getIntent().getExtras();
//        if (bundle != null) {
//            if (bundle.containsKey(Constants.kKeyForUserOtherImage)) {
//                String string = bundle.getString(Constants.kKeyForUserOtherImage);
//                OtherImages otherImages = (OtherImages) JsonUtils.toObject(string, OtherImages.class);
//                userOtherImages = otherImages.getUserOtherImages();
//                adaptar = new ImagesAdaptar(userOtherImages, this, content_listview);
//                content_listview.setAdapter(adaptar);
//            }
//        }

        userOtherImages = CommonUtility.getUserOtherImages(this);
        if (userOtherImages != null && userOtherImages.size() > 0) {
            setButtonViisibility(View.VISIBLE);
            adaptar = new ImagesAdaptar(userOtherImages, this, content_listview);
            content_listview.setAdapter(adaptar);
            content_listview.setLayoutManager(layoutManager);
        } else
            setButtonViisibility(View.GONE);

        addImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (userOtherImages == null || userOtherImages.size() < 5)
                    CommonUtility.openGalleryOrCamera(UserImagesActivity.this);
                else
                    miniApp.showToast(UserImagesActivity.this, "You can only add 5 images.");
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == com.miniapp.commonUtils.Constants.GALLERY_IMAGE_REQUEST && resultCode == RESULT_OK && data != null) {
            uploadImage(data.getData());
        } else if (requestCode == com.miniapp.commonUtils.Constants.CAMERA_IMAGE_REQUEST && resultCode == RESULT_OK) {
            Bitmap bitmap = (Bitmap) data.getExtras().get("data");
            setImageBitmap(bitmap);
        }
    }

    public void setButtonViisibility(int value) {
        saveButton.setVisibility(value);
    }

    public void uploadImage(Uri uri) {
        if (uri != null) {
            try {
                // scale the image to save on bandwidth
                Bitmap bitmap = CommonUtility.scaleBitmapDown(
                        MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri),
                        1000);
                setImageBitmap(bitmap);
            } catch (IOException e) {
                Toast.makeText(this, R.string.image_picker_error, Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(this, R.string.image_picker_error, Toast.LENGTH_LONG).show();
        }
    }

    public void setImageBitmap(Bitmap bitmap) {
//        setButtonViisibility(View.VISIBLE);
        UserOtherImages otherImages = new UserOtherImages();
        otherImages.setBitmap(bitmap);
        userOtherImages.add(otherImages);
        newImages.add(otherImages);
        if (adaptar == null) {
            adaptar = new ImagesAdaptar(userOtherImages, this, content_listview);
            content_listview.setAdapter(adaptar);
            content_listview.setLayoutManager(layoutManager);
            setButtonViisibility(View.VISIBLE);
        } else
            adaptar.updateAdaptar(userOtherImages, content_listview);
    }

    @Click(R.id.save_button)
    void saveImages() {
        if (removedImages != null && removedImages.size() > 0) {
            removeImagesFromServer(0);
        } else if (newImages != null && newImages.size() > 0) {
            uploadNewImages();
        } else {
            miniApp.showToast(UserImagesActivity.this, "Images saved successfully.");
            UserImagesActivity.this.finish();
        }
    }

    void removeImagesFromServer(final int pos) {
        if (miniApp.isConnected()) {
            final int size = removedImages.size();
            miniApp.showProgress(UserImagesActivity.this);
            String url = com.miniapp.commonUtils.Constants.ServiceConstants.SERVER_URL +
                    com.miniapp.commonUtils.Constants.ServiceConstants.DELETE_OTHER_IMAGES_URL_EXTRA;
            Map<String, String> headers = new HashMap<>();
            headers.put(com.miniapp.commonUtils.Constants.kKeyForAuthToken, CommonUtility.getCurrentUserToken(UserImagesActivity.this));
            VolleyMultipartRequest multipartRequest = new VolleyMultipartRequest(Request.Method.POST, url, headers, new Response.Listener<NetworkResponse>() {
                @Override
                public void onResponse(NetworkResponse response) {
                    String resultResponse = new String(response.data);
                    miniApp.hideProgress();
                    AddJobResponse updateResponse = (AddJobResponse) JsonUtils.toObject(resultResponse, AddJobResponse.class);
                    String message = updateResponse.getMessage();
                    if (updateResponse.getStatus() == com.miniapp.commonUtils.Constants.ResponseStatus.SUCCESS) {
                        int position = pos + 1;
                        if (position < size)
                            removeImagesFromServer(position);
                        else if (position == size)
                            uploadNewImages();
                    } else if (message.equals(com.miniapp.signup.commonUtils.Constants.kMsgForTokenExpired)) {
                        message = com.miniapp.signup.commonUtils.Constants.kMsgForSessionExpired;
                        miniApp.startNewActivity(UserImagesActivity.this, LoginActivity_.class);
                        miniApp.showToast(UserImagesActivity.this, message);
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    miniApp.hideProgress();
                    NetworkResponse response = error.networkResponse;
                    String json = "Try again";
                    if (response != null) {
                        json = new String(response.data);
                        json = miniApp.trimMessage(json, com.miniapp.commonUtils.Constants.ResponseStatus.Status_Message);
                        if (json != null && json.equals(com.miniapp.commonUtils.Constants.ResponseStatus.auth_token_not_valid_msg)) {
                            json = com.miniapp.commonUtils.Constants.ResponseStatus.SESSION_EXPIRED;
                        }
                    }
                    miniApp.showToast(UserImagesActivity.this, json);
                }
            }) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<>();
//                    int size = removedImages.size();
//                    for (int i = 0; i < size; i++) {
                    params.put("image", removedImages.get(pos).getImage());
//                    }
                    return params;
                }
            };
            VolleySingleton.getInstance(UserImagesActivity.this).addToRequestQueue(multipartRequest);
        }
    }

    void uploadNewImages() {
        if (miniApp.isConnected()) {
            miniApp.showProgress(UserImagesActivity.this);
            String url = com.miniapp.commonUtils.Constants.ServiceConstants.SERVER_URL + com.miniapp.commonUtils.Constants.ServiceConstants.EDIT_OTHER_IMAGES_URL_EXTRA;
            Map<String, String> headers = new HashMap<>();
            headers.put(com.miniapp.commonUtils.Constants.kKeyForAuthToken, CommonUtility.getCurrentUserToken(UserImagesActivity.this));
            VolleyMultipartRequest multipartRequest = new VolleyMultipartRequest(Request.Method.POST, url, headers, new Response.Listener<NetworkResponse>() {
                @Override
                public void onResponse(NetworkResponse response) {
                    String resultResponse = new String(response.data);
                    miniApp.hideProgress();
                    AddJobResponse updateResponse = (AddJobResponse) JsonUtils.toObject(resultResponse, AddJobResponse.class);
                    String message = updateResponse.getMessage();
                    if (updateResponse.getStatus() == com.miniapp.commonUtils.Constants.ResponseStatus.SUCCESS) {
                        CommonUtility.setUserOtherImages(userOtherImages, UserImagesActivity.this);
                        UserImagesActivity.this.finish();
                    } else if (message.equals(com.miniapp.signup.commonUtils.Constants.kMsgForTokenExpired)) {
                        message = com.miniapp.signup.commonUtils.Constants.kMsgForSessionExpired;
                        miniApp.startNewActivity(UserImagesActivity.this, LoginActivity_.class);
                        miniApp.showToast(UserImagesActivity.this, message);
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    miniApp.hideProgress();
                    NetworkResponse response = error.networkResponse;
                    String json = "Try again";
                    if (response != null) {
                        json = new String(response.data);
                        json = miniApp.trimMessage(json, com.miniapp.commonUtils.Constants.ResponseStatus.Status_Message);
                        if (json != null && json.equals(com.miniapp.commonUtils.Constants.ResponseStatus.auth_token_not_valid_msg)) {
                            json = com.miniapp.commonUtils.Constants.ResponseStatus.SESSION_EXPIRED;
                        }
                    }
                    miniApp.showToast(UserImagesActivity.this, json);
                }
            }) {
                @Override
                protected Map<String, DataPart> getByteData() {
                    Map<String, DataPart> params = new HashMap<>();
                    int size = newImages.size();
                    for (int i = 0; i < size; i++) {
                        params.put("images[" + i + "]", new DataPart("other_image.jpg",
                                AppHelper.getFileDataFromDrawable(UserImagesActivity.this.getBaseContext(),
                                        getDrawbleFromBitmap(newImages.get(i).getBitmap())), "image/jpeg"));
                    }
                    return params;
                }
            };
            VolleySingleton.getInstance(UserImagesActivity.this).addToRequestQueue(multipartRequest);
        }
    }

    Drawable getDrawbleFromBitmap(Bitmap bitmap) {
        return new BitmapDrawable(getResources(), bitmap);
    }
}
