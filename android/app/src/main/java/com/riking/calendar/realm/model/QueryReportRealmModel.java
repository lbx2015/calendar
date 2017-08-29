package com.riking.calendar.realm.model;

import com.google.gson.Gson;

import io.realm.RealmObject;

//@Entity
//@Table(name = "t_app_user")
public class QueryReportRealmModel extends RealmObject {

    public String id;
    public String reportName;
    public String reportCode;
    public String moduleType;
}
