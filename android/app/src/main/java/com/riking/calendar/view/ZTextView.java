package com.riking.calendar.view;

import android.content.Context;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;

/**
 * Created by zw.zhang on 2018/1/17.
 */

public class ZTextView extends AppCompatTextView {

    public ZTextView(Context context) {
        super(context);
        init();
    }

    public ZTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ZTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        setLineSpacing(0, 1.2f);
    }
}
