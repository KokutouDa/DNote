package com.kokutouda.dnote.dnote.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class BitmapUtils {

    /**
     *
     * @param context
     * @param bitmap
     * @return file name
     */
    public static String saveImage(Context context, Bitmap bitmap) {
        String fileName = getFilePrefixName();
        File file = getFileAbsolute(context, fileName);
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.flush();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return fileName;
    }

    public static File getFileAbsolute(Context context, String fileName) {
        File parentFile = context.getFilesDir();
        return new File(parentFile, fileName);
    }

    public static Bitmap getBitmap(Context context, String fileName) {
        File parent = context.getFilesDir();
        File file = new File(parent, fileName);
        return BitmapFactory.decodeFile(file.getPath());
    }

    private static String getFilePrefixName() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH-mm-ss", Locale.CHINA);
        return dateFormat.format(new Date()) + ".jpg";
    }
}
