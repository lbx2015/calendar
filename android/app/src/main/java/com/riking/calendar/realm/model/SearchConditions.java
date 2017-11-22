package com.riking.calendar.realm.model;

import java.util.Date;
import java.util.UUID;

import io.realm.RealmModel;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.RealmClass;

/**
 * Created by zw.zhang on 2017/7/13.
 */

@RealmClass
public class SearchConditions implements RealmModel {
    @PrimaryKey
    public String name;
    public Date updateTime;
}
