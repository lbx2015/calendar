package com.riking.calendar.fragment;

import com.riking.calendar.adapter.MyFollowersAdapter;
import com.riking.calendar.fragment.base.ZFragment;
import com.riking.calendar.listener.ZCallBackWithFail;
import com.riking.calendar.pojo.base.ResponseModel;
import com.riking.calendar.pojo.params.UserParams;
import com.riking.calendar.pojo.server.AppUserResult;
import com.riking.calendar.retrofit.APIClient;
import com.riking.calendar.util.ZToast;

import java.util.List;

/**
 * Created by zw.zhang on 2017/7/17.
 */

public class MyFOAFFragment extends ZFragment<MyFollowersAdapter> {

    @Override
    public MyFollowersAdapter getAdapter() {
        return new MyFollowersAdapter();
    }

    public void initViews() {
    }

    public void initEvents() {
    }

    public void loadData(final int page) {
        final UserParams params = new UserParams();
        params.pindex = page;
        APIClient.getFOAF(params, new ZCallBackWithFail<ResponseModel<List<AppUserResult>>>() {
            @Override
            public void callBack(ResponseModel<List<AppUserResult>> response) {
                mPullToLoadView.setComplete();
                isLoading = false;
                if (!failed) {
                /*    Gson g = new Gson();
                    TypeToken<List<AppUserResult>> token = new TypeToken<List<AppUserResult>>() {
                    };
                    List<AppUserResult> list = g.fromJson(response._data, token.getType());*/
                    List<AppUserResult> list = response._data;

                    if (list != null && list.size() < params.pcount) {
                        isHasLoadedAll = true;
                        if (list.size() == 0) {
                            ZToast.toastEmpty();
                            return;
                        }
                    }

                    if (list != null) {
                        nextPage = page + 1;
                        mAdapter.setData(list);
                    }
                }
            }
        });
    }
}
