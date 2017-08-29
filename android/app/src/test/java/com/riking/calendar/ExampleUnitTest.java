package com.riking.calendar;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.ldf.calendar.Const;
import com.riking.calendar.pojo.AppUser;
import com.riking.calendar.pojo.QueryReport;
import com.riking.calendar.pojo.QueryReportContainer;
import com.riking.calendar.pojo.QueryReportModel;
import com.riking.calendar.pojo.base.ResponseModel;
import com.riking.calendar.realm.model.Task;

import org.junit.Test;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import static org.junit.Assert.assertEquals;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() throws Exception {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void testSubString() throws Exception {
        String i = "0178";
        System.out.println(i.substring(0, 2));
        System.out.println(i.substring(2));
    }

    @Test
    public void testInterParse() throws Exception {
        String i = "01";
        System.out.println(Integer.parseInt(i));
    }

    @Test
    public void testGeneric() throws Exception {
        ResponseModel<AppUser> dd = new ResponseModel<>();
        AppUser user = new AppUser();
        user.id = "ddddd";
        dd._data = user;
        System.out.println(new Gson().toJson(dd));
    }

    @Test
    public void jsonReportTest() throws Exception {
        QueryReportModel t = new QueryReportModel();
        ArrayList<QueryReportContainer> list = new ArrayList<>();
        QueryReportContainer container = new QueryReportContainer();
        container.title = "reportTitle";
        QueryReport report = new QueryReport();
        report.reportName = "test";
        container.result = new ArrayList<QueryReport>();
        container.result.add(report);
        t._data = list;
        t._data.add(container);
        Gson gson = new GsonBuilder().setDateFormat("yyyyMMddHHmm").create();
        System.out.println(gson.toJson(t));
    }
}