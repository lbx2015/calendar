package com.riking.calendar.app;

import android.content.Context;
import android.graphics.Bitmap;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.lzy.ninegrid.NineGridView;
import com.riking.calendar.R;

/**
 * Created by zw.zhang on 2017/10/25.
 */

public class GlidImageLoader implements NineGridView.ImageLoader {
    @Override
    public void onDisplayImage(Context context, ImageView imageView, String s) {
        Glide.with(context).load(s).apply(new RequestOptions().placeholder(R.drawable.img_user_head).error(R.drawable.user_icon_head_notlogin)).into(imageView);
    }

    @Override
    public Bitmap getCacheImage(String s) {
        return null;
    }
}
