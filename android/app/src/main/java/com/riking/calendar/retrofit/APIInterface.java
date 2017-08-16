package com.riking.calendar.retrofit;

import com.riking.calendar.pojo.AppUser;
import com.riking.calendar.pojo.CtryHdayCrcy;
import com.riking.calendar.pojo.CtryHdayCryCondition;
import com.riking.calendar.pojo.GetHolidayModel;
import com.riking.calendar.pojo.GetVerificationModel;
import com.riking.calendar.pojo.HolidayConditionDemo;
import com.riking.calendar.pojo.MultipleResource;
import com.riking.calendar.pojo.QueryReport;
import com.riking.calendar.pojo.QueryReportModel;
import com.riking.calendar.pojo.ReminderModel;
import com.riking.calendar.pojo.TaskModel;
import com.riking.calendar.pojo.User;
import com.riking.calendar.pojo.UserList;

import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
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
    Call<UserList> doCreateUserWithField(@Field("queryParam") String name, @Field("job") String job);


    @POST("/remindApp/save")
    Call<ResponseBody> createRemind(@Body ReminderModel reminder);

    @POST("/Todo/save")
    Call<ResponseBody> createTask(@Body TaskModel taskModel);

    @POST("/ctryHdayCrcyApp/getMore")
    Call<ResponseBody> getHolidays(@Body GetHolidayModel getHolidayModel);

    @POST("/ctryHdayCrcyApp/getParam")
    Call<HolidayConditionDemo> getParams();

    @POST("/ctryHdayCrcyApp/getMore")
    Call<CtryHdayCryCondition> getMore(@Body CtryHdayCrcy ctryHdayCrcy);

    @POST("/ctryHdayCrcyApp/vagueQuery")
    Call<CtryHdayCryCondition> getVagueQuery(@Body CtryHdayCrcy ctryHdayCrcy);

    @POST("/checkValiCode")
    Call<GetVerificationModel> checkVarificationCode(@Body AppUser user);

    @POST("/getValiCode")
    Call<GetVerificationModel> getVarificationCode(@Body AppUser user);

    @POST("/appUserApp/addOrUpdate")
    Call<GetVerificationModel> updateUserInfo(@Body AppUser user);

    @POST("/reportListApp/getAllReport")
    Call<QueryReportModel> getAllReports(@Body QueryReport report);

    @Multipart
    @POST("/appUserApp/upLoad")
    Call<ResponseBody> postImage(@Part MultipartBody.Part body, @Part("id") String id);
}
