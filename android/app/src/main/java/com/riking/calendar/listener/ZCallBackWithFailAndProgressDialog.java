package com.riking.calendar.listener;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Looper;
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

public abstract class ZCallBackWithFailAndProgressDialog<T extends ResponseModel> implements Callback<T> {
    /**
     * override this method to do some thing after fail.
     */
    public boolean failed;
    private ProgressDialog mProgressDialog;

    private boolean isBackQuickly;

    public ZCallBackWithFailAndProgressDialog() {
        new android.os.Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                if (isBackQuickly) {
                    return;
                }
                //dismiss the dialog first
                if (mProgressDialog != null && mProgressDialog.isShowing()) {
                    mProgressDialog.dismiss();
                    mProgressDialog = null;
                }
                mProgressDialog = new ProgressDialog(MyApplication.mCurrentActivity);
                mProgressDialog.setOwnerActivity(MyApplication.mCurrentActivity);
                mProgressDialog.setMessage("正在加载中");
                mProgressDialog.setCanceledOnTouchOutside(false);
                mProgressDialog.show();
            }
        }, 1000);
    }

    public ZCallBackWithFailAndProgressDialog(Context t) {
        mProgressDialog = new ProgressDialog(t);
        mProgressDialog.setMessage("正在加载中");
        mProgressDialog.setCanceledOnTouchOutside(false);
        mProgressDialog.show();
    }

    public abstract void callBack(T response) throws Exception;

    private void closeProgressDialog() {
        if (mProgressDialog == null) {
            return;
        }
        Activity activity = mProgressDialog.getOwnerActivity();
        if (activity != null && !activity.isFinishing()) {
            mProgressDialog.dismiss();
        }
    }

    @Override
    public void onResponse(Call<T> call, Response<T> response) {
        isBackQuickly = true;
        closeProgressDialog();
        Logger.d("zzw", "request ok + " + call.request().toString());
        if (response == null || response.body() == null || response.body().code != 200) {
            failed = true;
            Toast.makeText(MyApplication.APP, MyApplication.APP.getString(R.string.error_network), Toast.LENGTH_SHORT).show();
        }

        try {
            callBack(response.body());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onFailure(Call<T> call, Throwable t) {
        isBackQuickly = true;
        closeProgressDialog();
        failed = true;
        try {
            callBack(null);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (NetStateReceiver.isNetAvailable) {
            Toast.makeText(MyApplication.APP, MyApplication.APP.getString(R.string.server_error), Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(MyApplication.APP, MyApplication.APP.getString(R.string.error_network), Toast.LENGTH_SHORT).show();
        }
    }
}
