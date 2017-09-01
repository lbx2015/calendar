package com.riking.calendar.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Display;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.riking.calendar.R;
import com.riking.calendar.activity.AddRemindActivity;
import com.riking.calendar.activity.ViewPagerActivity;
import com.riking.calendar.adapter.CalendarGridViewAdapter;
import com.riking.calendar.adapter.ReminderAdapter;
import com.riking.calendar.adapter.ReportAdapter;
import com.riking.calendar.adapter.TaskAdapter;
import com.riking.calendar.jiguang.Logger;
import com.riking.calendar.realm.model.QueryReportContainerRealmModel;
import com.riking.calendar.realm.model.Reminder;
import com.riking.calendar.realm.model.Task;
import com.riking.calendar.retrofit.APIClient;
import com.riking.calendar.retrofit.APIInterface;
import com.riking.calendar.util.CONST;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;
import io.realm.Sort;

/**
 * Created by zw.zhang on 2017/7/11.
 */

public class FirstFragment extends Fragment {
    private static int jumpMonth = 0; // 每次滑动，增加或减去一个月,默认为0（即显示当前月）
    private static int jumpYear = 0; // 滑动跨越一年，则增加或者减去一年,默认为0(即当前年)
    ViewPagerActivity a;
    RecyclerView recyclerView;
    RecyclerView taskRecyclerView;
    RecyclerView reportRecyclerView;
    ReminderAdapter reminderAdapter;
    CardView firstCardView;
    CardView secondCardView;
    CardView thirdCardView;
    TaskAdapter taskAdapter;
    Realm realm;
    APIInterface apiInterface;
    RealmResults<Reminder> reminders;
    private GestureDetector gestureDetector = null;
    private CalendarGridViewAdapter calV = null;
    private ViewFlipper flipper = null;
    private GridView gridView = null;
    //current year
    private int year_c = 0;
    //current month
    private int month_c = 0;
    /**
     * 上个月
     */
//    private ImageView prevMonth;
    //current day
    private int day_c = 0;
    /**
     * 下个月
     */
//    private ImageView nextMonth;
    /**
     * 每次添加gridview到viewflipper中时给的标记
     */
    private int gvFlag = 0;
    /**
     * 当前的年月，现在日历顶端
     */
    private TextView currentMonth;
    private View add;

    private void setListener() {
        View.OnClickListener c = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
//                    case R.id.nextMonth: // 下一个月
//                        enterNextMonth(gvFlag);
//                        break;
//                    case R.id.prevMonth: // 上一个月
//                        enterPrevMonth(gvFlag);
//                        break;
                    case R.id.add: {
                        startActivity(new Intent(getActivity(), AddRemindActivity.class));
                        break;
                    }
                    default:
                        break;
                }
            }
        };
//        prevMonth.setOnClickListener(c);
        add.setOnClickListener(c);
