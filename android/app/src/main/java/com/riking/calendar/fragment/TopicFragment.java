package com.riking.calendar.fragment;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.necer.ncalendar.utils.MyLog;
import com.necer.ncalendar.view.SimpleDividerItemDecoration;
import com.riking.calendar.adapter.HomeAdapter;
import com.riking.calendar.fragment.base.ZFragment;
import com.riking.calendar.listener.ZCallBackWithoutProgress;
import com.riking.calendar.pojo.base.ResponseModel;
import com.riking.calendar.pojo.params.HomeParams;
import com.riking.calendar.pojo.server.TQuestionResult;
import com.riking.calendar.retrofit.APIClient;
import com.riking.calendar.util.CONST;
import com.riking.calendar.util.DateUtil;

import java.util.Date;
import java.util.List;

/**
 * Created by zw.zhang on 2017/7/17.
 */

public class TopicFragment extends ZFragment<HomeAdapter> {
    String reqTimeStamp;
    String lastItemTime;

    @Override
    public HomeAdapter getAdapter() {
        return new HomeAdapter(getContext());
    }

    public void initViews() {
    }

    public void initEvents() {
        RecyclerView mRecyclerView = mPullToLoadView.getRecyclerView();
        LinearLayoutManager manager = new LinearLayoutManager(getActivity(),
                LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(manager);

        //adding custom divider
        mRecyclerView.addItemDecoration(new SimpleDividerItemDecoration(getContext()));
    }

    public void loadData(final int page) {
        HomeParams p = new HomeParams();
        if (page == 1) {
            if (reqTimeStamp == null) {
                p.direct = "up";
            } else {
                p.direct = "down";
            }
            p.reqTimeStamp = reqTimeStamp;
        } else {
            p.direct = "up";
            p.reqTimeStamp = lastItemTime;
        }

        APIClient.findHomePageData(p, new ZCallBackWithoutProgress<ResponseModel<List<TQuestionResult>>>() {
            @Override
            public void callBack(ResponseModel<List<TQuestionResult>> response) {
                MyLog.d("findNewList mPullToLoadView.setComplete()");
                mPullToLoadView.setComplete();
                isLoading = false;
                List<TQuestionResult> news = response._data;
                if (news.size() != 0) {
                    reqTimeStamp = DateUtil.date2String(news.get(0).createdTime, CONST.YYYYMMDDHHMMSSSSS);
                    Date lastTime = news.get(news.size() - 1).createdTime;
                    //adding last item create time is null protection
                    if (lastTime == null && news.size() > 1) {
                        lastTime = news.get(news.size() - 2).createdTime;
                    }
                    lastItemTime = DateUtil.date2String(lastTime, CONST.YYYYMMDDHHMMSSSSS);
                }
                appendData2Adapter(page, news);

            }
        });
      /*  isLoading = true;
        mPullToLoadView.mSwipeRefreshLayout.setRefreshing(true);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mPullToLoadView.setComplete();
                if (page > 3) {
                    Toast.makeText(getContext(), "没有更多数据了",
                            Toast.LENGTH_SHORT).show();
                    isHasLoadedAll = true;
                    return;
                }
                for (int i = 0; i <= 15; i++) {
                    mAdapter.add(i + "");
                }

                isLoading = false;
                nextPage = page + 1;
            }
        }, 3000);*/
    }
}
