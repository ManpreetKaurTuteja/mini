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
import android.widget.TextView;

import com.miniapp.R;
import com.miniapp.commonUtils.CommonUtility;
import com.miniapp.home.Utils.Constants;
import com.miniapp.home.activities.MainActivity;
import com.miniapp.home.adapters.JobsCategorySelectionAdapter;
import com.miniapp.home.adapters.JobsSubCategorySelectionAdapter;
import com.miniapp.home.fragments.MeFragment;
import com.miniapp.home.models.JobsCategory;
import com.miniapp.home.models.UserJobSubCategory;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mac
 */

public class JobSubCategoriesDailog {
    MainActivity mActivity;
    static Dialog mDialog;

    public JobSubCategoriesDailog(MainActivity pActivity) {
        mActivity = pActivity;
    }

    public void show(final Fragment fragment, String title, List<JobsCategory> jobsCategoryList) {
        if (jobsCategoryList != null && jobsCategoryList.size() > 0) {
            mDialog = new Dialog(mActivity, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
            LayoutInflater layoutInflater = (LayoutInflater) mActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            RelativeLayout layout = (RelativeLayout) layoutInflater.inflate(R.layout.job_sub_categories_items_layout, null);
            mDialog.setContentView(layout);
            mDialog.setCanceledOnTouchOutside(false);
            mDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            mDialog.show();
//        mDialog.setCancelable(false);
            final ListView listView = (ListView) layout.findViewById(R.id.pages_listview);

            final JobsSubCategorySelectionAdapter adapter = new JobsSubCategorySelectionAdapter(mActivity, listView, jobsCategoryList);
            listView.setAdapter(adapter);
            TextView categoryTitle = (TextView) layout.findViewById(R.id.category_title);
            categoryTitle.setText(title);
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
                    List<UserJobSubCategory> sJobs = adapter.getselectedJobs();
                    if (sJobs != null && sJobs.size() > 0) {
                        CommonUtility.setSelectedSubJobsCategory(sJobs, mActivity);
                        if (fragment instanceof MeFragment){
                            ((MeFragment)fragment).setCategoryText(CommonUtility.getSelectedSubJobsCategory(mActivity));
                        }
                        mDialog.dismiss();
                    } else {
                        mActivity.miniApp.showToast(mActivity, Constants.kMsgForOneJobCategory);
                    }
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
