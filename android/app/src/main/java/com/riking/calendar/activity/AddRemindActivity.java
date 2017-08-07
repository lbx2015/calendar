package com.riking.calendar.activity;

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

import com.flyco.tablayout.SlidingTabLayout;
import com.riking.calendar.R;
import com.riking.calendar.fragment.CreateReminderFragment;
import com.riking.calendar.pojo.ReminderModel;
import com.riking.calendar.realm.model.Reminder;
import com.riking.calendar.realm.model.Task;
import com.riking.calendar.retrofit.APIClient;
import com.riking.calendar.retrofit.APIInterface;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

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
    CreateTaskFragment taskFragment;
    APIInterface apiInterface;
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
    }

    public void onClickCancel(View v) {
        onBackPressed();
    }

    public void onClickConfirm(View v) {
        // Create the Realm instance
        realm = Realm.getDefaultInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");

        final String id = sdf.format(new Date());
        ;
        //insert  to realm
        // All writes must be wrapped in a transaction to facilitate safe multi threading
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                //remind fragment
                if (viewPager.getCurrentItem() == 0) {
                    // Add a remind
                    final Reminder reminder = realm.createObject(Reminder.class, id);
                    SimpleDateFormat dayFormat = new SimpleDateFormat("yyyyMMdd");
                    SimpleDateFormat timeFormat = new SimpleDateFormat("HHmm");
                    reminder.title = reminderFragment.remindTitle.getText().toString();
                    reminder.day = dayFormat.format(reminderFragment.time.getTime());
                    reminder.time = timeFormat.format(reminderFragment.time.getTime());
                    reminder.repeatFlag = reminderFragment.repeatFlag;
                    reminder.repeatWeek = reminderFragment.repeatWeeks;
                    reminder.aheadTime = reminderFragment.aheadTime;
                    reminder.isRemind = reminderFragment.isRemind;
                    reminder.isAllDay = reminderFragment.isAllDay;
                }
                //task fragment
                else {
                    Task task = realm.createObject(Task.class, UUID.randomUUID().toString());
                    task.isImport = taskFragment.isImportant;
                    task.createTime = new Date();
                    if (taskFragment.needToRemind) {
                        task.isReminded = 1;
                        task.remindTime = taskFragment.calendar.getTime();
                    }
                    task.title = taskFragment.title.getText().toString();
                }
            }
        });

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
        Log.d("zzw", "reminder model : " + mode);

        Call<ResponseBody> call = apiInterface.createRemind(mode);
        call.enqueue(new retrofit2.Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                ResponseBody r = response.body();
                String s;

                try {
                    Log.d("zzw", response.code() + "success " + r.source().readUtf8());
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.d("zzw", "fail" + t.getMessage());
            }
        });

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
