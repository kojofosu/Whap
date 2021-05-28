package com.mcdev.whap.Adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupMenu;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;
import com.mcdev.whap.Fragments.VideosFragment;
import com.mcdev.whap.Models.StatusModel;
import com.mcdev.whap.R;
import com.mcdev.whap.Utils.MyConstants;
import com.mcdev.whap.Utils.MyMethods;
import com.mcdev.whap.ViewStatusActivity;

import java.io.IOException;
import java.util.List;

public class VideoViewAdapter extends RecyclerView.Adapter<VideoViewAdapter.VideoViewHolder> {
    private final String TAG = this.getClass().getSimpleName();
    private final List<StatusModel> videoList;
    Context context;
    VideosFragment videosFragment;

    public VideoViewAdapter(List<StatusModel> videoList, Context context, VideosFragment videosFragment) {
        this.videoList = videoList;
        this.context = context;
        this.videosFragment = videosFragment;
    }

    @NonNull
    @Override
    public VideoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.items_status, parent, false);
        return new VideoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final VideoViewHolder holder, int position) {
        final StatusModel statusModel = videoList.get(position);

        if (statusModel.isVideo()) {
            //set video duration visible
            holder.videoDuration.setVisibility(View.VISIBLE);

            MyMethods.getThumbnailFromVideoWithGlide(context, statusModel.getFile().getAbsolutePath(),holder.imageView);
            /*setting video duration text*/
            holder.videoDuration.setText(MyMethods.getVideoLength(context, statusModel.getFile()));
        }

        /*download button onClick*/
        holder.downloadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    MyMethods.downloadFile(holder.imageView.getContext(), statusModel);
                } catch (IOException e) {
                    Log.e(TAG, "onClick: Error occurred", e);
                }
            }
        });


        /*video view onclick*/
        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ViewStatusActivity.class);
                intent.putExtra(MyConstants.statusUrlKey, statusModel.getFile().toURI().toString());
                intent.putExtra(MyConstants.statusTypeKey, MyConstants.statusTypeVideo);
                context.startActivity(intent);
            }
        });

        /*image view onLongClick*/
        holder.imageView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                PopupMenu popupMenu = new PopupMenu(context, v);
                popupMenu.getMenuInflater().inflate(R.menu.item_long_click_menu, popupMenu.getMenu());
                popupMenu.show();

                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        if (item.getItemId() == R.id.item_share) {
                            if (statusModel.isVideo()) {
                                //video
                                MyMethods.shareFile(context, MyConstants.MIME_ALL_VIDEOS, statusModel);
                            }else {
                                //images
                                MyMethods.shareFile(context, MyConstants.MIME_ALL_IMAGES, statusModel);
                            }
                        }
                        return false;
                    }
                });
                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return videoList.size();
    }

    public static class VideoViewHolder extends RecyclerView.ViewHolder{
        ImageView imageView;
        Button downloadBtn;
        Button videoDuration;
        public VideoViewHolder(@NonNull View itemView) {
            super(itemView);
            downloadBtn = itemView.findViewById(R.id.ibSaveToGallery);
            imageView = itemView.findViewById(R.id.ivThumbnail);
            videoDuration = itemView.findViewById(R.id.video_duration);
        }
    }
}
