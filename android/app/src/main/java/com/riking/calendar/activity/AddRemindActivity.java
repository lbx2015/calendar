package com.riking.calendar.activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.flyco.tablayout.SlidingTabLayout;
import com.ldf.calendar.Const;
import com.riking.calendar.R;
import com.riking.calendar.fragment.CreateReminderFragment;
import com.riking.calendar.realm.model.Reminder;
import com.riking.calendar.realm.model.Task;
import com.riking.calendar.retrofit.APIClient;
import com.riking.calendar.retrofit.APIInterface;
import com.riking.calendar.util.CONST;

import java.text.SimpleDateFormat;
import java.util.Date;

import io.realm.Realm;

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
    String reminderTitle;
    String taskTitle;
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
        TabLayout topTabLayout = (TabLayout) findViewById(R.id.top_tab_layout);
        topTabLayout.setupWithViewPager(viewPager);
        //set custom layout to adding divider
        for (int i = 0; i < topTabLayout.getTabCount(); i++) {
            TabLayout.Tab tab = topTabLayout.getTabAt(i);
            RelativeLayout relativeLayout = (RelativeLayout)getLayoutInflater().inflate(R.layout.third_fragment_tab_custom, topTabLayout, false);

            TextView tabTextView = (TextView) relativeLayout.findViewById(R.id.tab_title);
            View divider = relativeLayout.findViewById(R.id.divider);
            if (i == 0) {
                divider.setVisibility(View.GONE);
            }

            //tab.getText() is from tagGetPageTitle
            tabTextView.setText(tab.getText());
            tab.setCustomView(relativeLayout);
            tab.select();
        }
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
        //remind fragment
        if (viewPager.getCurrentItem() == 0) {
            reminderTitle = reminderFragment.remindTitle.getText().toString();
            if (reminderTitle == null || reminderTitle.trim().equals("")) {
                Toast.makeText(AddRemindActivity.this, "提醒内容不能为空", Toast.LENGTH_SHORT).show();
                return;
            }
        } else {
            taskTitle = taskFragment.title.getText().toString();
            if (taskTitle == null || taskTitle.trim().equals("")) {
                Toast.makeText(AddRemindActivity.this, "标题不能为空", Toast.LENGTH_SHORT).show();
                return;
            }
        }

        //insert  to realm
        // All writes must be wrapped in a transaction to facilitate safe multi threading
        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                Number maxRequestCode = realm.where(Reminder.class).max("requestCode");
                int maxRemindCode = maxRequestCode == null ? 0 : maxRequestCode.intValue() + 1;
                Number maxTaskCode = realm.where(Task.class).max("requestCode");
                int maxTaskAlarmCode = maxTaskCode == null ? 0 : maxTaskCode.intValue() + 1;
                int requestCode = Math.max(maxRemindCode, maxTaskAlarmCode);
                //remind fragment
                if (viewPager.getCurrentItem() == 0) {
                    // Add a remind
                    final Reminder reminder = realm.createObject(Reminder.class, id);
                    SimpleDateFormat dayFormat = new SimpleDateFormat("yyyyMMdd");
                    SimpleDateFormat timeFormat = new SimpleDateFormat("HHmm");
                    reminder.title = reminderTitle;
                    Date reminderDate = reminderFragment.reminderTimeCalendar.getTime();
                    reminder.day = dayFormat.format(reminderDate);
                    reminder.time = timeFormat.format(reminderDate);
                    Log.d("zzw", "reminder time" + reminder.time);
                    reminder.repeatFlag = reminderFragment.repeatFlag;
                    reminder.repeatWeek = reminderFragment.repeatWeek;
                    reminder.aheadTime = reminderFragment.aheadTime;
                    reminder.isRemind = reminderFragment.isRemind;
                    reminder.isAllDay = reminderFragment.isAllDay;
                    reminder.reminderTime = reminderDate;
                    reminder.requestCode = requestCode;
                    reminder.userId = userId;
                    APIClient.addAlarm(reminder, reminderFragment.reminderTimeCalendar);
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
                    task.title = taskTitle;
                    task.userId = userId;
                    if (task.isOpen == 1) {
                        task.requestCode = requestCode;
                        APIClient.addAlarm4Task(task);
                    }
                }
            }
        }, new Realm.Transaction.OnSuccess() {
            @Override
            public void onSuccess() {
                if (preference.getBoolean(Const.IS_LOGIN, false)) {
                    //remind fragment
                    if (viewPager.getCurrentItem() == 0) {
                        Reminder r = realm.where(Reminder.class).equalTo("id", id).findFirst();
                        //add a new remind to server.
                        APIClient.synchronousReminds(r, CONST.UPDATE, null);
                    } else {
                        APIClient.synchronousTasks(realm.where(Task.class).equalTo(Task.TODO_ID, id).findFirst(), CONST.ADD);
                    }
                }
                onBackPressed();
            }
        });

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
