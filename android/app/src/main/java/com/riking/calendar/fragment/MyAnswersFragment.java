package com.riking.calendar.fragment;

import com.riking.calendar.activity.MyStateActivity;
import com.riking.calendar.adapter.MyAnswersAdapter;
import com.riking.calendar.fragment.base.ZFragment;
import com.riking.calendar.listener.ZCallBack;
import com.riking.calendar.pojo.base.ResponseModel;
import com.riking.calendar.pojo.params.UserFollowParams;
import com.riking.calendar.pojo.server.QAnswerResult;
import com.riking.calendar.retrofit.APIClient;
import com.riking.calendar.util.ZToast;

import java.util.List;

/**
 * Created by zw.zhang on 2017/7/17.
 */

public class MyAnswersFragment extends ZFragment<MyAnswersAdapter> {

    MyStateActivity activity;

    public static MyAnswersFragment newInstance(MyStateActivity activity) {
        MyAnswersFragment f = new MyAnswersFragment();
        f.activity = activity;
        return f;
    }

    @Override
    public MyAnswersAdapter getAdapter() {
        return new MyAnswersAdapter(getContext());
    }

    public void initViews() {
    }

    public void initEvents() {
    }

    public void loadData(final int page) {
        UserFollowParams params = new UserFollowParams();
        params.pindex = page;
        params.userId = activity.userId;
        APIClient.getUserDynamicAnswers(params, new ZCallBack<ResponseModel<List<QAnswerResult>>>() {
            @Override
            public void callBack(ResponseModel<List<QAnswerResult>> response) {
                List<QAnswerResult> answers = response._data;
                isLoading = false;
                mPullToLoadView.setComplete();
                if (answers.size() == 0) {
                    ZToast.toast("没有更多数据了");
                    return;
                }
                mAdapter.setData(answers);
                nextPage = page + 1;
            }
        });
    }
}
