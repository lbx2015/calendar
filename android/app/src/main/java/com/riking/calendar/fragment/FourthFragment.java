package com.riking.calendar.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ldf.calendar.Utils;
import com.ldf.calendar.component.CalendarAttr;
import com.ldf.calendar.component.CalendarViewAdapter;
import com.ldf.calendar.interf.OnSelectDateListener;
import com.ldf.calendar.model.CalendarDate;
import com.ldf.calendar.view.Calendar;
import com.ldf.calendar.view.MonthPager;
import com.riking.calendar.R;
import com.riking.calendar.adapter.ExampleAdapter;
import com.riking.calendar.view.CustomDayView;
import com.riking.calendar.view.ThemeDayView;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by zw.zhang on 2017/7/11.
 */

public class FourthFragment extends Fragment {
    TextView textViewYearDisplay;
    TextView textViewMonthDisplay;
    TextView backToday;
    CoordinatorLayout content;
    MonthPager monthPager;
    RecyclerView rvToDoList;
    TextView scrollSwitch;
    TextView themeSwitch;

    private ArrayList<Calendar> currentCalendars = new ArrayList<>();
    private CalendarViewAdapter calendarAdapter;
    private OnSelectDateListener onSelectDateListener;
    private int mCurrentPage = MonthPager.CURRENT_DAY_INDEX;
    private Context context;
    private CalendarDate currentDate;
    private boolean initiated = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fourth_fragment, container, false);
        context = getActivity();
        content = (CoordinatorLayout) v.findViewById(R.id.content);
        monthPager = (MonthPager) v.findViewById(R.id.calendar_view);
        textViewYearDisplay = (TextView) v.findViewById(R.id.show_year_view);
        textViewMonthDisplay = (TextView) v.findViewById(R.id.show_month_view);
        backToday = (TextView) v.findViewById(R.id.back_today_button);
        scrollSwitch = (TextView) v.findViewById(R.id.scroll_switch);
        themeSwitch = (TextView) v.findViewById(R.id.theme_switch);
        rvToDoList = (RecyclerView) v.findViewById(R.id.list);
        rvToDoList.setHasFixedSize(true);
        rvToDoList.setLayoutManager(new LinearLayoutManager(context));//这里用线性显示 类似于listview
        rvToDoList.setAdapter(new ExampleAdapter(context));
        initCurrentDate();
        initCalendarView();
        initToolbarClickListener();
        return v;
    }


    private void initToolbarClickListener() {
        backToday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickBackToDayBtn();
            }
        });
        scrollSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (calendarAdapter.getCalendarType() == CalendarAttr.CalendayType.WEEK) {
                    Utils.scrollTo(content, rvToDoList, monthPager.getViewHeight(), 200);
                    calendarAdapter.switchToMonth();
                } else {
                    Utils.scrollTo(content, rvToDoList, monthPager.getCellHeight(), 200);
                    calendarAdapter.switchToWeek(monthPager.getRowIndex());
                }
            }
        });
        themeSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                refreshSelectBackground();
            }
        });
    }

    private void initCurrentDate() {
        currentDate = new CalendarDate();
        textViewYearDisplay.setText(currentDate.getYear() + "年");
        textViewMonthDisplay.setText(currentDate.getMonth() + "");
    }

    private void initCalendarView() {
        initListener();
        CustomDayView customDayView = new CustomDayView(context, R.layout.custom_day);
        calendarAdapter = new CalendarViewAdapter(
                context,
                onSelectDateListener,
                CalendarAttr.CalendayType.MONTH,
                customDayView);
        initMarkData();
        initMonthPager();
    }

    private void initMarkData() {
        HashMap<String, String> markData = new HashMap<>();
        markData.put("2017-8-9", "1");
        markData.put("2017-7-9", "0");
        markData.put("2017-6-9", "1");
        markData.put("2017-6-10", "0");
        calendarAdapter.setMarkData(markData);
    }

    private void initListener() {
        onSelectDateListener = new OnSelectDateListener() {
            @Override
            public void onSelectDate(CalendarDate date) {
                refreshClickDate(date);
            }

            @Override
            public void onSelectOtherMonth(int offset) {
                //偏移量 -1表示上一个月 ， 1表示下一个月
                monthPager.selectOtherMonth(offset);
            }
        };
    }

    private void refreshClickDate(CalendarDate date) {
        currentDate = date;
        textViewYearDisplay.setText(date.getYear() + "年");
        textViewMonthDisplay.setText(date.getMonth() + "");
    }

    private void initMonthPager() {
        monthPager.setAdapter(calendarAdapter);
        monthPager.setCurrentItem(MonthPager.CURRENT_DAY_INDEX);
        monthPager.setPageTransformer(false, new ViewPager.PageTransformer() {
            @Override
            public void transformPage(View page, float position) {
                position = (float) Math.sqrt(1 - Math.abs(position));
                page.setAlpha(position);
            }
        });
        monthPager.addOnPageChangeListener(new MonthPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                mCurrentPage = position;
                currentCalendars = calendarAdapter.getPagers();
                if (currentCalendars.get(position % currentCalendars.size()) instanceof Calendar) {
                    CalendarDate date = currentCalendars.get(position % currentCalendars.size()).getSeedDate();
                    currentDate = date;
                    textViewYearDisplay.setText(date.getYear() + "年");
                    textViewMonthDisplay.setText(date.getMonth() + "");
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }

    public void onClickBackToDayBtn() {
        refreshMonthPager();
    }

    private void refreshMonthPager() {
        CalendarDate today = new CalendarDate();
        calendarAdapter.notifyDataChanged(today);
        refreshClickDate(today);
    }

    private void refreshSelectBackground() {
        ThemeDayView themeDayView = new ThemeDayView(context, R.layout.custom_day_focus);
        calendarAdapter.setCustomDayRenderer(themeDayView);
        calendarAdapter.notifyDataSetChanged();
        calendarAdapter.notifyDataChanged(new CalendarDate());
    }

    //ViewPager适配器
    class MyPagerAdapter extends FragmentPagerAdapter {

        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            return 2;//页卡数
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return new ReminderFragment();
                case 1:
                    return new TaskFragment();
            }
            return null;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            return super.instantiateItem(container, position);
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView(container);//删除页卡
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "提醒";
                case 1:
                    return "代办";
            }

            return null;
        }
    }
}
