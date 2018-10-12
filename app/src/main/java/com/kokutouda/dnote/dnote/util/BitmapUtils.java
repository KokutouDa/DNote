package com.kokutouda.dnote.dnote.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class BitmapUtils {

    public static File createImageFiles(Context context) throws IOException {
        String prefix = getFilePrefixName();
        File parent = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        return File.createTempFile(prefix, ".jpg", parent);
    }

    private static String getFilePrefixName() {
        String prefix = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.CHINA).format(new Date());
        return "JPEG_" + prefix;
    }

    public static Bitmap getBitmap(String fileName) {
        File file = new File(fileName);
        return BitmapFactory.decodeFile(file.getPath());
    }
}
