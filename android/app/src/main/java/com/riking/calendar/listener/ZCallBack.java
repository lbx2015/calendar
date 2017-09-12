package com.riking.calendar.listener;

import android.widget.Toast;

import com.riking.calendar.R;
import com.riking.calendar.app.MyApplication;
import com.riking.calendar.jiguang.Logger;
import com.riking.calendar.pojo.base.ResponseModel;
import com.riking.calendar.util.NetStateReceiver;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by zw.zhang on 2017/8/17.
 */

public abstract class ZCallBack<T extends ResponseModel> implements Callback<T> {

    public abstract void callBack(T response);

    @Override
    public void onResponse(Call<T> call, Response<T> response) {
        Logger.d("zzw", "request ok + " + call.request().toString());
        if (response == null || response.body() == null || response.body().code != 200) {
            Toast.makeText(MyApplication.APP, MyApplication.APP.getString(R.string.error_network), Toast.LENGTH_SHORT).show();
        } else {
            callBack(response.body());
        }
    }

    @Override
    public void onFailure(Call<T> call, Throwable t) {
        Logger.d("zzw", "request fail + " + call.request().toString() + t.getMessage());
        if (NetStateReceiver.isNetAvailable) {
            Toast.makeText(MyApplication.APP, MyApplication.APP.getString(R.string.server_error), Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(MyApplication.APP, MyApplication.APP.getString(R.string.error_network), Toast.LENGTH_SHORT).show();
        }
    }
}
