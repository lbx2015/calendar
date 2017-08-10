package com.riking.calendar.widget.dialog;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetDialog;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.riking.calendar.R;
import com.riking.calendar.activity.EditReminderActivity;
import com.riking.calendar.fragment.ReminderFragment;
import com.riking.calendar.realm.model.Reminder;

import java.text.SimpleDateFormat;

import io.realm.Realm;

/**
 * Created by zw.zhang on 2017/7/26.
 */

public class LookReminderDialog extends BottomSheetDialog {
    public View btnDelete, btnEdit;
    Reminder r;

    public LookReminderDialog(@NonNull Context context, final Reminder r, final Realm realm) {
        super(context);
        this.r = r;
        setContentView(R.layout.look_reminder_dialog);

        btnDelete = findViewById(R.id.delete);
        btnEdit = findViewById(R.id.edit);
        TextView reminderTitle = (TextView) findViewById(R.id.title);
        reminderTitle.setText(r.title);
        TextView remindTime = (TextView) findViewById(R.id.remind_time);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy年MM月dd日 HH:mm");
        remindTime.setText(simpleDateFormat.format(r.reminderTime));
        TextView remindWay = (TextView) findViewById(R.id.remind_way);
        View cancel = findViewById(R.id.cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        View deleteButton = findViewById(R.id.delete);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                realm.executeTransaction(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        realm.where(Reminder.class).equalTo("id", r.id).findFirst().deleteFromRealm();
                    }
                });
                Toast.makeText(v.getContext(), "deleted", Toast.LENGTH_LONG).show();
                dismiss();
            }
        });

        View editButton = findViewById(R.id.edit);
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(v.getContext(), EditReminderActivity.class);
                i.putExtra("reminder_id", r.id);
                i.putExtra("reminder_title", r.title);
                i.putExtra("remind_time", r.time);
                i.putExtra("remind_day", r.day);
                i.putExtra("is_all_day", r.isAllDay);
                i.putExtra("is_remind", r.isRemind);
                i.putExtra("ahead_time", r.aheadTime);
                i.putExtra("repeat_flag", r.repeatFlag);
                i.putExtra("repeat_week", r.repeatWeek);
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                i.putExtra("repeat_date", sdf.format(r.reminderTime));
                v.getContext().startActivity(i);
                dismiss();
            }
        });

        if (r.isRemind == 0) {
            remindWay.setText("不提醒");
        } else if (r.aheadTime > 0) {
            remindWay.setText("提前" + r.aheadTime + "分提醒");
        } else {
            remindWay.setText("正点提醒");
        }

        TextView repeat = (TextView) findViewById(R.id.remind_repeat);
        if (r.repeatFlag == 0) {
            repeat.setText("不重复");
        } else {
            repeat.setText("重复提醒");
        }

    }

    public LookReminderDialog(@NonNull Context context, int theme) {
        super(context, theme);
    }

    protected LookReminderDialog(@NonNull Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

}
