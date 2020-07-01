package com.mcdev.whap.Adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mcdev.whap.Fragments.VideosFragment;
import com.mcdev.whap.Models.StatusModel;
import com.mcdev.whap.R;
import com.mcdev.whap.Utils.MyMethods;

import java.io.IOException;
import java.util.ArrayList;
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
            MyMethods.getThumbnailFromVideoWithGlide(context, statusModel.getFile().getAbsolutePath(),holder.imageView);
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

    }

    @Override
    public int getItemCount() {
        return videoList.size();
    }

    public class VideoViewHolder extends RecyclerView.ViewHolder{
        ImageView imageView;
        Button downloadBtn;
        public VideoViewHolder(@NonNull View itemView) {
            super(itemView);
            downloadBtn = itemView.findViewById(R.id.ibSaveToGallery);
            imageView = itemView.findViewById(R.id.ivThumbnail);
        }
    }
}
