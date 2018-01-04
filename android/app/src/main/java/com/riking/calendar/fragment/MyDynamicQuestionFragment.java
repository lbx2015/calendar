package com.riking.calendar.fragment;

import com.riking.calendar.activity.MyStateActivity;
import com.riking.calendar.adapter.MyQuestionsAdapter;
import com.riking.calendar.fragment.base.ZFragment;
import com.riking.calendar.listener.ZCallBack;
import com.riking.calendar.pojo.base.ResponseModel;
import com.riking.calendar.pojo.params.UserFollowParams;
import com.riking.calendar.pojo.server.QuestResult;
import com.riking.calendar.retrofit.APIClient;
import com.riking.calendar.util.ZToast;

import java.util.List;

/**
 * Created by zw.zhang on 2017/7/17.
 */

public class MyDynamicQuestionFragment extends ZFragment<MyQuestionsAdapter> {

    MyStateActivity activity;

    public static MyDynamicQuestionFragment newInstance(MyStateActivity activity) {
        MyDynamicQuestionFragment f = new MyDynamicQuestionFragment();
        f.activity = activity;
        return f;
    }

    @Override
    public MyQuestionsAdapter getAdapter() {
        return new MyQuestionsAdapter(getContext());
    }

    public void initViews() {
    }

    public void initEvents() {

    }

    public void loadData(final int page) {
        UserFollowParams params = new UserFollowParams();
        params.pindex = page;
        params.userId = activity.userId;
        APIClient.getUserDynamicQuestions(params, new ZCallBack<ResponseModel<List<QuestResult>>>() {
            @Override
            public void callBack(ResponseModel<List<QuestResult>> response) {
                List<QuestResult> answers = response._data;
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
