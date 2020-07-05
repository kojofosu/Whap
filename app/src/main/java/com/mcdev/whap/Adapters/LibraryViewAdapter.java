package com.mcdev.whap.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mcdev.whap.Fragments.ImagesFragment;
import com.mcdev.whap.Fragments.LibraryFragment;
import com.mcdev.whap.Models.StatusModel;
import com.mcdev.whap.R;
import com.mcdev.whap.Utils.MyMethods;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class LibraryViewAdapter extends RecyclerView.Adapter<LibraryViewAdapter.LibraryViewHolder> {
    private final String TAG = this.getClass().getSimpleName();
    private final List<StatusModel> libraryList;
    Context context;
    LibraryFragment libraryFragment;

    public LibraryViewAdapter(List<StatusModel> libraryList, Context context, LibraryFragment libraryFragment) {
        this.libraryList = libraryList;
        this.context = context;
        this.libraryFragment = libraryFragment;
    }

    @NonNull
    @Override
    public LibraryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.items_status, parent, false);
        return new LibraryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LibraryViewHolder holder, int position) {
        //removing download button from items
        holder.downloadBtn.setVisibility(View.GONE);

        final StatusModel statusModel = libraryList.get(position);

        /*checking if user has any saved statuses*/
        if (libraryList.size() < 1) {
            holder.noSavedStatusesTV.setVisibility(View.VISIBLE);
        }else{
            holder.noSavedStatusesTV.setVisibility(View.GONE);
        }

        if (statusModel.isVideo()) {
            //set video duration visible
            holder.videoDuration.setVisibility(View.VISIBLE);

            MyMethods.getThumbnailFromVideoWithGlide(context, statusModel.getFile().getAbsolutePath(),holder.imageView);
            /*setting video duration text*/
            holder.videoDuration.setText(MyMethods.getVideoLength(context, statusModel.getFile()));
        }else{
            //set video duration invisible
            holder.videoDuration.setVisibility(View.GONE);
            //set image to imageView with Picasso
            Picasso.get().load(statusModel.getFile()).into(holder.imageView);
        }

    }

    @Override
    public int getItemCount() {
        return libraryList.size();
    }

    public static class LibraryViewHolder extends RecyclerView.ViewHolder{
        ImageView imageView;
        Button downloadBtn;
        Button videoDuration;
        TextView noSavedStatusesTV;
        public LibraryViewHolder(@NonNull View itemView) {
            super(itemView);
            downloadBtn = itemView.findViewById(R.id.ibSaveToGallery);
            imageView = itemView.findViewById(R.id.ivThumbnail);
            videoDuration = itemView.findViewById(R.id.video_duration);
            noSavedStatusesTV = itemView.findViewById(R.id.no_saved_statuses_tv);
        }
    }
}
