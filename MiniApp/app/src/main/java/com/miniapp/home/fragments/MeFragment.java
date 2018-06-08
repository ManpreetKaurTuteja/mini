package com.miniapp.home.fragments;


import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.miniapp.R;
import com.miniapp.application.MiniApp;
import com.miniapp.commonUtils.CommonUtility;
import com.miniapp.commonUtils.Constants;
import com.miniapp.commonUtils.JsonUtils;
import com.miniapp.home.Utils.JobSubCategories;
import com.miniapp.home.activities.MainActivity;
import com.miniapp.home.activities.UserImagesActivity;
import com.miniapp.home.activities.UserImagesActivity_;
import com.miniapp.home.dialogs.JobCategoriesDailog;
import com.miniapp.home.models.EditUserProfileResponse;
import com.miniapp.home.models.JobsCategory;
import com.miniapp.home.models.JobsResponse;
import com.miniapp.home.models.JobsSubCategory;
import com.miniapp.home.models.OtherImages;
import com.miniapp.home.models.ProfileResponse;
import com.miniapp.home.models.UserJobSubCategory;
import com.miniapp.home.models.UserOtherImages;
import com.miniapp.login.activities.LoginActivity_;
import com.miniapp.models.UserDetails;
import com.miniapp.volley.AppHelper;
import com.miniapp.volley.VolleyMultipartRequest;
import com.miniapp.volley.VolleySingleton;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.App;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;
import org.w3c.dom.Text;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * A simple {@link Fragment} subclass.
 */
@EFragment(R.layout.fragment_me)
public class MeFragment extends Fragment {

    @App
    MiniApp miniApp;

    @ViewById(R.id.profile_pic)
    CircleImageView profilePic;
    @ViewById(R.id.edit_image)
    ImageView editImageIcon;
    @ViewById(R.id.email_texview)
    TextView emailTexview;
    @ViewById(R.id.phone_number_texview)
    TextView phoneNumberTexview;
    @ViewById(R.id.edit_update)
    Button editUpdateButton;
    @ViewById(R.id.username_edittext)
    EditText usernameEdittext;
    @ViewById(R.id.spinnerUserType)
    Spinner spinnerUserType;
    @ViewById(R.id.spinnerGender)
    Spinner spinnerGender;
    @ViewById(R.id.about_edittext)
    EditText aboutEdittext;
    @ViewById(R.id.categoryEdittext)
    EditText categoryEdittext;
    @ViewById(R.id.category_spinner)
    Spinner spinner;
    boolean isCategorySelectedFromApi;
    public List<UserOtherImages> userOtherImages;
    JobsCategory selectedCategory = new JobsCategory();
    boolean isProfileImage;

    MainActivity mainActivity;

    public MeFragment() {
        // Required empty public constructor
    }


    @AfterViews
    void init() {
        mainActivity = (MainActivity) this.getActivity();
        getMyDetails();
        addGenderData();
        addUserType();
    }

    void getMyDetails() {
        if (miniApp.isConnected()) {
            miniApp.showProgress(mainActivity);
            String url = com.miniapp.commonUtils.Constants.ServiceConstants.SERVER_URL +
                    com.miniapp.commonUtils.Constants.ServiceConstants.GET_PROFILE_URL_EXTRA;
            Map<String, String> headers = new HashMap<>();
            headers.put(com.miniapp.commonUtils.Constants.kKeyForAuthToken, CommonUtility.getCurrentUserToken(mainActivity));
            VolleyMultipartRequest multipartRequest = new VolleyMultipartRequest(Request.Method.GET, url, headers, new Response.Listener<NetworkResponse>() {
                @Override
                public void onResponse(NetworkResponse response) {
                    String resultResponse = new String(response.data);
                    miniApp.hideProgress();
                    ProfileResponse profileResponse = (ProfileResponse) JsonUtils.toObject(resultResponse, ProfileResponse.class);
                    String message = profileResponse.getMessage();
                    if (profileResponse.getStatus() == com.miniapp.commonUtils.Constants.ResponseStatus.SUCCESS) {
                        apiResponse(profileResponse.getUserDetails());
                    } else if (message.equals(com.miniapp.signup.commonUtils.Constants.kMsgForTokenExpired)) {
                        message = com.miniapp.signup.commonUtils.Constants.kMsgForSessionExpired;
                        miniApp.startNewActivity(mainActivity, LoginActivity_.class);
                        miniApp.showToast(mainActivity, message);
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
                        if (json != null && json.equals(com.miniapp.signup.commonUtils.Constants.kMsgForTokenExpired)) {
                            json = com.miniapp.signup.commonUtils.Constants.kMsgForSessionExpired;
                            miniApp.startNewActivity(mainActivity, LoginActivity_.class);
                        }
                    }
                    miniApp.showToast(mainActivity, json);
                }
            });
            VolleySingleton.getInstance(mainActivity).addToRequestQueue(multipartRequest);

        } else {
//            setNoNetVisibility(View.VISIBLE);
        }
    }

