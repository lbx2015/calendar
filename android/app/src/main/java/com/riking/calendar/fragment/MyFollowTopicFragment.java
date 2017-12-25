package com.riking.calendar.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.necer.ncalendar.utils.MyLog;
import com.riking.calendar.R;
import com.riking.calendar.adapter.SearchTopicAdapter;
import com.riking.calendar.fragment.base.ZFragment;
import com.riking.calendar.interfeet.PerformInputSearch;
import com.riking.calendar.listener.PullCallback;
import com.riking.calendar.pojo.base.ResponseModel;
import com.riking.calendar.pojo.params.SearchParams;
import com.riking.calendar.pojo.server.TopicResult;
import com.riking.calendar.retrofit.APIClient;
import com.riking.calendar.view.PullToLoadViewWithoutFloatButton;
import com.riking.calendar.viewholder.SearchTopicViewHolder;

import java.io.IOException;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by zw.zhang on 2017/7/17.
 */

public class MyFollowTopicFragment extends ZFragment<SearchTopicAdapter> {
    @Override
    public SearchTopicAdapter getAdapter() {
        return new SearchTopicAdapter();
    }

    public void initEvents() {
    }

    public void loadData(final int page) {
    }

    @Override
    public void initViews() {

    }
}