//        nextMonth.setOnClickListener(c);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        a = (ViewPagerActivity) getActivity();
        Log.d("zzw", this + " onCreate");
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-M-d");
        String currentDate = sdf.format(date); // 当期日期
        year_c = Integer.parseInt(currentDate.split("-")[0]);
        month_c = Integer.parseInt(currentDate.split("-")[1]);
        day_c = Integer.parseInt(currentDate.split("-")[2]);

        a.setTitle(R.string.calendar);
    }

    public void enterCurrentMonth() {
        if (calV.realCurrentDayPositionFlag > 0) {
            if (calV.realCurrentDayPositionFlag != calV.currentFlag) {
                calV.currentFlag = calV.realCurrentDayPositionFlag;
                calV.notifyDataSetChanged();
            }
            //do nothing if already in current month.
            return;
        }
        addGridView(); // 添加一个gridView
        //current month
        calV = new CalendarGridViewAdapter(a, a.getResources(), 0, 0, year_c, month_c, day_c, reminders);
        gridView.setAdapter(calV);
        addTextToTopTextView(currentMonth); // 移动到下一月后，将当月显示在头标题中
        flipper.addView(gridView, 1);
        Log.d("zzw", jumpMonth + "jumpMonth + month_c" + (jumpMonth + month_c));
        if (jumpMonth > 0) {
            flipper.setInAnimation(AnimationUtils.loadAnimation(a, R.anim.push_right_in));
            flipper.setOutAnimation(AnimationUtils.loadAnimation(a, R.anim.push_right_out));
            flipper.showPrevious();
        } else if (jumpMonth < 0) {
            flipper.setInAnimation(AnimationUtils.loadAnimation(a, R.anim.push_left_in));
            flipper.setOutAnimation(AnimationUtils.loadAnimation(a, R.anim.push_left_out));
            flipper.showNext();
        }
        //restore the jumpMonth and jumpYear to zero
        jumpMonth = 0;
        jumpYear = 0;
        flipper.removeViewAt(0);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d("zzw", this + " onCreateView");
        View v = inflater.inflate(R.layout.first_fragment, container, false);
//        prevMonth = (ImageView) v.findViewById(R.id.prevMonth);
//        nextMonth = (ImageView) v.findViewById(R.id.nextMonth);
        add = v.findViewById(R.id.add);
        firstCardView = (CardView) v.findViewById(R.id.first_cardview);
        secondCardView = (CardView) v.findViewById(R.id.second_cardview);
        thirdCardView = (CardView) v.findViewById(R.id.third_cardview);
        setListener();
        currentMonth = (TextView) v.findViewById(R.id.currentMonth);
        TextView todayButton = (TextView) v.findViewById(R.id.today_button);
        todayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Logger.d("zzw", "click today button.");
                enterCurrentMonth();
            }
        });

        gestureDetector = new GestureDetector(a, new FirstFragment.MyGestureListener());
        flipper = (ViewFlipper) v.findViewById(R.id.flipper);
        flipper.removeAllViews();
        calV = new CalendarGridViewAdapter(a, a.getResources(), jumpMonth, jumpYear, year_c, month_c, day_c, reminders);
        addGridView();
        gridView.setAdapter(calV);
        gridView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                gridView.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                View lastChild = gridView.getChildAt(gridView.getChildCount() - 1);
                Logger.d("zzw", "enterNextMonth calV.dayOfWeek " + calV.dayOfWeek + " calV.daysOfCurrentMonth: " + calV.daysOfCurrentMonth.size());
                ViewGroup.LayoutParams params = flipper.getLayoutParams();
                Logger.d("zzw", "flipper height: " + params.height);
                if (calV.getCount() > 35) {
                    params.height = lastChild.getMeasuredHeight() * 6;
                } else {
                    params.height = lastChild.getMeasuredHeight() * 5;
                }
                Logger.d("zzw", "reset flipper height: " + params.height);
                flipper.setLayoutParams(params);
            }
        });

//            flipper.invalidate();
        Logger.d("zzw", "after reset layout params flipper height: " + flipper.getLayoutParams().height);

        flipper.addView(gridView, 0);
        addTextToTopTextView(currentMonth);
        // Create the Realm instance
        realm = Realm.getDefaultInstance();
        //insert  to realm
        // All writes must be wrapped in a transaction to facilitate safe multi threading
//        realm.executeTransaction(new Realm.Transaction() {
//            @Override
//            public void execute(Realm realm) {
//                // Add a person
//                Reminder person = realm.createObject(Reminder.class, UUID.randomUUID().toString());
//                person.timeView = new Date(2017,6,24,12,12);
//                person.title = "Don't forget to clock off";
//            }
//        });

        recyclerView = (RecyclerView) v.findViewById(R.id.recycler_view);
        taskRecyclerView = (RecyclerView) v.findViewById(R.id.task_recycler_view);
        reportRecyclerView = (RecyclerView) v.findViewById(R.id.report_recycler_view);
        reportRecyclerView.setLayoutManager(new LinearLayoutManager(a));
        taskRecyclerView.setLayoutManager(new LinearLayoutManager(a));
        recyclerView.setLayoutManager(new LinearLayoutManager(a));
        initReminderAdapter();

        //only show the not complete tasks
        RealmResults<Task> tasks = realm.where(Task.class).equalTo(Task.IS_COMPLETE, 0).findAll();
        taskRecyclerView.setItemAnimator(new DefaultItemAnimator());
        taskAdapter = new TaskAdapter(tasks, realm);
        taskRecyclerView.setAdapter(taskAdapter);
        if (taskAdapter.getItemCount() == 0) {
            secondCardView.setVisibility(View.GONE);
        }

        realm.addChangeListener(new RealmChangeListener<Realm>() {
            @Override
            public void onChange(Realm realm) {
                if (taskAdapter.getItemCount() == 0) {
                    secondCardView.setVisibility(View.GONE);
                } else {
                    secondCardView.setVisibility(View.VISIBLE);
                }
                //the data is changed.
                taskAdapter.notifyDataSetChanged();
            }
        });

        apiInterface = APIClient.getClient().create(APIInterface.class);

