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

/**
 * Created by zw.zhang on 2017/12/14.
 */

public class CheckInFailDialog extends Dialog {
    int experience;

    public CheckInFailDialog(@NonNull Context context) {
        super(context);
    }

    public CheckInFailDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
    }

    protected CheckInFailDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        //set background transparent
        getWindow().setBackgroundDrawable(new ColorDrawable(ZR.getColor(android.R.color.transparent)));
        //disable cancel the dialog on touch
//        setCanceledOnTouchOutside(false);
        setContentView(R.layout.dialog_check_in_fail);

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


    }
}