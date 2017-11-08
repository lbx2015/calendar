package com.riking.calendar.retrofit;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import com.riking.calendar.BuildConfig;
import com.riking.calendar.app.MyApplication;
import com.riking.calendar.gson.AnnotationExclusionStrategy;
import com.riking.calendar.jiguang.Logger;
import com.riking.calendar.listener.CheckCallBack;
import com.riking.calendar.listener.ZCallBack;
import com.riking.calendar.listener.ZCallBackWithFail;
import com.riking.calendar.listener.ZRequestCallBack;
import com.riking.calendar.pojo.AppUser;
import com.riking.calendar.pojo.AppVersionResult;
import com.riking.calendar.pojo.QueryReport;
import com.riking.calendar.pojo.QueryReportContainer;
import com.riking.calendar.pojo.ReminderModel;
import com.riking.calendar.pojo.TaskModel;
import com.riking.calendar.pojo.WorkDate;
import com.riking.calendar.pojo.base.ResponseModel;
import com.riking.calendar.pojo.synch.SynResult;
import com.riking.calendar.realm.model.QueryReportContainerRealmModel;
import com.riking.calendar.realm.model.QueryReportRealmModel;
import com.riking.calendar.realm.model.Reminder;
import com.riking.calendar.realm.model.Task;
import com.riking.calendar.realm.model.WorkDateRealm;
import com.riking.calendar.service.ReminderService;
import com.riking.calendar.util.CONST;
import com.riking.calendar.util.DateUtil;
import com.riking.calendar.util.GsonStringConverterFactory;
import com.riking.calendar.util.Preference;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmResults;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;

/**
 * Created by zzw on 05/01/17.
 */

public class APIClient {

    private static Retrofit retrofit = null;
    public static APIInterface apiInterface = getClient().create(APIInterface.class);

    public static Retrofit getClient() {
        if (retrofit != null)
            return retrofit;
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();
        retrofit = new Retrofit.Builder()
//                .baseUrl("http://www.baidu.com")
//                .baseUrl("https://reqres.in")
                .baseUrl(CONST.URL_BASE)
//                .baseUrl("http://172.16.64.96:8281/")
//                .baseUrl("http://172.16.64.85:8281/")
                .addConverterFactory(new GsonStringConverterFactory())
                .addConverterFactory(GsonConverterFactory.create(new GsonBuilder().setExclusionStrategies(new AnnotationExclusionStrategy()).create()))
                .client(client)
                .build();
        return retrofit;
    }

    public static void checkVarificationCode(@Body AppUser user, final ZCallBack<ResponseModel<AppUser>> callBack){
        apiInterface.checkVarificationCode(user).enqueue(callBack);
    }
    public static void getVarificationCode( AppUser user, final ZCallBackWithFail<ResponseModel<AppUser>> callBack){
        apiInterface.getVarificationCode(user).enqueue(callBack);
    }

