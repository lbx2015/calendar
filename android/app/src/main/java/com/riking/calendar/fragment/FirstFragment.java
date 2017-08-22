package com.riking.calendar.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.widget.AbsListView;
import android.widget.AdapterView;
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
import com.riking.calendar.pojo.QueryReport;
import com.riking.calendar.pojo.QueryReportModel;
import com.riking.calendar.realm.model.Reminder;
import com.riking.calendar.realm.model.Task;
import com.riking.calendar.retrofit.APIClient;
import com.riking.calendar.retrofit.APIInterface;
import com.riking.calendar.util.CONST;
import com.riking.calendar.util.DateUtil;
import com.riking.calendar.util.LunarCalendar;
import com.riking.calendar.util.ZR;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;
import io.realm.Sort;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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
    TextView timeView;
    TextView weekDayView;
    ReminderAdapter reminderAdapter;
    TaskAdapter taskAdapter;
    Realm realm;
    APIInterface apiInterface;
    private GestureDetector gestureDetector = null;
    private CalendarGridViewAdapter calV = null;
    private ViewFlipper flipper = null;
    private GridView gridView = null;
    //current year
    private int year_c = 0;
    //current month
    private int month_c = 0;
    //current day
    private int day_c = 0;
    /**
     * 上个月
     */
//    private ImageView prevMonth;
    /**
     * 下个月
     */
//    private ImageView nextMonth;
    private String currentDate = "";
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
        currentDate = sdf.format(date); // 当期日期
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
        calV = new CalendarGridViewAdapter(a, a.getResources(), 0, 0, year_c, month_c, day_c);
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
        timeView = (TextView) v.findViewById(R.id.time);
        weekDayView = (TextView) v.findViewById(R.id.week_day);
