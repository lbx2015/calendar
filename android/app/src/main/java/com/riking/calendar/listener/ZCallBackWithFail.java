package com.riking.calendar.listener;

import android.widget.Toast;

import com.riking.calendar.R;
import com.riking.calendar.app.MyApplication;
import com.riking.calendar.jiguang.Logger;
import com.riking.calendar.pojo.base.ResponseModel;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by zw.zhang on 2017/8/17.
 */

public abstract class ZCallBackWithFail<T extends ResponseModel> implements Callback<T> {
    /**
     * override this method to do some thing after fail.
     */
    public boolean failed;

    public ZCallBackWithFail() {
    }

    public abstract void callBack(T response) throws Exception;

    @Override
    public void onResponse(Call<T> call, Response<T> response) {
        Logger.d("zzw", "request ok + " + call.request().toString());

        if (response == null || response.body() == null || response.body().code != 200) {
            failed = true;
        }

        try {
            callBack(response.body());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onFailure(Call<T> call, Throwable t) {
        failed = true;
        try {
            callBack(null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Logger.d("zzw", "request fail + " + call.request().toString() + t.getMessage());
        Toast.makeText(MyApplication.APP, MyApplication.APP.getString(R.string.error_network), Toast.LENGTH_SHORT).show();
    }
}
