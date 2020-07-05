package com.mcdev.whap.Adapters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mcdev.whap.Fragments.ImagesFragment;
import com.mcdev.whap.Models.StatusModel;
import com.mcdev.whap.R;
import com.mcdev.whap.Utils.MyConstants;
import com.mcdev.whap.Utils.MyMethods;
import com.mcdev.whap.ViewStatusActivity;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.List;

public class ImageViewAdapter extends RecyclerView.Adapter<ImageViewAdapter.ImageViewHolder> {
    private final String TAG = this.getClass().getSimpleName();
    private final List<StatusModel> imageList;
    Context context;
    ImagesFragment imagesFragment;

    public ImageViewAdapter(List<StatusModel> imageList, Context context, ImagesFragment imagesFragment) {
        this.imageList = imageList;
        this.context = context;
        this.imagesFragment = imagesFragment;
    }

    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.items_status, parent, false);

        return new ImageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ImageViewHolder holder, int position) {

        final StatusModel statusModel = imageList.get(position);

        if (!statusModel.isVideo()) {
            Picasso.get().load(statusModel.getFile()).into(holder.imageView);
        }

        /*download button onclick*/
        holder.downloadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    //downloading status
                    MyMethods.downloadFile(context, statusModel);
                    //downloadImage(statusModel);
                } catch (IOException e) {
                    Log.e(TAG, "onClick: Error occurred", e);
                }
            }
        });

        /*image view onclick*/
        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ViewStatusActivity.class);
                intent.putExtra(MyConstants.statusUrlKey, statusModel.getFile().toURI().toString());
                intent.putExtra(MyConstants.statusTypeKey, MyConstants.statusTypeImage);
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return imageList.size();
    }

    public static class ImageViewHolder extends RecyclerView.ViewHolder{
        ImageView imageView;
        Button downloadBtn;
        public ImageViewHolder(@NonNull View itemView) {
            super(itemView);
            downloadBtn = itemView.findViewById(R.id.ibSaveToGallery);
            imageView = itemView.findViewById(R.id.ivThumbnail);
        }
    }

}