//        apiInterface.getAllReports(null).enqueue(new ZCallBack<ResponseModel<QueryReportModel>>() {
//            @Override
//            public void callBack(ResponseModel<QueryReportModel> response) {
//                if (response != null) {
//                    Logger.d("zzw", "success loaded reports: " + response._data);
//                    reportRecyclerView.setAdapter(new ReportAdapter(response._data));
//                }
//            }
//        });

        RealmResults<QueryReportContainerRealmModel> reports = realm.where(QueryReportContainerRealmModel.class).findAll();
        Logger.d("zzw", "report adapter size: " + reports.size());
        final ReportAdapter reportAdapter = new ReportAdapter(reports);
        reportRecyclerView.setAdapter(reportAdapter);
        realm.addChangeListener(new RealmChangeListener<Realm>() {
            @Override
            public void onChange(Realm realm) {
                reportAdapter.notifyDataSetChanged();
            }
        });

        //set the layout params
        FrameLayout scrollView = (FrameLayout) v.findViewById(R.id.nested_recyclerview);
        CoordinatorLayout.LayoutParams paramss = (CoordinatorLayout.LayoutParams) scrollView.getLayoutParams();
        final int marginBottom = a.bottomTabs.getMeasuredHeight();
        paramss.setMargins(0, 0, 0, marginBottom);
        scrollView.setLayoutParams(paramss);
        return v;
    }

    public void initReminderAdapter() {
        final Date date = new Date();
        final Calendar c = Calendar.getInstance();
        c.setTime(date);

        int weekDay = c.get(Calendar.DAY_OF_WEEK);
        if (weekDay == Calendar.SUNDAY) {
            weekDay = 7;
        } else {
            weekDay--;
        }
        updateReminderAdapter(date, weekDay);
    }

    private void updateReminderAdapter(Date date, int weekDay) {

        SimpleDateFormat dayFormat = new SimpleDateFormat("yyyyMMdd");
        reminders = realm.where(Reminder.class).beginGroup().equalTo("day", dayFormat.format(date)).equalTo("repeatFlag", 0).endGroup()
                .or().beginGroup()
                .equalTo("repeatFlag", CONST.REPEAT_FLAG_WEEK)
                .contains("repeatWeek", String.valueOf(weekDay))
                .endGroup()
                .findAllSorted("time", Sort.ASCENDING);
        reminderAdapter = new ReminderAdapter(reminders, realm);
        recyclerView.setAdapter(reminderAdapter);
        if (reminders.size() == 0) {
            firstCardView.setVisibility(View.GONE);
        } else {
            firstCardView.setVisibility(View.VISIBLE);
        }

        realm.addChangeListener(new RealmChangeListener<Realm>() {
            @Override
            public void onChange(Realm realm) {
                if (reminderAdapter.getItemCount() == 0) {
                    firstCardView.setVisibility(View.GONE);
                } else {
                    firstCardView.setVisibility(View.VISIBLE);
                }

                reminderAdapter.notifyDataSetChanged();
            }
        });
        calV.updateReminders(reminders);
    }

    /**
     * 移动到下一个月
     *
     * @param gvFlag
     */
    private void enterNextMonth(int gvFlag) {
        addGridView(); // 添加一个gridView
        jumpMonth++; // 下一个月

        calV = new CalendarGridViewAdapter(a, this.getResources(), jumpMonth, jumpYear, year_c, month_c, day_c, reminders);
        gridView.setAdapter(calV);
        addTextToTopTextView(currentMonth); // 移动到下一月后，将当月显示在头标题中
        gvFlag++;

        gridView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                gridView.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                View lastChild = gridView.getChildAt(gridView.getChildCount() - 1);
                Logger.d("zzw", "enterNextMonth calV.dayOfWeek " + calV.dayOfWeek + " calV.daysOfCurrentMonth: " + calV.daysOfCurrentMonth.size());
                ViewGroup.LayoutParams params = flipper.getLayoutParams();
                Logger.d("zzw", "flipper height: " + params.height);
                if (calV.getCount() > 35) {
                    params.height = lastChild.getMeasuredHeight() * 6;
                } else {
                    params.height = lastChild.getMeasuredHeight() * 5;
                }
                Logger.d("zzw", "reset flipper height: " + params.height);
                flipper.setLayoutParams(params);
            }
        });

        flipper.addView(gridView, gvFlag);
        flipper.setInAnimation(AnimationUtils.loadAnimation(a, R.anim.push_left_in));
        flipper.setOutAnimation(AnimationUtils.loadAnimation(a, R.anim.push_left_out));
        flipper.showNext();
        flipper.removeViewAt(0);
    }

    /**
     * 移动到上一个月
     *
     * @param gvFlag
     */
    private void enterPrevMonth(int gvFlag) {
        addGridView(); // 添加一个gridView
        jumpMonth--; // 上一个月

        calV = new CalendarGridViewAdapter(a, this.getResources(), jumpMonth, jumpYear, year_c, month_c, day_c, reminders);
        gridView.setAdapter(calV);
        gvFlag++;
        addTextToTopTextView(currentMonth);// 移动到上一月后，将当月显示在头标题中

        gridView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                gridView.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                View lastChild = gridView.getChildAt(gridView.getChildCount() - 1);
                Logger.d("zzw", "enterNextMonth calV.dayOfWeek " + calV.dayOfWeek + " calV.daysOfCurrentMonth: " + calV.daysOfCurrentMonth.size());
                ViewGroup.LayoutParams params = flipper.getLayoutParams();
                Logger.d("zzw", "flipper height: " + params.height);
                if (calV.getCount() > 35) {
                    params.height = lastChild.getMeasuredHeight() * 6;
                } else {
                    params.height = lastChild.getMeasuredHeight() * 5;
                }
                Logger.d("zzw", "reset flipper height: " + params.height);
                flipper.setLayoutParams(params);
                flipper.invalidate();
            }
        });

        //addView 方法中的index越大，View显示越上面。
        flipper.addView(gridView, gvFlag);

        flipper.setInAnimation(AnimationUtils.loadAnimation(a, R.anim.push_right_in));
        flipper.setOutAnimation(AnimationUtils.loadAnimation(a, R.anim.push_right_out));
        flipper.showPrevious();
        flipper.removeViewAt(0);
    }

    /**
     * 添加头部的年份 闰哪月等信息
     *
     * @param view
     */
    public void addTextToTopTextView(TextView view) {
        StringBuffer textDate = new StringBuffer();
        // draw = getResources().getDrawable(R.drawable.top_day);
        // view.setBackgroundDrawable(draw);
        textDate.append(calV.getShowYear()).append("年").append(calV.getShowMonth()).append("月").append("\t");
        view.setText(textDate);
    }

    private void addGridView() {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT, AbsListView.LayoutParams.WRAP_CONTENT);
        // 取得屏幕的宽度和高度
        WindowManager windowManager = a.getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        int Width = display.getWidth();
        int Height = display.getHeight();

        gridView = new GridView(a);
        gridView.setNumColumns(7);
