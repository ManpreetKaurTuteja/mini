package com.miniapp.signup.commonUtils;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;

import com.miniapp.R;

/**
 * Created by mkaur on 11/14/17.
 */

public class Utility {
    static ProgressDialog mProgressDialog;
    static AlertDialog mAlertDialog;
    public static SharedPreferences sharedPreferences;
    private static boolean exists = false;

    public static void initializeSharedPreference(Context context) {
        Utility.sharedPreferences = context.getSharedPreferences(null, Context.MODE_PRIVATE);
        exists = true;
    }

    public static void showProgressDialog(Context context) {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(context);
            mProgressDialog.setMessage(Constants.kDialogMsgForSignup);
            mProgressDialog.setIndeterminate(true);
        }
        mProgressDialog.show();
    }

    public static void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.hide();
        }
    }
    public static void showDialog(Context context, String message) {
        if (context != null) {
            if (mAlertDialog == null || !mAlertDialog.isShowing()) {
                mAlertDialog = new AlertDialog.Builder(context).create();
                mAlertDialog.setTitle(context.getResources().getString(R.string.app_name));
                mAlertDialog.setMessage(message);
                mAlertDialog.setButton(DialogInterface.BUTTON_POSITIVE, com.miniapp.commonUtils.Constants.kKeyForOk,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        });
                mAlertDialog.show();
            }
        }
    }
}
