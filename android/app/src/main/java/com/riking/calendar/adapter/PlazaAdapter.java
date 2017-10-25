package com.riking.calendar.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.lzy.ninegrid.ImageInfo;
import com.lzy.ninegrid.preview.NineGridViewClickAdapter;
import com.riking.calendar.R;

import java.util.ArrayList;
import java.util.List;


public class PlazaAdapter extends RecyclerView.Adapter<PlazaViewHolder> {

    private Context context;
    private List<String> mList;

    public PlazaAdapter(Context context) {
        this.context = context;
        mList = new ArrayList<>();
    }

    @Override
    public PlazaViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(context).inflate(
                R.layout.plaza_item, viewGroup, false);
        return new PlazaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(PlazaViewHolder h, int i) {

        h.itemImage.setBorderWidth(2);
        h.itemImage.setBorderColor(h.itemImage.getResources().getColor(R.color.colorPrimary));
        RequestOptions options = new RequestOptions();
        Glide.with(h.itemImage.getContext()).load(R.drawable.img_user_head)
                .apply(options.fitCenter())
                .into(h.itemImage);
        h.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//             context.startActivity(new Intent(context, DetailsActivity.class));
            }
        });

        ArrayList<ImageInfo> imageInfo = new ArrayList<>();
        for (int j = 0; j < i && j < 9; j++) {
            ImageInfo info = new ImageInfo();
            info.setThumbnailUrl("http://img5.cache.netease.com/m/2015/5/21/20150521105913d3770_550.jpg");
            info.setBigImageUrl("http://img5.cache.netease.com/m/2015/5/21/20150521105913d3770_550.jpg");
            imageInfo.add(info);
        }
        h.nineGridView.setAdapter(new NineGridViewClickAdapter(context, imageInfo));

    }


    @Override
    public int getItemCount() {
        return mList.size();
    }

    public void add(String s) {
        mList.add(s);
        notifyDataSetChanged();
    }

    public void clear() {
        mList.clear();
        notifyDataSetChanged();
    }
}
