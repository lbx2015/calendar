package com.riking.calendar.fragment;

import com.riking.calendar.activity.MyFollowActivity;
import com.riking.calendar.adapter.SearchTopicAdapter;
import com.riking.calendar.fragment.base.ZFragment;
import com.riking.calendar.listener.ZCallBack;
import com.riking.calendar.pojo.base.ResponseModel;
import com.riking.calendar.pojo.params.UserFollowParams;
import com.riking.calendar.pojo.server.Topic;
import com.riking.calendar.retrofit.APIClient;
import com.riking.calendar.util.ZToast;

import java.util.List;

/**
 * Created by zw.zhang on 2017/7/17.
 */

public class MyFollowTopicFragment extends ZFragment<SearchTopicAdapter> {
    MyFollowActivity activity;

    public static MyFollowTopicFragment newInstance(MyFollowActivity activity) {
        MyFollowTopicFragment f = new MyFollowTopicFragment();
        f.activity = activity;
        return f;
    }
    @Override
    public SearchTopicAdapter getAdapter() {
        return new SearchTopicAdapter();
    }

    public void initEvents() {
    }

    public void loadData(final int page) {
        final UserFollowParams params = new UserFollowParams();
        params.pindex = page;
        params.userId = activity.userId;
        APIClient.getMyFollow(params, new ZCallBack<ResponseModel<List<Topic>>>() {
            @Override
            public void callBack(ResponseModel<List<Topic>> response) {
                mPullToLoadView.setComplete();
                List<Topic> list = response._data;
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

    @Override
    public void initViews() {

    }
}
