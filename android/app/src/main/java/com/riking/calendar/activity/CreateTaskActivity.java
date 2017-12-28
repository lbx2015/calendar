package com.riking.calendar.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.riking.calendar.R;
import com.riking.calendar.listener.ZCallBackWithFail;
import com.riking.calendar.pojo.base.ResponseModel;
import com.riking.calendar.pojo.params.Todo;
import com.riking.calendar.realm.model.Cat;
import com.riking.calendar.realm.model.Task;
import com.riking.calendar.retrofit.APIClient;
import com.riking.calendar.util.CONST;
import com.riking.calendar.util.DateUtil;
import com.riking.calendar.util.StringUtil;
import com.riking.calendar.util.ZDB;
import com.riking.calendar.util.ZR;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import io.realm.Realm;

/**
 * Created by zw.zhang on 2017/7/11.
 */
public class CreateTaskActivity extends AppCompatActivity {
    //whether the task is an important task
    public byte isImportant;
    TextView importantTextView;
    EditText taskEditText;
    String title;
    byte isImport;
    private ImageView importantImage;
    private Realm realm;
    private String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_task);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            id = bundle.getString("task_id");
            title = bundle.getString("task_title");
            isImport = bundle.getByte("is_import");
        }

        init();
    }

    public void clickBack(View v) {
        onBackPressed();
    }

    public void clickFinish(View v) {
        final String content = taskEditText.getText().toString();
        final SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");

        final String id = this.id == null ? sdf.format(new Date()) : this.id;
        Todo todo = new Todo();
        todo.content = content;
        todo.todoId = id;
        todo.isImportant = isImportant;
        todo.createdTime = DateUtil.date2String(new Date(), CONST.yyyyMMddHHmm);

        final ArrayList<Todo> tasks = new ArrayList<>(1);
        tasks.add(todo);
        APIClient.saveTodo(tasks, new ZCallBackWithFail<ResponseModel<String>>() {
            @Override
            public void callBack(ResponseModel<String> response) {
                ZDB.Instance.getRealm().executeTransactionAsync(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        Task task;
                        if (StringUtil.isEmpty(CreateTaskActivity.this.id)) {
                            task = realm.createObject(Task.class, id);
                        } else {
                            task = realm.where(Task.class).equalTo(Task.TODO_ID, id).findFirst();
                            realm.createObject(Cat.class, "1");

                        }

                        task.content = content;
                        task.isImportant = isImportant;
                        if (failed) {
                            //1 待同步
                            task.syncStatus = 1;
                        }
                    }
                }, new Realm.Transaction.OnSuccess() {
                    @Override
                    public void onSuccess() {
//                        if (ZPreference.pref.getBoolean(CONST.IS_LOGIN, false)) {
//                            APIClient.synchronousTasks(task, CONST.ADD);
//                        }
                        onBackPressed();
                    }
                });
            }
        });

    }

    public void clickImportant(View v) {
        if (isImportant == 0) {
            isImportant = 1;
        } else {
            isImportant = 0;
        }
        updateImportantImage();
    }

    private void updateImportantImage() {
        if (isImportant == 0) {
            importantImage.setImageDrawable(getDrawable(R.drawable.work_icon_star_s));
            importantTextView.setTextColor(ZR.getColor(R.color.color_222222));
        } else {
            importantImage.setImageDrawable(getDrawable(R.drawable.word_rm_icon_imp_n));
            importantTextView.setTextColor(ZR.getColor(R.color.color_999999));
        }
    }

    void init() {
        initViews();
        initEvents();
        initData();
    }

    private void initData() {
        if (!StringUtil.isEmpty(id)) {
            updateImportantImage();
            taskEditText.setText(title);
        }
    }

    private void initEvents() {
    }

    private void initViews() {
        taskEditText = findViewById(R.id.task_content);
        importantImage = findViewById(R.id.import_image);
        importantTextView = findViewById(R.id.import_text_view);
    }

}