//        gridView.setColumnWidth(40);
        // gridView.setStretchMode(GridView.STRETCH_COLUMN_WIDTH);
//        if (Width == 720 && Height == 1280) {
//            gridView.setColumnWidth(40);
//        }
        gridView.setGravity(Gravity.CENTER_VERTICAL);
        gridView.setSelector(new ColorDrawable(Color.TRANSPARENT));
        // 去除gridView边框
        gridView.setVerticalSpacing(0);
        gridView.setHorizontalSpacing(0);
        gridView.setOnTouchListener(new View.OnTouchListener() {
            // 将gridview中的触摸事件回传给gestureDetector

            public boolean onTouch(View v, MotionEvent event) {
                ViewParent parent = v.getParent();
                // or get a reference to the ViewPager and cast it to ViewParent
                parent.requestDisallowInterceptTouchEvent(true);
                return FirstFragment.this.gestureDetector.onTouchEvent(event);
                //consume the touch event to enable the month change
            }
        });

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
                // TODO Auto-generated method stub
                // 点击任何一个item，得到这个item的日期(排除点击的是周日到周六(点击不响应))
                int startPosition = calV.getStartPositon();
                int endPosition = calV.getEndPosition();
                if (startPosition <= position + 7 && position <= endPosition - 7) {
                    String scheduleDay = calV.getDateByClickItem(position).split("\\.")[0]; // 这一天的阳历
                    // String scheduleLunarDay =
                    // calV.getDateByClickItem(position).split("\\.")[1];
                    // //这一天的阴历
                    String scheduleYear = calV.getShowYear();
                    String scheduleMonth = calV.getShowMonth();
                    calV.currentFlag = position;
                    calV.notifyDataSetChanged();

                    final Calendar c = Calendar.getInstance();
                    c.set(Calendar.YEAR, Integer.parseInt(scheduleYear));
                    c.set(Calendar.MONTH, Integer.parseInt(scheduleMonth) - 1);
                    c.set(Calendar.DATE, Integer.parseInt(scheduleDay));

                    int weekDay = c.get(Calendar.DAY_OF_WEEK);
                    if (weekDay == Calendar.SUNDAY) {
                        weekDay = 7;
                    } else {
                        weekDay--;
                    }
                    updateReminderAdapter(c.getTime(), weekDay);

                    Toast.makeText(a, scheduleYear + "-" + scheduleMonth + "-" + scheduleDay, Toast.LENGTH_LONG).show();
                    // Toast.makeText(CalendarActivity.this, "点击了该条目",
                    // Toast.LENGTH_SHORT).show();z
                }
            }
        });
        gridView.setLayoutParams(params);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        realm.close();
    }

    private class MyGestureListener extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            int gvFlag = 0; // 每次添加gridview到viewflipper中时给的标记
            if (e1.getX() - e2.getX() > 120) {
                // 像左滑动
                enterNextMonth(gvFlag);
                return true;
            } else if (e1.getX() - e2.getX() < -120) {
                // 向右滑动
                enterPrevMonth(gvFlag);
                return true;
            }
            return false;
        }
    }
}
