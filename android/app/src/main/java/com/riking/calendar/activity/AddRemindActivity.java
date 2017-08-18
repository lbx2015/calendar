package com.riking.calendar.activity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.flyco.tablayout.SlidingTabLayout;
import com.ldf.calendar.Const;
import com.riking.calendar.R;
import com.riking.calendar.fragment.CreateReminderFragment;
import com.riking.calendar.jiguang.Logger;
import com.riking.calendar.pojo.ReminderModel;
import com.riking.calendar.pojo.TaskModel;
import com.riking.calendar.realm.model.Reminder;
import com.riking.calendar.realm.model.Task;
import com.riking.calendar.retrofit.APIClient;
import com.riking.calendar.retrofit.APIInterface;
import com.riking.calendar.service.ReminderService;
import com.riking.calendar.util.DateUtil;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import io.realm.Realm;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by zw.zhang on 2017/7/24.
 */

public class AddRemindActivity extends AppCompatActivity {
    MyPagerAdapter pagerAdapter;
    CreateReminderFragment reminderFragment;
    SharedPreferences preference;
    CreateTaskFragment taskFragment;
    APIInterface apiInterface;
    String userId;
    private ViewPager viewPager;
    private Realm realm;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        Log.d("zzw", this + "on create");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_remind);
        viewPager = (ViewPager) findViewById(R.id.pager);
        pagerAdapter = new MyPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(pagerAdapter);
        //adding view pager to the slidingTabLayout
        SlidingTabLayout topTabLayout = (SlidingTabLayout) findViewById(R.id.top_tab_layout);
        topTabLayout.setViewPager(viewPager);
        apiInterface = APIClient.getClient().create(APIInterface.class);
        preference = getSharedPreferences(Const.PREFERENCE_FILE_NAME, MODE_PRIVATE);
        userId = preference.getString(Const.USER_ID, null);
    }

    public void onClickCancel(View v) {
        onBackPressed();
    }

    public void onClickConfirm(View v) {
        // Create the Realm instance
        realm = Realm.getDefaultInstance();
        final SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");

        final String id = sdf.format(new Date());

        //insert  to realm
        // All writes must be wrapped in a transaction to facilitate safe multi threading
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                //remind fragment
                if (viewPager.getCurrentItem() == 0) {

                    final String reminderTitle = reminderFragment.remindTitle.getText().toString();
                    if (reminderTitle == null || reminderTitle.trim().equals("")) {
                        Toast.makeText(AddRemindActivity.this, "提醒内容不能为空", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    // Add a remind
                    final Reminder reminder = realm.createObject(Reminder.class, id);
                    SimpleDateFormat dayFormat = new SimpleDateFormat("yyyyMMdd");
                    SimpleDateFormat timeFormat = new SimpleDateFormat("HHmm");
                    reminder.title = reminderTitle;
                    Date reminderDate = reminderFragment.time.getTime();
                    reminder.day = dayFormat.format(reminderDate);
                    reminder.time = timeFormat.format(reminderDate);
                    Log.d("zzw", "reminder time" + reminder.time);
                    reminder.repeatFlag = reminderFragment.repeatFlag;
                    reminder.repeatWeek = reminderFragment.repeatWeek;
                    reminder.aheadTime = reminderFragment.aheadTime;
                    reminder.isRemind = reminderFragment.isRemind;
                    reminder.isAllDay = reminderFragment.isAllDay;
                    reminder.reminderTime = reminderDate;
                    reminder.userId = userId;
//set reminder
                    Intent intent = new Intent(AddRemindActivity.this, ReminderService.class);
                    intent.putExtra(Const.REMINDER_TITLE, reminder.title);
                    PendingIntent pendingIntent = PendingIntent.getService(AddRemindActivity.this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
//                    alarmManager.cancel(pendingIntent);
                    Calendar reminderCalendar = reminderFragment.time;
                    reminderCalendar.set(java.util.Calendar.MINUTE, reminderFragment.time.get(java.util.Calendar.MINUTE) - reminder.aheadTime);
                    if (reminder.isRemind == 1 && reminder.repeatFlag == 0) {
                        alarmManager.setExact(AlarmManager.RTC_WAKEUP, reminderCalendar.getTimeInMillis(), pendingIntent);
//                        long intervalMillis = 1000;
//                        alarmManager.setWindow(AlarmManager.RTC_WAKEUP, reminderCalendar.getTimeInMillis(),
//                                intervalMillis, pendingIntent);
                    } else if (reminder.isRemind == 1 && reminder.repeatFlag == 3) {
                        String repeatWeek = reminder.repeatWeek;
                        long intervalMillis = 0;
                        long remindTime;
                        //repeat each day
                        if (repeatWeek.length() == 7) {
                            intervalMillis = 24 * 3600 * 1000;
                            remindTime = DateUtil.getRepeatReminderTime(0, reminderCalendar.getTimeInMillis());
                            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, remindTime, intervalMillis, pendingIntent);
                        } else {
                            //repeat the alarm on each week days selected.
                            intervalMillis = 24 * 3600 * 1000 * 7;
                            for (int i = 1; i <= 7; i++) {
                                if (repeatWeek.contains(String.valueOf(i))) {
                                    remindTime = DateUtil.getRepeatReminderTime(i, reminderCalendar.getTimeInMillis());
                                    alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, remindTime, intervalMillis, pendingIntent);
                                }
                            }
                        }
                    }
                }
                //task fragment
                else {
                    Task task = realm.createObject(Task.class, id);
                    task.isImportant = taskFragment.isImportant;
                    SimpleDateFormat sdf = new SimpleDateFormat(Const.yyyyMMddHHmm);
                    task.appCreatedTime = sdf.format(new Date());
                    if (taskFragment.needToRemind) {
                        task.isOpen = 1;
                        task.strDate = sdf.format(taskFragment.calendar.getTime());
                    }
                    task.title = taskFragment.title.getText().toString();
                    task.userId = userId;
                    if (task.isOpen == 1) {
                        Intent intent = new Intent(AddRemindActivity.this, ReminderService.class);
                        intent.putExtra(Const.REMINDER_TITLE, task.title);
                        PendingIntent pendingIntent = PendingIntent.getService(AddRemindActivity.this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                        try {
                            alarmManager.setExact(AlarmManager.RTC_WAKEUP, sdf.parse(task.strDate).getTime(), pendingIntent);
                        } catch (ParseException e) {
                            Logger.d("zzw", "parse failed.");
                        }
                    }
                }
            }
        });

        if (preference.getBoolean(Const.IS_LOGIN, false)) {
            //remind fragment
            if (viewPager.getCurrentItem() == 0) {
                Reminder r = realm.where(Reminder.class).equalTo("id", id).findFirst();
                ReminderModel mode = new ReminderModel();
                mode.id = r.id;
                mode.aheadTime = r.aheadTime;
                mode.clientType = r.clientType;
                mode.currentWeek = r.currentWeek;
                mode.day = r.day;
                mode.isAllDay = r.isAllDay;
                mode.isRemind = r.isRemind;
                mode.repeatFlag = r.repeatFlag;
                mode.time = r.time;
                mode.title = r.title;
                mode.deleteState = r.deleteState;
                mode.endTime = r.endTime;
                mode.repeatWeek = r.repeatWeek;
                mode.userId = r.userId;
                Log.d("zzw", "reminder model : " + mode);

                Call<ResponseBody> call = apiInterface.createRemind(mode);
                call.enqueue(new retrofit2.Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        ResponseBody r = response.body();
                        try {
                            if (r == null || r.source() == null) {
                                Toast.makeText(getBaseContext(), getBaseContext().getString(R.string.create_failed), Toast.LENGTH_SHORT).show();
                            }
                            String s = r.source().readUtf8();
//                            String s2 = s.replace("\\", "");
//                            int i = s2.indexOf("}");
//                            int l = s2.lastIndexOf("}");
//                            Gson gson = new Gson();
//                            ReminderModel m;
//                            m = gson.fromJson(s2.substring(10, i + 1), ReminderModel.class);
                            Log.d("zzw", response.code() + "success " + s);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        Log.d("zzw", "fail" + t.getMessage());
                    }
                });
            } else {
                Task task = realm.where(Task.class).equalTo(Task.TODO_ID, id).findFirst();
                TaskModel taskModel = new TaskModel(task);
                Call<ResponseBody> call = apiInterface.createTask(taskModel);
                call.enqueue(new retrofit2.Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        ResponseBody r = response.body();
                        try {
                            if (r == null) {
                                Toast.makeText(getBaseContext(), getBaseContext().getString(R.string.create_failed), Toast.LENGTH_SHORT).show();
                            }
                            String s = r.source().readUtf8();
                            Log.d("zzw", "success " + s);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        Log.d("zzw", "fail" + t.getMessage());
                    }
                });

            }
        }
        onBackPressed();
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
                    reminderFragment = new CreateReminderFragment();
                    return reminderFragment;
                case 1:
                    taskFragment = new CreateTaskFragment();
                    return taskFragment;
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
                    return getString(R.string.reminder);
                case 1:
                    return getString(R.string.to_do);
            }

            return null;
        }

    }
}
