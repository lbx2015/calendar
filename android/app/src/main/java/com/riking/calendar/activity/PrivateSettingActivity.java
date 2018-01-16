package com.riking.calendar.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;

import com.necer.ncalendar.utils.MyLog;
import com.riking.calendar.R;
import com.riking.calendar.listener.ZCallBack;
import com.riking.calendar.pojo.base.ResponseModel;
import com.riking.calendar.pojo.params.UpdUserParams;
import com.riking.calendar.pojo.resp.AppUserResp;
import com.riking.calendar.retrofit.APIClient;
import com.riking.calendar.util.ZPreference;
import com.riking.calendar.util.ZToast;

/**
 * Created by zw.zhang on 2017/8/5.
 */

public class PrivateSettingActivity extends AppCompatActivity {
    Switch checkDynamicSwitch;
    Switch checkFollowSwitch;
    Switch checkCollectSwitch;
    AppUserResp user = ZPreference.getCurrentLoginUser();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_private_setting);
        checkDynamicSwitch = findViewById(R.id.check_my_dynamic);
        checkFollowSwitch = findViewById(R.id.check_my_follow);
        checkCollectSwitch = findViewById(R.id.check_my_collect);

        MyLog.d("user.checkMyCollectState" + user.checkMyCollectState);
        checkDynamicSwitch.setChecked(user.checkMyCollectState == 1);
        checkFollowSwitch.setChecked(user.checkMyFollowState == 1);
        checkCollectSwitch.setChecked(user.checkMyCollectState == 1);
        setCheckListener();
    }

    private void setCheckListener() {
        checkDynamicSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, final boolean isChecked) {
                final UpdUserParams u = new UpdUserParams();
                final int state = checkDynamicSwitch.isChecked() ? 1 : 0;
                u.checkMyDynamicState = state;
                u.checkMyCollectState = u.checkMyCollectState;
                u.checkMyFollowState = u.checkMyFollowState;
                MyLog.d("user.checkMyFollowState" + user.checkMyDynamicState);
                setUserDate(u, new UpdateUserInfoCallBack() {
                    @Override
                    void updateSuccess() {
                        user.checkMyDynamicState = state;
                        ZPreference.saveUserInfoAfterLogin(user);
                    }
                });
            }
        });
        checkFollowSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, final boolean isChecked) {
                UpdUserParams params = new UpdUserParams();
                final int state = checkDynamicSwitch.isChecked() ? 1 : 0;
                params.checkMyFollowState = state;
                params.checkMyCollectState = user.checkMyCollectState;
                params.checkMyFollowState = user.checkMyFollowState;
                MyLog.d("user.checkMyFollowState" + user.checkMyFollowState);
                setUserDate(params, new UpdateUserInfoCallBack() {

                    @Override
                    void updateSuccess() {
                        MyLog.d("");
                        user.checkMyFollowState = state;
                        ZPreference.saveUserInfoAfterLogin(user);
                    }
                });
            }
        });
        checkCollectSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, final boolean isChecked) {
                UpdUserParams p = new UpdUserParams();
                final int state = checkDynamicSwitch.isChecked() ? 1 : 0;
                p.checkMyCollectState = state;
                p.checkMyFollowState = user.checkMyFollowState;
                p.checkMyDynamicState = user.checkMyDynamicState;
                MyLog.d("user.checkCollectSwitch" + user.checkMyFollowState);
                setUserDate(p, new UpdateUserInfoCallBack() {
                    @Override
                    void updateSuccess() {
                        user.checkMyCollectState = state;
                        ZPreference.saveUserInfoAfterLogin(user);
                    }
                });
            }
        });
    }

    private void setUserDate(UpdUserParams user, final UpdateUserInfoCallBack callBack) {
        APIClient.modifyUserInfo(user, new ZCallBack<ResponseModel<String>>() {
            @Override
            public void callBack(ResponseModel<String> response) {
                ZToast.toast("信息更新成功！");
                callBack.updateSuccess();
            }
        });
    }

    public void clickBack(final View view) {
        onBackPressed();
    }

    public abstract class UpdateUserInfoCallBack {
        abstract void updateSuccess();
    }
}
