package com.miniapp.commonUtils;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.provider.MediaStore;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.miniapp.R;
import com.miniapp.home.Utils.MLogout;
import com.miniapp.home.activities.MainActivity;
import com.miniapp.home.models.JobsCategory;
import com.miniapp.home.models.OtherImages;
import com.miniapp.home.models.UserJobSubCategory;
import com.miniapp.home.models.UserOtherImages;
import com.miniapp.models.UserDetails;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by manpreet
 */
public class CommonUtility {
    public static SharedPreferences sharedPreferences;
    private static boolean mExists = false;
    public static AlertDialog alertDialog;
    public static ProgressDialog mProgressDialog;

    public static void initializeSharedPreference(Context context) {
        CommonUtility.sharedPreferences = context.getSharedPreferences(null, Context.MODE_PRIVATE);
        mExists = true;
    }

    //Device Token
    public static String getDeviceID(Context context) {
        if (sharedPreferences == null) {
            initializeSharedPreference(context);
        }
        return sharedPreferences.getString(Constants.kKeyForDeviceToken, null);
    }

    public static void setDeviceID(String deviceToken, Context context) {
        if (sharedPreferences == null) {
            initializeSharedPreference(context);
        }

        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString(Constants.kKeyForDeviceToken, deviceToken);
        editor.commit();

    }

    //Device Type
    public static int getDeviceType(Context context) {
        if (sharedPreferences == null) {
            initializeSharedPreference(context);
        }
        return sharedPreferences.getInt(Constants.kKeyForDeviceType, 1);
    }

    public static void setDeviceType(Context context) {
        if (sharedPreferences == null) {
            initializeSharedPreference(context);
        }
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(Constants.kKeyForDeviceType, 1);
        editor.commit();
    }

    // CurrentUserDetails (My details)
    public static UserDetails getCurrentUserDetails(Context context) {
        if (sharedPreferences == null) {
            initializeSharedPreference(context);
        }
        UserDetails userDetails = (UserDetails) JsonUtils.toObject(sharedPreferences.getString(Constants.kKeyForUserDetails, null), UserDetails.class);
        return userDetails;
    }

    public static void setCurrentUserDetails(UserDetails userDetails, Context context) {
        if (sharedPreferences == null) {
            initializeSharedPreference(context);
        }
        SharedPreferences.Editor editor = sharedPreferences.edit();
        String jsonUserDetails = JsonUtils.toString(userDetails);
        editor.putString(Constants.kKeyForUserDetails, jsonUserDetails);
        editor.commit();

    }

    public static List<JobsCategory> getCategories(Context context) {
        if (sharedPreferences == null) {
            initializeSharedPreference(context);
        }
        List<JobsCategory> userDetails = (List<JobsCategory>) JsonUtils.toObject(sharedPreferences.getString(Constants.kKeyForJobsCategories, null), JobsCategory.class);
        return userDetails;
    }

    public static void setCategories(List<JobsCategory> userDetails, Context context) {
        if (sharedPreferences == null) {
            initializeSharedPreference(context);
        }
        SharedPreferences.Editor editor = sharedPreferences.edit();
        String jsonUserDetails = JsonUtils.toString(userDetails);
        editor.putString(Constants.kKeyForJobsCategories, jsonUserDetails);
        editor.commit();

    }

    public static List<UserOtherImages> getUserOtherImages(Context context) {
        if (sharedPreferences == null) {
            initializeSharedPreference(context);
        }
        OtherImages otherImages = (OtherImages) JsonUtils.toObject(sharedPreferences.getString(com.miniapp.home.Utils.Constants.kKeyForUserOtherImage, null), OtherImages.class);
        List<UserOtherImages> userDetails = otherImages.getUserOtherImages();
        if (userDetails == null)
            userDetails = new ArrayList<>();
        return userDetails;
    }

    public static void setUserOtherImages(List<UserOtherImages> imagesList, Context context) {
        if (sharedPreferences == null) {
            initializeSharedPreference(context);
        }
        SharedPreferences.Editor editor = sharedPreferences.edit();

        OtherImages otherImages = new OtherImages();
        otherImages.setUserOtherImages(imagesList);
        String jsonUserDetails = JsonUtils.toString(otherImages);
        editor.putString(com.miniapp.home.Utils.Constants.kKeyForUserOtherImage, jsonUserDetails);
        editor.commit();
    }

    // CurrentUserId (my id)
    public static String getCurrentUserId(Context context) {
        if (sharedPreferences == null) {
            initializeSharedPreference(context);
        }
        return sharedPreferences.getString(Constants.kKeyForFacebookId, null);
    }

