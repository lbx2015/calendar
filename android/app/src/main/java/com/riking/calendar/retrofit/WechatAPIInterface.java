package com.riking.calendar.retrofit;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by zzw on 09/01/17.
 */

public interface WechatAPIInterface {
    @GET("sns/oauth2/access_token")
    Call<ResponseBody> getAccessToken(@Query("appid") String appid, @Query("secret") String secret, @Query("code") String code, @Query("grant_type") String grantType);

    @GET("sns/userinfo")
    Call<ResponseBody> getUserInfo(@Query("access_token") String accessToken, @Query("openid") String openId);

    @GET("sns/auth")
    Call<ResponseBody> isAccesstokenValid(@Query("access_token") String accessToken, @Query("openid") String openId);

    @GET("sns/oauth2/refresh_token")
    Call<ResponseBody> refreshToken(@Query("appid") String appId, @Query("grant_type") String grantType, @Query("refresh_token") String refreshToken);
}