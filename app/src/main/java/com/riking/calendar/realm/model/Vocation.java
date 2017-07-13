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
public class Vocation implements RealmModel {
    @PrimaryKey
    public String id = UUID.randomUUID().toString();
    public Date date;
    public String country;
    public String currency;
    public String name;
}
