package com.mcdev.whap.Utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import androidx.core.content.FileProvider;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.snackbar.Snackbar;
import com.mcdev.whap.Models.StatusModel;
import com.mcdev.whap.R;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.Objects;

public class MyMethods {


    /*download whatsApp statuses*/
    public static void downloadFile(Context context, StatusModel statusModel) throws IOException {

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
        copyFile(context, statusModel.getFile(), destinationFile);

        /*refreshing the gallery*/
        Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        intent.setData(Uri.fromFile(destinationFile));
        Objects.requireNonNull(context).sendBroadcast(intent);

    }

    /*copying*/
    public static void copyFile(Context context, File file, File destinationFile) throws IOException {
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
            //status bar alert
//            StatusBarAlertView statusBarAlertView = new StatusBarAlert.Builder((Activity) context)
//                    .withText("Saved...")
//                    .showProgress(true)
//                    .withDuration(1000)
//                    .withAlertColor(R.color.green)
//                    .build();

            //snack bar
            Snackbar.make(((Activity) context).findViewById(R.id.videos_swipe_to_refresh_layout),"Saved!" , Snackbar.LENGTH_SHORT).show();

        }
    }

    /*get thumbnail*/
    private Bitmap getThumbnail(StatusModel statusModel) {

        if (statusModel.isVideo()) {
            return ThumbnailUtils.createVideoThumbnail(statusModel.getFile().getAbsolutePath(),
                    MediaStore.Video.Thumbnails.MICRO_KIND);
        } else {
            return ThumbnailUtils.extractThumbnail(BitmapFactory.decodeFile(statusModel.getFile().getAbsolutePath()),
                    MyConstants.THUMBSIZE,
                    MyConstants.THUMBSIZE);
        }
    }

    public static void getThumbnailFromVideoWithGlide(Context context, String URI, ImageView imageView) {
        long interval = 5000 * 1000;
        RequestOptions options = new RequestOptions().frame(interval);
        Glide.with(context).asBitmap()
                .load(URI)
                .apply(options)
                .into(imageView);
    }

    public static String getVideoLength(Context context, File videoFile) {
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
//use one of overloaded setDataSource() functions to set your data source
        retriever.setDataSource(context, Uri.fromFile(videoFile));
        String time = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
        long timeInMillisec = Long.parseLong(time );
        long secs = timeInMillisec / 1000;
        String duration = secs + " secs";

        retriever.release();

        return duration;
    }

    public static void shareFile(Context context, String mimeType, StatusModel statusModel) {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType(mimeType);
        File imageFile = new File(statusModel.getFile().getPath());
        Uri uri = FileProvider.getUriForFile(context, "com.squareup.picasso.LibraryViewAdapter.provider", imageFile);
        shareIntent.putExtra(Intent.EXTRA_STREAM, uri);
        context.startActivity(Intent.createChooser(shareIntent, "Share using"));
    }

    public static void makeFullScreen(Activity activity) {
        Window window = activity.getWindow();
        window.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
    }
}
