package com.riking.calendar.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.riking.calendar.R;
import com.riking.calendar.realm.model.Task;
import com.riking.calendar.retrofit.APIClient;
import com.riking.calendar.util.CONST;

import java.text.SimpleDateFormat;
import java.util.Date;

import io.realm.Realm;

/**
 * Created by zw.zhang on 2017/8/3.
 */

public class EditTaskActivity extends AppCompatActivity {
    CreateTaskFragment taskFragment;
    String title;
    byte isImport;
    boolean isRemind;
    String remindTime;
    private Realm realm;
    private String id;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_task);
        taskFragment = new CreateTaskFragment();
        setFragment(taskFragment);

        Bundle bundle = getIntent().getExtras();
        id = bundle.getString("task_id");
        title = bundle.getString("task_title");
        isImport = bundle.getByte("is_import");
        isRemind = bundle.getBoolean("is_remind");
        remindTime = bundle.getString("remind_time");
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

    @Override
    protected void onStart() {
        super.onStart();
        taskFragment.title.setText(title);
        taskFragment.isImportant = isImport;
        taskFragment.needToRemind = isRemind;
        if (isRemind) {
            taskFragment.aSwitch.toggle();
            taskFragment.remindTime.setText(remindTime);
        }
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
                Task task = realm.where(Task.class).equalTo(Task.TODO_ID, id).findFirst();
                task.isImportant = taskFragment.isImportant;
                SimpleDateFormat sdf = new SimpleDateFormat(CONST.yyyyMMddHHmm);
                task.appCreatedTime = sdf.format(new Date());
                if (taskFragment.needToRemind) {
                    task.isOpen = 1;
                    task.strDate = sdf.format(taskFragment.calendar.getTime());
                }
                task.title = taskFragment.title.getText().toString();
                APIClient.synchronousTasks(task, CONST.UPDATE);
            }
        });
        onBackPressed();
    }
}