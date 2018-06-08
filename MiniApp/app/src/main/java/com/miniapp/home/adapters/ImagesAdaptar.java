package com.miniapp.home.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.miniapp.R;
import com.miniapp.commonUtils.CommonUtility;
import com.miniapp.commonUtils.Constants;
import com.miniapp.commonUtils.JsonUtils;
import com.miniapp.home.activities.FullImageActivity_;
import com.miniapp.home.activities.JobDetailActivity_;
import com.miniapp.home.activities.UserImagesActivity;
import com.miniapp.home.models.JobData;
import com.miniapp.home.models.UserOtherImages;

import java.io.ByteArrayOutputStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;


/**
 * Created by mac
 */

public class ImagesAdaptar extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    View view;
    List<UserOtherImages> imagesList;
    UserImagesActivity context;
    //    DateFormat utc = new SimpleDateFormat("MMM dd, yyyy hh:mm aa"); //new format
//    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-mm-dd HH:mm:ss"); //received format
    int height;
    RecyclerView recyclerView;

    public ImagesAdaptar(List<UserOtherImages> imagesList, UserImagesActivity context, RecyclerView recyclerView) {
        this.imagesList = imagesList;
        this.context = context;
        this.recyclerView = recyclerView;
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        height = display.getHeight();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.other_images_items_layout, parent, false);
        return new ImagesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        CommonUtility.hideProgressDialog();
        ImagesViewHolder jobsViewHolder = (ImagesViewHolder) holder;
        final UserOtherImages jobsDetails = imagesList.get(position);
        final String thumb = jobsDetails.getThumb();
        final Bitmap bitmap = jobsDetails.getBitmap();
        if (thumb != null)
            Glide.with(context).load(thumb).diskCacheStrategy(DiskCacheStrategy.NONE).
                    skipMemoryCache(true).placeholder(R.drawable.placeholder)
                    .into(jobsViewHolder.imageView)
                    ;
        else if (bitmap != null) {
            jobsViewHolder.imageView.setImageBitmap(bitmap);
        }
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, FullImageActivity_.class);
                intent.putExtra(com.miniapp.home.Utils.Constants.kKeyForUserOtherImage, JsonUtils.toString(jobsDetails));
                context.startActivity(intent);
            }
        });

        jobsViewHolder.removeIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imagesList.remove(position);
//                context.userOtherImages.remove(position);
                context.removedImages.add(jobsDetails);
                updateAdaptar(imagesList, recyclerView);
            }
        });
    }

    @Override
    public int getItemCount() {

        return imagesList.size();
    }


    static class ImagesViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView, removeIcon;

        ImagesViewHolder(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.other_image);
            removeIcon = (ImageView) itemView.findViewById(R.id.remove_icon);
        }
    }

    public void updateAdaptar(List<UserOtherImages> imageDetailses, RecyclerView recyclerView) {
        this.imagesList = imageDetailses;
        if (imagesList != null && imagesList.size() > 0)
            context.setButtonViisibility(View.VISIBLE);
        else
            context.setButtonViisibility(View.GONE);
        this.notifyDataSetChanged();
        recyclerView.getLayoutManager().scrollToPosition(imagesList.size());
    }
}