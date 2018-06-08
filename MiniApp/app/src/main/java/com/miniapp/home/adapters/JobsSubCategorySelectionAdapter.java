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
import com.miniapp.home.models.UserJobSubCategory;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mac on 14/04/18.
 */

public class JobsSubCategorySelectionAdapter extends BaseAdapter {
    Context mContext;
    ListView mListView;
    List<JobsCategory> mPagesArray;
    List<JobsCategory> selectedJobs = new ArrayList();


    public JobsSubCategorySelectionAdapter(Context context, ListView listView, List<JobsCategory> pPagesArray) {
        mContext = context;
        this.mListView = listView;
        this.mPagesArray = pPagesArray;
        List<UserJobSubCategory> selectedSubJobs = CommonUtility.getSelectedSubJobsCategory(context);
        for (int i=0;i<selectedSubJobs.size();i++){
            JobsCategory category = new JobsCategory();
            category.setCategory_name(selectedSubJobs.get(i).getSubcategory_title());
            category.setCategory_id(selectedSubJobs.get(i).getSubcategory_id());
            category.setDescription(selectedSubJobs.get(i).getSub_category_description());
            selectedJobs.add(category);
        }
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
        final ViewGroup layout = (ViewGroup) inflater.inflate(R.layout.selecetd_job_sub_categories_items_layout, pViewGroup, false);
        final JobsCategory selectedJobsData = mPagesArray.get(p);
        TextView nameTextview = (TextView) layout.findViewById(R.id.job_name_textview);
        nameTextview.setText(selectedJobsData.getCategory_name());
        final CheckBox pageCheckbox = (CheckBox) layout.findViewById(R.id.job_checkbox);
        int ssize = selectedJobs.size();
        for (int q = 0; q < ssize; q++) {
            if (selectedJobs != null && selectedJobs.size() > 0) {
                if (selectedJobs.get(q).getCategory_id() == (selectedJobsData.getCategory_id())) {
                    pageCheckbox.setChecked(true);
                    break;
                }
            } else
                pageCheckbox.setSelected(false);
        }
        pageCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    selectedJobs.add(selectedJobsData);
                } else {
                    int size = selectedJobs.size();
                    for (int q = 0; q < size; q++) {
                        if (selectedJobs.get(q).getCategory_id() == (selectedJobsData.getCategory_id())) {
                            selectedJobs.remove(q);
                            break;
                        }
                    }
                }
            }
        });
        return layout;
    }

    public List<UserJobSubCategory> getselectedJobs() {
        List<UserJobSubCategory> selectedSubJobs = new ArrayList<>();
        for (int i=0;i<selectedJobs.size();i++){
            UserJobSubCategory category = new UserJobSubCategory();
            category.setSubcategory_title(selectedJobs.get(i).getCategory_name());
            category.setSubcategory_id(selectedJobs.get(i).getCategory_id());
            category.setSub_category_description(selectedJobs.get(i).getDescription());
            selectedSubJobs.add(category);
        }
        return selectedSubJobs;
    }
}