//        prevMonth = (ImageView) v.findViewById(R.id.prevMonth);
//        nextMonth = (ImageView) v.findViewById(R.id.nextMonth);
        add = v.findViewById(R.id.add);
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
        calV = new CalendarGridViewAdapter(a, a.getResources(), jumpMonth, jumpYear, year_c, month_c, day_c);
        addGridView();
        gridView.setAdapter(calV);
        Logger.d("zzw", "enterNextMonth calV.dayOfWeek " + calV.dayOfWeek + " calV.daysOfCurrentMonth: " + calV.daysOfCurrentMonth.size());
        if (calV.daysOfCurrentMonth.size() <= 35) {
            ViewGroup.LayoutParams params = flipper.getLayoutParams();
            Logger.d("zzw", "flipper height: " + params.height);
            params.height = (int) ZR.convertDpToPx(getContext(), 280);
            Logger.d("zzw", "reset flipper height: " + params.height);
            flipper.setLayoutParams(params);
//            flipper.invalidate();
            Logger.d("zzw", "after reset layout params flipper height: " + flipper.getLayoutParams().height);
        }

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

        final Date date = new Date();
        final Calendar c = Calendar.getInstance();
        c.setTime(date);

        int weekDay = c.get(Calendar.DAY_OF_WEEK);
        if (weekDay == Calendar.SUNDAY) {
            weekDay = 7;
        } else {
            weekDay--;
        }

        LunarCalendar lc = new LunarCalendar();
        SimpleDateFormat chineseFormat = new SimpleDateFormat("yyyy年MM月dd日");
        timeView.setText(chineseFormat.format(date));
        String weekNo = "第" + c.get(Calendar.WEEK_OF_YEAR) + "周";
        weekDayView.setText(weekNo + "   " + DateUtil.getWeekNameInChinese(weekDay)
                + " " + lc.getLunarDate(c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DATE), false));

        SimpleDateFormat dayFormat = new SimpleDateFormat("yyyyMMdd");
        final RealmResults<Reminder> reminders = realm.where(Reminder.class).beginGroup().equalTo("day", dayFormat.format(new Date())).equalTo("repeatFlag", 0).endGroup()
                .or().beginGroup()
                .equalTo("repeatFlag", CONST.REPEAT_FLAG_WEEK)
                .contains("repeatWeek", String.valueOf(weekDay))
                .endGroup()
                .findAllSorted("time", Sort.ASCENDING);
        reminderAdapter = new ReminderAdapter(reminders, realm);
        recyclerView.setAdapter(reminderAdapter);
        realm.addChangeListener(new RealmChangeListener<Realm>() {
            @Override
            public void onChange(Realm realm) {
                reminderAdapter.notifyDataSetChanged();
            }
        });

        //only show the not complete tasks
        RealmResults<Task> tasks = realm.where(Task.class).equalTo(Task.IS_COMPLETE, 0).findAll();
        taskRecyclerView.setItemAnimator(new DefaultItemAnimator());
        taskAdapter = new TaskAdapter(tasks, realm);
        taskRecyclerView.setAdapter(taskAdapter);

        apiInterface = APIClient.getClient().create(APIInterface.class);

        realm.addChangeListener(new RealmChangeListener<Realm>() {
            @Override
            public void onChange(Realm realm) {
                //the data is changed.
                taskAdapter.notifyDataSetChanged();
            }
        });
     /*   LinkedHashMap<String, List<Report>> reports = new LinkedHashMap<String, List<Report>>();
        ArrayList<Report> list = new ArrayList<>();
        Report r = new Report();
        r.id = "ida";
        r.moduleType = "module type";
        r.reportCode = "report code";
        r.reportName = "report name";
        list.add(r);
        reports.put("title", list);
        reportRecyclerView.setAdapter(new ReportAdapter(reports));*/

        apiInterface.getAllReports(new QueryReport()).enqueue(new Callback<QueryReportModel>() {
            @Override
            public void onResponse(Call<QueryReportModel> call, Response<QueryReportModel> response) {
                QueryReportModel reports = response.body();
                if (reports != null) {
                    Logger.d("zzw", "success loaded reports: " + reports);
                    reportRecyclerView.setAdapter(new ReportAdapter(reports));
                }
            }

            @Override
            public void onFailure(Call<QueryReportModel> call, Throwable t) {
                Logger.d("zzw", "reports loaded failed: " + t.getMessage());
            }
        });
        return v;
    }

    /**
     * 移动到下一个月
     *
     * @param gvFlag
     */
    private void enterNextMonth(int gvFlag) {
        addGridView(); // 添加一个gridView
        jumpMonth++; // 下一个月

        calV = new CalendarGridViewAdapter(a, this.getResources(), jumpMonth, jumpYear, year_c, month_c, day_c);
        gridView.setAdapter(calV);
        addTextToTopTextView(currentMonth); // 移动到下一月后，将当月显示在头标题中
        gvFlag++;

        Logger.d("zzw", "enterNextMonth calV.dayOfWeek " + calV.dayOfWeek + " calV.daysOfCurrentMonth: " + calV.daysOfCurrentMonth.size());

        ViewGroup.LayoutParams params = flipper.getLayoutParams();
        //reset set the height of the month view
        if (calV.daysOfCurrentMonth.size() <= 35) {
            Logger.d("zzw", "enterNextMonth flipper height: " + params.height);
            params.height = (int) ZR.convertDpToPx(getContext(), 280);
            Logger.d("zzw", "enterNextMonth reset flipper height: " + params.height);
        } else {
            params.height = (int) ZR.convertDpToPx(getContext(), 330);
        }
        flipper.setLayoutParams(params);
//        flipper.invalidate();

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

        calV = new CalendarGridViewAdapter(a, this.getResources(), jumpMonth, jumpYear, year_c, month_c, day_c);
        Logger.d("zzw", " enterPrevMonth calV.dayOfWeek " + calV.dayOfWeek + " calV.daysOfCurrentMonth: " + calV.daysOfCurrentMonth.size());
        ViewGroup.LayoutParams params = flipper.getLayoutParams();
        if (calV.daysOfCurrentMonth.size() <= 35) {
            Logger.d("zzw", "enterPrevMonth flipper height: " + params.height);
            params.height = (int) ZR.convertDpToPx(getContext(), 280);
            Logger.d("zzw", "enterPrevMonth reset flipper height: " + params.height);
        } else {
            params.height = (int)ZR.convertDpToPx(getContext(),330);
        }
            flipper.setLayoutParams(params);
//            flipper.invalidate();

        gridView.setAdapter(calV);
        gvFlag++;
        addTextToTopTextView(currentMonth); // 移动到上一月后，将当月显示在头标题中
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
        gridView.setColumnWidth(40);
        // gridView.setStretchMode(GridView.STRETCH_COLUMN_WIDTH);
        if (Width == 720 && Height == 1280) {
            gridView.setColumnWidth(40);
        }
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
