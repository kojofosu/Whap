package com.mcdev.whap.Utils;

import android.content.Context;
import android.os.Environment;

import java.io.File;

public class MyConstants {
    public static final int THUMBSIZE = 128;
    public static final String NOMEDIA = ".nomedia";
    public static final String statusTypeKey = "statusType";
    public static final String statusUrlKey = "statusUrl";
    public static final String statusTypeVideo = "video";
    public static final String statusTypeImage = "image";

    public static File getWhatsAppStatusDir (Context context){
        return new File(Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "WhatsApp/Media/.Statuses/");
    }

    public static File getSavedStatusesDir(Context context){
        return new File(Environment.getExternalStorageDirectory().getAbsolutePath()  + File.separator + "Whap/");
    }

}
