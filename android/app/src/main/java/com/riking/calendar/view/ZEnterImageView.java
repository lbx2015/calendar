package com.riking.calendar.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.text.TextPaint;
import android.util.AttributeSet;

import com.riking.calendar.R;
import com.riking.calendar.jiguang.Logger;
import com.riking.calendar.util.ZR;

/**
 * Created by zw.zhang on 2017/8/24.
 * This custom image view not scale.
 * using ScaleType.Center by default.
 */

public class ZEnterImageView extends android.support.v7.widget.AppCompatImageView {

    float textSize;
    AttributeSet attrs;
    String text = "立即进入";

    public ZEnterImageView(Context context) {
        super(context);
        init();
    }

    public ZEnterImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.ZEnterImageView);
        text = a.getText(R.styleable.ZEnterImageView_buttonText).toString();
        init();
    }

    public ZEnterImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        float textSize = ZR.convertDpToPx(getContext(), 17);
        TextPaint paint = new TextPaint();
        paint.setColor(Color.WHITE);
        paint.setTextSize(textSize);
        int imgWidth = getMeasuredWidth();
        int imgHeight = getMeasuredHeight();
        float txtWidth = paint.measureText(text);

        int x = (int) (imgWidth / 2 - txtWidth / 2);
        int y = (int) (imgHeight / 2 + textSize / 3); // 6 is half of the text size
        Logger.d("zzw", "draw text; " + text);
        canvas.drawText(text, x, y, paint);
    }

    private void init() {
        setScaleType(ScaleType.CENTER);

    }
}
