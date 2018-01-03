package com.riking.calendar.fragment;

import com.necer.ncalendar.utils.MyLog;
import com.riking.calendar.activity.MyFollowActivity;
import com.riking.calendar.adapter.MyFollowersAdapter;
import com.riking.calendar.fragment.base.ZFragment;
import com.riking.calendar.listener.ZCallBackWithFail;
import com.riking.calendar.pojo.base.ResponseModel;
import com.riking.calendar.pojo.params.UserFollowParams;
import com.riking.calendar.pojo.server.AppUserResult;
import com.riking.calendar.retrofit.APIClient;
import com.riking.calendar.util.ZToast;

import java.util.List;

/**
 * Created by zw.zhang on 2017/7/17.
 */

public class MyFollowerPersonFragment extends ZFragment<MyFollowersAdapter> {
    MyFollowActivity activity;

    public static MyFollowerPersonFragment newInstance(MyFollowActivity activity) {
        MyFollowerPersonFragment f = new MyFollowerPersonFragment();
        f.activity = activity;
        return f;
    }

    @Override
    public MyFollowersAdapter getAdapter() {
        return new MyFollowersAdapter();
    }

    public void initViews() {
    }

    public void initEvents() {
    }

    public void loadData(final int page) {

        final UserFollowParams params = new UserFollowParams();
        params.userId = activity.userId;
        //person type
        params.objType = 3;
        params.pindex = page;
        APIClient.getMyFavoriateUsers(params, new ZCallBackWithFail<ResponseModel<List<AppUserResult>>>() {
            @Override
            public void callBack(ResponseModel<List<AppUserResult>> response) {
                mPullToLoadView.setComplete();
                isLoading = false;
                if (!failed) {

                    List<AppUserResult> list = response._data;
                    MyLog.d("list size: " + list.size());
                    if (list.size() < params.pcount) {
                        isHasLoadedAll = true;
                        if (list.size() == 0) {
                            ZToast.toastEmpty();
                            return;
                        }
                    }
                    nextPage = page + 1;
                    mAdapter.setData(list);
                }
            }
        });
    }
}