    void apiResponse(UserDetails userDetails) {
        String dp = userDetails.getProfile_image_thumb();
        if (dp != null || !dp.equals(""))
            Glide.with(this).load(userDetails.getProfile_image_thumb()).diskCacheStrategy(DiskCacheStrategy.NONE).
                    placeholder(R.drawable.avatar).
                    skipMemoryCache(true).into(profilePic);
        usernameEdittext.setText(userDetails.getUsername());
        emailTexview.setText(userDetails.getEmail());
        phoneNumberTexview.setText(userDetails.getPhone_no());
        aboutEdittext.setText(userDetails.getBiography());
        setCategoryText(userDetails.getUserJobSubCategories());
        spinnerGender.setSelection(setGenderValue(userDetails.getGender()));
        spinnerUserType.setSelection(userDetails.getUser_type());
        userOtherImages = userDetails.getOtherImages();
        CommonUtility.setUserOtherImages(userDetails.getOtherImages(), mainActivity);
        selectedCategory.setCategory_name(userDetails.getCategory_name());
        selectedCategory.setCategory_id(userDetails.getCategory_id());
        selectedCategory.setDescription(userDetails.getCategory_desc());
        showCategories();
    }

    int setGenderValue(String gender) {
        if (gender.equalsIgnoreCase(com.miniapp.signup.commonUtils.Constants.Gender.MALE)) {
            return 1;
        } else if (gender.equalsIgnoreCase(com.miniapp.signup.commonUtils.Constants.Gender.FEMALE)) {
            return 2;
        } else if (gender.equalsIgnoreCase(com.miniapp.signup.commonUtils.Constants.Gender.OTHERS)) {
            return 3;
        }
        return 0;
    }

    void addUserType() {
        List<String> list = new ArrayList<String>();
        list.add(com.miniapp.signup.commonUtils.Constants.UserType.CHOOSE);
        list.add(com.miniapp.signup.commonUtils.Constants.UserType.HIRE);
        list.add(com.miniapp.signup.commonUtils.Constants.UserType.FREELANCER);

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>
                (mainActivity, R.layout.spinner_item_layout, list);

        dataAdapter.setDropDownViewResource
                (android.R.layout.simple_spinner_dropdown_item);
        spinnerUserType.setAdapter(dataAdapter);
    }

    String getUserType() {
        String user = "";
        if (spinnerUserType.getSelectedItemPosition() != 0)
            user = spinnerUserType.getSelectedItemPosition() + "";
        return user;
    }

    void addGenderData() {
        List<String> list = new ArrayList<String>();
        list.add(com.miniapp.signup.commonUtils.Constants.Gender.CHOOSE);
        list.add(com.miniapp.signup.commonUtils.Constants.Gender.MALE);
        list.add(com.miniapp.signup.commonUtils.Constants.Gender.FEMALE);
        list.add(com.miniapp.signup.commonUtils.Constants.Gender.OTHERS);

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(mainActivity, R.layout.spinner_item_layout, list);

        dataAdapter.setDropDownViewResource
                (android.R.layout.simple_spinner_dropdown_item);
        spinnerGender.setAdapter(dataAdapter);
    }

    String getUserGender() {
        String gender = "";
        if (spinnerGender.getSelectedItemPosition() != 0)
            gender = spinnerGender.getSelectedItem().toString();
        return gender;
    }


