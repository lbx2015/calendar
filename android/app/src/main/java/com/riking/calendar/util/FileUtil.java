package com.riking.calendar.util;

import android.os.Environment;

import com.ldf.calendar.Const;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by zw.zhang on 2017/8/18.
 */

public class FileUtil {
    public static File generateImageFile() {
        File mFile1 = Environment.getExternalStorageDirectory();
        File imagePath = new File(mFile1, Const.IMAGE_PATH);
        if (imagePath.exists() && imagePath.isDirectory()) {

        } else {
            imagePath.mkdirs();
        }
        return new File(imagePath, new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date()) + ".jpg");
    }
}
