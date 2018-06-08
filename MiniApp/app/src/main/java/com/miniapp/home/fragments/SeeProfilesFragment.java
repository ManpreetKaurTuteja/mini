package com.miniapp.home.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.miniapp.R;
import com.miniapp.application.MiniApp;
import com.miniapp.commonUtils.CommonUtility;
import com.miniapp.commonUtils.JsonUtils;
import com.miniapp.home.activities.MainActivity;
import com.miniapp.home.adapters.UsersProfilesAdapter;
import com.miniapp.home.models.AllUsersProfiles;
import com.miniapp.home.models.JobsResponse;
import com.miniapp.login.activities.LoginActivity_;
import com.miniapp.models.UserDetails;
import com.miniapp.signup.commonUtils.Constants;
import com.miniapp.volley.VolleyMultipartRequest;
import com.miniapp.volley.VolleySingleton;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.App;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
@EFragment(R.layout.fragment_see_profiles)
public class SeeProfilesFragment extends Fragment {

    @App
    MiniApp miniApp;
    private RecyclerView.LayoutManager layoutManager;
    MainActivity mainActivity;
    @ViewById(R.id.content_listview)
    RecyclerView content_listview;
    UsersProfilesAdapter adaptar;
    boolean isLoadingDone;
    int currentPage = 1, lastPos = 0;
    List<UserDetails> mUsersList = new ArrayList<>();

    public SeeProfilesFragment() {
        // Required empty public constructor
    }

    @AfterViews
    void init() {
        mainActivity = (MainActivity) this.getActivity();
        layoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        content_listview.setLayoutManager(layoutManager);
        fetchProfiles();
    }

    void fetchProfiles() {
        if (miniApp.isConnected()) {
            isLoadingDone = false;
            miniApp.showProgress(mainActivity);
            String url = com.miniapp.commonUtils.Constants.ServiceConstants.SERVER_URL +
                    com.miniapp.commonUtils.Constants.ServiceConstants.FETCH_FREELANCERS_URL_EXTRA;
            if (currentPage > 0)
                url = url + "?page=" + currentPage;
            Map<String, String> headers = new HashMap<>();
            headers.put(com.miniapp.commonUtils.Constants.kKeyForAuthToken, CommonUtility.getCurrentUserToken(mainActivity));
            VolleyMultipartRequest multipartRequest = new VolleyMultipartRequest(Request.Method.GET, url, headers, new Response.Listener<NetworkResponse>() {
                @Override
                public void onResponse(NetworkResponse response) {
                    String resultResponse = new String(response.data);
                    miniApp.hideProgress();
                    try {
                        AllUsersProfiles profilesResponse = (AllUsersProfiles) JsonUtils.toObject(resultResponse, AllUsersProfiles.class);
                        String message = profilesResponse.getMessage();
                        if (profilesResponse.getStatus() == com.miniapp.commonUtils.Constants.ResponseStatus.SUCCESS) {
                            apiResponse(profilesResponse.getUsers_list(), profilesResponse.getTotal_page());
                        } else if (message.equals(Constants.kMsgForTokenExpired)) {
                            message = Constants.kMsgForSessionExpired;
                            miniApp.startNewActivity(mainActivity, LoginActivity_.class);
                            miniApp.showToast(mainActivity, message);
                        }

                    } catch (Exception e) {

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
                        if (json != null && json.equals(Constants.kMsgForTokenExpired)) {
                            json = Constants.kMsgForSessionExpired;
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

    void apiResponse(final List<UserDetails> usersList, final int totalPages) {
//        if (usersList != null && usersList.size() > 0) {
//            adaptar = new UsersProfilesAdapter(usersList, mainActivity);
//            content_listview.setAdapter(adaptar);
//        }


        lastPos = this.mUsersList.size();
        if (usersList != null && usersList.size() > 0) {
            isLoadingDone = true;
            for (int i = 0; i < usersList.size(); i++) {
                this.mUsersList.add(usersList.get(i));
            }
            if (adaptar == null || lastPos == 0) {
                adaptar = new UsersProfilesAdapter(usersList, mainActivity);
                content_listview.setAdapter(adaptar);
                adaptar.notifyDataSetChanged();
            } else {
                adaptar.updateAdaptar(this.mUsersList, content_listview, lastPos);
            }
            content_listview.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    LinearLayoutManager layoutManager = LinearLayoutManager.class.cast(recyclerView.getLayoutManager());
                    int lastVisible = layoutManager.findLastVisibleItemPosition();
                    boolean endHasBeenReached = lastVisible + 1 >= usersList.size();
                    if (isLoadingDone && totalPages > currentPage && usersList.size() > 0 && endHasBeenReached) {
                        currentPage += 1;
                        fetchProfiles();
                    }
                }
            });
        } else {
            if (lastPos == 0)
                miniApp.showToast(mainActivity, "No updates for this category");
        }
    }
}
