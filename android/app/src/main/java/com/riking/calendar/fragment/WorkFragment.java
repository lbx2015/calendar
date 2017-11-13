package com.riking.calendar.fragment;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.necer.ncalendar.calendar.NCalendar;
import com.necer.ncalendar.listener.OnCalendarChangedListener;
import com.riking.calendar.R;
import com.riking.calendar.activity.AddRemindActivity;
import com.riking.calendar.activity.ViewPagerActivity;
import com.riking.calendar.adapter.ReminderAdapter;
import com.riking.calendar.adapter.ReportAdapter;
import com.riking.calendar.adapter.ReportOnlineAdapter;
import com.riking.calendar.adapter.TaskAdapter;
import com.riking.calendar.jiguang.Logger;
import com.riking.calendar.listener.ZCallBack;
import com.riking.calendar.pojo.AppUserReportCompleteRel;
import com.riking.calendar.pojo.QueryReportContainer;
import com.riking.calendar.pojo.base.ResponseModel;
import com.riking.calendar.realm.model.QueryReportContainerRealmModel;
import com.riking.calendar.realm.model.Reminder;
import com.riking.calendar.realm.model.Task;
import com.riking.calendar.realm.model.WorkDateRealm;
import com.riking.calendar.retrofit.APIClient;
import com.riking.calendar.util.CONST;
import com.riking.calendar.util.Preference;

import org.joda.time.DateTime;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmConfiguration;
import io.realm.RealmQuery;
import io.realm.RealmResults;
import io.realm.Sort;

/**
 * Created by zw.zhang on 2017/7/11.
 */

public class WorkFragment extends Fragment implements OnCalendarChangedListener {
    private static int jumpMonth = 0; // 每次滑动，增加或减去一个月,默认为0（即显示当前月）
    private static int jumpYear = 0; // 滑动跨越一年，则增加或者减去一年,默认为0(即当前年)
    public RealmResults<Reminder> reminders;
    public ArrayList<String> notRepeatRemindDaysOfMonth = new ArrayList<>();
    public String repeatWeekReminds;
    public HashMap<String, Date> weeks = new HashMap<>();//weekly repeat reminders
    public Date ealiestRemindWorkDate;//work day repeat reminders
    public Date ealiestRemindHolidayDate;//holiday repeat reminders
    public ArrayList<String> workOnWeekendDates = new ArrayList<>();//work on saturday or sunday
    public ArrayList<String> notWorkOnWorkDates = new ArrayList<>();//not work on monday to friday
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
    //current year month
    String yearMonth;
    ReportOnlineAdapter reportOnlineAdapter;
    View v;
    Date currentDay;
//    private GestureDetector gestureDetector = null;
//    private CalendarGridViewAdapter calV = null;
//    private ViewFlipper flipper = null;
//    private GridView gridView = null;
    //current year
    private int year_c = 0;
    /**
     * 下个月
     */
//    private ImageView nextMonth;
    //current month
    private int month_c = 0;
    /**
     * 上个月
     */
//    private ImageView prevMonth;
    //current day
    private int day_c = 0;
    /**
     * 每次添加gridview到viewflipper中时给的标记
     */
    private int gvFlag = 0;
    /**
     * 当前的年月，现在日历顶端
     */
    private TextView currentMonth;
    private View add;
    private SwipeRefreshLayout swipeRefreshLayout;

