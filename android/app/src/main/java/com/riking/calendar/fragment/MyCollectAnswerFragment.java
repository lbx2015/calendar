package com.riking.calendar.fragment;

import com.necer.ncalendar.utils.MyLog;
import com.riking.calendar.activity.MyCollectActivity;
import com.riking.calendar.adapter.MyCollectAnswerAdapter;
import com.riking.calendar.fragment.base.ZFragment;
import com.riking.calendar.listener.ZCallBack;
import com.riking.calendar.pojo.base.ResponseModel;
import com.riking.calendar.pojo.params.UserFollowParams;
import com.riking.calendar.pojo.server.QAnswerResult;
import com.riking.calendar.retrofit.APIClient;
import com.riking.calendar.util.StringUtil;
import com.riking.calendar.util.ZToast;

import java.util.List;

/**
 * Created by zw.zhang on 2017/7/17.
 */

public class MyCollectAnswerFragment extends ZFragment<MyCollectAnswerAdapter> {
    MyCollectActivity activity;

    public static MyCollectAnswerFragment newInstance(MyCollectActivity activity) {
        MyCollectAnswerFragment f = new MyCollectAnswerFragment();
        f.activity = activity;
        return f;
    }

    @Override
    public MyCollectAnswerAdapter getAdapter() {
        return new MyCollectAnswerAdapter();
    }

    public void initViews() {
    }

    public void initEvents() {
    }

    public void loadData(final int page) {

        final UserFollowParams params = new UserFollowParams();
        if (!StringUtil.isEmpty(activity.userId)) {
            params.userId = activity.userId;
        }
        params.pindex = page;
        APIClient.getMyCollectAnswer(params, new ZCallBack<ResponseModel<List<QAnswerResult>>>() {
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
