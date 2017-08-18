package com.riking.calendar.adapter;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.riking.calendar.R;
import com.riking.calendar.jiguang.Logger;
import com.riking.calendar.pojo.CtryHdayCrcy;

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
        MyTask myTask = new MyTask();
        myTask.imageView = holder.countryImage;
        myTask.execute(r.iconUrl);
    }

    @Override
    public int getItemCount() {
        return vocationList.size();
    }

    public static class MyTask extends AsyncTask<String, Void, Bitmap> {
        public ImageView imageView;

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected Bitmap doInBackground(String... voids) {

            Bitmap bitmap = null;
            try {
                URL url = new URL(voids[0]);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                InputStream inputStream = connection.getInputStream();
                bitmap = BitmapFactory.decodeStream(inputStream);
            } catch (IOException e) {
                Logger.d("zzw", "image loaded failed" + e.getMessage());
            }
            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            imageView.setImageBitmap(bitmap);
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
