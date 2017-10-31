package com.riking.calendar.util;

import android.os.Environment;

import com.riking.calendar.jiguang.Logger;

import java.io.File;
import java.io.FileInputStream;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by zw.zhang on 2017/8/18.
 */

public class FileUtil {

    public static File getImageSaveDir() {
        File mFile1 = Environment.getExternalStorageDirectory();
        return new File(mFile1, CONST.IMAGE_PATH);
    }
    public static File generateImageFile() {
        File mFile1 = Environment.getExternalStorageDirectory();
        File imagePath = new File(mFile1, CONST.IMAGE_PATH);
        if (imagePath.exists() && imagePath.isDirectory()) {

        } else {
            imagePath.mkdirs();
        }
        return new File(imagePath, new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date()) + ".jpg");
    }

    public static String getImageFilePath(String imageName) {
        File mFile1 = Environment.getExternalStorageDirectory();
        return mFile1.getAbsolutePath() + CONST.IMAGE_PATH + "/" + imageName;
    }

    public static boolean imageExists(String imageName) {
        Logger.d("zzw", "imagename: " + imageName);
        File mFile = Environment.getExternalStorageDirectory();
        File imageDirectory = new File(mFile, CONST.IMAGE_PATH);
        Logger.d("zzw", "imageDirectory: " + imageDirectory);
        if (!imageDirectory.exists()) {
            Logger.d("zzw", "imageDirectory not exists: " + imageDirectory);
            return false;
        } else {
            Logger.d("zzw", "imageDirectory exists: " + imageDirectory);
            File[] files = imageDirectory.listFiles();
            for (int i = 0; i < files.length; i++) {
                File f = files[i];
                Logger.d("zzw", "f.getName: " + f.getName() + " imageName : " + imageName);
                if (f.getName().endsWith(imageName)) {
                    return true;
                }
            }
            return false;
        }
    }


    public long getFileSizes(File f) throws Exception {

        long s = 0;
        if (f.exists()) {
            FileInputStream fis = null;
            fis = new FileInputStream(f);
            s = fis.available();
            fis.close();
        } else {
            f.createNewFile();
            System.out.println("文件夹不存在");
        }

        return s;
    }

    /**
     * 递归
     * */
    public static long getFileSize(File f) {
        long size = 0;
        File flist[] = f.listFiles();
        for (int i = 0; i < flist.length; i++) {
            if (flist[i].isDirectory()) {
                size = size + getFileSize(flist[i]);
            } else {
                size = size + flist[i].length();
            }
        }
        return size;
    }

    /**
     * 转换文件大小
     * */
    public static String formatFileSize(long fileS) {
        DecimalFormat df = new DecimalFormat("#.00");
        String fileSizeString = "";
        if (fileS < 1024) {
            fileSizeString = df.format((double) fileS) + "B";
        } else if (fileS < 1048576) {
            fileSizeString = df.format((double) fileS / 1024) + "K";
        } else if (fileS < 1073741824) {
            fileSizeString = df.format((double) fileS / 1048576) + "M";
        } else {
            fileSizeString = df.format((double) fileS / 1073741824) + "G";
        }
        return fileSizeString;
    }

//  /**
//   * 递归求取目录文件个数
//   * */
//  public long getlist(File f) {
//      long size = 0;
//      File flist[] = f.listFiles();
//      System.out.println("-------------" + flist.length);
//      size = flist.length;
//      for (int i = 0; i < flist.length; i++) {
//          if (flist[i].isDirectory()) {
//              size = size + getlist(flist[i]);
//              size--;
//          }
//      }
//      return size;
//  }

}
