package com.riking.calendar.activity;

import android.view.View;

import com.riking.calendar.activity.base.ZActivity;
import com.riking.calendar.adapter.SystemNoticeAdapter;
import com.riking.calendar.listener.ZCallBackWithFail;
import com.riking.calendar.pojo.base.ResponseModel;
import com.riking.calendar.pojo.params.HomeParams;
import com.riking.calendar.pojo.server.SysNoticeResult;
import com.riking.calendar.retrofit.APIClient;

import java.util.List;

/**
 * Created by zw.zhang on 2017/7/24.
 * answer comments page
 */

public class SystemNoticeActivity extends ZActivity<SystemNoticeAdapter> { //Fragment 数组


    @Override
    public SystemNoticeAdapter getAdapter() {
        return new SystemNoticeAdapter();
    }


    public void loadData(final int page) {
        HomeParams params = new HomeParams();
        APIClient.getSystemMessage(params, new ZCallBackWithFail<ResponseModel<List<SysNoticeResult>>>() {
            @Override
            public void callBack(ResponseModel<List<SysNoticeResult>> response) {
                if (failed) {

                } else {
                    setData2Adapter(response._data);
                }
            }
        });

    }

    @Override
    public void initViews() {
    }

    @Override
    public void initEvents() {
        activityTitle.setText("系统通知");
    }
}
