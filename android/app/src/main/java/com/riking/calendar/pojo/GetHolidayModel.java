package com.riking.calendar.pojo;

import io.realm.annotations.PrimaryKey;

/**
 * Created by zw.zhang on 2017/8/7.
 */

public class GetHolidayModel {
    @PrimaryKey
    public String id;
    //concurrency
    public String crcy;
    //country
    public String ctryName;
    //holiday date
    public String hdayDate;
    //holiday name
    public String hdayName;
}
