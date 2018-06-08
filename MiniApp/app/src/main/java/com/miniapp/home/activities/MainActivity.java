package com.miniapp.home.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.SearchView;
import android.widget.TextView;

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
import com.miniapp.home.Utils.MLogout;
import com.miniapp.home.adapters.JobsAdaptar;
import com.miniapp.home.fragments.AboutUsFragment;
import com.miniapp.home.fragments.AboutUsFragment_;
import com.miniapp.home.fragments.AddJobFragment;
import com.miniapp.home.fragments.AddJobFragment_;
import com.miniapp.home.fragments.ContactUsFragment;
import com.miniapp.home.fragments.ContactUsFragment_;
import com.miniapp.home.fragments.MeFragment;
import com.miniapp.home.fragments.MeFragment_;
import com.miniapp.home.fragments.ProposalFragment_;
import com.miniapp.home.fragments.SeeProfilesFragment;
import com.miniapp.home.fragments.SeeProfilesFragment_;
import com.miniapp.home.fragments.SettingsFragment;
import com.miniapp.home.fragments.SettingsFragment_;
import com.miniapp.home.fragments.WalletFragment;
import com.miniapp.home.fragments.WalletFragment_;
import com.miniapp.home.fragments.WorkFragment;
import com.miniapp.home.fragments.WorkFragment_;
import com.miniapp.home.models.JobData;
import com.miniapp.home.models.JobsResponse;
import com.miniapp.login.activities.LoginActivity_;
import com.miniapp.models.UserDetails;
import com.miniapp.volley.VolleyMultipartRequest;
import com.miniapp.volley.VolleySingleton;
import com.sasidhar.smaps.payumoney.PayUMoney_Constants;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.App;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

