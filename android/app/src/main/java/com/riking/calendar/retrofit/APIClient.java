package com.riking.calendar.retrofit;

import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.ldf.calendar.Const;
import com.riking.calendar.app.MyApplication;
import com.riking.calendar.gson.AnnotationExclusionStrategy;
import com.riking.calendar.jiguang.Logger;
import com.riking.calendar.listener.ZCallBack;
import com.riking.calendar.listener.ZCallBackWithFail;
import com.riking.calendar.listener.ZRequestCallBack;
import com.riking.calendar.pojo.AppUser;
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
import com.riking.calendar.util.CONST;
import com.riking.calendar.util.GsonStringConverterFactory;
import com.riking.calendar.util.Preference;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmResults;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

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
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();
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

    public static void updatePendingUpdates() {
        final Realm realm = Realm.getDefaultInstance();
        final RealmResults<Reminder> reminders = realm.where(Reminder.class).equalTo("syncStatus", 1).findAll();
        final ArrayList<ReminderModel> reminderModels = new ArrayList<ReminderModel>();

        for (Reminder r : reminders) {
            reminderModels.add(new ReminderModel(r));
        }
        APIClient.apiInterface.synchronousReminds(reminderModels).enqueue(new ZCallBack<ResponseModel<String>>() {
            @Override
            public void callBack(ResponseModel<String> response) {
                if (!failed) {
                    realm.executeTransaction(new Realm.Transaction() {
                        @Override
                        public void execute(Realm realm) {
                            for (Reminder r : reminders) {
                                r.syncStatus = 0;
                                if (r.deleteState != 0) {
                                    r.deleteFromRealm();
                                }
                            }
                        }
                    });
                }
            }
        });

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

    public static void synchronousTasks(final Task task, final byte operationType) {
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
                                task.deleteFromRealm();
                            }
                        } else {
                            if (failed) {
                                task.syncStatus = 1;
                            } else {
                            }
                        }
                    }
                });
            }
        });
    }

    public static void synchronousReminds(final Reminder r, final byte operationType, final ZRequestCallBack callBack) {
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
                        if (operationType == 1) {
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
                                r.deleteFromRealm();
                            }
                        } else if (operationType == CONST.UPDATE) {
                            if (failed) {
                                Toast.makeText(MyApplication.APP, "上传失败", Toast.LENGTH_LONG).show();
                                r.syncStatus = 1;
                            } else {
                                Toast.makeText(MyApplication.APP, "上传成功", Toast.LENGTH_LONG).show();
                            }
                        }
                    }
                });
            }
        });
    }

    public static void synchAll() {
        AppUser u = new AppUser();
        u.id = Preference.pref.getString(Const.USER_ID, "");
        //get user's reminders and tasks
        APIClient.apiInterface.synchronousAll(u).enqueue(new ZCallBack<ResponseModel<SynResult>>() {
            @Override
            public void callBack(final ResponseModel<SynResult> response) {
                final Realm realm = Realm.getDefaultInstance();
                realm.executeTransactionAsync(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        List<ReminderModel> reminders = response._data.remind;
                        List<TaskModel> tasks = response._data.todo;
                        if (reminders != null) {
                            for (ReminderModel m : reminders) {
                                Reminder r = new Reminder(m);
                                realm.copyToRealmOrUpdate(r);
                            }
                        }
                        if (tasks != null) {
                            for (TaskModel m : tasks) {
                                Task t = new Task(m);
                                realm.copyToRealmOrUpdate(t);
                            }
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
}
