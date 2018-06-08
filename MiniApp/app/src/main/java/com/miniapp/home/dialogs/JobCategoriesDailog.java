package com.miniapp.home.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.miniapp.R;
import com.miniapp.commonUtils.CommonUtility;
import com.miniapp.home.Utils.Constants;
import com.miniapp.home.activities.MainActivity;
import com.miniapp.home.adapters.JobsCategorySelectionAdapter;
import com.miniapp.home.fragments.MeFragment;
import com.miniapp.home.fragments.MeFragment_;
import com.miniapp.home.models.JobsCategory;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mac
 */

public class JobCategoriesDailog {
    MainActivity mActivity;
    static Dialog mDialog;

    public JobCategoriesDailog(MainActivity pActivity) {
        mActivity = pActivity;
    }

    public void show(List<JobsCategory> jobsCategoryList, final Fragment fragment) {
        if (jobsCategoryList != null && jobsCategoryList.size() > 0) {
            mDialog = new Dialog(mActivity, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
            LayoutInflater layoutInflater = (LayoutInflater) mActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            RelativeLayout layout = (RelativeLayout) layoutInflater.inflate(R.layout.job_categories_items_layout, null);
            mDialog.setContentView(layout);
            mDialog.setCanceledOnTouchOutside(false);
            mDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            mDialog.show();
//        mDialog.setCancelable(false);
            final ListView listView = (ListView) layout.findViewById(R.id.pages_listview);

            final JobsCategorySelectionAdapter adapter = new JobsCategorySelectionAdapter(mActivity, listView, jobsCategoryList);
            listView.setAdapter(adapter);
            Button cancelButton = (Button) layout.findViewById(R.id.cancel_button);
            cancelButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mDialog.dismiss();
                }
            });

            Button saveButton = (Button) layout.findViewById(R.id.save_button);
            saveButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    CommonUtility.setSelectedJobCategory(adapter.getselectedJob(), mActivity);
//                    if (fragment instanceof MeFragment){
//                        ((MeFragment)fragment).setCategoryText(CommonUtility.getSelectedSubJobsCategory(mActivity));
//                    }
                    mDialog.dismiss();
                }
            });
        } else {
            mActivity.miniApp.showToast(mActivity, "No job categories found.");
        }
    }

    public boolean isShowingDialog() {
        if (mDialog == null || !mDialog.isShowing())
            return false;
        return true;
    }
}
