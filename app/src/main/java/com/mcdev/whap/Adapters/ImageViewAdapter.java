package com.mcdev.whap.Adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mcdev.whap.Fragments.ImagesFragment;
import com.mcdev.whap.Models.StatusModel;
import com.mcdev.whap.R;
import com.mcdev.whap.Utils.MyConstants;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.List;
import java.util.Objects;

public class ImageViewAdapter extends RecyclerView.Adapter<ImageViewAdapter.ImageViewHolder> {
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
    public void onBindViewHolder(@NonNull ImageViewHolder holder, int position) {

        final StatusModel statusModel = imageList.get(position);
        if (!statusModel.isVideo()) {
            Picasso.get().load(statusModel.getFile()).into(holder.imageView);
        }

        /*download button onclick*/
        holder.downloadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    downloadImage(statusModel);
                } catch (IOException e) {
                    e.printStackTrace();
                }
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

    /*download whatsApp statuses*/
    public void downloadImage(StatusModel statusModel) throws IOException {

        if (!MyConstants.getSavedStatusesDir(context).exists()) {
            MyConstants.getSavedStatusesDir(context).mkdirs();
        }

        //creating the file
        File destinationFile = new File(MyConstants.getSavedStatusesDir(context) + File.separator + statusModel.getTitle());
        //if user tries to save the same file twice, the old one will be deleted
        if (destinationFile.exists()) {
            destinationFile.delete();
        }

        //copying file from whatsApp directory to our directory
        copyFile(statusModel.getFile(), destinationFile);

        /*refreshing the gallery*/
        Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        intent.setData(Uri.fromFile(destinationFile));
        Objects.requireNonNull(context).sendBroadcast(intent);

    }

    private void copyFile(File file, File destinationFile) throws IOException {
        //checking if destination file's parent exits
        if (!Objects.requireNonNull(destinationFile.getParentFile()).exists()) {
            destinationFile.getParentFile().mkdirs();
        }

        /*checking if destination file exists*/
        if (!destinationFile.exists()) {
            destinationFile.createNewFile();
        }

        FileChannel source = new FileInputStream(file).getChannel();
        FileChannel destination = new FileOutputStream(destinationFile).getChannel();
        destination.transferFrom(source, 0, source.size());     //start copying

        /*close channels*/
        source.close();
        destination.close();

        if (!source.isOpen() && !destination.isOpen()) {
            Toast.makeText(context, "Success", Toast.LENGTH_SHORT).show();
        }
    }
}
