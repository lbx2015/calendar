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
import com.riking.calendar.R;
import com.riking.calendar.adapter.SearchPersonAdapter;
import com.riking.calendar.fragment.base.ZFragment;
import com.riking.calendar.interfeet.PerformInputSearch;
import com.riking.calendar.pojo.base.ResponseModel;
import com.riking.calendar.pojo.params.SearchParams;
import com.riking.calendar.pojo.server.AppUserResult;
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

public class SearchPersonFragment extends ZFragment<SearchPersonAdapter> implements PerformInputSearch {
    String searchCondition;

    @Nullable
    @Override
    public View createView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.search_fragment, container, false);
    }

    @Override
    public SearchPersonAdapter getAdapter() {
        return new SearchPersonAdapter(getContext());
    }

    public void initViews() {
    }

    public void initEvents() {
    }

    public void loadData(final int page) {
        search(searchCondition);
       /* new Handler().postDelayed(new Runnable() {
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
        }, 1000);*/
    }

    @Override
    public void search(String searchCondition) {

        if (TextUtils.isEmpty(searchCondition)) {
            setComplete();
            return;
        }

        this.searchCondition = searchCondition;

        SearchParams params = new SearchParams();
        params.keyWord = searchCondition;
        //search person
        params.objType = 3;

        APIClient.findSearchList(params, new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                setComplete();

                ResponseBody r = response.body();
                try {
                    String sourceString = r.source().readUtf8();
                    Gson s = new Gson();

                    JsonObject jsonObject = s.fromJson(sourceString, JsonObject.class);
                    String _data = jsonObject.get("_data").toString();

                    //do nothing when the data is empty.
                    if (TextUtils.isEmpty(_data.trim())) {
                        return;
                    }

                    TypeToken<ResponseModel<List<AppUserResult>>> token = new TypeToken<ResponseModel<List<AppUserResult>>>() {
                    };

                    ResponseModel<List<AppUserResult>> responseModel = s.fromJson(sourceString, token.getType());
                    List<AppUserResult> list = responseModel._data;
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
