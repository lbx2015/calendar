package com.riking.calendar;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.riking.calendar.pojo.AppUser;
import com.riking.calendar.pojo.QueryReport;
import com.riking.calendar.pojo.QueryReportContainer;
import com.riking.calendar.pojo.base.ResponseModel;
import com.riking.calendar.pojo.server.Industry;
import com.riking.calendar.util.Debug;

import org.joda.time.DateTime;
import org.junit.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;

import static org.junit.Assert.assertEquals;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    String repeatWeekReminds;

    @Test
    public void testTimeFormat() throws Exception {
        String format = "yyyyMMddhhmmssSSS";
        SimpleDateFormat s = new SimpleDateFormat(format);
        Date d = new Date();
        System.out.println(s.format(d));
    }

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
        user.userId = "ddddd";
        dd._data = user;
        System.out.println(new Gson().toJson(dd));
    }

    @Test
    public void testDateFormatParse() {
        String day = "20141210";
        String time = "0601";
        try {
            Date reminderTime = new SimpleDateFormat("yyyyMMddHHmm").parse(day + time);
            System.out.print(reminderTime);
        } catch (ParseException e) {
            Debug.Handle(e);
        }
    }

    @Test
    public void testStringParse() {
        String time = "06:01";
        System.out.print(time.replace(":", ""));
    }

    @org.junit.Test
    public void testGsonString() {
        String id = "adsfjdl";
        JsonObject j = new JsonObject();
        j.addProperty("userId", id);
        AppUser a = new AppUser();
        System.out.println(new Gson().toJson(j));
        System.out.println(new Gson().toJson(a));

    }

    @Test
    public void jsonReportTest() throws Exception {
        ArrayList<QueryReportContainer> list = new ArrayList<>();
        QueryReportContainer container = new QueryReportContainer();
        container.title = "reportTitle";
        QueryReport report = new QueryReport();
        report.reportName = "test";
        container.result = new ArrayList<QueryReport>();
        container.result.add(report);
        Gson gson = new GsonBuilder().setDateFormat("yyyyMMddHHmm").create();
        System.out.println(gson.toJson(list));
    }

    @Test
    public void testDateFormat() {
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-M-d");
        String currentDate = sdf.format(date); // 当期日期
        int year_c = Integer.parseInt(currentDate.split("-")[0]);
        int month_c = Integer.parseInt(currentDate.split("-")[1]);
        int day_c = Integer.parseInt(currentDate.split("-")[2]);
        System.out.println("currentDate: " + currentDate + " year_c:" + year_c + "month_c:" + month_c + "day_c:" + day_c);
    }

    @Test
    public void testMode() {
        System.out.print(6 % 7);
    }

    @Test
    public void testCharacter() {
        ArrayList<String> weekRepeatReminders = new ArrayList<>();
        weekRepeatReminders.add("123");
        weekRepeatReminders.add("012");
        HashSet<Character> weeks = new HashSet<>();
        for (String r : weekRepeatReminders) {
            for (char ch : r.toCharArray()) {
                weeks.add(ch);
            }
        }

        for (char ch : weeks) {
            repeatWeekReminds += ch;
        }
        System.out.println(repeatWeekReminds);
    }

    @Test
    public void appendingZero() {
        /*
        0 - to pad with zeros
3 - to set width to 3
         */
        System.out.println(String.format("%02d", 1));
    }

    @Test
    public void testTime() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, 2017);
        cal.set(Calendar.MONTH, 8);
        cal.set(Calendar.DATE, 4);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MINUTE, 0);
        System.out.print(cal.getTime());
    }

    @Test
    public void testSparseArray() {
        HashMap<String, String> s = new HashMap<>();
        String week = "1234";
        for (char i : week.toCharArray()) {
            s.put(String.valueOf(i), "a" + i);
        }
        System.out.println(s.get(String.valueOf(2)));
    }

    @Test
    public void testBinarySmartOperator() {
        System.out.println("1^0: " + (1 ^ 0));
        System.out.println("0^1: " + (0 ^ 1));
        System.out.println("1^1: " + (1 ^ 1));
        System.out.println("0^0: " + (0 ^ 0));
    }

    @Test
    public void testRemoveNonNumber() {
        String s = "albl2.df234:d34";
        String numberRefined = s.replaceAll("[^0-9]", "");
//        String numberRefined = s.replaceAll("[^\\d]", "");
        //223434
        System.out.println(numberRefined);
    }

    @Test
    public void testDateTime() {
        System.out.println(new DateTime());
        System.out.println(DateTime.now().withTimeAtStartOfDay());
        System.out.print(new DateTime(new DateTime().toLocalDate().toString()));
    }

    @Test
    public void testNumberMod() {
        for (int i = 0; i < 100; i++) {
            System.out.println(i % 3);
        }
    }

    @Test
    public void testClass() {
        System.out.println(Industry.class.getSimpleName());
        System.out.print(Industry.class.getName());
    }
}