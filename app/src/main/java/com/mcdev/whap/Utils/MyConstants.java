package com.mcdev.whap.Utils;

import android.content.Context;
import android.os.Environment;

import java.io.File;

public class MyConstants {
    public static final int THUMBSIZE = 128;
    public static final String NOMEDIA = ".nomedia";

    public static File getWhatsAppStatusDir (Context context){
        return new File(Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "WhatsApp/Media/.Statuses/");
    }

    public static File getSavedStatusesDir(Context context){
        return new File(Environment.getExternalStorageDirectory().getAbsolutePath()  + File.separator + "Whap/");
    }

}
