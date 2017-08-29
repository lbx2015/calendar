package com.riking.calendar.realm.model;

import io.realm.RealmList;
import io.realm.RealmObject;

//@Entity
//@Table(name = "t_app_user")
public class QueryReportContainerRealmModel extends RealmObject {
    public String title;
    public RealmList<QueryReportRealmModel> result;
}
