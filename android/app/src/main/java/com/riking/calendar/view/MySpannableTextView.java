package com.riking.calendar.view;

import android.content.Context;
import android.content.res.Resources;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatTextView;
import android.text.Layout;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.riking.calendar.R;
import com.riking.calendar.util.ZR;


/**
 * 缩放展开的动画简单Textview
 *
 * @author igeek2014@hotmail.com
 */
public class MySpannableTextView extends AppCompatTextView {

    public MySpannableTextView(Context context) {
        super(context, null);
    }

    public MySpannableTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        limitTextViewString(this.getText().toString(), 140, this, new OnClickListener() {
            @Override
            public void onClick(View v) {
                //设置监听函数
            }
        });
    }


    /**
     * get the last char index for max limit row,if not exceed the limit,return -1
     *
     * @param textView
     * @param content
     * @param width
     * @param maxLine
     * @return
     */
    private int getLastCharIndexForLimitTextView(TextView textView, String content, int width, int maxLine) {
        TextPaint textPaint = textView.getPaint();
        StaticLayout staticLayout = new StaticLayout(content, textPaint, width, Layout.Alignment.ALIGN_NORMAL, 1, 0, false);

        if (staticLayout.getLineCount() > maxLine) {
            int i = staticLayout.getLineStart(maxLine - 1) - 1;
            int end = staticLayout.getLineEnd(maxLine - 1) - 1;
            String s = content.substring(i, end);
            StaticLayout staticLayout2 = new StaticLayout(s + "...展开", textPaint, width, Layout.Alignment.ALIGN_NORMAL, 1, 0, false);
            while (staticLayout2.getLineCount() > 1) {
                end = end - 1;
                s = content.substring(i, end);
                staticLayout2 = new StaticLayout(s + "...展开", textPaint, width, Layout.Alignment.ALIGN_NORMAL, 1, 0, false);
            }
            return end;//exceed
        } else return -1;//not exceed the max line
    }

    /**
     * 限制TextView显示字符字符，并且添加showMore和show more的点击事件
     *
     * @param textString
     * @param textView
     * @param clickListener textView的点击监听器
     */
    public void limitTextViewString(String textString, int maxFirstShowCharCount, final TextView textView, final View.OnClickListener clickListener) {
        //计算处理花费时间
        final long startTime = System.currentTimeMillis();
        if (textView == null) return;
        int width = textView.getWidth();//在recyclerView和ListView中，由于复用的原因，这个TextView可能以前就画好了，能获得宽度
        if (width == 0)
            width = (int) (Resources.getSystem().getDisplayMetrics().widthPixels - ZR.convertDpToPx(30));//获取textView的实际宽度，这里可以用各种方式（一般是dp转px写死）填入TextView的宽度
        int lastCharIndex = getLastCharIndexForLimitTextView(textView, textString, width, 3);
//返回-1表示没有达到行数限制
        if (lastCharIndex < 0 && textString.length() <= maxFirstShowCharCount) {
            //如果行数没超过限制
            textView.setText(textString);
            return;
        }
        //如果超出了行数限制
        textView.setMovementMethod(LinkMovementMethod.getInstance());//this will deprive the recyclerView's focus
        if (lastCharIndex > maxFirstShowCharCount || lastCharIndex < 0) {
            lastCharIndex = maxFirstShowCharCount;
        }
        //构造spannableString
        String explicitText = null;
        String explicitTextAll;
        if (textString.charAt(lastCharIndex) == '\n') {//manual enter
            explicitText = textString.substring(0, lastCharIndex);
        } else if (lastCharIndex > 20) {
            //如果最大行数限制的那一行到达12以后则直接显示 显示更多
            explicitText = textString.substring(0, lastCharIndex);
        }
        int sourceLength = explicitText.length();
        String showMore = "显示更多";
        explicitText = explicitText + "..." + showMore;
        final SpannableString mSpan = new SpannableString(explicitText);


        String dismissMore = "收起";
        explicitTextAll = textString + dismissMore;
        final SpannableString mSpanALL = new SpannableString(explicitTextAll);
        mSpanALL.setSpan(new ClickableSpan() {
            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                ds.setColor(textView.getResources().getColor(R.color.color_999999));
                ds.setAntiAlias(true);
                ds.setUnderlineText(false);
            }

            @Override
            public void onClick(View widget) {
                textView.setText(mSpan);
                textView.setOnClickListener(null);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (clickListener != null)
                            textView.setOnClickListener(clickListener);//prevent the double click
                    }
                }, 20);
            }
        }, textString.length(), explicitTextAll.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        mSpan.setSpan(new ClickableSpan() {
            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                ds.setColor(textView.getResources().getColor(R.color.colorPrimary));
                ds.setAntiAlias(true);
                ds.setUnderlineText(false);
            }

            @Override
            public void onClick(View widget) {//"...show more" click event
                textView.setText(mSpanALL);
                textView.setOnClickListener(null);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (clickListener != null)
                            textView.setOnClickListener(clickListener);//prevent the double click
                    }
                }, 20);
            }
        }, sourceLength, explicitText.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        //设置为“显示更多”状态下的TextVie
        textView.setText(mSpan);

        Log.i("info", "字符串处理耗时" + (System.currentTimeMillis() - startTime) + " ms");
    }
}
