package com.riking.calendar.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.riking.calendar.R;
import com.riking.calendar.activity.InputEmailVerifyCodeActivity;
import com.riking.calendar.adapter.MyFollowersAdapter;
import com.riking.calendar.fragment.base.ZFragment;
import com.riking.calendar.listener.ZCallBack;
import com.riking.calendar.listener.ZCallBackWithFail;
import com.riking.calendar.listener.ZClickListenerWithLoginCheck;
import com.riking.calendar.pojo.base.ResponseModel;
import com.riking.calendar.pojo.params.UpdUserParams;
import com.riking.calendar.pojo.params.UserParams;
import com.riking.calendar.pojo.resp.AppUserResp;
import com.riking.calendar.pojo.server.AppUserResult;
import com.riking.calendar.retrofit.APIClient;
import com.riking.calendar.util.CONST;
import com.riking.calendar.util.StringUtil;
import com.riking.calendar.util.ZPreference;
import com.riking.calendar.util.ZToast;

import java.util.List;

import static android.app.Activity.RESULT_CANCELED;

/**
 * Created by zw.zhang on 2017/7/17.
 */

public class MyColleagueFragment extends ZFragment<MyFollowersAdapter> {
    View notBindEmailLayout;
    TextView addEmailTv;
    AppUserResp currentUser = ZPreference.getCurrentLoginUser();

    @Override
    public MyFollowersAdapter getAdapter() {
        return new MyFollowersAdapter();
    }

    //should override this method
    public View createView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.my_colleague_fragment, container, false);
    }

    public void initViews() {
        notBindEmailLayout = v.findViewById(R.id.not_bind_email_layout);
        addEmailTv = v.findViewById(R.id.button_message_tv);
    }

    public void initEvents() {
        if (StringUtil.isEmpty(currentUser.email)) {
            mPullToLoadView.setVisibility(View.GONE);
            notBindEmailLayout.setVisibility(View.VISIBLE);
            addEmailTv.setText("立即添加");
            addEmailTv.setOnClickListener(new ZClickListenerWithLoginCheck() {
                @Override
                public void click(View v) {
                    addEmailDialog(new UpdateUserInfoCallBack() {
                        @Override
                        public void newValue(final String newValue) {
                            if (currentUser.email != null && currentUser.email.equals(newValue)) {
                                return;
                            }

                            UpdUserParams user = new UpdUserParams();
                            user.email = newValue;
                            callServerApi2UpdateUserInfo(newValue, user);
                        }

                        @Override
                        public void updateSuccess(String newValue) {
                            currentUser.email = newValue;
                            addEmailTv.setText("立即验证");
                            currentUser.isIdentify = 0;
                            ZPreference.saveUserInfoAfterLogin(currentUser);
                            validateEmail();
                        }
                    });
                }
            });
        } else if (currentUser.isIdentify != 1) {
            mPullToLoadView.setVisibility(View.GONE);
            notBindEmailLayout.setVisibility(View.VISIBLE);
            addEmailTv.setText("立即验证");
            addEmailTv.setOnClickListener(new ZClickListenerWithLoginCheck() {
                @Override
                public void click(View v) {
                    validateEmail();
                }
            });
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Make sure the request was successful
        if (resultCode != RESULT_CANCELED && requestCode == CONST.VERIFY_EMAIL) {
            currentUser.isIdentify = data.getIntExtra(CONST.EMAIL_VALIDATE, 0);
            ZPreference.saveUserInfoAfterLogin(currentUser);
            mPullToLoadView.setVisibility(View.VISIBLE);
            notBindEmailLayout.setVisibility(View.GONE);
            //reload data
            mPullToLoadView.initLoad();
        }
    }

    private void validateEmail() {
        UserParams userParams = new UserParams();
        userParams.email = currentUser.email;
        APIClient.sendEmailVerifyCode(userParams, new ZCallBackWithFail<ResponseModel<String>>() {
            @Override
            public void callBack(ResponseModel<String> response) {
                MyColleagueFragment.this.startActivityForResult(new Intent(getContext(), InputEmailVerifyCodeActivity.class), CONST.VERIFY_EMAIL);
            }
        });
    }

    /**
     * @param callBack
     */
    private void addEmailDialog(final UpdateUserInfoCallBack callBack) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("邮箱");
        // I'm using fragment here so I'm using getView() to provide ViewGroup
        // but you can provide here any other instance of ViewGroup from your Fragment / Activity
        View viewInflated;
        viewInflated = LayoutInflater.from(getActivity()).inflate(R.layout.edit_user_email_dialog, null, false);

        // Set up the input
        final EditText input = (EditText) viewInflated.findViewById(R.id.input);
        // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
        builder.setView(viewInflated);
//        input.setText(initValue == null ? "" : initValue);
//        input.setSelection(initValue == null ? 0 : initValue.length());
        // Set up the buttons
        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                Editable editable = input.getText();
                if (editable == null) {
                    return;
                }
                final String newComments = input.getText().toString();
                if (newComments.length() > 0) {
                    callBack.newValue(newComments);
                }
            }
        });
        builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();
    }

    public void loadData(final int page) {
        //do nothing if user not validate the email
        if (currentUser.isIdentify != 1) {
            return;
        }
        final UserParams params = new UserParams();
        params.pindex = page;
        APIClient.getColleague(params, new ZCallBackWithFail<ResponseModel<List<AppUserResult>>>() {
            @Override
            public void callBack(ResponseModel<List<AppUserResult>> response) {
                mPullToLoadView.setComplete();
                isLoading = false;
                if (!failed) {
//                    Gson g = new Gson();
//                    TypeToken<List<AppUserResult>> token = new TypeToken<List<AppUserResult>>() {
//                    };
//                    List<AppUserResult> list = g.fromJson(response._data, token.getType());
                    List<AppUserResult> list = response._data;
                    if (list != null && list.size() < params.pcount) {
                        isHasLoadedAll = true;
                        if (list.size() == 0) {
                            ZToast.toastEmpty();
                            return;
                        }
                    }

                    if (list != null) {
                        nextPage = page + 1;
                        mAdapter.setData(list);
                    }
                }
            }
        });
    }

    public abstract class UpdateUserInfoCallBack {
        //call the method when user complete the input and click ok
        public abstract void newValue(String newValue);

        public abstract void updateSuccess(String newValue);

        //call server
        public void callServerApi2UpdateUserInfo(final String newValue, UpdUserParams user) {
            APIClient.modifyUserInfo(user, new ZCallBack<ResponseModel<String>>() {
                @Override
                public void callBack(ResponseModel<String> response) {
                    updateSuccess(newValue);
                    ZToast.toast("信息更新成功！");
                }
            });
        }
    }
}
