package com.riking.calendar.pojo;

import com.google.gson.Gson;

import io.realm.annotations.PrimaryKey;

//@Entity
//@Table(name = "t_app_user")
public class QueryReport {
    @PrimaryKey
    public String id;
    public String reportName;
    public String reportCode;
    public String moduleType;
    public String caliberType;
    public String frequency;

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