    private NCalendar ncalendar;

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
        ncalendar.toToday();
//        if (calV.realCurrentDayPositionFlag > 0) {
//            if (calV.realCurrentDayPositionFlag != calV.currentFlag) {
//                calV.currentFlag = calV.realCurrentDayPositionFlag;
//                calV.notifyDataSetChanged();
//            }
//            //do nothing if already in current month.
//            return;
//        }
//        addGridView(); // 添加一个gridView
//        //current month
//        calV = new CalendarGridViewAdapter(this, a.getResources(), 0, 0, year_c, month_c, day_c);
//        gridView.setAdapter(calV);
//        addTextToTopTextView(currentMonth); // 移动到下一月后，将当月显示在头标题中
//        flipper.addView(gridView, 1);
//        Log.d("zzw", jumpMonth + "jumpMonth + month_c" + (jumpMonth + month_c));
//        if (jumpMonth > 0) {
//            flipper.setInAnimation(AnimationUtils.loadAnimation(a, R.anim.push_right_in));
//            flipper.setOutAnimation(AnimationUtils.loadAnimation(a, R.anim.push_right_out));
//            flipper.showPrevious();
//        } else if (jumpMonth < 0) {
//            flipper.setInAnimation(AnimationUtils.loadAnimation(a, R.anim.push_left_in));
//            flipper.setOutAnimation(AnimationUtils.loadAnimation(a, R.anim.push_left_out));
//            flipper.showNext();
//        }
//        //restore the jumpMonth and jumpYear to zero
//        jumpMonth = 0;
//        jumpYear = 0;
//        flipper.removeViewAt(0);
//        gridView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
//            @Override
//            public void onGlobalLayout() {
//                gridView.getViewTreeObserver().removeGlobalOnLayoutListener(this);
//                View lastChild = gridView.getChildAt(gridView.getChildCount() - 1);
//                Logger.d("zzw", "enterNextMonth calV.dayOfWeek " + calV.dayOfWeek + " calV.daysOfCurrentMonth: " + calV.daysOfCurrentMonth.size());
//                ViewGroup.LayoutParams params = flipper.getLayoutParams();
//                Logger.d("zzw", "flipper height: " + params.height);
//                //The days of current month need to using 6 row of grid view to showing the days
//                if (calV.getCount() > 35) {
//                    params.height = lastChild.getMeasuredHeight() * 6 + gridView.getPaddingTop();
//                }
//                //The days of current month need to using 5 rows of grid view too showing days
//                //by the way one row have 7 columns.
//                else {
//                    params.height = lastChild.getMeasuredHeight() * 5 + gridView.getPaddingTop();
//                }
//                Logger.d("zzw", "reset flipper height: " + params.height);
//                flipper.setLayoutParams(params);
//                flipper.invalidate();
//            }
//        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Create the Realm instance
        realm = Realm.getDefaultInstance();
        if (v != null) {
            return v;
        }

        v = inflater.inflate(R.layout.work_fragment, container, false);
        ncalendar = (NCalendar) v.findViewById(R.id.n_calendar);
        ncalendar.setWorkFragment(this);
        ncalendar.setOnCalendarChangedListener(this);
        //set fragment

                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                List<String> list = new ArrayList<>();
//                list.add("2017-09-21");
//                list.add("2017-10-21");
//                list.add("2017-10-1");
//                list.add("2017-10-15");
//                list.add("2017-10-18");
//                list.add("2017-10-26");
//                list.add("2017-11-21");

                //reset the values
                notRepeatRemindDaysOfMonth.clear();
                Log.d("zzw","set repeatWeekReminds");
                repeatWeekReminds = "";
                weeks.clear();
                ealiestRemindHolidayDate = null;
                ealiestRemindWorkDate = null;

                if (realm.isClosed()) {
                    realm = Realm.getDefaultInstance();
                }
                RealmResults<Reminder> reminders = realm.where(Reminder.class)
                        .beginGroup()
                        .beginsWith("day", yearMonth)//this month
                        .equalTo("repeatFlag", 0)//not repeat reminders.
                        .endGroup().findAllSorted("time", Sort.ASCENDING);
                for (Reminder r : reminders) {
                    if (r.reminderTime != null) {
                        list.add(dateFormat.format(r.reminderTime));
                        ncalendar.setPoint(list);
                    }
                }

                //find the repeat week days
                RealmResults<Reminder> weekRepeatReminders = realm.where(Reminder.class)
                        .beginGroup()
                        .equalTo("repeatFlag", 3)
                        .endGroup().findAll();

                for (Reminder r : weekRepeatReminders) {
                    if (r.repeatWeek != null) {
                        for (char ch : r.repeatWeek.toCharArray()) {
                            String key = String.valueOf(ch);
                            if (weeks.get(key) == null || r.reminderTime.before(weeks.get(key))) {
                                Logger.d("zzw", "put week repeat remind: " + ch + " : " + r.reminderTime);
                                weeks.put(key, r.reminderTime);
                            }
                        }
                    }
                }

                for (String key : weeks.keySet()) {
                    repeatWeekReminds = repeatWeekReminds + key;
                }

