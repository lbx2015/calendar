package com.necer.ncalendar.adapter;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.necer.ncalendar.listener.OnClickMonthViewListener;
import com.necer.ncalendar.view.MonthView;
import com.riking.calendar.util.ZR;

import org.joda.time.DateTime;

/**
 * Created by 闫彬彬 on 2017/8/28.
 * QQ:619008099
 */

public class MonthAdapter extends CalendarAdapter {

    private OnClickMonthViewListener mOnClickMonthViewListener;

    public MonthAdapter(Context mContext, int count, int curr, DateTime dateTime, OnClickMonthViewListener onClickMonthViewListener) {
        super(mContext, count, curr, dateTime);
        this.mOnClickMonthViewListener = onClickMonthViewListener;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        container.removeAllViews();//clear children firstly
        MonthView nMonthView = (MonthView) mCalendarViews.get(position);
        if (nMonthView == null) {
            int i = position - mCurr;
            DateTime dateTime = this.mDateTime.plusMonths(i);
            nMonthView = new MonthView(mContext, dateTime, mOnClickMonthViewListener);
            mCalendarViews.put(position, nMonthView);
            nMonthView.fragment = fragment;
        }

        //Riking adding a parent view group for the month view
        LinearLayout f = new LinearLayout(mContext);
        LinearLayout.LayoutParams l = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        int m = (int) ZR.convertDpToPx(mContext, 10);
        f.setPadding(m, 0, m, 0);
        f.setLayoutParams(l);
        f.addView(nMonthView);
        container.addView(f);
        return f;
    }
}
