package com.riking.calendar.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;

import com.riking.calendar.jiguang.Logger;
import com.riking.calendar.pojo.QueryReport;
import com.riking.calendar.pojo.QueryReportContainer;
import com.riking.calendar.pojo.QueryReportModel;
import com.riking.calendar.realm.model.QueryReportContainerRealmModel;
import com.riking.calendar.realm.model.QueryReportRealmModel;
import com.riking.calendar.retrofit.APIClient;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmList;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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
                Intent i = new Intent(LaunchActivity.this, WelcomeActivity.class);
                startActivity(i);
            }
        }, 2000);
        final Realm realm = Realm.getDefaultInstance();
        APIClient.apiInterface.getAllReports(null).enqueue(new Callback<QueryReportModel>() {
            @Override
            public void onResponse(Call<QueryReportModel> call, final Response<QueryReportModel> response) {
                Logger.d("zzw", "load reports ok");
                realm.executeTransaction(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        ArrayList<QueryReportContainer> reportContainers = response.body()._data;
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

            @Override
            public void onFailure(Call<QueryReportModel> call, Throwable t) {
                Logger.d("zzw", "load reports fail" + t.getMessage());
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