                //find work day reminds
                RealmResults<Reminder> workDayReminds = realm.where(Reminder.class)
                        .equalTo("repeatFlag", CONST.REPEAT_FLAG_WORK_DAY)//work day
                        .findAll();
                for (Reminder r : workDayReminds) {
                    //keep the workRemind reminderTimeCalendar as the earliest.
                    if (ealiestRemindWorkDate == null || r.reminderTime.before(ealiestRemindWorkDate)) {
                        ealiestRemindWorkDate = r.reminderTime;
                    }
                }

                //find holiday reminds
                RealmResults<Reminder> holidayReminds = realm.where(Reminder.class)
                        .equalTo("repeatFlag", CONST.REPEAT_FLAG_HOLIDAY)//holiday
                        .findAll();
                for (Reminder r : holidayReminds) {
                    if (ealiestRemindHolidayDate == null || r.reminderTime.before(ealiestRemindHolidayDate)) {
                        ealiestRemindHolidayDate = r.reminderTime;
                    }
                }


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

//        gestureDetector = new GestureDetector(a, new FirstFragment.MyGestureListener());
//        flipper = (ViewFlipper) v.findViewById(R.id.flipper);
//        flipper.removeAllViews();
//        calV = new CalendarGridViewAdapter(this, a.getResources(), jumpMonth, jumpYear, year_c, month_c, day_c);
//        addGridView();
//        gridView.setAdapter(calV);
//        gridView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
//            @Override
//            public void onGlobalLayout() {
//                gridView.getViewTreeObserver().removeGlobalOnLayoutListener(this);
//                View lastChild = gridView.getChildAt(gridView.getChildCount() - 1);
//                Logger.d("zzw", "enterNextMonth calV.dayOfWeek " + calV.dayOfWeek + " calV.daysOfCurrentMonth: " + calV.daysOfCurrentMonth.size());
//                ViewGroup.LayoutParams params = flipper.getLayoutParams();
//                Logger.d("zzw", "flipper height: " + params.height);
//                if (calV.getCount() > 35) {
//                    params.height = lastChild.getMeasuredHeight() * 6 + gridView.getPaddingTop();
//                } else {
//                    params.height = lastChild.getMeasuredHeight() * 5 + gridView.getPaddingTop();
//                }
//                Logger.d("zzw", "reset flipper height: " + params.height);
//                flipper.setLayoutParams(params);
//            }
//        });

//            flipper.invalidate();
//        Logger.d("zzw", "after reset layout params flipper height: " + flipper.getLayoutParams().height);

//        flipper.addView(gridView, 0);
        addTextToTopTextView(currentMonth);

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
        RealmResults<Task> tasks = realm.where(Task.class).equalTo(Task.IS_COMPLETE, 0).notEqualTo(Task.DELETESTATE, CONST.DELETE).findAll();
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
        currentDay = new Date();
        //what the fuck logic,
        updateReportAdapter(currentDay);

        RealmResults<WorkDateRealm> works = realm.where(WorkDateRealm.class).findAll();
        for (WorkDateRealm w : works) {
            int weekDay = Integer.parseInt(w.weekday);
            //not work on work day
            if (w.isWork == 0 && weekDay < 6) {
                notWorkOnWorkDates.add(w.date);
            }
            //work on weekends
            else if (w.isWork == 1 && weekDay > 5) {
                workOnWeekendDates.add(w.date);
            }
        }

        swipeRefreshLayout = (SwipeRefreshLayout) v.findViewById(R.id.swipeRefreshLayout);

        // Configure the refreshing colors
        swipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //first page
                updateReportAdapter(currentDay);
            }
        });


        //set the layout params
        FrameLayout scrollView = (FrameLayout) v.findViewById(R.id.nested_recyclerview);
