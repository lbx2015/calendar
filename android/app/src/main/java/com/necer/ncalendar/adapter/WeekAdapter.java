package com.necer.ncalendar.adapter;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.necer.ncalendar.listener.OnClickWeekViewListener;
import com.necer.ncalendar.view.WeekView;
import com.riking.calendar.util.ZR;

import org.joda.time.DateTime;

/**
 * Created by 闫彬彬 on 2017/8/30.
 * QQ:619008099
 */

public class WeekAdapter extends CalendarAdapter {

    private OnClickWeekViewListener mOnClickWeekViewListener;

    public WeekAdapter(Context mContext, int count, int curr, DateTime dateTime, OnClickWeekViewListener onClickWeekViewListener) {
        super(mContext, count, curr, dateTime);
        this.mOnClickWeekViewListener = onClickWeekViewListener;
    }


    @Override
    public Object instantiateItem(ViewGroup container, int position) {

        WeekView nWeekView = (WeekView) mCalendarViews.get(position);
        if (nWeekView == null) {
            nWeekView = new WeekView(mContext, mDateTime.plusDays((position - mCurr) * 7),mOnClickWeekViewListener);
            mCalendarViews.put(position, nWeekView);
            nWeekView.fragment = fragment;
        }
        //Riking adding a parent ViewGroup for the week view
        LinearLayout f = new LinearLayout(mContext);
        LinearLayout.LayoutParams l = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        int m = (int) ZR.convertDpToPx(mContext, 10);
        f.setPadding(m, 0, m, 0);
        f.setLayoutParams(l);
        f.addView(nWeekView);
        container.addView(f);
        return f;
    }
}
