package com.riking.calendar.fragment;

import com.riking.calendar.activity.MyCollectActivity;
import com.riking.calendar.activity.MyStateActivity;
import com.riking.calendar.adapter.MyDynamicAnswerCommentListAdapter;
import com.riking.calendar.fragment.base.ZFragment;
import com.riking.calendar.listener.ZCallBack;
import com.riking.calendar.pojo.base.ResponseModel;
import com.riking.calendar.pojo.params.UserFollowParams;
import com.riking.calendar.pojo.server.QACommentResult;
import com.riking.calendar.retrofit.APIClient;
import com.riking.calendar.util.StringUtil;
import com.riking.calendar.util.ZToast;

import java.util.List;

/**
 * Created by zw.zhang on 2017/7/17.
 */

public class AnswerCommentsFragment extends ZFragment<MyDynamicAnswerCommentListAdapter> {
    @Override
    public MyDynamicAnswerCommentListAdapter getAdapter() {
        return new MyDynamicAnswerCommentListAdapter(getContext());
    }

    MyStateActivity activity;

    public static AnswerCommentsFragment newInstance(MyStateActivity activity) {
        AnswerCommentsFragment f = new AnswerCommentsFragment();
        f.activity = activity;
        return f;
    }

    public void initViews() {
    }

    public void initEvents() {
    }

    public void loadData(final int page) {
        loadAnswerComments(page);
    }

    private void loadAnswerComments(final int page) {
        UserFollowParams params = new UserFollowParams();
        params.pindex = page;
        if (!StringUtil.isEmpty(activity.userId)) {
            params.userId = activity.userId;
        }
        APIClient.getUserDynamicComments(params, new ZCallBack<ResponseModel<List<QACommentResult>>>() {
            @Override
            public void callBack(ResponseModel<List<QACommentResult>> response) {
                List<QACommentResult> comments = response._data;
                isLoading = false;
                mPullToLoadView.setComplete();
                if (comments.size() == 0) {
                    ZToast.toast("没有更多数据了");
                    return;
                }
                mAdapter.addAll(comments);
                nextPage = page + 1;
            }
        });
    }
}