//        CoordinatorLayout.LayoutParams paramss = (CoordinatorLayout.LayoutParams) scrollView.getLayoutParams();
//        final int marginBottom = mTabLayout.getMeasuredHeight();
//        paramss.setMargins(0, 0, 0, marginBottom);
//        scrollView.setLayoutParams(paramss);
        return v;
    }

    public boolean isNetAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) a.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = connectivityManager.getActiveNetworkInfo();
        return info != null && info.isConnectedOrConnecting();
    }

    public void updateReportsWithLocalRealm() {
        RealmConfiguration.Builder defaultRealmConfiguration = new RealmConfiguration.Builder()
                .deleteRealmIfMigrationNeeded().name(CONST.DEFAUT_REALM_DATABASE_NAME);
        Realm r = Realm.getInstance(defaultRealmConfiguration.build());
        RealmResults<QueryReportContainerRealmModel> reports = r.where(QueryReportContainerRealmModel.class).findAll();
        Logger.d("zzw", "report adapter size: " + reports.size());
        final ReportAdapter reportAdapter = new ReportAdapter(reports);
        reportRecyclerView.setAdapter(reportAdapter);
        realm.addChangeListener(new RealmChangeListener<Realm>() {
            @Override
            public void onChange(Realm realm) {
                reportAdapter.notifyDataSetChanged();
            }
        });
    }

    public void updateReportAdapter(Date date) {
        if (reportRecyclerView == null) {
            return;
        }

        if (Preference.pref.getBoolean(CONST.IS_LOGIN, false) && isNetAvailable()) {
            AppUserReportCompleteRel requestBody = new AppUserReportCompleteRel();
            requestBody.appUserId = Preference.pref.getString(CONST.USER_ID, "");
            requestBody.completeDate = new SimpleDateFormat(CONST.yyyyMMdd).format(date);
            APIClient.apiInterface.getUserReports(requestBody).enqueue(new ZCallBack<ResponseModel<ArrayList<QueryReportContainer>>>() {
                @Override
                public void callBack(ResponseModel<ArrayList<QueryReportContainer>> response) {
                    swipeRefreshLayout.setRefreshing(false);
                    final ArrayList<QueryReportContainer> reportContainers = response._data;
                    reportOnlineAdapter = new ReportOnlineAdapter(reportContainers);
                    reportRecyclerView.setAdapter(reportOnlineAdapter);
                }
            });
        } else {
            if (swipeRefreshLayout != null) {
                swipeRefreshLayout.setRefreshing(false);
            }
            updateReportsWithLocalRealm();
        }
    }

    /**
     * get today's reminders.
     */
    public void initReminderAdapter() {
        final Calendar c = Calendar.getInstance();
        c.setTime(new Date());
        updateReminderAdapter(c);
    }

    public void getRemindDaysOfMonth(String yearMonth) {
        //reset the values
        notRepeatRemindDaysOfMonth.clear();
        repeatWeekReminds = "";
        weeks.clear();
        ealiestRemindHolidayDate = null;
        ealiestRemindWorkDate = null;

        if (realm.isClosed()) {
            realm = Realm.getDefaultInstance();
        }
        RealmResults<Reminder> reminders = realm.where(Reminder.class)
                .beginGroup()
                .beginsWith("day", yearMonth)//this month
                .equalTo("repeatFlag", 0)//not repeat reminders.
                .endGroup().findAllSorted("time", Sort.ASCENDING);
        Calendar c = Calendar.getInstance();
        for (Reminder r : reminders) {
            if (r.reminderTime != null) {
                c.setTime(r.reminderTime);
                notRepeatRemindDaysOfMonth.add(String.valueOf(c.get(Calendar.DATE)));
            }
        }

        //find the repeat week days
        RealmResults<Reminder> weekRepeatReminders = realm.where(Reminder.class)
                .beginGroup()
                .equalTo("repeatFlag", 3)
                .endGroup().findAll();

        for (Reminder r : weekRepeatReminders) {
            if (r.repeatWeek != null) {
                for (char ch : r.repeatWeek.toCharArray()) {
                    String key = String.valueOf(ch);
                    if (weeks.get(key) == null || r.reminderTime.before(weeks.get(key))) {
                        Logger.d("zzw", "put week repeat remind: " + ch + " : " + r.reminderTime);
                        weeks.put(key, r.reminderTime);
                    }
                }
            }
        }

        for (String key : weeks.keySet()) {
            repeatWeekReminds = repeatWeekReminds + key;
        }

        //find work day reminds
        RealmResults<Reminder> workDayReminds = realm.where(Reminder.class)
                .equalTo("repeatFlag", CONST.REPEAT_FLAG_WORK_DAY)//work day
                .findAll();
        for (Reminder r : workDayReminds) {
            //keep the workRemind reminderTimeCalendar as the earliest.
            if (ealiestRemindWorkDate == null || r.reminderTime.before(ealiestRemindWorkDate)) {
                ealiestRemindWorkDate = r.reminderTime;
            }
        }

        //find holiday reminds
        RealmResults<Reminder> holidayReminds = realm.where(Reminder.class)
                .equalTo("repeatFlag", CONST.REPEAT_FLAG_HOLIDAY)//holiday
                .findAll();
        for (Reminder r : holidayReminds) {
            if (ealiestRemindHolidayDate == null || r.reminderTime.before(ealiestRemindHolidayDate)) {
                ealiestRemindHolidayDate = r.reminderTime;
            }
        }
    }

    public void getRemindDaysOfMonth(Date date) {
        SimpleDateFormat monthFormat = new SimpleDateFormat("yyyyMM");
        getRemindDaysOfMonth(monthFormat.format(date));

    }

    public void updateReminderAdapter(Calendar c) {
        if (recyclerView == null) {
            return;
        }
        int weekDay = c.get(Calendar.DAY_OF_WEEK);
        if (weekDay == Calendar.SUNDAY) {
            weekDay = 7;
        } else {
            weekDay--;
        }
        //Fix the reminder reminderTimeCalendar is not before the current day
        c.set(Calendar.HOUR_OF_DAY, 23);
        c.set(Calendar.MINUTE, 59);

        Date date = c.getTime();
        SimpleDateFormat dayFormat = new SimpleDateFormat("yyyyMMdd");
        String currentDay = dayFormat.format(date);
        RealmQuery<Reminder> query = realm.where(Reminder.class).beginGroup().equalTo("day", currentDay).equalTo("repeatFlag", 0).endGroup()
                .or().beginGroup()
                .equalTo("repeatFlag", CONST.REPEAT_FLAG_WEEK)
                .contains("repeatWeek", String.valueOf(weekDay))
                .lessThan("reminderTime", date)
                .endGroup();

        boolean isTodayWorkDay;
        //weekends
        if (weekDay > 5) {
            if (workOnWeekendDates.contains(currentDay)) {
                isTodayWorkDay = true;
            } else {
                isTodayWorkDay = false;
            }
        } else {
            if (notWorkOnWorkDates.contains(currentDay)) {
                isTodayWorkDay = false;
            } else {
                isTodayWorkDay = true;
            }
        }

        //find the work day repeat reminders
        if (isTodayWorkDay) {
            query.or()
                    .beginGroup()
                    .equalTo("repeatFlag", CONST.REPEAT_FLAG_WORK_DAY)
                    .lessThanOrEqualTo("reminderTime", date)
                    .endGroup();
        } else {
            query.or()
                    .beginGroup()
                    .equalTo("repeatFlag", CONST.REPEAT_FLAG_HOLIDAY)
                    .lessThanOrEqualTo("reminderTime", date)
                    .endGroup();
        }

        query.equalTo("deleteState", 0);

        reminders = query.findAllSorted("time", Sort.ASCENDING);
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
                getRemindDaysOfMonth(yearMonth);
//                calV.notifyDataSetChanged();
            }
        });
    }
