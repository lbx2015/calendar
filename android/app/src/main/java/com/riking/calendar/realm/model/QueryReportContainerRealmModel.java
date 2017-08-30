package com.riking.calendar.realm.model;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.RealmModule;

//@Entity
//@Table(name = "t_app_user")
public class QueryReportContainerRealmModel extends RealmObject {
    @PrimaryKey
    public String title;
    public RealmList<QueryReportRealmModel> result;
}
