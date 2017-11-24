package com.riking.calendar.widget.dialog;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetDialog;
import android.view.View;

import com.riking.calendar.R;

/**
 * Created by zw.zhang on 2017/7/26.
 */

public class ShareBottomDialog extends BottomSheetDialog {

    public ShareBottomDialog(@NonNull Context context) {
        super(context);
        setContentView(R.layout.share_bottom_dialog);
        findViewById(R.id.cancel_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

    }

    public ShareBottomDialog(@NonNull Context context, int theme) {
        super(context, theme);
    }

    protected ShareBottomDialog(@NonNull Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }
}