//
//    /**
//     * 移动到下一个月
//     *
//     * @param gvFlag
//     */
//    private void enterNextMonth(int gvFlag) {
//        addGridView(); // 添加一个gridView
//        jumpMonth++; // 下一个月
//        calV = new CalendarGridViewAdapter(this, this.getResources(), jumpMonth, jumpYear, year_c, month_c, day_c);
//        gridView.setAdapter(calV);
//        addTextToTopTextView(currentMonth); // 移动到下一月后，将当月显示在头标题中
//        gvFlag++;
//
//        gridView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
//            @Override
//            public void onGlobalLayout() {
//                gridView.getViewTreeObserver().removeGlobalOnLayoutListener(this);
//                View lastChild = gridView.getChildAt(gridView.getChildCount() - 1);
//                Logger.d("zzw", "enterNextMonth calV.dayOfWeek " + calV.dayOfWeek + " calV.daysOfCurrentMonth: " + calV.daysOfCurrentMonth.size());
//                ViewGroup.LayoutParams params = flipper.getLayoutParams();
//                Logger.d("zzw", "flipper height: " + params.height);
//                if (calV.getCount() > 35) {
//                    params.height = lastChild.getMeasuredHeight() * 6 + gridView.getPaddingTop();
//                } else {
//                    params.height = lastChild.getMeasuredHeight() * 5 + gridView.getPaddingTop();
//                }
//                Logger.d("zzw", "reset flipper height: " + params.height);
//                flipper.setLayoutParams(params);
//            }
//        });
//
//        flipper.addView(gridView, gvFlag);
//        flipper.setInAnimation(AnimationUtils.loadAnimation(a, R.anim.push_left_in));
//        flipper.setOutAnimation(AnimationUtils.loadAnimation(a, R.anim.push_left_out));
//        flipper.showNext();
//        flipper.removeViewAt(0);
//    }

    /**
     * 移动到上一个月
     *
     * @param gvFlag
     */
