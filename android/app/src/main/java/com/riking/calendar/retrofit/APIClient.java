package com.riking.calendar.retrofit;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.necer.ncalendar.utils.MyLog;
import com.riking.calendar.BuildConfig;
import com.riking.calendar.app.MyApplication;
import com.riking.calendar.gson.AnnotationExclusionStrategy;
import com.riking.calendar.jiguang.Logger;
import com.riking.calendar.listener.CheckCallBack;
import com.riking.calendar.listener.ZCallBack;
import com.riking.calendar.listener.ZCallBackWithFail;
import com.riking.calendar.listener.ZCallBackWithoutProgress;
import com.riking.calendar.listener.ZRequestCallBack;
import com.riking.calendar.pojo.AppUser;
import com.riking.calendar.pojo.AppUserRecommend;
import com.riking.calendar.pojo.AppVersionResult;
import com.riking.calendar.pojo.ReminderModel;
import com.riking.calendar.pojo.TaskModel;
import com.riking.calendar.pojo.WorkDate;
import com.riking.calendar.pojo.base.ResponseModel;
import com.riking.calendar.pojo.params.CommentParams;
import com.riking.calendar.pojo.params.HomeParams;
import com.riking.calendar.pojo.params.NewsParams;
import com.riking.calendar.pojo.params.QAnswerParams;
import com.riking.calendar.pojo.params.RCompletedRelParams;
import com.riking.calendar.pojo.params.ReportCompletedRelParam;
import com.riking.calendar.pojo.params.ReportParams;
import com.riking.calendar.pojo.params.SearchParams;
import com.riking.calendar.pojo.params.SubscribeReportParam;
import com.riking.calendar.pojo.params.TQuestionParams;
import com.riking.calendar.pojo.params.Todo;
import com.riking.calendar.pojo.params.TopicParams;
import com.riking.calendar.pojo.params.UpdUserParams;
import com.riking.calendar.pojo.params.UserFollowParams;
import com.riking.calendar.pojo.params.UserParams;
import com.riking.calendar.pojo.resp.AppUserResp;
import com.riking.calendar.pojo.server.AppUserResult;
import com.riking.calendar.pojo.server.Banner;
import com.riking.calendar.pojo.server.CurrentReportTaskResp;
import com.riking.calendar.pojo.server.HotSearch;
import com.riking.calendar.pojo.server.Industry;
import com.riking.calendar.pojo.server.NCReply;
import com.riking.calendar.pojo.server.News;
import com.riking.calendar.pojo.server.NewsComment;
import com.riking.calendar.pojo.server.OtherUserResp;
import com.riking.calendar.pojo.server.QAComment;
import com.riking.calendar.pojo.server.QACommentResult;
import com.riking.calendar.pojo.server.QAExcellentResp;
import com.riking.calendar.pojo.server.QAnswerResult;
import com.riking.calendar.pojo.server.QuestResult;
import com.riking.calendar.pojo.server.QuestionAnswer;
import com.riking.calendar.pojo.server.ReportCompletedRelResult;
import com.riking.calendar.pojo.server.ReportListResult;
import com.riking.calendar.pojo.server.ReportResult;
import com.riking.calendar.pojo.server.SysNoticeParams;
import com.riking.calendar.pojo.server.SysNoticeResult;
import com.riking.calendar.pojo.server.TQuestionResult;
import com.riking.calendar.pojo.server.Topic;
import com.riking.calendar.pojo.server.TopicQuestion;
import com.riking.calendar.pojo.server.UserOperationInfo;
import com.riking.calendar.pojo.synch.LoginParams;
import com.riking.calendar.pojo.synch.SynResult;
import com.riking.calendar.realm.model.Reminder;
import com.riking.calendar.realm.model.Task;
import com.riking.calendar.realm.model.WorkDateRealm;
import com.riking.calendar.service.ReminderService;
import com.riking.calendar.util.CONST;
import com.riking.calendar.util.DateUtil;
import com.riking.calendar.util.GsonStringConverterFactory;
import com.riking.calendar.util.StringUtil;
import com.riking.calendar.util.ZDB;
import com.riking.calendar.util.ZPreference;
import com.riking.calendar.util.ZR;
import com.riking.calendar.util.convert.DateTypeDeserializer;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import io.realm.Realm;
import io.realm.RealmResults;
import okhttp3.Interceptor;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Callback;
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
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(new Interceptor() {

                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        String pkParam = System.currentTimeMillis() + ZR.getDeviceId();
                        Request original = chain.request();
                        Request request = original.newBuilder()
                                .header("pkParams", pkParam)
                                .header("token", ZR.getPrivateKey(pkParam))
                                .method(original.method(), original.body())
                                .build();
                        return chain.proceed(request);
                    }
                })
                .addInterceptor(interceptor).build();

        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(Date.class, new DateTypeDeserializer());
        gsonBuilder.setExclusionStrategies(new AnnotationExclusionStrategy());
        retrofit = new Retrofit.Builder()
