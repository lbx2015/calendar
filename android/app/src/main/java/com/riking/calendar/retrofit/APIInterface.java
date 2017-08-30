package com.riking.calendar.retrofit;

import com.riking.calendar.pojo.AppUser;
import com.riking.calendar.pojo.CtryHdayCrcy;
import com.riking.calendar.pojo.CtryHdayCryCondition;
import com.riking.calendar.pojo.Dictionary;
import com.riking.calendar.pojo.GetHolidayModel;
import com.riking.calendar.pojo.GetVerificationModel;
import com.riking.calendar.pojo.HolidayConditionDemo;
import com.riking.calendar.pojo.MultipleResource;
import com.riking.calendar.pojo.QueryReport;
import com.riking.calendar.pojo.QueryReportModel;
import com.riking.calendar.pojo.ReminderModel;
import com.riking.calendar.pojo.TaskModel;
import com.riking.calendar.pojo.UploadImageModel;
import com.riking.calendar.pojo.User;
import com.riking.calendar.pojo.UserList;
import com.riking.calendar.pojo.base.ResponseModel;

import java.util.ArrayList;

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

    @GET("/reportList/getForHtml")
    Call<ResponseBody> getHtml(@Query("id") String id);

    @GET("api/unknown")
    Call<MultipleResource> doGetListResources();

    @POST("api/users")
    Call<User> createUser(@Body User user);

    @GET("api/users?")
    Call<UserList> doGetUserList(@Query("page") String page);

    @FormUrlEncoded
    @POST("api/users?")
    Call<UserList> doCreateUserWithField(@Field("queryParam") String name, @Field("job") String job);


    @POST("remindApp/save")
    Call<ResponseBody> createRemind(@Body ReminderModel reminder);

    @POST("Todo/save")
    Call<ResponseBody> createTask(@Body TaskModel taskModel);

    @POST("ctryHdayCrcyApp/getMore")
    Call<ResponseBody> getHolidays(@Body GetHolidayModel getHolidayModel);

    @POST("ctryHdayCrcyApp/getParam")
    Call<HolidayConditionDemo> getParams();

    @POST("ctryHdayCrcyApp/getMore")
    Call<CtryHdayCryCondition> getMore(@Body CtryHdayCrcy ctryHdayCrcy);

    @POST("ctryHdayCrcyApp/vagueQuery")
    Call<CtryHdayCryCondition> getVagueQuery(@Body CtryHdayCrcy ctryHdayCrcy);

    @POST("checkValiCode")
    Call<GetVerificationModel> checkVarificationCode(@Body AppUser user);

    @POST("getValiCode")
    Call<GetVerificationModel> getVarificationCode(@Body AppUser user);

    @POST("appUserApp/addOrUpdate")
    Call<ResponseModel<String>> updateUserInfo(@Body AppUser user);

    @POST("reportListApp/getAllReport")
    Call<QueryReportModel> getAllReports(@Query("id") String notUsed);

    @POST("appAboutApp/reportHtml")
    Call<ResponseModel<String>> getReportDetail(@Body QueryReport report);

    @POST("appAboutApp/aboutHtml")
    Call<ResponseModel<String>> getAboutHtml(@Body Object object);

    @POST("appAboutApp/agreementHtml")
    Call<ResponseModel<String>> getAgreementHtml(@Query("id") String notUsed);

    @Multipart
    @POST("appUserApp/upLoad")
    Call<UploadImageModel> postImage(@Part MultipartBody.Part body, @Part("id") String id);

    @POST("modelPropDictApp/T_APP_USER")
    Call<ResponseModel<ArrayList<Dictionary>>> getDictionary(@Body ArrayList<String> fields);
}
