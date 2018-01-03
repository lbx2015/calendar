package com.riking.calendar.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.riking.calendar.R;
import com.riking.calendar.adapter.MyNewsAdapter;
import com.riking.calendar.fragment.base.ZFragment;
import com.riking.calendar.util.ZPreference;

/**
 * Created by zw.zhang on 2017/7/17.
 */

public class MyNewsFragment extends ZFragment<MyNewsAdapter> {
    TextView cancelTv;
    TextView doneTv;
    boolean isEditState;

    @Override
    public View createView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.my_news_fragment, container, false);
    }

    @Override
    public MyNewsAdapter getAdapter() {
        return new MyNewsAdapter();
    }

    public void initViews() {
        cancelTv = v.findViewById(R.id.cancel);
        doneTv = v.findViewById(R.id.editButton);
    }

    public void initEvents() {
        if (ZPreference.isLogin()) {
            mPullToLoadView.setVisibility(View.VISIBLE);
            doneTv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (isEditState) {
                        exitEditMode();
                    } else {
                        enterEditMode();
                    }
                }
            });

            cancelTv.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    exitEditMode();
                }
            });
        } else {
            mPullToLoadView.setVisibility(View.GONE);
        }
    }

    private void exitEditMode() {
        isEditState = false;
        cancelTv.setVisibility(View.GONE);
        doneTv.setText("编辑");

        mAdapter.editMode = false;
        mAdapter.notifyDataSetChanged();
    }

    private void enterEditMode() {
        isEditState = true;
        cancelTv.setVisibility(View.VISIBLE);
        doneTv.setText("完成");

        mAdapter.editMode = true;
        mAdapter.notifyDataSetChanged();
    }

    public void loadData(final int page) {
        mPullToLoadView.setComplete();
/*
        final UserParams params = new UserParams();
        params.pindex = page;
        APIClient.getColleague(params, new ZCallBackWithFail<ResponseModel<String>>() {
            @Override
            public void callBack(ResponseModel<String> response) {
                mPullToLoadView.setComplete();
                isLoading = false;
                if (!failed) {
                    Gson g = new Gson();
                    TypeToken<List<AppUserResult>> token = new TypeToken<List<AppUserResult>>() {
                    };
                    List<AppUserResult> list = g.fromJson(response._data, token.getType());

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
        });*/
    }
}
