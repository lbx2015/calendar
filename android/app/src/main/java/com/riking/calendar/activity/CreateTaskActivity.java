package com.riking.calendar.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.riking.calendar.R;
import com.riking.calendar.realm.model.Task;
import com.riking.calendar.retrofit.APIClient;
import com.riking.calendar.util.CONST;
import com.riking.calendar.util.Preference;
import com.riking.calendar.util.ZDB;
import com.riking.calendar.util.ZR;

import java.text.SimpleDateFormat;
import java.util.Date;

import io.realm.Realm;

/**
 * Created by zw.zhang on 2017/7/11.
 */

public class CreateTaskActivity extends AppCompatActivity {
    //whether the task is an important task
    public byte isImportant;
    public EditText content;
    TextView importantTextView;
    EditText taskEditText;
    Task task;
    private ImageView importantImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_task);
        init();
    }

    public void clickBack(View v) {
        onBackPressed();
    }

    public void clickFinish(View v) {
        final String content = taskEditText.getText().toString();
        final SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
        final String id = sdf.format(new Date());
        ZDB.Instance.getRealm().executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                task = realm.createObject(Task.class, id);
                task.title = content;
                task.isImportant = isImportant;
            }
        }, new Realm.Transaction.OnSuccess() {
            @Override
            public void onSuccess() {
                if (Preference.pref.getBoolean(CONST.IS_LOGIN, false)) {
                    APIClient.synchronousTasks(task, CONST.ADD);
                }
                onBackPressed();
            }
        });
    }

    public void clickImportant(View v) {
        if (isImportant == 0) {
            isImportant = 1;
            importantImage.setImageDrawable(getDrawable(R.drawable.work_icon_star_s));
            importantTextView.setTextColor(ZR.getColor(R.color.color_222222));
        } else {
            isImportant = 0;
            importantImage.setImageDrawable(getDrawable(R.drawable.word_rm_icon_imp_n));
            importantTextView.setTextColor(ZR.getColor(R.color.color_999999));
        }
    }

    void init() {
        initViews();
        initEvents();
    }

    private void initEvents() {
    }

    private void initViews() {
        taskEditText = findViewById(R.id.task_content);
        importantImage = findViewById(R.id.import_image);
        importantTextView = findViewById(R.id.import_text_view);
    }

}