//                .baseUrl("http://www.baidu.com")
//                .baseUrl("https://reqres.in")
//                .baseUrl(CONST.TL_API_TEST)
                .baseUrl(CONST.TL_API_TEST)
//                .baseUrl("http://172.16.64.96:8281/")
//                .baseUrl("http://172.16.64.85:8281/")
                .addConverterFactory(new GsonStringConverterFactory())
                .addConverterFactory(GsonConverterFactory.create(gsonBuilder.create()))
                .client(client)
                .build();
        return retrofit;
    }

    public static void checkVarificationCode(@Body LoginParams user, final ZCallBackWithFail<ResponseModel<AppUserResp>> callBack) {
        user.phone = StringUtil.getPhoneNumber(user.phone);
        apiInterface.checkVarificationCode(user).enqueue(callBack);
    }

    public static void getVarificationCode(LoginParams user, final ZCallBackWithFail<ResponseModel<AppUser>> callBack) {
        apiInterface.getVarificationCode(user).enqueue(callBack);
    }

    public static void updatePendingReminds(final ZRequestCallBack callBack) {
        final Realm realm = Realm.getDefaultInstance();
        final RealmResults<Reminder> reminders = realm.where(Reminder.class).equalTo("syncStatus", 1).findAll();
        final ArrayList<ReminderModel> reminderModels = new ArrayList<ReminderModel>();

        for (Reminder r : reminders) {
            reminderModels.add(new ReminderModel(r));
        }
        APIClient.apiInterface.synchronousReminds(reminderModels).enqueue(new ZCallBackWithoutProgress<ResponseModel<String>>() {
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
        final List<Todo> models = new ArrayList<>();

        Logger.d("zzw", "found padding tasks size " + tasks.size());
        for (Task t : tasks) {
            models.add(new Todo(t));
        }
        apiInterface.synchronousTasks(models).enqueue(new ZCallBackWithoutProgress<ResponseModel<String>>() {
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
        final ArrayList<Todo> tasks = new ArrayList<>(1);
        Todo t = new Todo(task);
        if (operationType == CONST.DELETE) {
            t.deleteFlag = 1;
        }

        tasks.add(t);
        apiInterface.synchronousTasks(tasks).enqueue(new ZCallBackWithFail<ResponseModel<String>>() {
            @Override
            public void callBack(ResponseModel<String> s) {
                Realm realm = Realm.getDefaultInstance();
                realm.executeTransaction(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        if (operationType == CONST.DELETE) {
                            if (failed) {
                                Logger.d("zzw", "set delete State 1 of " + task.content);
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

    public static void addRemind(final ReminderModel r) {
        MyLog.d(" remind model: " + r);
        apiInterface.saveRemind(r).enqueue(new ZCallBackWithFail<ResponseModel<ReminderModel>>() {
            @Override
            public void callBack(ResponseModel<ReminderModel> response) throws Exception {
                if (failed) {
                    Toast.makeText(MyApplication.APP, "上传失败", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(MyApplication.APP, "上传成功", Toast.LENGTH_LONG).show();
                }
                ZDB.Instance.getRealm().executeTransactionAsync(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        Reminder reminder = realm.where(Reminder.class).equalTo(Reminder.REMINDER_ID, r.id).findFirst();
                        if (failed) {
                            reminder.syncStatus = 1;
                        } else {
//                            addAlarm(reminder, reminder.reminderTime);
                        }
                    }
                }, new Realm.Transaction.OnSuccess() {
                    @Override
                    public void onSuccess() {
//                        Reminder reminder = ZDB.Instance.getRealm().where(Reminder.class).equalTo(Reminder.REMINDER_ID, r.id).findFirst();
//                        addAlarm(reminder, reminder.reminderTime);
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
            public void callBack(ResponseModel<String> s) {
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
            intent.putExtra(CONST.REMINDER_TITLE, task.content);
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
        Logger.d("zzw", "提醒时间1-->" + DateUtil.getCustonFormatTime(reminderCalendar.getTimeInMillis(), "yyyy/MM/dd/HH/mm") + r.toString());
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
        jsonObject.addProperty("userId", ZPreference.pref.getString(CONST.USER_ID, ""));
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
                            taskIds.add(t.todoId);
                        }

                        List<ReminderModel> reminders = response._data.remind;
                        List<TaskModel> tasks = response._data.todo;
                        if (reminders != null) {
                            for (ReminderModel m : reminders) {
                                int requestCode = realm.where(Reminder.class).equalTo(Reminder.REMINDER_ID, m.id).findFirst().requestCode;
                                Reminder r = new Reminder(m);
                                remindIds.remove(r.id);
                                r.requestCode = requestCode;
                                realm.copyToRealmOrUpdate(r);
                                addAlarm(r, r.reminderTime);
                            }
                        }

                        //delete those which is from server
                        for (String id : remindIds) {
                            Reminder r = realm.where(Reminder.class).equalTo(Reminder.REMINDER_ID, id).findFirst();
                            cancelAlarm(r.requestCode);
                            r.deleteFromRealm();
                        }

                        if (tasks != null) {
                            for (TaskModel m : tasks) {
                                Task t = new Task(m);
                                int requestCode = realm.where(Task.class).equalTo(Task.TODO_ID, t.todoId).findFirst().requestCode;
                                t.requestCode = requestCode;
                                taskIds.remove(t.todoId);
                                realm.copyToRealmOrUpdate(t);
                                addAlarm4Task(t);
                            }
                        }

                        //delete those which is from server
//                        for (String id : taskIds) {
//                            realm.where(Task.class).equalTo(Task.TODO_ID, id).findFirst().deleteFromRealm();
//                        }
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

    public static void getAllReports(Callback<ResponseModel<ArrayList<AppUserRecommend>>> zCallBack) {
        APIClient.apiInterface.getPositionByIndustry().enqueue(zCallBack);
    }


    /**
     * when user not login ,showing all reports
     */
    public static void getAllReports() {
       /* APIClient.apiInterface.getAllReports(null).enqueue(new ZCallBack<ResponseModel<ArrayList<QueryReportContainer>>>() {
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
                            //adding null protection
                            if (c.result == null) continue;
                            for (QueryReport q : c.result) {
                                QueryReportRealmModel reportRealmModel = new QueryReportRealmModel();
                                reportRealmModel.userId = q.userId;
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
        });*/
    }

    public static void checkUpdate(final CheckCallBack updateCallback) {
        JsonObject j = new JsonObject();
        j.addProperty("versionNo", BuildConfig.VERSION_NAME);
        j.addProperty("clientType", "2");//1 is iphone 2 is android
        apiInterface.getAppVersion(j).enqueue(new ZCallBack<ResponseModel<AppVersionResult>>() {
            @Override
            public void callBack(ResponseModel<AppVersionResult> response) {
                if (response._data != null) {
                    updateCallback.onSuccess(response._data);
                }
            }
        });
    }

    /**
     * get industries
     */
    public static void getIndustries(final ZCallBackWithFail<ResponseModel<ArrayList<Industry>>> c) {
        apiInterface.findIndustry().enqueue(c);
    }

    /**
     * get positions
     */
    public static void getPositions(HashMap<String, String> industryMap, final ZCallBackWithFail<ResponseModel<ArrayList<Industry>>> c) {
        apiInterface.getPositionByIndustry(industryMap).enqueue(c);
    }

    public static void findUserReportList(AppUser user, ZCallBackWithFail<ResponseModel<List<ReportResult>>> c) {
        apiInterface.findSubscribeReportList(user).enqueue(c);
    }

    public static void userAddReportEdit(SubscribeReportParam reportResult, ZCallBackWithFail<ResponseModel<String>> z) {
        apiInterface.saveSubscribeReport(reportResult).enqueue(z);
    }

    public static void updateUserReportRelById(SubscribeReportParam reportRel, ZCallBack<ResponseModel<String>> c) {
        apiInterface.updateUserReportRelById(reportRel).enqueue(c);
    }

    public static void findNewsList(NewsParams params, ZCallBack<ResponseModel<List<News>>> c) {
        apiInterface.findNewsList(params).enqueue(c);
    }

    public static void getNewsDetail(NewsParams params, ZCallBack<ResponseModel<News>> c) {
        apiInterface.getNewsDetail(params).enqueue(c);
    }

    public static void findNewsCommentList(NewsParams params, ZCallBackWithFail<ResponseModel<News>> c) {
        apiInterface.findNewsCommentList(params).enqueue(c);
    }

    public static void newsCollect(NewsParams params, ZCallBack<ResponseModel<String>> c) {
        apiInterface.newsCollect(params).enqueue(c);
    }

    public static void newsCommentPub(NewsParams params, ZCallBackWithFail<ResponseModel<NewsComment>> c) {
        apiInterface.newsCommentPub(params).enqueue(c);
    }

    public static void commentReply(CommentParams params, ZCallBackWithFail<ResponseModel<NCReply>> c) {
        apiInterface.commentReply(params).enqueue(c);
    }

    public static void commentAgree(CommentParams params, ZCallBack<ResponseModel<String>> c) {
        apiInterface.commentAgree(params).enqueue(c);
    }

    public static void shieldQuestion(HomeParams params, ZCallBack<ResponseModel<String>> c) {
        apiInterface.shielfProblem(params).enqueue(c);
    }

    public static void getTopicQuestion(TQuestionParams params, ZCallBack<ResponseModel<TopicQuestion>> c) {
        apiInterface.getTopicQuestion(params).enqueue(c);
    }

    public static void follow(TQuestionParams params, ZCallBack<ResponseModel<String>> c) {
        apiInterface.follow(params).enqueue(c);
    }

    public static void qAnswerAgree(QAnswerParams params, ZCallBack<ResponseModel<String>> c) {
        apiInterface.qAnswerAgree(params).enqueue(c);
    }

    public static void qACommentList(QAnswerParams params, ZCallBackWithFail<ResponseModel<QuestionAnswer>> c) {
        apiInterface.qACommentList(params).enqueue(c);
    }

    public static void qACommentPub(QAnswerParams params, ZCallBack<ResponseModel<QAComment>> c) {
        apiInterface.qACommentPub(params).enqueue(c);
    }

    public static void findSearchList(SearchParams params, Callback<ResponseBody> c) {
        apiInterface.findSearchList(params).enqueue(c);
    }

    public static void getTopic(TopicParams params, ZCallBack<ResponseModel<Topic>> c) {
        apiInterface.getTopic(params).enqueue(c);
    }

    public static void getEssenceAnswer(TopicParams params, ZCallBackWithoutProgress<ResponseModel<List<QAnswerResult>>> c) {
        apiInterface.getEssenceAnswer(params).enqueue(c);
    }

    public static void getQuestionsOfTopic(TopicParams params, ZCallBackWithoutProgress<ResponseModel<List<QuestResult>>> c) {
        apiInterface.getQuestionsOfTopic(params).enqueue(c);
    }

    public static void getExcellentAnswer(TopicParams params, ZCallBackWithoutProgress<ResponseModel<List<QAExcellentResp>>> c) {
        apiInterface.getExcellentResp(params).enqueue(c);
    }

    public static void findHomePageData(HomeParams params, ZCallBackWithoutProgress<ResponseModel<List<TQuestionResult>>> c) {
        apiInterface.findHomePageData(params).enqueue(c);
    }

    public static void getAnswerInfo(QAnswerParams params, ZCallBack<ResponseModel<QuestionAnswer>> c) {
        apiInterface.getAnswerInfo(params).enqueue(c);
    }

    public static void getMyFavoriateUsers(UserFollowParams params, ZCallBackWithFail<ResponseModel<List<AppUserResult>>> c) {
        apiInterface.getMyFavoriteUsers(params).enqueue(c);
    }

    public static void getMyFans(UserFollowParams params, ZCallBack<ResponseModel<List<AppUserResult>>> c) {
        apiInterface.myFans(params).enqueue(c);
    }

    public static void getMyAnswers(UserFollowParams params, ZCallBackWithFail<ResponseModel<List<QAnswerResult>>> c) {
        //my answer number
        params.optType = 2;
        apiInterface.getMyAnswers(params).enqueue(c);
    }

    public static void signIn(UserParams params, ZCallBackWithFail<ResponseModel<Map<String, Integer>>> c) {
        apiInterface.signIn(params).enqueue(c);
    }

    public static void answerInvite(TQuestionParams params, ZCallBack<ResponseModel<String>> c) {
        apiInterface.answerInvite(params).enqueue(c);
    }

    public static void modifyUserInfo(UpdUserParams params, ZCallBack<ResponseModel<String>> c) {
        apiInterface.modifyUserInfo(params).enqueue(c);
    }

    public static void getAllEmailSuffix(ZCallBackWithoutProgress<ResponseModel<List<String>>> c) {
        apiInterface.getAllEmailSuffix().enqueue(c);
    }

    public static void publishFeedBack(List<MultipartBody.Part> file, String userId, String content, ZCallBack<ResponseModel<String>> c) {
        apiInterface.feedBackPublish(file, userId, content).enqueue(c);
    }

    public static void getReports(ReportParams params, ZCallBack<ResponseModel<List<ReportListResult>>> c) {
        apiInterface.getReports(params).enqueue(c);
    }

    public static void getUserOperationInfo(UserParams params, ZCallBack<ResponseModel<UserOperationInfo>> c) {
        apiInterface.getUserOperationInfo(params).enqueue(c);
    }

    public static void getUserDynamicComments(UserFollowParams params, ZCallBack<ResponseModel<List<QACommentResult>>> c) {
        // 1-评论；2-回答；3-提问
        params.optType = 1;
        apiInterface.getUserDynamicComments(params).enqueue(c);
    }

    public static void getUserDynamicAnswers(UserFollowParams params, ZCallBack<ResponseModel<List<QAnswerResult>>> c) {
        // 1-评论；2-回答；3-提问
        params.optType = 2;
        apiInterface.getUserDynamicAnswers(params).enqueue(c);
    }

    public static void getUserDynamicQuestions(UserFollowParams params, ZCallBack<ResponseModel<List<QuestResult>>> c) {
        // 1-评论；2-回答；3-提问
        params.optType = 3;
        apiInterface.getUserDynamicQuestions(params).enqueue(c);
    }

    public static void getTaskDates(ReportCompletedRelParam param, ZCallBack<ResponseModel<List<String>>> c) {
        apiInterface.getTaskDates(param).enqueue(c);
    }

    public static void findCurrentTasks(RCompletedRelParams param, ZCallBack<ResponseModel<List<CurrentReportTaskResp>>> c) {
        apiInterface.findCurrentTasks(param).enqueue(c);
    }

    public static void getMyFollow(UserFollowParams params, ZCallBackWithFail<ResponseModel<List<Topic>>> c) {
        //get topics
        params.objType = 2;
        apiInterface.getMyFollowTopic(params).enqueue(c);
    }

    public static void getMyFollowQuestion(UserFollowParams params, ZCallBackWithFail<ResponseModel<List<QuestResult>>> c) {
        params.objType = 1;
        apiInterface.getMyFollowQuestion(params).enqueue(c);
    }

    public static void getEditHtmlUrl(TQuestionParams params, ZCallBack<ResponseModel<String>> c) {
        apiInterface.getEditHtmlUrl(params).enqueue(c);
    }

    public static void getMyCollectAnswer(UserFollowParams params, ZCallBack<ResponseModel<List<QAnswerResult>>> c) {
        params.objType = 1;
        apiInterface.getMyCollectAnswer(params).enqueue(c);
    }

    public static void getMyCollectNews(UserFollowParams params, ZCallBack<ResponseModel<List<News>>> c) {
        params.objType = 2;
        apiInterface.getMyCollectNews(params).enqueue(c);
    }

    public static void sendEmailVerifyCode(UserParams params, ZCallBackWithFail<ResponseModel<String>> c) {
        apiInterface.sendEmailVerifyCode(params).enqueue(c);
    }

    public static void emailIdentify(UserParams params, ZCallBackWithFail<ResponseModel<String>> c) {
        apiInterface.emailIdentify(params).enqueue(c);
    }

    public static void getColleague(UserParams params, ZCallBackWithFail<ResponseModel<List<AppUserResult>>> c) {
        apiInterface.getColleagues(params).enqueue(c);
    }

    public static void getContacts(UserParams params, ZCallBack<ResponseModel<List<String>>> c) {
        apiInterface.getContacts(params).enqueue(c);
    }

    public static void inviteContact(UserParams params, ZCallBack<ResponseModel<String>> c) {
        apiInterface.contactsInvite(params).enqueue(c);
    }

    public static void completeReport(RCompletedRelParams params, ZCallBack<ResponseModel<String>> c) {
        apiInterface.completeReport(params).enqueue(c);
    }

    public static void saveTodo(List<Todo> params, ZCallBackWithFail<ResponseModel<String>> c) {
        apiInterface.synchronousTasks(params).enqueue(c);
    }

    public static void saveRemind(ReminderModel model, ZCallBack<ResponseModel<ReminderModel>> callBack) {
        apiInterface.saveRemind(model).enqueue(callBack);
    }

    public static void myGrade(UserParams reminderModel, ZCallBack<ResponseModel<String>> c) {
        apiInterface.myGrade(reminderModel).enqueue(c);
    }

    public static void getOtherPersonInfo(UserParams params, ZCallBack<ResponseModel<OtherUserResp>> c) {
        apiInterface.getOther(params).enqueue(c);
    }

    /**
     * policy
     */
    public static void getPolicyHtml(ZCallBack<ResponseModel<String>> c) {
        apiInterface.policyHtml().enqueue(c);
    }

    public static void getTopicByQuestion(TQuestionParams params, ZCallBackWithFail<ResponseModel<List<Topic>>> c) {
        apiInterface.getTopicByQuestion(params).enqueue(c);
    }

    public static void getFOAF(UserParams params, ZCallBackWithFail<ResponseModel<List<AppUserResult>>> c) {
        apiInterface.getFOAF(params).enqueue(c);
    }

    public static void findHotSearchList(ZCallBack<ResponseModel<List<HotSearch>>> c) {
        apiInterface.findHotSearchList().enqueue(c);
    }

    public static void findExpireTasks(ReportCompletedRelParam param, ZCallBackWithFail<ResponseModel<List<List<ReportCompletedRelResult>>>> c) {
        apiInterface.findExpireTasks(param).enqueue(c);
    }

    public static void findHisCompletedTasks(ReportCompletedRelParam param, ZCallBackWithFail<ResponseModel<List<List<ReportCompletedRelResult>>>> c) {
        apiInterface.findHisCompletedTasks(param).enqueue(c);
    }

    public static void getBanners(ZCallBackWithFail<ResponseModel<List<Banner>>> c) {
        apiInterface.getBanners().enqueue(c);
    }

    public static void getUserMessage(HomeParams params, ZCallBackWithFail<ResponseModel<List<SysNoticeResult>>> c) {
        apiInterface.getUserMessage(params).enqueue(c);
    }

    public static void getSystemMessage(HomeParams params, ZCallBackWithFail<ResponseModel<List<SysNoticeResult>>> c) {
        apiInterface.getSystemMessage(params).enqueue(c);
    }

    public static void deleteMessages(SysNoticeParams params, ZCallBack<ResponseModel<String>> c) {
        apiInterface.deleteMessages(params).enqueue(c);
    }

    public static void getAnswerEditHtml(TQuestionParams params, ZCallBack<ResponseModel<String>> c) {
        apiInterface.getAnswerEditHtml(params).enqueue(c);
    }
}