    @Click({R.id.edit_update, R.id.categoryEdittext, R.id.edit_image, R.id.other_images_button})
    void clickActions(View view) {
        switch (view.getId()) {
            case R.id.edit_update:
                editUpdate();
                break;
            case R.id.categoryEdittext:
                JobSubCategories subCategories = new JobSubCategories(mainActivity);
                subCategories.fetchSubCategories(selectedCategory, MeFragment.this);
                break;
            case R.id.edit_image:
                isProfileImage = true;
                CommonUtility.openGalleryOrCamera(this.getActivity());
                break;
            case R.id.other_images_button:
                Intent intent = new Intent(mainActivity, UserImagesActivity_.class);
//                OtherImages otherImages = new OtherImages();
//                otherImages.setUserOtherImages(userOtherImages);
//                String otherImagesString = JsonUtils.toString(otherImages);
//                intent.putExtra(com.miniapp.home.Utils.Constants.kKeyForUserOtherImage, otherImagesString);
                mainActivity.startActivity(intent);
                break;
        }
    }

    public void addSpineerData(final List<JobsCategory> jobsList) {
        isCategorySelectedFromApi = true;
        List<String> list = new ArrayList<String>();
        list.add("Choose Category");
        int size = jobsList.size();
        for (int i = 0; i < size; i++) {
            list.add(jobsList.get(i).getCategory_name());
            if (jobsList.get(i).getCategory_id() == selectedCategory.getCategory_id())
                spinner.setSelection(i + 1);
        }

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>
                (mainActivity, R.layout.spinner_item_layout, list);

        dataAdapter.setDropDownViewResource
                (android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(dataAdapter);
        for (int i = 0; i < size; i++) {
            if (jobsList.get(i).getCategory_id() == selectedCategory.getCategory_id())
                spinner.setSelection(i + 1);
        }
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                if (position > 0) {
                    if (isCategorySelectedFromApi) {
                        isCategorySelectedFromApi = false;
                    } else {
                        JobSubCategories subCategories = new JobSubCategories(mainActivity);
                        JobsCategory category = new JobsCategory();
                        category.setCategory_id(jobsList.get(position - 1).getCategory_id());
                        category.setCategory_name(jobsList.get(position - 1).getCategory_name());
                        category.setDescription(jobsList.get(position - 1).getDescription());
                        selectedCategory = category;
                        subCategories.fetchSubCategories(category, MeFragment.this);
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    void editUpdate() {
        updateDetails();
//        if (editUpdateButton.getText().toString().equals(Constants.EDIT_TEXT)) {
//            showEditIcons();
//        } else {
//            hideEditIcons();
//        }
    }

    void showCategories() {
        com.miniapp.home.Utils.JobCategories jobCategories = new com.miniapp.home.Utils.JobCategories((MainActivity) this.getActivity());
        jobCategories.fetchCategories(this);
    }

    void showEditIcons() {
        editImageIcon.setVisibility(View.VISIBLE);
        editUpdateButton.setText(Constants.UPDATE_TEXT);
        usernameEdittext.setEnabled(true);
//        spinnerCategory.setEnabled(true);
        aboutEdittext.setEnabled(true);
        categoryEdittext.setEnabled(true);
    }

    public void setCategoryText(List<UserJobSubCategory> categoryList) {
        if (categoryList != null) {
            int size = categoryList.size();
            String category = "";
            for (int i = 0; i < size; i++) {
                category += categoryList.get(i).getSubcategory_title() + ((i == size - 1) ? com.miniapp.home.Utils.Constants.EMPTY_STRING : com.miniapp.home.Utils.Constants.SEPARATOR);
            }
            categoryEdittext.setText(category);
        }
    }

    void hideEditIcons() {
        editImageIcon.setVisibility(View.GONE);
        editUpdateButton.setText(Constants.EDIT_TEXT);
        usernameEdittext.setEnabled(false);
//        spinnerCategory.setEnabled(false);
        aboutEdittext.setEnabled(false);
        categoryEdittext.setEnabled(false);
        updateDetails();
    }

    void updateDetails() {
        if (miniApp.isConnected()) {
            miniApp.showProgress(mainActivity);
            String url = Constants.ServiceConstants.SERVER_URL + Constants.ServiceConstants.EDIT_PROFILE_URL_EXTRA;
            Map<String, String> headers = new HashMap<>();
            headers.put(com.miniapp.commonUtils.Constants.kKeyForAuthToken, CommonUtility.getCurrentUserToken(mainActivity));
            VolleyMultipartRequest multipartRequest = new VolleyMultipartRequest(Request.Method.POST, url, headers, new Response.Listener<NetworkResponse>() {
                @Override
                public void onResponse(NetworkResponse response) {
                    String resultResponse = new String(response.data);
                    miniApp.hideProgress();
                    EditUserProfileResponse editUserProfileResponse = (EditUserProfileResponse) JsonUtils.toObject(resultResponse, EditUserProfileResponse.class);
                    if (editUserProfileResponse.getStatus() == Constants.ResponseStatus.SUCCESS) {
                        mainActivity.hideTitle();
                        miniApp.changeScreen(new WorkFragment_(), Constants.FragmentAction.REPLACE, R.id.main_activity_container);
                    }
                    miniApp.showToast(mainActivity, editUserProfileResponse.getMessage());
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    miniApp.hideProgress();
                    NetworkResponse response = error.networkResponse;
                    String json = "Try again";
                    if (response != null) {
                        json = new String(response.data);
                        json = miniApp.trimMessage(json, Constants.ResponseStatus.Status_Message);
                        if (json != null && json.equals(Constants.ResponseStatus.auth_token_not_valid_msg)) {
                            json = Constants.ResponseStatus.SESSION_EXPIRED;
                        }
                    }
                    miniApp.showToast(mainActivity, json);
                }
            }) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<>();
                    params.put(Constants.kKeyForPhoneNum, phoneNumberTexview.getText().toString());
                    params.put(Constants.kKeyForUsername, usernameEdittext.getText().toString());
                    params.put(Constants.kKeyForUserType, getUserType());
                    params.put(Constants.kKeyForUserGender, getUserGender());
                    params.put(Constants.kKeyForBiography, aboutEdittext.getText().toString());
                    int cId = selectedCategory.getCategory_id();
                    params.put(Constants.kKeyForCategoryId, cId + "");

                    List<JobsSubCategory> subCategoryList = new ArrayList<>();
                    List<UserJobSubCategory> jobsCategoryList = CommonUtility.getSelectedSubJobsCategory(mainActivity);
                    int size = jobsCategoryList.size();
                    for (int i = 0; i < size; i++) {
                        params.put("user_subcategory[" + i + "][sub_category_id]", jobsCategoryList.get(i).getSubcategory_id() + "");
                    }
                    return params;
                }

//                    @Override
//                    protected Map<String, DataPart> getByteData () {
//                        Map<String, DataPart> params = new HashMap<>();
//                    params.put(Constants.kKeyForFileToUpload, new DataPart());
////                        params.put(Constants.kKeyForFileToUpload, new DataPart("profile_image.jpg", AppHelper.getFileDataFromDrawable(mainActivity.getBaseContext(), profilePic.getDrawable()), "image/jpeg"));
//                        return params;
//                    }
            };
            VolleySingleton.getInstance(mainActivity).

                    addToRequestQueue(multipartRequest);
        } else {
//                miniApp.showToast(Constants.kMsgForUploading);
        }
//        }
    }

    public void uploadImage(Uri uri) {
        if (uri != null) {
            try {
                // scale the image to save on bandwidth
                Bitmap bitmap =
                        CommonUtility.scaleBitmapDown(
                                MediaStore.Images.Media.getBitmap(this.getActivity().getContentResolver(), uri),
                                1200);
                setImageBitmap(bitmap);
            } catch (IOException e) {
                Toast.makeText(this.getActivity(), R.string.image_picker_error, Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(this.getActivity(), R.string.image_picker_error, Toast.LENGTH_LONG).show();
        }
    }

    public void setImageBitmap(Bitmap bitmap) {
//        profilePic.getLayoutParams().height = 100;
//        profilePic.getLayoutParams().width = 100;
        profilePic.setImageBitmap(bitmap);
    }
}
