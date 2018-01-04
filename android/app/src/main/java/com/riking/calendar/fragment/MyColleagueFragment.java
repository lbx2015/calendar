package com.riking.calendar.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.riking.calendar.R;
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

public class MyColleagueFragment extends ZFragment<MyFollowersAdapter> {

    @Override
    public MyFollowersAdapter getAdapter() {
        return new MyFollowersAdapter();
    }
    //should override this method
    public View createView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.z_fragment, container, false);
    }
    public void initViews() {
    }

    public void initEvents() {
    }

    public void loadData(final int page) {

        final UserParams params = new UserParams();
        params.pindex = page;
        APIClient.getColleague(params, new ZCallBackWithFail<ResponseModel<List<AppUserResult>>>() {
            @Override
            public void callBack(ResponseModel<List<AppUserResult>> response) {
                mPullToLoadView.setComplete();
                isLoading = false;
                if (!failed) {
//                    Gson g = new Gson();
//                    TypeToken<List<AppUserResult>> token = new TypeToken<List<AppUserResult>>() {
//                    };
//                    List<AppUserResult> list = g.fromJson(response._data, token.getType());
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
