package com.riking.calendar.activity;

import android.view.View;

import com.necer.ncalendar.utils.MyLog;
import com.riking.calendar.activity.base.ZActivity;
import com.riking.calendar.adapter.MyRepliesAdapter;
import com.riking.calendar.listener.ZCallBackWithFail;
import com.riking.calendar.pojo.base.ResponseModel;
import com.riking.calendar.pojo.params.UserFollowParams;
import com.riking.calendar.pojo.server.QAnswerResult;
import com.riking.calendar.retrofit.APIClient;
import com.riking.calendar.util.CONST;
import com.riking.calendar.util.ZPreference;

import java.util.List;

/**
 * Created by zw.zhang on 2017/7/24.
 * answers page
 */

public class MyRepliesActivity extends ZActivity<MyRepliesAdapter> { //Fragment 数组
    private String userId;

    @Override
    public MyRepliesAdapter getAdapter() {
        return new MyRepliesAdapter();
    }

    public void initViews() {
    }

    public void initEvents() {
        userId = getIntent().getStringExtra(CONST.USER_ID);
        init();
        if (ZPreference.getUserId().equals(userId)) {
            activityTitle.setText("我的回答");
        } else {
            if (getIntent().getIntExtra(CONST.USER_SEX, 0) == 0) {
                activityTitle.setText("她的回答");
            } else {
                activityTitle.setText("他的回答");
            }
        }
    }

    public void loadData(final int page) {
        isLoading = true;
        if (page > 1 && isHasLoadedAll) {
            return;
        }

        final UserFollowParams params = new UserFollowParams();
        params.userId = userId;
        params.pindex = page;
        MyLog.d("load my replies");
        APIClient.getMyAnswers(params, new ZCallBackWithFail<ResponseModel<List<QAnswerResult>>>() {
            @Override
            public void callBack(ResponseModel<List<QAnswerResult>> response) {
                mPullToLoadView.setComplete();
                isLoading = false;
                if (failed) {

                } else {
                    List<QAnswerResult> list = response._data;
                    setData2Adapter(page, list);
                }
            }
        });
    }

    public void clickBack(final View view) {
        onBackPressed();
    }
}
