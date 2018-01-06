package com.riking.calendar.fragment;

import com.necer.ncalendar.utils.MyLog;
import com.riking.calendar.activity.TopicActivity;
import com.riking.calendar.adapter.HotAnswerOfTopicAdapter;
import com.riking.calendar.fragment.base.ZFragment;
import com.riking.calendar.listener.ZCallBackWithoutProgress;
import com.riking.calendar.pojo.base.ResponseModel;
import com.riking.calendar.pojo.params.TopicParams;
import com.riking.calendar.pojo.server.QAnswerResult;
import com.riking.calendar.retrofit.APIClient;
import com.riking.calendar.util.ZToast;

import java.util.List;

/**
 * Created by zw.zhang on 2017/7/17.
 */

public class HotAnswerOfTopicFragment extends ZFragment<HotAnswerOfTopicAdapter> {
    TopicActivity activity;

    public static HotAnswerOfTopicFragment newInstance(TopicActivity activity) {
        HotAnswerOfTopicFragment fragment = new HotAnswerOfTopicFragment();
        fragment.activity = activity;
        return fragment;
    }

    @Override
    public HotAnswerOfTopicAdapter getAdapter() {
        return new HotAnswerOfTopicAdapter();
    }

    public void initEvents() {    }

    public void initViews() {
    }

    public void loadData(final int page) {
        final TopicParams params = new TopicParams();
        params.topicId = activity.topicId;
        //excellent answer
        params.optType = 1;
        params.pcount = 30;
        params.pindex = page;

        APIClient.getEssenceAnswer(params, new ZCallBackWithoutProgress<ResponseModel<List<QAnswerResult>>>() {
            @Override
            public void callBack(ResponseModel<List<QAnswerResult>> response) {
                mPullToLoadView.setComplete();

                List<QAnswerResult> list = response._data;
                MyLog.d("list size: " + list.size());
                if (list.size() < params.pcount) {
                    isHasLoadedAll = true;
                    if (list.size() == 0) {
                        ZToast.toastEmpty();
                        return;
                    }
                }
                isLoading = false;
                nextPage = page + 1;
                mAdapter.setData(list);
            }
        });
    }
}
