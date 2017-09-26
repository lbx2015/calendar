package com.riking.calendar.activity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Display;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.ldf.calendar.Const;
import com.riking.calendar.R;
import com.riking.calendar.adapter.CalendarGridViewAdapter;
import com.riking.calendar.service.AlarmService;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private static final int MENU_ITEM_SCHEDULER = 1;
    private static final int MENU_ITEM_RETROFIT_ACTIVITY = 2;
    private static final int MENU_ITEM_REALM = 3;
    private static final int MENU_ITEM_NO_ACTIONBAR = 4;
    private static final int MENU_ITEM_DRAWER = 5;
    private static final int MENU_ITEM_ALARM = 6;
    private static final int MENU_ITEM_VIEWPAGER = 7;
    private static final int MENU_TAB_LAYOUT = 8;
    private static final int MENU_COORDINATE_LAYOUT = 9;
    private static final int MENU_CUSTOM_BEHAVIOR = 10;
    private static int jumpMonth = 0; // 每次滑动，增加或减去一个月,默认为0（即显示当前月）
    private static int jumpYear = 0; // 滑动跨越一年，则增加或者减去一年,默认为0(即当前年)
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
    private String currentDate = "";
    /**
     * 每次添加gridview到viewflipper中时给的标记
     */
    private int gvFlag = 0;
    /**
     * 当前的年月，现在日历顶端
     */
    private TextView currentMonth;
    /**
     * 上个月
     */
    private ImageView prevMonth;
    /**
     * 下个月
     */
    private ImageView nextMonth;
    private Callback callback = new Callback() {
        @Override
        public void onFailure(Call call, IOException e) {
            Log.d("zzw", "failure----" + e.getMessage());
        }

        @Override
        public void onResponse(Call call, Response response) throws IOException {
            Log.d("zzw", "success----");
            showmessage(response.body().string());

        }
    };

    public MainActivity() {

        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-M-d");
        currentDate = sdf.format(date); // 当期日期
        year_c = Integer.parseInt(currentDate.split("-")[0]);
        month_c = Integer.parseInt(currentDate.split("-")[1]);
        day_c = Integer.parseInt(currentDate.split("-")[2]);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(Menu.NONE, MENU_ITEM_SCHEDULER, Menu.NONE, R.string.add_schedule);
        menu.add(Menu.NONE, MENU_ITEM_RETROFIT_ACTIVITY, Menu.NONE, "retro fit activity");
        menu.add(Menu.NONE, MENU_ITEM_REALM, Menu.NONE, "realm example");
        menu.add(Menu.NONE, MENU_ITEM_NO_ACTIONBAR, Menu.NONE, "no action bar activity");
        menu.add(Menu.NONE, MENU_ITEM_DRAWER, Menu.NONE, "drawer");
        menu.add(Menu.NONE, MENU_ITEM_ALARM, Menu.NONE, "alarm");
        menu.add(Menu.NONE, MENU_ITEM_VIEWPAGER, Menu.NONE, "viewpager");
        menu.add(Menu.NONE, MENU_TAB_LAYOUT, Menu.NONE, "tablayout");
        menu.add(Menu.NONE, MENU_COORDINATE_LAYOUT, Menu.NONE, "coordinateLayout");
        menu.add(Menu.NONE, MENU_CUSTOM_BEHAVIOR, Menu.NONE, "custom behavior");

        return super.onCreateOptionsMenu(menu);
    }

    public void simplePostClick(View view) {
        RequestBody requestBody = new FormBody.Builder().add("name", "jay").add("sex", "ç?").build();
        Request request = new Request.Builder().url("http://jeff-pluto-1874.iteye.com/blog/869710").addHeader("welcome", "helloworld").post(requestBody).build();
//        okHttpClient.newCall(request).enqueue(callback);

    }

    public void showmessage(final String string) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(MainActivity.this, string, Toast.LENGTH_LONG).show();
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case MENU_ITEM_SCHEDULER: {
                Log.d("zzw", "selected");
                Request request = new Request.Builder()
                        .url("http://jeff-pluto-1874.iteye.com/blog/869710")
                        .build();
                OkHttpClient okHttpClient = new OkHttpClient();
                okHttpClient.newCall(request).enqueue(callback);
                break;
            }
            case MENU_ITEM_RETROFIT_ACTIVITY: {
                startActivity(new Intent(this, RetrofitActivity.class));
                break;
            }

            case MENU_ITEM_REALM: {
                startActivity(new Intent(this, RealmIntroActivity.class));
                break;
            }

            case MENU_ITEM_NO_ACTIONBAR: {
                startActivity(new Intent(this, NoActionBarActivity.class));
                break;
            }
            case MENU_ITEM_DRAWER: {
                startActivity(new Intent(this, DrawerActivity.class));
                break;
            }
            case MENU_ITEM_ALARM: {
                Intent intent = new Intent(this, AlarmService.class);
                PendingIntent pintent = PendingIntent.getService(this, 0, intent, 0);
                AlarmManager alarm = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                alarm.cancel(pintent);
//                alarm.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), 5000, pintent);
                Calendar c = Calendar.getInstance();
                c.set(Calendar.YEAR, c.get(Calendar.YEAR));
                c.set(Calendar.MONTH, c.get(Calendar.MONTH));
                c.set(Calendar.DATE, c.get(Calendar.DATE));
                c.set(Calendar.HOUR, c.get(Calendar.HOUR));
                c.set(Calendar.MINUTE, c.get(Calendar.MINUTE));
                c.set(Calendar.SECOND, c.get(Calendar.SECOND) + 10);
                Log.d("zzw", "System.currentTimeMillis()" + System.currentTimeMillis());
                Log.d("zzw", "c.getTimeMillis()" + c.getTimeInMillis());
                alarm.setExact(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), pintent);
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");


                Toast.makeText(this, "alarm scheduled at " + sdf.format(c.getTime()), Toast.LENGTH_LONG).show();
                break;
            }
            case MENU_ITEM_VIEWPAGER: {
                startActivity(new Intent(this, ViewPagerActivity.class));
                break;
            }
            case MENU_TAB_LAYOUT: {
                break;
            }
            case MENU_COORDINATE_LAYOUT: {
                startActivity(new Intent(this, CoordinateLayoutActivity.class));
                break;
            }
            case MENU_CUSTOM_BEHAVIOR:{
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle(R.string.calendar);
        currentMonth = (TextView) findViewById(R.id.currentMonth);
        prevMonth = (ImageView) findViewById(R.id.prevMonth);
        nextMonth = (ImageView) findViewById(R.id.nextMonth);
        setListener();

        gestureDetector = new GestureDetector(this, new MyGestureListener());
        flipper = (ViewFlipper) findViewById(R.id.flipper);
        flipper.removeAllViews();
        calV = new CalendarGridViewAdapter(this, getResources(), jumpMonth, jumpYear, year_c, month_c, day_c);
        addGridView();
        gridView.setAdapter(calV);
        flipper.addView(gridView, 0);
        addTextToTopTextView(currentMonth);
    }

    /**
     * 移动到下一个月
     *
     * @param gvFlag
     */
    private void enterNextMonth(int gvFlag) {
        addGridView(); // 添加一个gridView
        jumpMonth++; // 下一个月

        calV = new CalendarGridViewAdapter(this, this.getResources(), jumpMonth, jumpYear, year_c, month_c, day_c);
        gridView.setAdapter(calV);
        addTextToTopTextView(currentMonth); // 移动到下一月后，将当月显示在头标题中
        gvFlag++;
        flipper.addView(gridView, gvFlag);
        flipper.setInAnimation(AnimationUtils.loadAnimation(this, R.anim.push_left_in));
        flipper.setOutAnimation(AnimationUtils.loadAnimation(this, R.anim.push_left_out));
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

        calV = new CalendarGridViewAdapter(this, this.getResources(), jumpMonth, jumpYear, year_c, month_c, day_c);
        gridView.setAdapter(calV);
        gvFlag++;
        addTextToTopTextView(currentMonth); // 移动到上一月后，将当月显示在头标题中
        //addView 方法中的index越大，View显示越上面。
        flipper.addView(gridView, gvFlag);

        flipper.setInAnimation(AnimationUtils.loadAnimation(this, R.anim.push_right_in));
        flipper.setOutAnimation(AnimationUtils.loadAnimation(this, R.anim.push_right_out));
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
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT, AbsListView.LayoutParams.MATCH_PARENT);
        // 取得屏幕的宽度和高度
        WindowManager windowManager = getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        int Width = display.getWidth();
        int Height = display.getHeight();

        gridView = new GridView(this);
        gridView.setNumColumns(7);
        gridView.setColumnWidth(40);
        // gridView.setStretchMode(GridView.STRETCH_COLUMN_WIDTH);
        if (Width == 720 && Height == 1280) {
            gridView.setColumnWidth(40);
        }
        gridView.setGravity(Gravity.CENTER_VERTICAL);
        gridView.setSelector(new ColorDrawable(Color.TRANSPARENT));
        // 去除gridView边框
        gridView.setVerticalSpacing(1);
        gridView.setHorizontalSpacing(1);
        gridView.setOnTouchListener(new View.OnTouchListener() {
            // 将gridview中的触摸事件回传给gestureDetector

            public boolean onTouch(View v, MotionEvent event) {
                // TODO Auto-generated method stub
                return MainActivity.this.gestureDetector.onTouchEvent(event);
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
                    Toast.makeText(MainActivity.this, scheduleYear + "-" + scheduleMonth + "-" + scheduleDay, Toast.LENGTH_LONG).show();
                    // Toast.makeText(CalendarActivity.this, "点击了该条目",
                    // Toast.LENGTH_SHORT).show();z
                }
            }
        });
        gridView.setLayoutParams(params);
    }

    private void setListener() {
        prevMonth.setOnClickListener(this);
        nextMonth.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
            case R.id.nextMonth: // 下一个月
                enterNextMonth(gvFlag);
                break;
            case R.id.prevMonth: // 上一个月
                enterPrevMonth(gvFlag);
                break;
            default:
                break;
        }
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