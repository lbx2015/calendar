package com.riking.calendar.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.necer.ncalendar.utils.MyLog;
import com.riking.calendar.R;
import com.riking.calendar.fragment.CreateReminderFragment;
import com.riking.calendar.pojo.ReminderModel;
import com.riking.calendar.realm.model.Reminder;
import com.riking.calendar.retrofit.APIClient;
import com.riking.calendar.util.CONST;
import com.riking.calendar.util.DateUtil;
import com.riking.calendar.util.StringUtil;
import com.riking.calendar.util.ZDB;
import com.riking.calendar.util.ZPreference;
import com.riking.calendar.util.ZToast;

import java.text.SimpleDateFormat;
import java.util.Date;

import io.realm.Realm;

/**
 * Created by zw.zhang on 2017/8/3.
 */

public class EditReminderActivity extends AppCompatActivity {
    CreateReminderFragment reminderFragment;
    private String id;
    private String reportId;
    private String submitEndTime;
    private String submitStartTime;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_reminder);
        reminderFragment = new CreateReminderFragment();
        setFragment(reminderFragment);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            id = bundle.getString("reminder_id");
            reportId = bundle.getString("reportId");
            MyLog.d("got reportId on create " + reportId);
            submitStartTime = bundle.getString("submitStartTime");
            submitEndTime = bundle.getString("submitEndTime");
            reminderFragment.title = bundle.getString("reminder_title");
            reminderFragment.isRemind = bundle.getByte("is_remind");
            reminderFragment.isAllDay = bundle.getByte("is_all_day");
            reminderFragment.aheadTime = bundle.getByte("ahead_time");
            reminderFragment.repeatFlag = bundle.getByte("repeat_flag");
            reminderFragment.repeatWeek = bundle.getString("repeat_week");
            reminderFragment.remiderTime = bundle.getString("repeat_date");
            loadRemindFromDB();
        }
    }

    public void loadRemindFromDB() {
        if (!StringUtil.isEmpty(id)) {
            Reminder reminder = ZDB.Instance.getRealm().where(Reminder.class).equalTo(Reminder.REMINDER_ID, id).findFirst();
            if (reminder != null) {
                reminderFragment.title = reminder.title;
                reminderFragment.isRemind = reminder.isRemind;
                reminderFragment.isAllDay = reminder.isAllDay;
                reminderFragment.aheadTime = reminder.aheadTime;
                reminderFragment.repeatFlag = reminder.repeatFlag;
                reminderFragment.repeatWeek = reminder.repeatWeek;
                reminderFragment.remiderTime = DateUtil.getReminderTimeShowString(reminder.day, reminder.time);
            }
        }
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
        MyLog.d(" report id when click confirm: " + reportId);
        insertIntoRealm();
        ZToast.toast("闹钟添加成功");
        Intent intent = new Intent();
        intent.putExtra(CONST.REMINDER_ID, id);
        setResult(RESULT_OK, intent);
    }

    private void insertIntoRealm() {
        //insert  to realm
        // All writes must be wrapped in a transaction to facilitate safe multi threading
        ZDB.Instance.getRealm().executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                Reminder reminder;
                if (StringUtil.isEmpty(id)) {
                    id = DateUtil.date2String(new Date(), CONST.YYYYMMDDHHMMSSSSS);
                    reminder = realm.createObject(Reminder.class, id);
                } else {
                    reminder = realm.where(Reminder.class).equalTo(Reminder.REMINDER_ID, id).findFirst();
                }
                if (reminder == null) return;
                SimpleDateFormat dayFormat = new SimpleDateFormat("yyyyMMdd");
                SimpleDateFormat timeFormat = new SimpleDateFormat("HHmm");
                reminder.title = reminderFragment.title;
                reminder.reportId = reportId;
                Date reminderDate = reminderFragment.reminderTimeCalendar.getTime();
                reminder.reminderTime = reminderDate;
                reminder.day = dayFormat.format(reminderDate);
                reminder.time = timeFormat.format(reminderDate);
                Log.d("zzw", "saved day : " + reminder.day + " saved time: " + reminder.time);
                reminder.repeatFlag = reminderFragment.repeatFlag;
                reminder.repeatWeek = reminderFragment.repeatWeek;
                reminder.aheadTime = reminderFragment.aheadTime;
                reminder.isRemind = reminderFragment.isRemind;
                reminder.isAllDay = reminderFragment.isAllDay;
                reminder.userId = ZPreference.getUserId();
                reminder.submitEndTime = submitEndTime;
                reminder.submitStartTime = submitStartTime;
                APIClient.addAlarm(reminder, reminderDate);
//                APIClient.synchronousReminds(reminder, CONST.UPDATE, null);
            }
        }, new Realm.Transaction.OnSuccess() {
            @Override
            public void onSuccess() {
                MyLog.d("reportId " + reportId);
                APIClient.addRemind(new ReminderModel(ZDB.Instance.getRealm().where(Reminder.class).equalTo(Reminder.REMINDER_ID, id).findFirst()));
            }
        });
    }
}
