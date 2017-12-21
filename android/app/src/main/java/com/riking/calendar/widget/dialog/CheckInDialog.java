package com.riking.calendar.widget.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.riking.calendar.R;
import com.riking.calendar.util.ZR;
import com.riking.calendar.view.ZEnterImageView;

/**
 * Created by zw.zhang on 2017/12/14.
 */

public class CheckInDialog extends Dialog {
    public ZEnterImageView zEnterImageView;
    int experience;

    public CheckInDialog(@NonNull Context context) {
        super(context);
    }

    public CheckInDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
    }

    protected CheckInDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    public void setExperience(int experience) {
        this.experience = experience;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        requestWindowFeature(Window.FEATURE_NO_TITLE);

        //set background transparent
//        getWindow().setBackgroundDrawable(new ColorDrawable(ZR.getColor(android.R.color.transparent)));
        //disable cancel the dialog on touch
//        setCanceledOnTouchOutside(false);

    }

    @Override
    protected void onStart() {
        super.onStart();
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //set background transparent
        getWindow().setBackgroundDrawable(new ColorDrawable(ZR.getColor(android.R.color.transparent)));
        //disable cancel the dialog on touch
//        setCanceledOnTouchOutside(false);

        findViewById(R.id.close_im).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        findViewById(R.id.i_konw_tv).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        ((TextView) findViewById(R.id.current_experience)).setText("当前" + experience + "积分");
    }
}
