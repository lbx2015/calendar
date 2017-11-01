package com.riking.calendar.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.riking.calendar.R;
import com.riking.calendar.util.CONST;

/**
 * Created by zw.zhang on 2017/7/24.
 */

public class LookImageActivity extends AppCompatActivity {

    ImageView imageView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        Log.d("zzw", this + "on create");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.look_image_activity);
        imageView = findViewById(R.id.image_view);
        String imageUrl = getIntent().getExtras().getString(CONST.IMAGE_URL);
        Glide.with(this).load(imageUrl).into(imageView);
    }

    @Override
    public View onCreateView(View parent, String name, Context context, AttributeSet attrs) {
        return super.onCreateView(parent, name, context, attrs);
    }
}
