package com.mcdev.whap.Adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupMenu;

import androidx.annotation.NonNull;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.mcdev.whap.Fragments.LibraryFragment;
import com.mcdev.whap.Models.StatusModel;
import com.mcdev.whap.R;
import com.mcdev.whap.Utils.MyConstants;
import com.mcdev.whap.Utils.MyMethods;
import com.mcdev.whap.ViewStatusActivity;
import com.squareup.picasso.BuildConfig;
import com.squareup.picasso.Picasso;

import java.io.File;
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

        /*holder on click listener*/
        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (statusModel.isVideo()) {
                    Intent intent = new Intent(context, ViewStatusActivity.class);
                    intent.putExtra(MyConstants.statusUrlKey, statusModel.getFile().toURI().toString());
                    intent.putExtra(MyConstants.statusTypeKey, MyConstants.statusTypeVideo);
                    context.startActivity(intent);
                } else {
                    Intent intent = new Intent(context, ViewStatusActivity.class);
                    intent.putExtra(MyConstants.statusUrlKey, statusModel.getFile().toURI().toString());
                    intent.putExtra(MyConstants.statusTypeKey, MyConstants.statusTypeImage);
                    context.startActivity(intent);
                }
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
        return libraryList.size();
    }

    public static class LibraryViewHolder extends RecyclerView.ViewHolder{
        ImageView imageView;
        Button downloadBtn;
        Button videoDuration;
        public LibraryViewHolder(@NonNull View itemView) {
            super(itemView);
            downloadBtn = itemView.findViewById(R.id.ibSaveToGallery);
            imageView = itemView.findViewById(R.id.ivThumbnail);
            videoDuration = itemView.findViewById(R.id.video_duration);
        }
    }
}
