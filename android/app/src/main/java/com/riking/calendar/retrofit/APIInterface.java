package com.riking.calendar.retrofit;

import com.google.gson.JsonObject;
import com.riking.calendar.pojo.AppUser;
import com.riking.calendar.pojo.AppUserRecommend;
import com.riking.calendar.pojo.AppUserReportCompleteRel;
import com.riking.calendar.pojo.AppUserReportRel;
import com.riking.calendar.pojo.AppUserReportResult;
import com.riking.calendar.pojo.AppVersionResult;
import com.riking.calendar.pojo.CtryHdayCrcy;
import com.riking.calendar.pojo.CtryHdayCryCondition;
import com.riking.calendar.pojo.Dictionary;
import com.riking.calendar.pojo.GetHolidayModel;
import com.riking.calendar.pojo.HolidayConditionDemo;
import com.riking.calendar.pojo.MultipleResource;
import com.riking.calendar.pojo.QueryReport;
import com.riking.calendar.pojo.QueryReportContainer;
import com.riking.calendar.pojo.ReminderModel;
import com.riking.calendar.pojo.TaskModel;
import com.riking.calendar.pojo.UploadImageModel;
import com.riking.calendar.pojo.User;
import com.riking.calendar.pojo.UserList;
import com.riking.calendar.pojo.WorkDate;
import com.riking.calendar.pojo.base.ResponseModel;
import com.riking.calendar.pojo.resp.AppUserResp;
import com.riking.calendar.pojo.server.Industry;
import com.riking.calendar.pojo.server.ReportAgence;
import com.riking.calendar.pojo.server.ReportFrequency;
import com.riking.calendar.pojo.synch.LoginParams;
import com.riking.calendar.pojo.synch.SynResult;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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
    Call<ResponseBody> getHtml(@Query("userId") String id);

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

    @POST("user/login")
    Call<ResponseModel<AppUserResp>> checkVarificationCode(@Body LoginParams user);

    @POST("common/getValidCode")
    Call<ResponseModel<AppUser>> getVarificationCode(@Body LoginParams user);

    @POST("appUserApp/addOrUpdate")
    Call<ResponseModel<String>> updateUserInfo(@Body AppUser user);

    /**
     * get all reports when user not login
     *
     * @return
     */
    @POST("reportListApp/getAllReport")
    Call<ResponseModel<List<ReportAgence>>> getAllReports(@Body AppUser user);

    @POST("common/getCommend")
    Call<ResponseModel<ArrayList<AppUserRecommend>>> getPositionByIndustry();

    /**
     * get all reports of user
     *
     * @param body
     * @return
     */
    @POST("appUserReport/getUserRepor")
    Call<ResponseModel<ArrayList<QueryReportContainer>>> getUserReports(@Body AppUserReportCompleteRel body);

    @POST("appAboutApp/reportHtml")
    Call<ResponseModel<String>> getReportDetail(@Body QueryReport report);

    @POST("appAboutApp/aboutHtml")
    Call<ResponseModel<String>> getAboutHtml(@Query("versionNumber") String versionNumber);

    @POST("appAboutApp/agreementHtml")
    Call<ResponseModel<String>> getAgreementHtml(@Query("userId") String notUsed);

    @Multipart
    @POST("appUserApp/upLoad")
    Call<UploadImageModel> postImage(@Part MultipartBody.Part body, @Part("userId") String id);

    @POST("modelPropDictApp/T_APP_USER")
    Call<ResponseModel<ArrayList<Dictionary>>> getDictionary(@Body ArrayList<String> fields);

    @POST("synchronous/synchronousDate")
    Call<ResponseModel<ArrayList<WorkDate>>> getWorkDays();

    /**
     * get user's reminders and tasks and other info ..
     */
    @POST("synchronous/synchronousAll")
    Call<ResponseModel<SynResult>> synchronousAll(@Body JsonObject user);

    @POST("synchronous/synchronousReminds")
    Call<ResponseModel<String>> synchronousReminds(@Body List<ReminderModel> reminderModels);

    @POST("synchronous/synchronousTodos")
    Call<ResponseModel<String>> synchronousTasks(@Body List<TaskModel> tasks);

    @POST("common/getappVersion")
    Call<ResponseModel<AppVersionResult>> getAppVersion(@Body JsonObject currentVersionId);

    @POST("common/findIndustry")
    Call<ResponseModel<ArrayList<Industry>>> findIndustry();

    /**
     * get positions for one industry
     *
     * @return
     */
    @POST("common/getPositionByIndustry")
    Call<ResponseModel<ArrayList<Industry>>> getPositionByIndustry(@Body HashMap<String, String> industryId);

    /**
     * set user's interesting reports into server
     */
    @POST("/appUserReport/userAddReportEdit")
    Call<ResponseModel<Short>> interestingReports(@Body AppUserReportResult appUserReportResult);

    /**
     * find the repords of user ordered
     */
    @POST("/appUserReport/findUserReportList")
    Call<ResponseModel<List<ReportFrequency>>> findUserReportList(@Body AppUser user);

    @POST("/appUserReport/userAddReportEdit")
    Call<ResponseModel<Short>> userAddReportEdit(@Body AppUserReportResult reportResult);

    @POST("/reportListApp/getReportByName")
    Call<ResponseModel<List<ReportFrequency>>> getReportByName(@Body HashMap<String, String> reporName);

    @POST("/appUserReport/updateUserReportRelById")
    Call<ResponseModel<String>> updateUserReportRelById(@Body AppUserReportRel reportRel);

}
