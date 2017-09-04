package com.riking.calendar.realm.model;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class WorkDateRealm extends RealmObject {
    //20171219
    @PrimaryKey
    public String date;
    public String weekday;
    public byte isWork;
}
