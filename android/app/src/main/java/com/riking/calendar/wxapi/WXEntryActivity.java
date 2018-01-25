package com.riking.calendar.wxapi;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.necer.ncalendar.utils.MyLog;
import com.riking.calendar.app.MyApplication;
import com.riking.calendar.retrofit.APIClient;
import com.riking.calendar.util.CONST;
import com.riking.calendar.util.StringUtil;
import com.riking.calendar.util.ZPreference;
import com.riking.calendar.util.ZToast;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.modelmsg.ShowMessageFromWX;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WXEntryActivity extends Activity implements IWXAPIEventHandler {

    private static final int TIMELINE_SUPPORTED_VERSION = 0x21020001;
    private static final int RETURN_MSG_TYPE_LOGIN = 1;
    private static final int RETURN_MSG_TYPE_SHARE = 2;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            //如果没回调onResp，八成是这句没有写
            MyApplication.APP.mWxApi.handleIntent(getIntent(), this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        setIntent(intent);
        MyApplication.APP.mWxApi.handleIntent(intent, this);
    }

    // 微信发送请求到第三方应用时，会回调到该方法
    @Override
    public void onReq(BaseReq req) {
//		switch (req.getType()) {
//		case ConstantsAPI.COMMAND_GETMESSAGE_FROM_WX:
//			goToGetMsg();
//			break;
//		case ConstantsAPI.COMMAND_SHOWMESSAGE_FROM_WX:
//			goToShowMsg((ShowMessageFromWX.Req) req);
//			break;
//		default:
//			break;
//		}
    }

    // 第三方应用发送到微信的请求处理后的响应结果，会回调到该方法
    //app发送消息给微信，处理返回消息的回调
    @Override
    public void onResp(BaseResp resp) {
        switch (resp.errCode) {

            case BaseResp.ErrCode.ERR_AUTH_DENIED:
            case BaseResp.ErrCode.ERR_USER_CANCEL:
                if (RETURN_MSG_TYPE_SHARE == resp.getType()) ZToast.toast("分享失败");
                else ZToast.toast("登录失败");
                break;
            case BaseResp.ErrCode.ERR_OK:
                switch (resp.getType()) {
                    case RETURN_MSG_TYPE_LOGIN:
                        //拿到了微信返回的code,立马再去请求access_token
                        final String code = ((SendAuth.Resp) resp).code;
                        MyLog.d("code = " + code);

                        final String accessToken = null;
                        final String openId = ZPreference.pref.getString(CONST.OPEN_ID, null);
                        if (accessToken != null) {
                            APIClient.isWeChatAccessTokenValid(accessToken, openId, new Callback<ResponseBody>() {
                                @Override
                                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                    if (response.isSuccessful() && response.code() == 200) {
                                        String sourceString = null;
                                        try {
                                            sourceString = response.body().source().readUtf8();
                                            Gson s = new Gson();
                                            JsonObject jsonObject = s.fromJson(sourceString, JsonObject.class);
                                            if (jsonObject.get("errcode").getAsInt() != 0) {
                                                refreshWechatToken();
                                            } else {
                                                getInfo(accessToken, openId);
                                            }
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }

                                    } else {
                                        getAccessTokenAndInfo(code);
                                    }
                                }

                                @Override
                                public void onFailure(Call<ResponseBody> call, Throwable t) {

                                }
                            });
                        } else {
                            getAccessTokenAndInfo(code);
                        }
                        //就在这个地方，用网络库什么的或者自己封的网络api，发请求去咯，注意是get请求
                        finish();
                        break;

                    case RETURN_MSG_TYPE_SHARE:
                        ZToast.toast("微信分享成功");
                        finish();
                        break;
                }
                break;
        }

    }

    private void refreshWechatToken() {
        String refreshToken = ZPreference.pref.getString(CONST.ACCESS_TOKEN, null);
        if (refreshToken != null) {
            APIClient.refreshWechatToken(refreshToken, new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.isSuccessful() && response.code() == 200) {
                        String sourceString = null;
                        MyLog.d("wechatback sourceString " + sourceString);
                        if (StringUtil.isEmpty(sourceString)) {
                            refreshWechatToken();
                            return;
                        }
                        try {
                            sourceString = response.body().source().readUtf8();
                            Gson s = new Gson();
                            JsonObject jsonObject = s.fromJson(sourceString, JsonObject.class);
                            if (jsonObject.get("errcode") != null && jsonObject.get("errcode").getAsInt() != 0) {
                                return;
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }


                        String accessToken = ZPreference.pref.getString(CONST.ACCESS_TOKEN, null);
                        String openId = ZPreference.pref.getString(CONST.OPEN_ID, null);
                        if (accessToken != null && openId != null) {
                            getInfo(accessToken, openId);
                        }
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {

                }
            });
        }
    }

    private void getAccessTokenAndInfo(String code) {
        APIClient.getAccessToken(code, new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    try {
                        String sourceString = response.body().source().readUtf8();
                        MyLog.d("wechatback sourceString " + sourceString);
                        if (StringUtil.isEmpty(sourceString)) {
                            refreshWechatToken();
                            return;
                        }
                        Gson s = new Gson();
                        JsonObject jsonObject = s.fromJson(sourceString, JsonObject.class);
                        String accessToken = jsonObject.get("access_token").toString();
//                        String expires_in = jsonObject.get("expires_in").toString();
                        String refresh_token = jsonObject.get("refresh_token").toString();
                        String openid = jsonObject.get("openid").toString();
//                        String scope = jsonObject.get("scope").toString();
                        MyLog.d("wechatback" + openid);
                        SharedPreferences.Editor editor = ZPreference.pref.edit();
                        editor.putString(CONST.ACCESS_TOKEN, accessToken);
                        editor.putString(CONST.REFRESH_TOKEN, refresh_token);
                        editor.putString(CONST.OPEN_ID, openid);
                        editor.commit();
                        refreshWechatToken();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }

    private void getInfo(String accessToken, String openid) {
        APIClient.getWechatInfo(accessToken, openid, new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful() && response.code() == 200) {
                    String sourceString = null;
                    try {
                        sourceString = response.body().source().readUtf8();
                        if (StringUtil.isEmpty(sourceString)) {
                            return;
                        }
                        Gson s = new Gson();
                        JsonObject jsonObject = s.fromJson(sourceString, JsonObject.class);
                        String nikeName = jsonObject.get("nickname").toString();
                        MyLog.d("wechatuserInfo" + sourceString);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }

    private void goToGetMsg() {
//		Intent intent = new Intent(this, GetFromWXActivity.class);
//		intent.putExtras(getIntent());
//		startActivity(intent);
//		finish();
    }

    private void goToShowMsg(ShowMessageFromWX.Req showReq) {
//		WXMediaMessage wxMsg = showReq.message;
//		WXAppExtendObject obj = (WXAppExtendObject) wxMsg.mediaObject;
//
//		StringBuffer msg = new StringBuffer(); // ��֯һ������ʾ����Ϣ����
//		msg.append("description: ");
//		msg.append(wxMsg.description);
//		msg.append("\n");
//		msg.append("extInfo: ");
//		msg.append(obj.extInfo);
//		msg.append("\n");
//		msg.append("filePath: ");
//		msg.append(obj.filePath);
//
//		Intent intent = new Intent(this, ShowFromWXActivity.class);
//		intent.putExtra(Constants.ShowMsgActivity.STitle, wxMsg.title);
//		intent.putExtra(Constants.ShowMsgActivity.SMessage, msg.toString());
//		intent.putExtra(Constants.ShowMsgActivity.BAThumbData, wxMsg.thumbData);
//		startActivity(intent);
        finish();
    }
}