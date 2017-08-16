package com.riking.calendar;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.ldf.calendar.Const;
import com.riking.calendar.pojo.QueryReport;
import com.riking.calendar.pojo.QueryReportContainer;
import com.riking.calendar.pojo.QueryReportContainerList;
import com.riking.calendar.pojo.QueryReportModel;
import com.riking.calendar.pojo.Report;
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
    public void jsonTest() throws Exception {
        Task t = new Task();
        ArrayList<Report> list = new ArrayList<>();
        Report r = new Report();
        r.id = "ida";
        r.moduleType = "module type";
        r.reportCode = "report code";
        r.reportName = "report name";
        list.add(r);
        t.strDate = new SimpleDateFormat(Const.yyyyMMddHHmm).format(new Date());
//        Gson gson = new Gson();
        Gson gson = new GsonBuilder().setDateFormat("yyyyMMddHHmm").create();

//        HashMap<String, List<String>> employees = new HashMap<>();
//        employees.put("A", Arrays.asList("Andreas", "Arnold", "Aden"));
//        employees.put("C", Arrays.asList("Christian", "Carter"));
//        employees.put("M", Arrays.asList("Marcus", "Mary"));
//        String employeeJson = gson.toJson(employees);
        System.out.println(gson.toJson(t));
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
        QueryReportContainerList queryList = new QueryReportContainerList();
        queryList.list = new ArrayList<QueryReportContainer>();
        t._data = queryList;
        t._data.list.add(container);
        Gson gson = new GsonBuilder().setDateFormat("yyyyMMddHHmm").create();
        System.out.println(gson.toJson(t));
    }
}