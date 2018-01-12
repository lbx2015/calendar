package com.riking.calendar.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.riking.calendar.BuildConfig;
import com.riking.calendar.R;
import com.riking.calendar.listener.ZCallBack;
import com.riking.calendar.pojo.base.ResponseModel;
import com.riking.calendar.retrofit.APIClient;
import com.riking.calendar.util.CONST;
import com.riking.calendar.util.FileUtil;
import com.riking.calendar.util.GlideUtil;
import com.riking.calendar.util.ZDB;
import com.riking.calendar.util.ZGoto;
import com.riking.calendar.util.ZPreference;

import java.io.File;

import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * Created by zw.zhang on 2017/8/5.
 */

public class SettingActivity extends AppCompatActivity implements View.OnClickListener {
    TextView cacheSizeTextview;
    SharedPreferences preferences;
    boolean doubleBackToClearMemory;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        preferences = getSharedPreferences(CONST.PREFERENCE_FILE_NAME, MODE_PRIVATE);
        setContentView(R.layout.activity_setting);
        cacheSizeTextview = (TextView) findViewById(R.id.cache_size);
        //set the image cache file size
        long imageSize = FileUtil.getFileSize(new File(Environment.getExternalStorageDirectory(), CONST.IMAGE_PATH)) + GlideUtil.getInstance().getCacheImageSize(this);
        if (imageSize > 0) {
            cacheSizeTextview.setText(FileUtil.formatFileSize(imageSize));
        } else {
            cacheSizeTextview.setText(getString(R.string.no_need_to_clear));
        }

        if (ZPreference.pref.getBoolean(CONST.IS_LOGIN, false)) {
            findViewById(R.id.login_out_card_view).setVisibility(View.VISIBLE);
        }

        findViewById(R.id.back).setOnClickListener(this);
        findViewById(R.id.login_out_button).setOnClickListener(this);
        findViewById(R.id.clear_cache_relatvie_layout).setOnClickListener(this);
    }

    public void onClickAbout(View view) {

//        Intent i = new Intent(SettingActivity.this, WebviewEditorActivity.class);
//        i.putExtra(CONST.WEB_URL, "http://172.16.64.96:8280/app/getImage.html");
//        i.putExtra(CONST.ACTIVITY_NAME, "SettingActivity");
//        startActivity(i);
        APIClient.apiInterface.getAboutHtml(BuildConfig.VERSION_NAME).enqueue(new ZCallBack<ResponseModel<String>>() {
            @Override
            public void callBack(ResponseModel<String> response) {
                Intent i = new Intent(SettingActivity.this, WebviewActivity.class);
                i.putExtra(CONST.WEB_URL, response._data);
                i.putExtra(CONST.ACTIVITY_NAME, "SettingActivity");
                startActivity(i);
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back: {
                onBackPressed();
                break;
            }
            case R.id.login_out_button: {
                SharedPreferences.Editor editor = preferences.edit();
                editor.clear();
                editor.commit();
                //cancel the alarms
                ZDB.Instance.cancelAlarmsWhenLoginOut();
                //change realm database
                RealmConfiguration.Builder builder = new RealmConfiguration.Builder()
                        .deleteRealmIfMigrationNeeded();
                builder.name(CONST.DEFAUT_REALM_DATABASE_NAME);
                Realm.setDefaultConfiguration(builder.build());
                ZGoto.toLoginActivity();
                finish();
                break;
            }
            case R.id.clear_cache_relatvie_layout: {
                if (doubleBackToClearMemory) {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            File imageDirectory = new File(Environment.getExternalStorageDirectory(), CONST.IMAGE_PATH);
                            for (File f : imageDirectory.listFiles()) {
                                f.delete();
                            }

                            //Glide clear cache
                            GlideUtil.getInstance().clearImageAllCache(SettingActivity.this);

                            SettingActivity.this.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    cacheSizeTextview.setText(cacheSizeTextview.getContext().getString(R.string.no_need_to_clear));
                                    Toast.makeText(cacheSizeTextview.getContext(), cacheSizeTextview.getResources().getString(R.string.cleared), Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }).start();
                    return;
                }

                this.doubleBackToClearMemory = true;
                Toast.makeText(this, getString(R.string.click_again_clear_memory), Toast.LENGTH_SHORT).show();

                new Handler().postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        doubleBackToClearMemory = false;
                    }
                }, 2000);

                break;
            }
        }
    }
}