    public static void updatePendingReminds(final ZRequestCallBack callBack) {
        final Realm realm = Realm.getDefaultInstance();
        final RealmResults<Reminder> reminders = realm.where(Reminder.class).equalTo("syncStatus", 1).findAll();
        final ArrayList<ReminderModel> reminderModels = new ArrayList<ReminderModel>();

        for (Reminder r : reminders) {
            reminderModels.add(new ReminderModel(r));
        }
        APIClient.apiInterface.synchronousReminds(reminderModels).enqueue(new ZCallBack<ResponseModel<String>>() {
            @Override
            public void callBack(ResponseModel<String> response) {
                if (callBack != null) {
                    callBack.success();
                }
                realm.executeTransaction(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        for (Reminder r : reminders) {
                            r.syncStatus = 0;
                            if (r.deleteState != 0) {
                                cancelAlarm(r.requestCode);
                                r.deleteFromRealm();
                            }
                        }
                    }
                });
            }
        });

    }

    public static void updatePendingTasks(final ZRequestCallBack callBack) {
        final Realm realm = Realm.getDefaultInstance();
        //upload pending tasks
        final RealmResults<Task> tasks = realm.where(Task.class).equalTo("syncStatus", 1).findAll();
        final List<TaskModel> models = new ArrayList<>();

        Logger.d("zzw", "found padding tasks size " + tasks.size());
        for (Task t : tasks) {
            models.add(new TaskModel(t));
        }

        apiInterface.synchronousTasks(models).enqueue(new ZCallBack<ResponseModel<String>>() {
            @Override
            public void callBack(ResponseModel<String> response) {
                realm.executeTransaction(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        if (callBack != null) {
                            callBack.success();
                        }
                        for (Task task : tasks) {
                            //the task is updated
                            task.syncStatus = 0;
                            //delete the pending item from realm
                            if (task.deleteState != 0) {
                                task.deleteFromRealm();
                            }
                        }
                    }
                });
            }
        });
    }

    public static void updatePendingUpdates() {
        updatePendingReminds(null);
        updatePendingTasks(null);
    }

    public static void synchronousTasks(final Task task, final byte operationType) {
        if (!task.isValid()) {
            return;
        }
        final ArrayList<TaskModel> tasks = new ArrayList<>(1);
        TaskModel t = new TaskModel(task);
        if (operationType == CONST.DELETE) {
            t.deleteState = 1;
        }

        tasks.add(t);
        apiInterface.synchronousTasks(tasks).enqueue(new ZCallBackWithFail<ResponseModel<String>>() {
            @Override
            public void callBack() {
                Realm realm = Realm.getDefaultInstance();
                realm.executeTransaction(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        if (operationType == CONST.DELETE) {
                            if (failed) {
                                Logger.d("zzw", "set delete State 1 of " + task.title);
                                task.deleteState = 1;
                                task.syncStatus = 1;
                            } else {
                                cancelAlarm(task.requestCode);
                                task.deleteFromRealm();
                            }
                        } else {
                            if (failed) {
                                task.syncStatus = 1;
                            } else {
                                addAlarm4Task(task);
                            }
                        }
                    }
                });
            }
        });
    }

    public static void synchronousReminds(final Reminder r, final byte operationType, final ZRequestCallBack callBack) {
        if (!r.isValid()) {
            return;
        }
        final ArrayList<ReminderModel> reminderModels = new ArrayList<ReminderModel>(1);
        ReminderModel m = null;
        if (operationType == 1) {
            m = new ReminderModel();
            m.id = r.id;
            m.deleteState = 1;
        } else {
            m = new ReminderModel(r);
        }
        reminderModels.add(m);
        APIClient.apiInterface.synchronousReminds(reminderModels).enqueue(new ZCallBackWithFail<ResponseModel<String>>() {
            @Override
            public void callBack() {
                Realm realm = Realm.getDefaultInstance();
                realm.executeTransaction(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        if (operationType == CONST.DELETE) {
                            if (failed) {
                                if (callBack != null) {
                                    callBack.fail();
                                }
                                r.deleteState = 1;
                                r.syncStatus = 1;
                            } else {
                                if (callBack != null) {
                                    callBack.success();
                                }
                                cancelAlarm(r.requestCode);
                                r.deleteFromRealm();
                            }
                        } else if (operationType == CONST.UPDATE) {
                            if (failed) {
                                Toast.makeText(MyApplication.APP, "上传失败", Toast.LENGTH_LONG).show();
                                r.syncStatus = 1;
                            } else {
                                addAlarm(r, r.reminderTime);
                                Toast.makeText(MyApplication.APP, "上传成功", Toast.LENGTH_LONG).show();
                            }
                        }
                    }
                });
            }
        });
    }

    public static void cancelAlarm(int requestCode) {
        Intent intent = new Intent(MyApplication.APP, ReminderService.class);
        PendingIntent pendingIntent = PendingIntent.getService(MyApplication.APP, requestCode, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager = (AlarmManager) MyApplication.APP.getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(pendingIntent);
    }

    /**
     * create alarm
     */
    public static void addAlarm(Reminder r, Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        addAlarm(r, c);
    }

    public static void addAlarm4Task(Task task) {
        if (task.isOpen == 1) {
            SimpleDateFormat sdf = new SimpleDateFormat(CONST.yyyyMMddHHmm);
            AlarmManager alarmManager = (AlarmManager) MyApplication.APP.getSystemService(Context.ALARM_SERVICE);
            Intent intent = new Intent(MyApplication.APP, ReminderService.class);
            intent.putExtra(CONST.REMINDER_TITLE, task.title);
            PendingIntent pendingIntent = PendingIntent.getService(MyApplication.APP, task.requestCode, intent, PendingIntent.FLAG_UPDATE_CURRENT);
            try {
                alarmManager.setExact(AlarmManager.RTC_WAKEUP, sdf.parse(task.strDate).getTime(), pendingIntent);
            } catch (ParseException e) {
                Logger.d("zzw", "parse failed.");
            }
        }
        //cancel the task
        else if (task.requestCode > 0) {
            cancelAlarm(task.requestCode);
        }
    }

    /**
     * create alarm
     */
    public static void addAlarm(Reminder r, Calendar time) {
        AlarmManager alarmManager = (AlarmManager) MyApplication.APP.getSystemService(Context.ALARM_SERVICE);
        //set reminder
        Intent intent = new Intent(MyApplication.APP, ReminderService.class);
        intent.putExtra(CONST.REMINDER_TITLE, r.title);
        PendingIntent pendingIntent = PendingIntent.getService(MyApplication.APP, r.requestCode, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        //alarmManager.cancel(pendingIntent);
        Calendar reminderCalendar = time;
        // 这里时区需要设置一下，不然可能个别手机会有8个小时的时间差
        reminderCalendar.setTimeZone(TimeZone.getTimeZone("GMT+8"));
        reminderCalendar.set(java.util.Calendar.MINUTE, time.get(java.util.Calendar.MINUTE) - r.aheadTime);
        //下面这两个看字面意思也知道
        reminderCalendar.set(Calendar.SECOND, 0);
        reminderCalendar.set(Calendar.MILLISECOND, 0);
        Logger.d("zzw", "提醒时间1-->" + DateUtil.getCustonFormatTime(reminderCalendar.getTimeInMillis(), "yyyy/MM/dd/HH/mm"));
        if (r.isRemind == 1 && r.repeatFlag == 0) {
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, reminderCalendar.getTimeInMillis(), pendingIntent);
//                        long intervalMillis = 1000;
//                        alarmManager.setWindow(AlarmManager.RTC_WAKEUP, reminderCalendar.getTimeInMillis(),
//                                intervalMillis, pendingIntent);
        } else if (r.isRemind == 1 && r.repeatFlag == 3) {
            String repeatWeek = r.repeatWeek;
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

    public static void synchAll() {
        ZRequestCallBack updateCallBack = new ZRequestCallBack() {
            @Override
            public void success() {
                successCount = successCount + 1;
                if (successCount == 2) {
                    getReminderAndTasksFromServer();
                }
            }

            @Override
            public void fail() {
                successCount = successCount - 1;
            }
        };
        updatePendingReminds(updateCallBack);
        updatePendingTasks(updateCallBack);
    }

    public static void getReminderAndTasksFromServer() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("id", Preference.pref.getString(CONST.USER_ID, ""));
        //get user's reminders and tasks
        APIClient.apiInterface.synchronousAll(jsonObject).enqueue(new ZCallBack<ResponseModel<SynResult>>() {
            @Override
            public void callBack(final ResponseModel<SynResult> response) {
                final Realm realm = Realm.getDefaultInstance();
                realm.executeTransactionAsync(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        //alread synched tasks which means those data in server, if server not contain them, means deleted from server.
                        RealmResults<Task> synchedTasks = realm.where(Task.class).equalTo("syncStatus", 0).findAll();
                        RealmResults<Reminder> synchedReminders = realm.where(Reminder.class).equalTo("syncStatus", 0).findAll();

                        ArrayList<String> remindIds = new ArrayList<String>();
                        ArrayList<String> taskIds = new ArrayList<String>();
                        for (Reminder r : synchedReminders) {
                            remindIds.add(r.id);
                        }
                        for (Task t : synchedTasks) {
                            taskIds.add(t.todo_Id);
                        }

                        List<ReminderModel> reminders = response._data.remind;
                        List<TaskModel> tasks = response._data.todo;
                        if (reminders != null) {
                            for (ReminderModel m : reminders) {
                                int requestCode = realm.where(Reminder.class).equalTo("id", m.id).findFirst().requestCode;
                                Reminder r = new Reminder(m);
                                remindIds.remove(r.id);
                                r.requestCode = requestCode;
                                realm.copyToRealmOrUpdate(r);
                                addAlarm(r, r.reminderTime);
                            }
                        }

                        //delete those which is from server
                        for (String id : remindIds) {
                            Reminder r = realm.where(Reminder.class).equalTo("id", id).findFirst();
                            cancelAlarm(r.requestCode);
                            r.deleteFromRealm();
                        }

                        if (tasks != null) {
                            for (TaskModel m : tasks) {
                                Task t = new Task(m);
                                int requestCode = realm.where(Task.class).equalTo("todo_Id", t.todo_Id).findFirst().requestCode;
                                t.requestCode = requestCode;
                                taskIds.remove(t.todo_Id);
                                realm.copyToRealmOrUpdate(t);
                                addAlarm4Task(t);
                            }
                        }

                        //delete those which is from server
                        for (String id : taskIds) {
                            realm.where(Task.class).equalTo(Task.TODO_ID, id).findFirst().deleteFromRealm();
                        }
                    }
                });
            }
        });
    }

    public static void getWorkDays() {
        APIClient.apiInterface.getWorkDays().enqueue(new ZCallBack<ResponseModel<ArrayList<WorkDate>>>() {
            @Override
            public void callBack(final ResponseModel<ArrayList<WorkDate>> response) {
                final Realm realm = Realm.getDefaultInstance();
                Logger.d("zzw", "load work date ok");
                realm.executeTransaction(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        ArrayList<WorkDate> workDates = response._data;
                        Logger.d("zzw", "workDates size" + workDates.size());
                        for (WorkDate c : workDates) {
                            WorkDateRealm r = new WorkDateRealm();
                            r.date = c.date;
                            r.weekday = c.weekday;
                            r.isWork = c.isWork;
                            realm.copyToRealmOrUpdate(r);
                        }
                    }
                });
            }
        });
    }

    /**
     * when user not login ,showing all reports
     */
    public static void getAllReports() {
        APIClient.apiInterface.getAllReports(null).enqueue(new ZCallBack<ResponseModel<ArrayList<QueryReportContainer>>>() {
            @Override
            public void callBack(final ResponseModel<ArrayList<QueryReportContainer>> response) {
                final Realm realm = Realm.getDefaultInstance();
                realm.executeTransaction(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        ArrayList<QueryReportContainer> reportContainers = response._data;
                        Logger.d("zzw", "report size" + reportContainers.size());
                        for (QueryReportContainer c : reportContainers) {
                            QueryReportContainerRealmModel queryReportContainerRealmModel = new QueryReportContainerRealmModel();
                            queryReportContainerRealmModel.title = c.title;
                            queryReportContainerRealmModel.result = new RealmList<>();
                            for (QueryReport q : c.result) {
                                QueryReportRealmModel reportRealmModel = new QueryReportRealmModel();
                                reportRealmModel.id = q.id;
                                reportRealmModel.reportName = q.reportName;
                                reportRealmModel.moduleType = q.moduleType;
                                reportRealmModel.reportCode = q.reportCode;
                                queryReportContainerRealmModel.result.add(reportRealmModel);
                            }
                            realm.copyToRealmOrUpdate(queryReportContainerRealmModel);
                        }
                    }
                });

            }
        });
    }

    public static void checkUpdate(final CheckCallBack updateCallback) {
        JsonObject j = new JsonObject();
        j.addProperty("versionNumber", BuildConfig.VERSION_NAME);
        j.addProperty("type", "2");//1 is iphone 2 is android
        apiInterface.getAppVersion(j).enqueue(new ZCallBack<ResponseModel<AppVersionResult>>() {
            @Override
            public void callBack(ResponseModel<AppVersionResult> response) {
                updateCallback.onSuccess(response._data);
            }
        });
    }
}