    public static void setCurrentUserId(String userId, Context context) {
        if (sharedPreferences == null) {
            initializeSharedPreference(context);
        }
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(Constants.kKeyForFacebookId, userId);
        editor.commit();

    }

    // CurrentUser auth token
    public static String getCurrentUserToken(Context context) {
        if (sharedPreferences == null) {
            initializeSharedPreference(context);
        }
        return sharedPreferences.getString(Constants.kKeyForAuthToken, null);
    }

    public static void setCurrentUserToken(String token, Context context) {
        if (sharedPreferences == null) {
            initializeSharedPreference(context);
        }
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(Constants.kKeyForAuthToken, token);
        editor.commit();

    }

    public static JobsCategory getSelectedJobsCategory(Context context) {
        if (sharedPreferences == null) {
            initializeSharedPreference(context);
        }
        String pagesDataString = sharedPreferences.getString(Constants.kKeyForSelectedJobsCategory, null);
        JobsCategory jobsCategory = (JobsCategory) JsonUtils.toObject(pagesDataString, JobsCategory.class);
        return jobsCategory;
    }

    public static void setSelectedJobCategory(JobsCategory pagesData, Context context) {
        if (sharedPreferences == null) {
            initializeSharedPreference(context);
        }
        SharedPreferences.Editor editor = sharedPreferences.edit();
        String jsonPagesData = JsonUtils.toString(pagesData);
        editor.putString(Constants.kKeyForSelectedJobsCategory, jsonPagesData);
        editor.commit();
    }

    public static List<UserJobSubCategory> getSelectedSubJobsCategory(Context context) {
        if (sharedPreferences == null) {
            initializeSharedPreference(context);
        }
        String pagesDataString = sharedPreferences.getString(Constants.kKeyForSelectedSubJobsCategories, null);
        List<UserJobSubCategory> pagesDataList = new ArrayList<>();
        if (pagesDataString != null) {
            try {
                JSONArray pageArray = new JSONArray(pagesDataString);
                for (int i = 0; i < pageArray.length(); i++) {
                    UserJobSubCategory jobsCategory = new UserJobSubCategory();
                    JSONObject obj = pageArray.getJSONObject(i);
                    jobsCategory.setSubcategory_id(obj.getInt("subcategory_id"));
                    jobsCategory.setSubcategory_title(obj.getString("subcategory_title"));
                    pagesDataList.add(jobsCategory);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return pagesDataList;
    }

    public static void setSelectedSubJobsCategory(List<UserJobSubCategory> pagesData, Context context) {
        if (sharedPreferences == null) {
            initializeSharedPreference(context);
        }
        SharedPreferences.Editor editor = sharedPreferences.edit();
        String jsonPagesData = JsonUtils.toString(pagesData);
        editor.putString(Constants.kKeyForSelectedSubJobsCategories, jsonPagesData);
        editor.commit();
    }

    public static void setStatusBarColor(AppCompatActivity activity) {
        if (android.os.Build.VERSION.SDK_INT >= 21) {
            Window window = activity.getWindow();
            window.setStatusBarColor(ContextCompat.getColor(activity, R.color.app_color));
        }
    }

    public static void showDialog(final Context context, final String message) {
        if (context != null) {
            if (alertDialog == null || !alertDialog.isShowing()) {
                alertDialog = new AlertDialog.Builder(context).create();
                alertDialog.setTitle(context.getResources().getString(R.string.app_name));
                alertDialog.setMessage(message);
                alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, Constants.kKeyForOk,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                if (message.equalsIgnoreCase(Constants.kMsgForConfirmLogout)) {
                                    MLogout mLogout = new MLogout((MainActivity) context);
                                    mLogout.performLogout();
                                }
                                alertDialog.dismiss();
                            }
                        });
                alertDialog.setButton(DialogInterface.BUTTON_NEGATIVE, Constants.kKeyForCancel,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                alertDialog.dismiss();
                            }
                        });
                alertDialog.show();
            }
        }
    }

    public static boolean isConnected(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();
        if (!isConnected)
            Toast.makeText(context, context.getResources().getString(R.string.no_internet_connection), Toast.LENGTH_SHORT).show();
        return isConnected;
    }

    public static void showProgressDialog(Context context, String... progressMessage) {
        if (mProgressDialog == null || !mProgressDialog.isShowing() && isConnected(context)) {
//            if (progressMessage.length == 0)
//                mProgressDialog = new ProgressDialog(context, R.style.MyTheme);
//            else
            mProgressDialog = new ProgressDialog(context);
            mProgressDialog.setCancelable(false);
            mProgressDialog.setMessage(progressMessage.length == 0 ? "" : progressMessage[0]);
            mProgressDialog.show();
        }
    }

    public static void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
            mProgressDialog = null;
        }
    }

    public static void setLoggedIn(boolean value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(Constants.kKeyForIsLoggedIn, value);
        editor.commit();
    }

    public static boolean isLoggedIn() {
        return sharedPreferences.getBoolean(Constants.kKeyForIsLoggedIn, false);
    }

    public static void openGalleryOrCamera(final Context pContext) {
        AlertDialog.Builder builder = new AlertDialog.Builder(pContext);
        builder
                .setMessage("Select")
                .setPositiveButton("Gallery", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startGalleryChooser(pContext);
                    }
                })
                .setNegativeButton("Camera", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startCamera(pContext);
                    }
                });
        builder.create().show();
    }

    public static void startGalleryChooser(Context pContext) {
        if (PermissionUtils.requestPermission(((Activity) pContext), Constants.GALLERY_PERMISSIONS_REQUEST, android.Manifest.permission.READ_EXTERNAL_STORAGE)) {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            ((Activity) pContext).startActivityForResult(Intent.createChooser(intent, "Select a photo"),
                    Constants.GALLERY_IMAGE_REQUEST);
        }
    }

    public static void startCamera(Context pContext) {
        if (PermissionUtils.requestPermission(
                ((Activity) pContext),
                Constants.CAMERA_PERMISSIONS_REQUEST,
                android.Manifest.permission.READ_EXTERNAL_STORAGE,
                android.Manifest.permission.CAMERA)) {
            Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            ((Activity) pContext).startActivityForResult(cameraIntent, Constants.CAMERA_IMAGE_REQUEST);
        }
    }

    public static Bitmap scaleBitmapDown(Bitmap bitmap, int maxDimension) {

        int originalWidth = bitmap.getWidth();
        int originalHeight = bitmap.getHeight();
        int resizedWidth = maxDimension;
        int resizedHeight = maxDimension;

        if (originalHeight > originalWidth) {
            resizedHeight = maxDimension;
            resizedWidth = (int) (resizedHeight * (float) originalWidth / (float) originalHeight);
        } else if (originalWidth > originalHeight) {
            resizedWidth = maxDimension;
            resizedHeight = (int) (resizedWidth * (float) originalHeight / (float) originalWidth);
        } else if (originalHeight == originalWidth) {
            resizedHeight = maxDimension;
            resizedWidth = maxDimension;
        }
        return Bitmap.createScaledBitmap(bitmap, resizedWidth, resizedHeight, false);
    }
