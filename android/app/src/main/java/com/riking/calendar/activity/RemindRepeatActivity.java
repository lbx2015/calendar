package com.riking.calendar.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.riking.calendar.R;

/**
 * Created by zw.zhang on 2017/7/24.
 */

public class RemindRepeatActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        Log.d("zzw", this + "on create");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remind_kind);
    }
}
