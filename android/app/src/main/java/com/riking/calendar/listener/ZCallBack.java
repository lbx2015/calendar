package com.riking.calendar.listener;

import android.widget.Toast;

import com.riking.calendar.R;
import com.riking.calendar.app.MyApplication;
import com.riking.calendar.helper.CodeDef;
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
        if (response == null || response.body() == null) {
            Toast.makeText(MyApplication.APP, MyApplication.APP.getString(R.string.error_server), Toast.LENGTH_SHORT).show();
        }else if(response.body().code == CodeDef.EMP.LOGIN_TIME_OUT){
            Toast.makeText(MyApplication.APP, MyApplication.APP.getString(R.string.error_login_time_out) + response.body().code, Toast.LENGTH_SHORT).show();
        }
        else if (response.body().code == CodeDef.EMP.USER_PASS_ERR) {
            Toast.makeText(MyApplication.APP, MyApplication.APP.getString(R.string.error_password) + response.body().code, Toast.LENGTH_SHORT).show();
        } else if (response.body().code == CodeDef.EMP.CHECK_CODE_ERR) {
            Toast.makeText(MyApplication.APP, MyApplication.APP.getString(R.string.check_code_password) + response.body().code, Toast.LENGTH_SHORT).show();
        } else if (response.body().code == CodeDef.EMP.CHECK_CODE_TIME_OUT) {
            Toast.makeText(MyApplication.APP, MyApplication.APP.getString(R.string.check_code_time_out) + response.body().code, Toast.LENGTH_SHORT).show();
        } else if ((response.body().code == CodeDef.EMP.DATA_NOT_FOUND)) {
            Toast.makeText(MyApplication.APP, MyApplication.APP.getString(R.string.data_not_found) + response.body().code, Toast.LENGTH_SHORT).show();
        } else if (response.body().code != CodeDef.SUCCESS) {
            Toast.makeText(MyApplication.APP, MyApplication.APP.getString(R.string.error_server) + response.body().code, Toast.LENGTH_SHORT).show();
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
