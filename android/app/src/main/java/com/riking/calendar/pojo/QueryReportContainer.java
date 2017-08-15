package com.riking.calendar.pojo;

import com.google.gson.Gson;

import java.util.ArrayList;

//@Entity
//@Table(name = "t_app_user")
public class QueryReportContainer {
    public String title;
    public ArrayList<QueryReport> result;

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
