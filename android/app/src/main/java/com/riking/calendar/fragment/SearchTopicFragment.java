package com.riking.calendar.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.necer.ncalendar.utils.MyLog;
import com.riking.calendar.R;
import com.riking.calendar.adapter.SearchTopicAdapter;
import com.riking.calendar.fragment.base.ZFragment;
import com.riking.calendar.interfeet.PerformInputSearch;
import com.riking.calendar.pojo.base.ResponseModel;
import com.riking.calendar.pojo.params.SearchParams;
import com.riking.calendar.pojo.server.Topic;
import com.riking.calendar.retrofit.APIClient;

import java.io.IOException;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by zw.zhang on 2017/7/17.
 */

public class SearchTopicFragment extends ZFragment<SearchTopicAdapter> implements PerformInputSearch {
    String searchCondition;

    @Nullable
    @Override
    public View createView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.search_fragment, container, false);
    }

    @Override
    public SearchTopicAdapter getAdapter() {
        return new SearchTopicAdapter();
    }

    public void initViews() {
    }

    public void initEvents() {

    }

    public void loadData(final int page) {
        search(searchCondition);
    }

    @Override
    public void search(String searchCondition) {
        this.searchCondition = searchCondition;

        if (TextUtils.isEmpty(searchCondition)) {
            setComplete();
            return;
        }

        SearchParams params = new SearchParams();
        params.keyWord = searchCondition;
        //search topics
        params.objType = 2;

        APIClient.findSearchList(params, new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                setComplete();
                ResponseBody r = response.body();
                try {
                    String sourceString = r.source().readUtf8();
                    if (TextUtils.isEmpty(sourceString.trim())) {
                        return;
                    }
                    Gson s = new Gson();
                    JsonObject jsonObject = s.fromJson(sourceString, JsonObject.class);
                    String _data = jsonObject.get("_data").toString();

                    MyLog.d("_data topic " + _data);
                    //do nothing when the data is empty.
                    if (TextUtils.isEmpty(_data.trim())) {
                        return;
                    }

                    TypeToken<ResponseModel<List<Topic>>> token = new TypeToken<ResponseModel<List<Topic>>>() {
                    };

                    ResponseModel<List<Topic>> responseModel = s.fromJson(sourceString, token.getType());
                    List<Topic> list = responseModel._data;
                    setData2Adapter(1, list);
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                setComplete();
            }
        });

    }
}
