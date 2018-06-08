package com.miniapp.home.fragments;


import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.miniapp.R;
import com.miniapp.application.MiniApp;
import com.miniapp.commonUtils.CommonUtility;
import com.miniapp.commonUtils.Constants;
import com.miniapp.commonUtils.JsonUtils;
import com.miniapp.home.Utils.JobSubCategories;
import com.miniapp.home.activities.MainActivity;
import com.miniapp.home.models.AddJobResponse;
import com.miniapp.home.models.CategoryIds;
import com.miniapp.home.models.JobsCategory;
import com.miniapp.home.models.JobsSubCategory;
import com.miniapp.home.models.UserJobSubCategory;
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

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
@EFragment(R.layout.fragment_add_job)
public class AddJobFragment extends Fragment {


    public AddJobFragment() {
        // Required empty public constructor
    }

    @App
    MiniApp miniApp;

    @ViewById(R.id.title_edittext)
    EditText titleEdittext;
    @ViewById(R.id.description_edittext)
    EditText descriptionEdittext;
    @ViewById(R.id.category_spinner)
    Spinner spinner;
    @ViewById(R.id.sub_category_spinner)
    Spinner subSpinner;
    @ViewById(R.id.sub_spinnerLayout)
    FrameLayout sub_spinnerLayout;
    @ViewById(R.id.image)
    ImageView image;
    List<JobsCategory> jobsList = new ArrayList<>(), jobSubList = new ArrayList<>();
    MainActivity mainActivity;
    JobsCategory selectedCategory = new JobsCategory();
    JobsCategory sub_selectedCategory = new JobsCategory();
    boolean isCategorySelectedFromApi;

    @AfterViews
    void init() {
        mainActivity = (MainActivity) this.getActivity();
        showCategories();
//        addSpineerData();
    }

    @Click({R.id.add_post})
    void addPost(View view) {
        if (spinner.getSelectedItemPosition() == 0)
            miniApp.showToast(mainActivity, "Kindly select category.");
        else if (image.getDrawable() == null) {
            miniApp.showToast(mainActivity, "Kindly select image.");
        } else
            addJob();
    }

    @Click(R.id.select_image)
    void selectImage() {
        CommonUtility.openGalleryOrCamera(mainActivity);
    }

