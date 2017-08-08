package com.riking.calendar.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.riking.calendar.R;
import com.riking.calendar.fragment.CreateReminderFragment;
import com.riking.calendar.realm.model.Reminder;

import java.text.SimpleDateFormat;
import java.util.Date;

import io.realm.Realm;

/**
 * Created by zw.zhang on 2017/8/3.
 */

public class EditReminderActivity extends AppCompatActivity {
    CreateReminderFragment reminderFragment;

    private Realm realm;
    private String id;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_reminder);
        reminderFragment = new CreateReminderFragment();
        setFragment(reminderFragment);

        Bundle bundle = getIntent().getExtras();
        id = bundle.getString("reminder_id");
        reminderFragment.title = bundle.getString("reminder_title");
        reminderFragment.isRemind = bundle.getByte("is_remind");
        reminderFragment.isAllDay = bundle.getByte("is_all_day");
        reminderFragment.aheadTime = bundle.getByte("ahead_time");
        reminderFragment.repeatFlag = bundle.getByte("repeat_flag");
        reminderFragment.repeatWeek = bundle.getString("repeat_week");
        reminderFragment.remiderTime = bundle.getString("repeat_date");
    }

    // This could be moved into an abstract BaseActivity
    // class for being re-used by several instances
    protected void setFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction =
                fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_containerone, fragment);
        fragmentTransaction.commit();
    }


    public void onClickCancel(View v) {
        onBackPressed();
    }

    public void onClickConfirm(View v) {
        // Create the Realm instance
        realm = Realm.getDefaultInstance();

        //insert  to realm
        // All writes must be wrapped in a transaction to facilitate safe multi threading
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                Reminder reminder = realm.where(Reminder.class).equalTo("id", id).findFirst();
                SimpleDateFormat dayFormat = new SimpleDateFormat("yyyyMMdd");
                SimpleDateFormat timeFormat = new SimpleDateFormat("HHmm");
                reminder.title = reminderFragment.remindTitle.getText().toString();
                Date reminderDate = reminderFragment.time.getTime();
                reminder.day = dayFormat.format(reminderDate);
                reminder.time = timeFormat.format(reminderDate);
                Log.d("zzw", "saved day : " + reminder.day + " saved time: " + reminder.time);
                reminder.repeatFlag = reminderFragment.repeatFlag;
                reminder.repeatWeek = reminderFragment.repeatWeek;
                reminder.aheadTime = reminderFragment.aheadTime;
                reminder.isRemind = reminderFragment.isRemind;
                reminder.isAllDay = reminderFragment.isAllDay;
                reminder.reminderTime = reminderDate;
            }
        });

        onBackPressed();
    }
}
