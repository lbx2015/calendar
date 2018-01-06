package com.riking.calendar.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.necer.ncalendar.utils.MyLog;
import com.riking.calendar.R;
import com.riking.calendar.adapter.MyNewsAdapter;
import com.riking.calendar.fragment.base.ZFragment;
import com.riking.calendar.listener.ZCallBackWithFail;
import com.riking.calendar.pojo.base.ResponseModel;
import com.riking.calendar.pojo.params.HomeParams;
import com.riking.calendar.pojo.server.SysNoticeResult;
import com.riking.calendar.retrofit.APIClient;
import com.riking.calendar.util.CONST;
import com.riking.calendar.util.DateUtil;
import com.riking.calendar.util.ZGoto;
import com.riking.calendar.util.ZPreference;
import com.riking.calendar.util.ZR;

import java.util.List;

/**
 * Created by zw.zhang on 2017/7/17.
 */

public class MyNewsFragment extends ZFragment<MyNewsAdapter> {
    TextView cancelTv;
    TextView doneTv;
    boolean isEditState;
    View bottomEditLayout;
    ImageView selectAllView;
    View deleteView;
    View notLoginLayout;
    String reqTimeStamp;
    List<SysNoticeResult> userMessages;
    List<SysNoticeResult> systemMessages;

    @Override
    public View createView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.my_news_fragment, container, false);
    }

    @Override
    public MyNewsAdapter getAdapter() {
        return new MyNewsAdapter();
    }

    public void initViews() {
        notLoginLayout = v.findViewById(R.id.not_login_layout);
        cancelTv = v.findViewById(R.id.cancel);
        doneTv = v.findViewById(R.id.editButton);
        bottomEditLayout = v.findViewById(R.id.bottom_edit_layout);
        selectAllView = v.findViewById(R.id.select_all_image);
        deleteView = v.findViewById(R.id.delete_button);
    }

    public void initEvents() {
        if (ZPreference.isLogin()) {
            mPullToLoadView.setVisibility(View.VISIBLE);
            notLoginLayout.setVisibility(View.GONE);
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
            deleteView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    exitEditMode();
                }
            });
            selectAllView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mAdapter.selectAll) {
                        mAdapter.selectAll = false;
                        selectAllView.setImageDrawable(ZR.getDrawable(R.drawable.mess_icon_editdelete_n));
                    } else {
                        selectAllView.setImageDrawable(ZR.getDrawable(R.drawable.mess_icon_editdelete_s));
                        mAdapter.selectAll = true;
                    }
                    mAdapter.notifyDataSetChanged();
                }
            });
        } else {
            mPullToLoadView.setVisibility(View.GONE);
            notLoginLayout.setVisibility(View.VISIBLE);
            mPullToLoadView.setVisibility(View.GONE);
            notLoginLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ZGoto.toLoginActivity();
                }
            });
        }
    }

    private void exitEditMode() {
        isEditState = false;
        cancelTv.setVisibility(View.GONE);
        bottomEditLayout.setVisibility(View.GONE);
        doneTv.setText("编辑");

        mAdapter.editMode = false;
        mAdapter.notifyDataSetChanged();
    }

    private void enterEditMode() {
        isEditState = true;
        cancelTv.setVisibility(View.VISIBLE);
        doneTv.setText("完成");
        bottomEditLayout.setVisibility(View.VISIBLE);

        mAdapter.editMode = true;
        mAdapter.notifyDataSetChanged();
    }

    public void loadData(final int page) {
        MyLog.d("news load data page: " + page);
        getSystemMessage(page);
    }

    private void getSystemMessage(final int page) {
        if (page == 1) {
            mAdapter.clear();
            mRecyclerView.clearDisappearingChildren();
            getSystemMessage();
        }
        getUserMessage(page);

    }

    private void getSystemMessage() {
        HomeParams params = new HomeParams();
        APIClient.getSystemMessage(params, new ZCallBackWithFail<ResponseModel<List<SysNoticeResult>>>() {
            @Override
            public void callBack(ResponseModel<List<SysNoticeResult>> response) {
                if (failed) {

                } else {
                    systemMessages = response._data;
                    mAdapter.addAllAtStart(systemMessages);
                }
            }
        });
    }

    private void getUserMessage(final int page) {
        HomeParams params = new HomeParams();
        if (page > 1) {
            params.reqTimeStamp = reqTimeStamp;
        }
        APIClient.getUserMessage(params, new ZCallBackWithFail<ResponseModel<List<SysNoticeResult>>>() {
            @Override
            public void callBack(ResponseModel<List<SysNoticeResult>> response) throws Exception {
                isLoading = false;
                mPullToLoadView.setComplete();
                if (failed) {

                } else {
                    userMessages = response._data;
                    if (userMessages.size() > 0) {
                        reqTimeStamp = DateUtil.date2String(userMessages.get(userMessages.size() - 1).createdTime, CONST.YYYYMMDDHHMMSSSSS);
                        nextPage = page + 1;
                        mAdapter.addAllAtEnd(userMessages);
                    }
                }
            }
        });
    }
}
