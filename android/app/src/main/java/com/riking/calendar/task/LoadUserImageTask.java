package com.riking.calendar.task;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ImageView;

import com.riking.calendar.app.MyApplication;
import com.riking.calendar.jiguang.Logger;
import com.riking.calendar.util.CONST;
import com.riking.calendar.util.FileUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by zw.zhang on 2017/8/25.
 */

public class LoadUserImageTask extends AsyncTask<String, Void, Bitmap> {
    public ImageView imageView;
    public String imageName;
    public boolean imageFileExists;
    public SharedPreferences preferences = MyApplication.APP.getSharedPreferences(CONST.PREFERENCE_FILE_NAME, Context.MODE_PRIVATE);
    public File file;

    @Override
    protected void onPreExecute() {
    }

    @Override
    protected Bitmap doInBackground(String... voids) {
        Bitmap bitmap = null;
        try {
            imageName = voids[0].substring(voids[0].lastIndexOf('/') + 1);
            if (FileUtil.imageExists(imageName)) {
                Logger.d("zzw", "no need load url: " + imageName);
                imageFileExists = true;
                return BitmapFactory.decodeFile(FileUtil.getImageFilePath(imageName));
            } else {
                imageFileExists = false;
            }

            URL url = new URL(voids[0]);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            InputStream inputStream = connection.getInputStream();
            bitmap = BitmapFactory.decodeStream(inputStream);
        } catch (IOException e) {
            Logger.d("zzw", "image loaded failed" + e.getMessage());
        }
        return bitmap;
    }

    @Override
    protected void onPostExecute(final Bitmap bitmap) {
        if (imageView != null && bitmap != null) {
            imageView.setImageBitmap(bitmap);
            Logger.d("zzw", "imageName: " + imageName);
            new Thread(new Runnable() {
                @Override
                public void run() {
                    Logger.d("zzw", "file exists: " + imageFileExists);
                    if (!imageFileExists) {
                        File imageFile = new File(FileUtil.getImageFilePath(imageName));
                        try {
                            imageFile.createNewFile();
                            FileOutputStream outputStream;
                            outputStream = new FileOutputStream(imageFile);
                            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
                            outputStream.flush();
                            outputStream.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }).start();
        }
    }
}