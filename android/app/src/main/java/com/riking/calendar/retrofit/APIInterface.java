package com.riking.calendar.retrofit;

import com.riking.calendar.pojo.CtryHdayCrcy;
import com.riking.calendar.pojo.CtryHdayCryCondition;
import com.riking.calendar.pojo.GetHolidayModel;
import com.riking.calendar.pojo.HolidayConditionDemo;
import com.riking.calendar.pojo.MultipleResource;
import com.riking.calendar.pojo.ReminderModel;
import com.riking.calendar.pojo.User;
import com.riking.calendar.pojo.UserList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import retrofit2.http.Url;

/**
 * Created by zzw on 09/01/17.
 */

public interface APIInterface {

    @GET
    Call<ResponseBody> doGetListResources(@Url String url);

    @GET("/api/unknown")
    Call<MultipleResource> doGetListResources();

    @POST("/api/users")
    Call<User> createUser(@Body User user);

    @GET("/api/users?")
    Call<UserList> doGetUserList(@Query("page") String page);

    @FormUrlEncoded
    @POST("/api/users?")
    Call<UserList> doCreateUserWithField(@Field("name") String name, @Field("job") String job);


    @POST("/remindApp/save")
    Call<ResponseBody> createRemind(@Body ReminderModel reminder);

    @POST("/ctryHdayCrcyApp/getMore")
    Call<ResponseBody> getHolidays(@Body GetHolidayModel getHolidayModel);

    @POST("/ctryHdayCrcyApp/getParam")
    Call<HolidayConditionDemo> getParams();

    @POST("/ctryHdayCrcyApp/getMore")
    Call<CtryHdayCryCondition> getMore(@Body CtryHdayCrcy ctryHdayCrcy);


}
