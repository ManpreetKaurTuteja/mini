package com.miniapp.home.fragments;


import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.miniapp.R;
import com.miniapp.application.MiniApp;
import com.miniapp.commonUtils.CommonUtility;
import com.miniapp.commonUtils.JsonUtils;
import com.miniapp.home.activities.MainActivity;
import com.miniapp.home.adapters.JobsAdaptar;
import com.miniapp.home.models.AddJobResponse;
import com.miniapp.home.models.JobData;
import com.miniapp.home.models.JobsResponse;
import com.miniapp.login.activities.LoginActivity_;
import com.miniapp.models.UserDetails;
import com.miniapp.signup.activities.SignupActivity;
import com.miniapp.signup.commonUtils.Constants;
import com.miniapp.signup.model.SignupResponse;
import com.miniapp.volley.AppHelper;
import com.miniapp.volley.VolleyMultipartRequest;
import com.miniapp.volley.VolleySingleton;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.App;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
@EFragment(R.layout.fragment_work)
public class WorkFragment extends Fragment {


    @App
    MiniApp miniApp;

    @ViewById(R.id.jobs_content_listview)
    public RecyclerView content_listview;
    @ViewById(R.id.noJobTextview)
    TextView noJobTextview;
    @ViewById(R.id.noNetParent)
    LinearLayout noNetParent;
    @ViewById(R.id.noNetTextview)
    TextView noNetTextview;

    private RecyclerView.LayoutManager layoutManager;
    MainActivity mainActivity;
    JobsAdaptar adaptar;
    boolean isLoadingDone;
    int currentPage = 1, lastPos = 0;
    List<JobData> jobsList = new ArrayList<>();

    public WorkFragment() {
        // Required empty public constructor
    }

    @AfterViews
    void init() {
        mainActivity = (MainActivity) this.getActivity();
        layoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        content_listview.setLayoutManager(layoutManager);
        fetchJobs();
    }

    public void showNoDataText() {
        CommonUtility.hideProgressDialog();
        noJobTextview.setVisibility(View.VISIBLE);
        content_listview.setVisibility(View.GONE);
        noNetParent.setVisibility(View.GONE);
    }

    public void showRecycler() {
        noJobTextview.setVisibility(View.GONE);
        content_listview.setVisibility(View.VISIBLE);
        noNetParent.setVisibility(View.GONE);
    }

    public void setNoNetVisibility(int value) {
        CommonUtility.hideProgressDialog();
        noNetParent.setVisibility(value);
        noJobTextview.setVisibility(View.GONE);
        if (value == View.VISIBLE) {
            content_listview.setVisibility(View.GONE);
        } else {
            content_listview.setVisibility(View.VISIBLE);
        }
    }

    @Click(R.id.retryButton)
    void retryLoading() {
        fetchJobs();
    }

    void fetchJobs() {
        if (miniApp.isConnected()) {
            isLoadingDone = false;
            miniApp.showProgress(mainActivity);
            String url = com.miniapp.commonUtils.Constants.ServiceConstants.SERVER_URL +
                    com.miniapp.commonUtils.Constants.ServiceConstants.GET_JOBS_URL_EXTRA;
            if (currentPage > 1)
                url = url + "?page=" + currentPage;
            Map<String, String> headers = new HashMap<>();
            headers.put(com.miniapp.commonUtils.Constants.kKeyForAuthToken, CommonUtility.getCurrentUserToken(mainActivity));
            VolleyMultipartRequest multipartRequest = new VolleyMultipartRequest(Request.Method.GET, url, headers, new Response.Listener<NetworkResponse>() {
                @Override
                public void onResponse(NetworkResponse response) {
                    String resultResponse = new String(response.data);
                    miniApp.hideProgress();
                    try {
                        JobsResponse jobsResponse = (JobsResponse) JsonUtils.toObject(resultResponse, JobsResponse.class);
                        String message = jobsResponse.getMessage();
                        if (jobsResponse.getStatus() == com.miniapp.commonUtils.Constants.ResponseStatus.SUCCESS) {
                            showRecycler();
                            apiResponse(jobsResponse.getJobDataList(), jobsResponse.getTotal_page());
                        } else if (message.equals(Constants.kMsgForTokenExpired) || message.equals(Constants.kMsgForYourTokenExpired)) {
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
            setNoNetVisibility(View.VISIBLE);
        }
    }

    void apiResponse(final List<JobData> jobsList, final int totalPages) {
//        if (jobsList != null && jobsList.size() > 0) {
//            showRecycler();
//            JobsAdaptar adaptar = new JobsAdaptar(jobsList, mainActivity);
//            content_listview.setAdapter(adaptar);
//        } else
//            showNoDataText();


        lastPos = this.jobsList.size();
        if (jobsList != null) {
            isLoadingDone = true;
            for (int i = 0; i < jobsList.size(); i++) {
                this.jobsList.add(jobsList.get(i));
            }
            if (adaptar == null || lastPos == 0) {
                adaptar = new JobsAdaptar(jobsList, mainActivity);
                content_listview.setAdapter(adaptar);
                adaptar.notifyDataSetChanged();
            } else {
                adaptar.updateAdaptar(this.jobsList, content_listview, lastPos);
            }
            content_listview.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    LinearLayoutManager layoutManager = LinearLayoutManager.class.cast(recyclerView.getLayoutManager());
                    int lastVisible = layoutManager.findLastVisibleItemPosition();
                    boolean endHasBeenReached = lastVisible + 1 >= jobsList.size();
                    if (isLoadingDone && totalPages > currentPage && jobsList.size() > 0 && endHasBeenReached) {
                        currentPage += 1;

                        fetchJobs();
                    }
                }
            });
        } else {
            if (lastPos == 0)
                miniApp.showToast(mainActivity, "No jobs found.");
        }
    }
}