//    public static void showInterstitialAd(Context pContext) {
//        Random r = new Random();
//        int num = r.nextInt(1000 - 100) + 5;
//        if (num % 9 == 0) {
//            final InterstitialAd mInterstitialAd = new InterstitialAd(pContext);
//            mInterstitialAd.setAdUnitId(pContext.getResources().getString(R.string.ADMOB_interstial_ADD_UNIT_ID));
//            mInterstitialAd.loadAd(new AdRequest.Builder().build());
//
//            mInterstitialAd.setAdListener(new AdListener() {
//                @Override
//                public void onAdLoaded() {
//                    // Code to be executed when an ad finishes loading.
//                    mInterstitialAd.show();
//                }
//
//                @Override
//                public void onAdFailedToLoad(int errorCode) {
//                    // Code to be executed when an ad request fails.
//                    Log.d("TAG", "The interstitial failed.");
//                }
//
//                @Override
//                public void onAdOpened() {
//                    // Code to be executed when the ad is displayed.
//                }
//
//                @Override
//                public void onAdLeftApplication() {
//                    // Code to be executed when the user has left the app.
//                }
//
//                @Override
//                public void onAdClosed() {
//                    // Code to be executed when when the interstitial ad is closed.
//                }
//            });
//        }
//    }

    public static void displayImage(Context context, ImageView imageView) {
        UserDetails userDetails = getCurrentUserDetails(context);
        if (userDetails != null) {
            String profileURL = userDetails.getProfile_image_thumb();
            if (profileURL != null && !profileURL.isEmpty()
                    && !profileURL.equals("null")) {
                Glide.with(context).load(profileURL).into(imageView);
            } else {
                imageView.setImageDrawable(context.getResources().getDrawable(R.drawable.avatar));
            }
        }
    }

    public static void displayName(Context context, TextView view) {
        UserDetails userDetails = getCurrentUserDetails(context);
        if (userDetails != null) {
            String userName = userDetails.getUsername();
            if (userName != null && !userName.isEmpty()
                    && !userName.equals("null")) {
                view.setText(userName);
            } else {
                view.setText("");
            }
        }
    }
}
