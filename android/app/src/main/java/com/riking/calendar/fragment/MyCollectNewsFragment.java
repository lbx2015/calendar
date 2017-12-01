package com.riking.calendar.fragment;

import com.necer.ncalendar.utils.MyLog;
import com.riking.calendar.activity.MyCollectActivity;
import com.riking.calendar.adapter.NewsAdapter;
import com.riking.calendar.fragment.base.ZFragment;
import com.riking.calendar.listener.ZCallBack;
import com.riking.calendar.pojo.base.ResponseModel;
import com.riking.calendar.pojo.params.UserFollowParams;
import com.riking.calendar.pojo.server.News;
import com.riking.calendar.retrofit.APIClient;
import com.riking.calendar.util.ZToast;

import java.util.List;

/**
 * Created by zw.zhang on 2017/7/17.
 */

public class MyCollectNewsFragment extends ZFragment<NewsAdapter> {
    MyCollectActivity activity;

    public static MyCollectNewsFragment newInstance(MyCollectActivity activity) {
        MyCollectNewsFragment f = new MyCollectNewsFragment();
        f.activity = activity;
        return f;
    }

    @Override
    public NewsAdapter getAdapter() {
        return new NewsAdapter();
    }

    public void initViews() {
    }

    public void initEvents() {
    }

    public void loadData(final int page) {
        final UserFollowParams params = new UserFollowParams();
        params.userId = activity.userId;
        params.pindex = page;
        APIClient.getMyCollectNews(params, new ZCallBack<ResponseModel<List<News>>>() {
            @Override
            public void callBack(ResponseModel<List<News>> response) {
                mPullToLoadView.setComplete();

                List<News> list = response._data;
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
