package com.riking.calendar.pojo;

import com.google.gson.Gson;

//@Entity
//@Table(name = "t_app_user")
public class QueryReport {
    @Override
    public String toString() {
        return new Gson().toJson(this);
    }

    public String id;
    public String reportName;
    public String reportCode;
    public String moduleType;
}
