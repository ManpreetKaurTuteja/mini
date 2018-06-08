package com.miniapp.login.google;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.miniapp.login.activities.LoginActivity;
import com.miniapp.login.google.commonUtils.Constants;
import com.miniapp.login.google.commonUtils.Utility;
import com.miniapp.login.model.LoginRequest;

/**
 * Created by mkaur on 11/14/17.
 */

public class CMGoogleLogin implements GoogleApiClient.OnConnectionFailedListener {
    public static int RC_SIGN_IN = 700;
    LoginActivity mActivity;
    private GoogleApiClient mGoogleApiClient;

    public CMGoogleLogin(LoginActivity pActivity) {
        this.mActivity = pActivity;
    }

    public void initialize() {
        /* Configure sign-in to request the user's ID, email address, and basic
 profile. ID and basic profile are included in DEFAULT_SIGN_IN. */
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleApiClient = new GoogleApiClient.Builder(mActivity)
                .enableAutoManage(mActivity, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
    }

    public void signIn() {
        mActivity.miniApp.showProgress(mActivity);
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        mActivity.startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    private void signOut() {
        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status pStatus) {

                    }
                });
    }

    private void handleSignInResult(GoogleSignInResult pResult) {
        mActivity.miniApp.hideProgress();
        if (pResult.isSuccess()) {
            // Signed in successfully, show authenticated UI.
            GoogleSignInAccount acct = pResult.getSignInAccount();
            String personName = acct.getDisplayName();
            String email = acct.getEmail();


            LoginRequest request = new LoginRequest();
            request.setEmail(email);
            request.setUsername(personName);
            request.setGender("f");
            request.setSocial_id(acct.getId());
            request.setGcm_id(acct.getId());
            mActivity.login(com.miniapp.commonUtils.Constants.ServiceConstants.LOGIN_GOOGLE_URL_EXTRA, request);
        } else {
            // Signed out, show unauthenticated UI.
            Log.e(Constants.TAG, "Failure");
        }
    }


    public void handleResponse(Intent pData) {
        GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(pData);
        handleSignInResult(result);
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult pConnectionResult) {

    }
}
