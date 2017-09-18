package com.riking.calendar.widget.dialog;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatDialog;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import com.riking.calendar.R;
import com.riking.calendar.app.MyApplication;

import java.util.List;

import static com.riking.calendar.R.color.dialogColorPrimary;

/**
 * Created by zw.zhang on 2017/7/26.
 */

public class SearchDialog extends AppCompatDialog {

    public static final int check = 111;
    public EditText editText;
    public View.OnClickListener searchClickListener;
    View searchButton;
    View voiceSearchButton;

    public SearchDialog(@NonNull Context context) {
        //step 1, required. to stretch the dialog to full screen
        super(context, R.style.DialogFullScreenTheme);

    }

    public SearchDialog(@NonNull Context context, int theme) {
        super(context, theme);
    }

    protected SearchDialog(@NonNull Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.search_dialog);
        KeepStatusBar();
        Window window = getWindow();

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        getWindow().setBackgroundDrawable(new ColorDrawable(getContext().getResources().getColor(dialogColorPrimary)));

        // clear FLAG_TRANSLUCENT_STATUS flag:
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

        // add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

        // finally change the color
        window.setStatusBarColor(ContextCompat.getColor(getContext(), R.color.colorPrimary));

        searchButton = findViewById(R.id.search);
        editText = (EditText) findViewById(R.id.search_edit_view);
        Log.d("zzw", "set on cick listener: " + searchClickListener);
        searchButton.setOnClickListener(searchClickListener);
        voiceSearchButton = findViewById(R.id.voice_search_button);
        voiceSearchButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                PackageManager pm = getContext().getPackageManager();
                List<ResolveInfo> activities = pm
                        .queryIntentActivities(new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH), 0);
                if (activities.size() != 0) {
            /* 设备存在 */
                    Intent i = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                    i.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                    i.putExtra(RecognizerIntent.EXTRA_PROMPT, "Speak up bitch!");
                    MyApplication.mCurrentActivity.startActivityForResult(i, check);
                } else {

            /* 没有设备请点击安装按钮进行安装呢 */
                    Toast.makeText(getContext(), "没有安装语音听写软件，例如讯飞语记", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    //step 2, required
    private void KeepStatusBar() {
        WindowManager.LayoutParams attrs = getWindow().getAttributes();
        attrs.flags &= ~WindowManager.LayoutParams.FLAG_FULLSCREEN;
        getWindow().setAttributes(attrs);
    }
}
