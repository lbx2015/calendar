package com.riking.calendar.adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ldf.calendar.Const;
import com.riking.calendar.R;
import com.riking.calendar.app.MyApplication;
import com.riking.calendar.jiguang.Logger;
import com.riking.calendar.pojo.CtryHdayCrcy;
import com.riking.calendar.task.LoadUserImageTask;
import com.riking.calendar.util.FileUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

/**
 * Created by zw.zhang on 2017/7/12.
 */

public class VocationRecyclerViewAdapter extends RecyclerView.Adapter<VocationRecyclerViewAdapter.MyViewHolder> {
    private List<CtryHdayCrcy> vocationList;

    public VocationRecyclerViewAdapter(List<CtryHdayCrcy> r) {
        this.vocationList = r;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.vocation_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        CtryHdayCrcy r = vocationList.get(position);
        holder.date.setText(r.hdayDate);
        holder.country.setText(r.ctryNameValue);
        holder.currency.setText(r.crcy);
        holder.vocation.setText(r.hdayNameValue);
        LoadUserImageTask myTask = new LoadUserImageTask();
        myTask.imageView = holder.countryImage;
        myTask.execute(r.flagUrl);
    }

    @Override
    public int getItemCount() {
        return vocationList.size();
    }

    public static class MyTask extends AsyncTask<String, Void, Bitmap> {
        public ImageView imageView;
        public String imageName;
        public boolean imageFileExists;
        public SharedPreferences preferences = MyApplication.APP.getSharedPreferences(Const.PREFERENCE_FILE_NAME, Context.MODE_PRIVATE);
        public File file;

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected Bitmap doInBackground(String... voids) {
            Bitmap bitmap = null;
            try {
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

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView date, country, currency, vocation;
        public ImageView countryImage;

        public MyViewHolder(View view) {
            super(view);
            date = (TextView) view.findViewById(R.id.date);
            country = (TextView) view.findViewById(R.id.country);
            currency = (TextView) view.findViewById(R.id.currency);
            vocation = (TextView) view.findViewById(R.id.vocation);
            countryImage = (ImageView) view.findViewById(R.id.country_image);

        }

    }
}
