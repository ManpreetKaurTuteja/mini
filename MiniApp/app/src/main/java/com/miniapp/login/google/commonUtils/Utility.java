package com.miniapp.login.google.commonUtils;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
/**
 * Created by mkaur on 11/14/17.
 */

public class Utility {
    static ProgressDialog mProgressDialog;
    public static SharedPreferences mSharedPreferences;
    private static boolean exists = false;

    public static void initializeSharedPreference(Context pContext) {
        Utility.mSharedPreferences = pContext.getSharedPreferences(null, Context.MODE_PRIVATE);
        exists = true;
    }

//    public static void showProgressDialog(Context pContext) {
//        if (mProgressDialog == null) {
//            mProgressDialog = new ProgressDialog(pContext);
//            mProgressDialog.setMessage(Constants.kDialogMsgForLogin);
//            mProgressDialog.setIndeterminate(true);
//        }
//        mProgressDialog.show();
//    }
//
//    public static void hideProgressDialog() {
//        if (mProgressDialog != null && mProgressDialog.isShowing()) {
//            mProgressDialog.hide();
//        }
//    }

    //  Access Token
    public static void setGoogleToken(String pToken) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString(Constants.kKeyForGoogleToken, (pToken));
        editor.commit();
    }

    public static String getGoogleToken(Context pContext) {
        if (mSharedPreferences == null) {
            initializeSharedPreference(pContext);
        }
        String facebookToken = mSharedPreferences.getString(Constants.kKeyForGoogleToken, null);
        return facebookToken;
    }
}