@EActivity(R.layout.activity_main)
public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    @App
    public MiniApp miniApp;
    CircleImageView userImage;
    TextView username, userType;
    @ViewById(R.id.fragment_title)
    TextView fragmentTitle;
    @ViewById(R.id.search_view)
    SearchView searchView;
    public Toolbar toolbar;
    NavigationView navigationView;
    DrawerLayout drawer;
    ActionBarDrawerToggle toggle;
    int backPressedCount = 0;

    @AfterViews
    void init() {
        miniApp.setActivity(this);
//        CommonUtility.setStatusBarColor(this);
        // Toolbar customization
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View navigationHeaderView = navigationView.getHeaderView(0);
        userImage = (CircleImageView) navigationHeaderView.findViewById(R.id.headeimageView);
        username = (TextView) navigationHeaderView.findViewById(R.id.user_name);
        userType = (TextView) navigationHeaderView.findViewById(R.id.user_type);

        navigationHeaderView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setTitle("Profile");
                miniApp.changeScreen(new MeFragment_(), Constants.FragmentAction.REPLACE, R.id.main_activity_container);
                drawer.closeDrawer(GravityCompat.START);
            }
        });


        addHamburger();
        miniApp.changeScreen(new WorkFragment_(), Constants.FragmentAction.ADD, R.id.main_activity_container);


        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                search(s);
                return false;
            }
        });
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        backPressedCount = 0;
        int id = item.getItemId();
        Fragment fragment = null;
        if (id == R.id.find_work) {
            hideTitle();
            fragment = new WorkFragment_();
        } else if (id == R.id.add_job) {
            setTitle("Add Job");
            fragment = new AddJobFragment_();
        } else if (id == R.id.proposals) {
            setTitle("Proposals");
            fragment = new ProposalFragment_();
        } else if (id == R.id.see_profiles) {
            setTitle("Other Users");
            fragment = new SeeProfilesFragment_();
        } else if (id == R.id.settings) {
            setTitle(com.miniapp.signup.commonUtils.Constants.kKeyForSettings);
            fragment = new SettingsFragment_();
        } else if (id == R.id.about) {
            setTitle("About Us");
            fragment = new AboutUsFragment_();
        }else if (id == R.id.wallet) {
            setTitle("Wallet");
            fragment = new WalletFragment_();
        } else if (id == R.id.logout) {
            CommonUtility.showDialog(this, Constants.kMsgForConfirmLogout);
//            MLogout logout = new MLogout(this);
//            logout.performLogout();
        }
        if (fragment != null) {
//            if (fragment instanceof WorkFragment && !(miniApp.getCurrentFragment() instanceof WorkFragment)) {
//                miniApp.changeScreen(fragment, Constants.FragmentAction.ADD, R.id.main_activity_container);
//            } else {
//                miniApp.changeScreen(miniApp.getCurrentFragment(), Constants.FragmentAction.REMOVE, R.id.main_activity_container);
            miniApp.changeScreen(fragment, Constants.FragmentAction.REPLACE, R.id.main_activity_container);
//            }
        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void setTitle(String title) {
        fragmentTitle.setVisibility(View.VISIBLE);
        searchView.setVisibility(View.GONE);
        fragmentTitle.setText(title);
    }

    public void hideTitle() {
        fragmentTitle.setVisibility(View.GONE);
        searchView.setVisibility(View.VISIBLE);
        fragmentTitle.setText("");
    }

    @Override
    protected void onResume() {
        super.onResume();
        miniApp.setActivity(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == Constants.GALLERY_IMAGE_REQUEST && resultCode == RESULT_OK && data != null) {
            Fragment fragment = miniApp.getCurrentFragment();
            if (fragment instanceof AddJobFragment)
                ((AddJobFragment) fragment).uploadNews(data.getData());
            else if (fragment instanceof MeFragment)
                ((MeFragment) fragment).uploadImage(data.getData());
        } else if (requestCode == Constants.CAMERA_IMAGE_REQUEST && resultCode == RESULT_OK) {
            Bitmap bitmap = (Bitmap) data.getExtras().get("data");
            Fragment fragment = miniApp.getCurrentFragment();
            if (fragment instanceof AddJobFragment)
                ((AddJobFragment) fragment).setImageBitmap(bitmap);
            else if (fragment instanceof MeFragment)
                ((MeFragment) fragment).setImageBitmap(bitmap);
        }else if (requestCode == PayUMoney_Constants.PAYMENT_REQUEST) {
            switch (resultCode) {
                case RESULT_OK:
                    miniApp.showToast(this, "Payment Success.");
                    break;
                case RESULT_CANCELED:
                    miniApp.showToast(this, "Payment Cancelled | Failed.");
                    break;
            }

        }
    }

    void addHamburger() {
        toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close) {
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                // Do whatever you want here

            }

            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                UserDetails userDetails = CommonUtility.getCurrentUserDetails(MainActivity.this);
                String user_name = "", profile_image = "", user_type = "";
                if (userDetails != null) {
                    user_name = userDetails.getUsername();
                    profile_image = userDetails.getProfile_image();
                    user_type = (userDetails.getUser_type() == 2) ? com.miniapp.signup.commonUtils.Constants.UserType.FREELANCER :
                            ((userDetails.getUser_type() == 1) ? com.miniapp.signup.commonUtils.Constants.UserType.HIRE :
                                    "");
                    Glide.with(MainActivity.this).load(profile_image).diskCacheStrategy(DiskCacheStrategy.NONE).
                            skipMemoryCache(true).into(userImage);
                }
                username.setText(user_name);
                userType.setText(user_type);
            }
        };
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        drawer.setDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {

            }

            @Override
            public void onDrawerOpened(View drawerView) {
                UserDetails userDetails = CommonUtility.getCurrentUserDetails(MainActivity.this);
                String user_name = "", profile_image = "", user_type = "";
                if (userDetails != null) {
                    user_name = userDetails.getUsername();
                    profile_image = userDetails.getProfile_image();
                    user_type = (userDetails.getUser_type() == 2) ? com.miniapp.signup.commonUtils.Constants.UserType.FREELANCER :
                            ((userDetails.getUser_type() == 1) ? com.miniapp.signup.commonUtils.Constants.UserType.HIRE :
                                    "");
                    Glide.with(MainActivity.this).load(profile_image).diskCacheStrategy(DiskCacheStrategy.NONE).
                            skipMemoryCache(true).into(userImage);
                }
                username.setText(user_name);
                userType.setText(user_type);
            }

            @Override
            public void onDrawerClosed(View drawerView) {
            }

            @Override
            public void onDrawerStateChanged(int newState) {
                // Hide keyboard
                final InputMethodManager imm = (InputMethodManager) MainActivity.this.getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(drawer.getWindowToken(), 0);
            }
        });
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        toggle.syncState();
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else if (miniApp.getCurrentFragment() instanceof AboutUsFragment) {
            if (((AboutUsFragment) (miniApp.getCurrentFragment())).canWebviewGoBack()) {
            } else {
                goToBackground();
            }
        } else if (miniApp.getCurrentFragment() instanceof ContactUsFragment) {
            setTitle("Settings");
            miniApp.changeScreen(miniApp.getCurrentFragment(), Constants.FragmentAction.REMOVE, R.id.main_activity_container);
        } else {
            goToBackground();
        }
    }

    void goToBackground() {
        if (!(miniApp.getCurrentFragment() instanceof WorkFragment)) {
            hideTitle();
            miniApp.changeScreen(new WorkFragment_(), Constants.FragmentAction.REPLACE, R.id.main_activity_container);
        } else {
            backPressedCount++;
            if (backPressedCount == 1)
                miniApp.showToast(this, Constants.kMsgForExit);
            else if (backPressedCount == 2)
                finish();
        }
    }

    void search(String ch){
        if (miniApp.isConnected()) {
            miniApp.showProgress(this);
            String url = com.miniapp.commonUtils.Constants.ServiceConstants.SERVER_URL +
                    Constants.ServiceConstants.SEARCH_URL_EXTRA+ch;

            Map<String, String> headers = new HashMap<>();
            headers.put(com.miniapp.commonUtils.Constants.kKeyForAuthToken, CommonUtility.getCurrentUserToken(MainActivity.this));
            VolleyMultipartRequest multipartRequest = new VolleyMultipartRequest(Request.Method.GET, url, headers, new Response.Listener<NetworkResponse>() {
                @Override
                public void onResponse(NetworkResponse response) {
                    String resultResponse = new String(response.data);
                    miniApp.hideProgress();
                    try {
                        JobsResponse jobsResponse = (JobsResponse) JsonUtils.toObject(resultResponse, JobsResponse.class);
                        String message = jobsResponse.getMessage();
                        if (jobsResponse.getStatus() == com.miniapp.commonUtils.Constants.ResponseStatus.SUCCESS) {
//                            apiResponse(jobsResponse.getJobDataList(), jobsResponse.getTotal_page());
                        } else if (message.equals(com.miniapp.signup.commonUtils.Constants.kMsgForTokenExpired)) {
                            message = com.miniapp.signup.commonUtils.Constants.kMsgForSessionExpired;
                            miniApp.startNewActivity(MainActivity.this, LoginActivity_.class);
                            miniApp.showToast(MainActivity.this, message);
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
                        if (json != null && json.equals(com.miniapp.signup.commonUtils.Constants.kMsgForTokenExpired)) {
                            json = com.miniapp.signup.commonUtils.Constants.kMsgForSessionExpired;
                            miniApp.startNewActivity(MainActivity.this, LoginActivity_.class);
                        }
                    }
                    miniApp.showToast(MainActivity.this, json);
                }
            });
            VolleySingleton.getInstance(MainActivity.this).addToRequestQueue(multipartRequest);

        } else {
//            setNoNetVisibility(View.VISIBLE);
        }
    }


//    void apiResponse(final List<JobData> jobsList, final int totalPages) {
//        if (jobsList != null && jobsList.size() > 0) {
////            showRecycler();
//            JobsAdaptar adaptar = new JobsAdaptar(jobsList, mainActivity);
//            content_listview.setAdapter(adaptar);
//        }
//        else
//            showNoDataText();
//    }
}
