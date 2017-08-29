package com.riking.calendar.realm.model;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

//@Entity
//@Table(name = "t_app_user")
public class QueryReportRealmModel extends RealmObject {
    @PrimaryKey
    public String id;
    public String reportName;
    public String reportCode;
    public String moduleType;
}