    public void addSpineerData(final List<JobsCategory> jobsList) {
        isCategorySelectedFromApi = true;
        this.jobsList = jobsList;
        List<String> list = new ArrayList<String>();
        list.add("Choose Category");
//        List<JobsCategory> jobsList = CommonUtility.getCategories(mainActivity);
        int size = jobsList.size();
        for (int i = 0; i < size; i++) {
            list.add(jobsList.get(i).getCategory_name());
        }

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>
                (mainActivity, R.layout.spinner_item_layout, list);

        dataAdapter.setDropDownViewResource
                (android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(dataAdapter);
//        spinner.setOnFocusChangeListener(new View.OnFocusChangeListener() {
//            @Override
//            public void onFocusChange(View view, boolean b) {
//
//            }
//        });
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                if (position > 0) {
//                    if (isCategorySelectedFromApi) {
//                        isCategorySelectedFromApi = false;
//                    } else {
                        JobSubCategories subCategories = new JobSubCategories(mainActivity);
                        JobsCategory category = new JobsCategory();
                        category.setCategory_id(jobsList.get(position - 1).getCategory_id());
                        category.setCategory_name(jobsList.get(position - 1).getCategory_name());
                        category.setDescription(jobsList.get(position - 1).getDescription());
                        subCategories.fetchSubCategories(category, AddJobFragment.this);
                        selectedCategory = category;
//                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    void showCategories() {
        com.miniapp.home.Utils.JobCategories jobCategories = new com.miniapp.home.Utils.JobCategories((MainActivity) this.getActivity());
        jobCategories.fetchCategories(this);
    }

    public void addSubSpineerData(final List<JobsCategory> jobsList) {
        sub_spinnerLayout.setVisibility(View.VISIBLE);
        jobSubList = jobsList;
        List<String> list = new ArrayList<String>();
        list.add("Choose Sub Category");
//        List<JobsCategory> jobsList = CommonUtility.getCategories(mainActivity);
        int size = jobsList.size();
        for (int i = 0; i < size; i++) {
            list.add(jobsList.get(i).getCategory_name());
        }

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>
                (mainActivity, R.layout.spinner_item_layout, list);

        dataAdapter.setDropDownViewResource
                (android.R.layout.simple_spinner_dropdown_item);
        subSpinner.setAdapter(dataAdapter);
        subSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                if (position > 0) {
//                    sub_selectedCategory =
                    if (isCategorySelectedFromApi) {
                        isCategorySelectedFromApi = false;
                    } else {
//                        JobSubCategories subCategories = new JobSubCategories(mainActivity);
                        JobsCategory category = new JobsCategory();
                        category.setCategory_id(jobSubList.get(position - 1).getCategory_id());
                        category.setCategory_name(jobSubList.get(position - 1).getCategory_name());
                        category.setDescription(jobSubList.get(position - 1).getDescription());
                        sub_selectedCategory = category;
//                        subCategories.fetchSubCategories(category, AddJobFragment.this);
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    void addJob() {
        if (miniApp.isConnected()) {
            final int cId = spinner.getSelectedItemPosition();
            if (cId > 0) {
                miniApp.showProgress(mainActivity);
                String url = Constants.ServiceConstants.SERVER_URL + Constants.ServiceConstants.POST_JOB_URL_EXTRA;
                Map<String, String> headers = new HashMap<>();
                headers.put(com.miniapp.commonUtils.Constants.kKeyForAuthToken, CommonUtility.getCurrentUserToken(mainActivity));
                VolleyMultipartRequest multipartRequest = new VolleyMultipartRequest(Request.Method.POST, url, headers, new Response.Listener<NetworkResponse>() {
                    @Override
                    public void onResponse(NetworkResponse response) {
                        String resultResponse = new String(response.data);
                        miniApp.hideProgress();
                        AddJobResponse updateResponse = (AddJobResponse) JsonUtils.toObject(resultResponse, AddJobResponse.class);
                        String message = updateResponse.getMessage();
                        if (updateResponse.getStatus() == Constants.ResponseStatus.SUCCESS) {
                            mainActivity.hideTitle();
                            mainActivity.miniApp.changeScreen(new WorkFragment_(), Constants.FragmentAction.REPLACE, R.id.main_activity_container);
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
                            json = miniApp.trimMessage(json, Constants.ResponseStatus.Status_Message);
                            if (json != null && json.equals(Constants.ResponseStatus.auth_token_not_valid_msg)) {
                                json = Constants.ResponseStatus.SESSION_EXPIRED;
                                miniApp.startNewActivity(mainActivity, LoginActivity_.class);
                            }
                        }
                        miniApp.showToast(mainActivity, json);
                    }
                }) {
                    @Override
                    protected Map<String, String> getParams() {
                        Map<String, String> params = new HashMap<>();
                        params.put(Constants.kKeyForTitle, titleEdittext.getText().toString());
                        params.put(Constants.kKeyForDescription, descriptionEdittext.getText().toString());
                        params.put(Constants.kKeyForCategoryId, jobsList.get(cId - 1).getCategory_id() + "");
                        int cId = selectedCategory.getCategory_id();
                        params.put(Constants.kKeyForCategoryId, cId + "");

                        List<CategoryIds> subCategoryList = new ArrayList<>();
                            CategoryIds subCategory = new CategoryIds();
                            subCategory.setId(sub_selectedCategory.getCategory_id());
                            subCategoryList.add(subCategory);
                            params.put("sub_category[0][id]", sub_selectedCategory.getCategory_id()+"");
                        return params;
                    }

                    @Override
                    protected Map<String, DataPart> getByteData() {
                        Map<String, DataPart> params = new HashMap<>();
                        params.put(Constants.kKeyForImageUrl, new DataPart("job_image.jpg", AppHelper.getFileDataFromDrawable(mainActivity.getBaseContext(), image.getDrawable()), "image/jpeg"));
                        return params;
                    }
                };
//                multipartRequest.setRetryPolicy(new DefaultRetryPolicy(
//                        0,
//                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
//                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                VolleySingleton.getInstance(mainActivity).addToRequestQueue(multipartRequest);
            } else {
            }
        } else {
            miniApp.showToast(mainActivity, Constants.kMsgForCategorySelection);
        }
    }

    public void uploadNews(Uri uri) {
        if (uri != null) {
            try {
                // scale the image to save on bandwidth
                Bitmap bitmap =
                        CommonUtility.scaleBitmapDown(
                                MediaStore.Images.Media.getBitmap(mainActivity.getContentResolver(), uri),
                                1200);
                setImageBitmap(bitmap);
            } catch (IOException e) {
                Toast.makeText(mainActivity, R.string.image_picker_error, Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(mainActivity, R.string.image_picker_error, Toast.LENGTH_LONG).show();
        }
    }

    public void setImageBitmap(Bitmap bitmap) {
        image.setVisibility(View.VISIBLE);
        image.getLayoutParams().height = 200;
        image.getLayoutParams().width = ViewGroup.LayoutParams.WRAP_CONTENT;
        image.setImageBitmap(bitmap);
    }

}
