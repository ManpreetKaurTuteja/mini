package com.miniapp.home.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.miniapp.R;
import com.miniapp.commonUtils.CommonUtility;
import com.miniapp.home.Utils.JobSubCategories;
import com.miniapp.home.activities.MainActivity;
import com.miniapp.home.models.JobsCategory;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mac on 14/04/18.
 */

public class JobsCategorySelectionAdapter extends BaseAdapter {
    Context mContext;
    ListView mListView;
    List<JobsCategory> mPagesArray;
    JobsCategory selectedJobs;
    RadioButton mCurrentlyCheckedRB;

    public JobsCategorySelectionAdapter(Context context, ListView listView, List<JobsCategory> pPagesArray) {
        mContext = context;
        this.mListView = listView;
        this.mPagesArray = pPagesArray;
        this.selectedJobs = CommonUtility.getSelectedJobsCategory(mContext);
    }

    @Override
    public int getCount() {
        return mPagesArray.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(final int p, View pView, ViewGroup pViewGroup) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        final ViewGroup layout = (ViewGroup) inflater.inflate(R.layout.selecetd_job_categories_items_layout, pViewGroup, false);
        final JobsCategory selectedJobsData = mPagesArray.get(p);
        TextView nameTextview = (TextView) layout.findViewById(R.id.job_name_textview);
        nameTextview.setText(selectedJobsData.getCategory_name());
        final RadioButton pageCheckbox = (RadioButton) layout.findViewById(R.id.job_radio);

        if (selectedJobs != null) {
            if (selectedJobs.getCategory_id() == (selectedJobsData.getCategory_id())) {
                pageCheckbox.setChecked(true);
                mCurrentlyCheckedRB = pageCheckbox;
            } else
                pageCheckbox.setSelected(false);
        } else
            pageCheckbox.setSelected(false);
        pageCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    if (mCurrentlyCheckedRB != null)
                        mCurrentlyCheckedRB.setChecked(false);
                    mCurrentlyCheckedRB = pageCheckbox;
                    selectedJobs = selectedJobsData;
                    JobSubCategories subCategories = new JobSubCategories((MainActivity) mContext);
                    subCategories.fetchSubCategories(selectedJobsData,((MainActivity) mContext).miniApp.getCurrentFragment());
                } else {
//                    pageCheckbox.setSelected(false);
                }
            }
        });
        return layout;
    }

    public JobsCategory getselectedJob() {
        return selectedJobs;
    }
}
