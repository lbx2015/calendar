package com.riking.calendar.fragment;

import com.riking.calendar.adapter.AnswerCommentListAdapter;
import com.riking.calendar.fragment.base.ZFragment;
import com.riking.calendar.listener.ZCallBack;
import com.riking.calendar.pojo.base.ResponseModel;
import com.riking.calendar.pojo.params.QAnswerParams;
import com.riking.calendar.pojo.server.QAComment;
import com.riking.calendar.retrofit.APIClient;
import com.riking.calendar.util.ZToast;

import java.util.List;

/**
 * Created by zw.zhang on 2017/7/17.
 */

public class AnswerCommentsFragment extends ZFragment<AnswerCommentListAdapter> {
    @Override
    public AnswerCommentListAdapter getAdapter() {
        return new AnswerCommentListAdapter(getContext());
    }
    public void initViews() {
    }

    public void initEvents() {
    }

    public void loadData(final int page) {
        loadAnswerComments(page);
    }

    private void loadAnswerComments(final int page) {
        QAnswerParams params = new QAnswerParams();
        APIClient.qACommentList(params, new ZCallBack<ResponseModel<List<QAComment>>>() {
            @Override
            public void callBack(ResponseModel<List<QAComment>> response) {
                List<QAComment> comments = response._data;
                isLoading = false;
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
