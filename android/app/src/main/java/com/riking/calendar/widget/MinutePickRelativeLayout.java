package com.riking.calendar.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.RelativeLayout;

import com.riking.calendar.R;

/**
 * Created by zw.zhang on 2017/7/27.
 */

public class MinutePickRelativeLayout extends RelativeLayout {
    public MinutePickRelativeLayout(Context context) {
        super(context);
    }

    public MinutePickRelativeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MinutePickRelativeLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public MinutePickRelativeLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Paint paint = new Paint();
        paint.setColor(getResources().getColor(R.color.divider_color_d9d9d9));
        float density = getResources().getDisplayMetrics().scaledDensity;
        Log.d("zzw", "density: " + density);
        float halfFontSize = density * 14;
        paint.setStrokeWidth(getResources().getDisplayMetrics().scaledDensity);

        int middleY = getMeasuredHeight() / 2;
        canvas.drawLine(0, middleY - halfFontSize, getMeasuredWidth(), middleY - halfFontSize, paint);
        canvas.drawLine(0, middleY + halfFontSize, getMeasuredWidth(), middleY + halfFontSize, paint);
    }
}