//    private void enterPrevMonth(int gvFlag) {
//        addGridView(); // 添加一个gridView
//        jumpMonth--; // 上一个月
//        calV = new CalendarGridViewAdapter(this, this.getResources(), jumpMonth, jumpYear, year_c, month_c, day_c);
//        gridView.setAdapter(calV);
//        gvFlag++;
//        addTextToTopTextView(currentMonth);// 移动到上一月后，将当月显示在头标题中
//
//        gridView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
//            @Override
//            public void onGlobalLayout() {
//                gridView.getViewTreeObserver().removeGlobalOnLayoutListener(this);
//                View lastChild = gridView.getChildAt(gridView.getChildCount() - 1);
//                Logger.d("zzw", "enterNextMonth calV.dayOfWeek " + calV.dayOfWeek + " calV.daysOfCurrentMonth: " + calV.daysOfCurrentMonth.size());
//                ViewGroup.LayoutParams params = flipper.getLayoutParams();
//                Logger.d("zzw", "flipper height: " + params.height);
//                if (calV.getCount() > 35) {
//                    params.height = lastChild.getMeasuredHeight() * 6 + gridView.getPaddingTop();
//                } else {
//                    params.height = lastChild.getMeasuredHeight() * 5 + gridView.getPaddingTop();
//                }
//                Logger.d("zzw", "reset flipper height: " + params.height);
//                flipper.setLayoutParams(params);
//                flipper.invalidate();
//            }
//        });
//
//        //addView 方法中的index越大，View显示越上面。
//        flipper.addView(gridView, gvFlag);
//
//        flipper.setInAnimation(AnimationUtils.loadAnimation(a, R.anim.push_right_in));
//        flipper.setOutAnimation(AnimationUtils.loadAnimation(a, R.anim.push_right_out));
//        flipper.showPrevious();
//        flipper.removeViewAt(0);
//    }

    /**
     * 添加头部的年份 闰哪月等信息
     *
     * @param view
     */
    public void addTextToTopTextView(TextView view) {
        StringBuffer textDate = new StringBuffer();
        // draw = getResources().getDrawable(R.drawable.top_day);
        // view.setBackgroundDrawable(draw);
//        textDate.append(calV.getShowYear()).append("年").append(calV.getShowMonth()).append("月").append("\t");
        view.setText(textDate);
    }
/*
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
                    currentDay = c.getTime();

                    updateReminderAdapter(c);
                    updateReportAdapter(c.getTime());

//                    Toast.makeText(a, scheduleYear + "-" + scheduleMonth + "-" + scheduleDay, Toast.LENGTH_LONG).show();
                    // Toast.makeText(CalendarActivity.this, "点击了该条目",
                    // Toast.LENGTH_SHORT).show();z
                }
            }
        });
        gridView.setLayoutParams(params);
    }*/

    @Override
    public void onCalendarChanged(DateTime dateTime) {
        StringBuffer textDate = new StringBuffer();
        textDate.append(dateTime.getYear()).append("年").append(dateTime.getMonthOfYear()).append("月").append("\t");
        currentMonth.setText(textDate);
        updateReminderAdapter(dateTime.toCalendar(Locale.getDefault()));
        updateReportAdapter(dateTime.toDate());
//        tv_month.setText(dateTime.getMonthOfYear() + "月");
//        tv_date.setText(dateTime.getYear() + "年" + dateTime.getMonthOfYear() + "月" + dateTime.getDayOfMonth() + "日");
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        realm.close();
    }
/*
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
    }*/
}