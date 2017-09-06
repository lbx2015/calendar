package com.riking.calendar.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;

import com.ldf.calendar.Const;
import com.riking.calendar.jiguang.Logger;
import com.riking.calendar.listener.ZCallBack;
import com.riking.calendar.pojo.QueryReport;
import com.riking.calendar.pojo.QueryReportContainer;
import com.riking.calendar.pojo.WorkDate;
import com.riking.calendar.pojo.base.ResponseModel;
import com.riking.calendar.realm.model.QueryReportContainerRealmModel;
import com.riking.calendar.realm.model.QueryReportRealmModel;
import com.riking.calendar.realm.model.WorkDateRealm;
import com.riking.calendar.retrofit.APIClient;
import com.riking.calendar.util.Preference;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmList;

/**
 * Created by zw.zhang on 2017/7/24.
 */

public class LaunchActivity extends AppCompatActivity {

    Handler handler = new Handler();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        Log.d("zzw", this + "on create");
        super.onCreate(savedInstanceState);
        ImageView view = new ImageView(this);
        setContentView(view);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (Preference.pref.getBoolean(Const.NEED_WELCOME_ACTIVITY, true)) {
                    //Welcome activity only need once
                    Preference.put(Const.NEED_WELCOME_ACTIVITY, false);
                    Intent i = new Intent(LaunchActivity.this, WelcomeActivity.class);
                    startActivity(i);
                    finish();
                } else {
                    Intent i = new Intent(LaunchActivity.this, ViewPagerActivity.class);
                    startActivity(i);
                    finish();
                }

            }
        }, 2000);

        final Realm realm = Realm.getDefaultInstance();
        //if the user is not login
        if (!Preference.pref.getBoolean(Const.IS_LOGIN, false)) {

            APIClient.apiInterface.getAllReports(null).enqueue(new ZCallBack<ResponseModel<ArrayList<QueryReportContainer>>>() {
                @Override
                public void callBack(final ResponseModel<ArrayList<QueryReportContainer>> response) {
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
        APIClient.apiInterface.getWorkDays().enqueue(new ZCallBack<ResponseModel<ArrayList<WorkDate>>>() {
            @Override
            public void callBack(final ResponseModel<ArrayList<WorkDate>> response) {
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

    @Override
    protected void onRestart() {
        super.onRestart();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent i = new Intent(LaunchActivity.this, WelcomeActivity.class);
                startActivity(i);
                finish();
            }
        }, 2000);
    }
}
