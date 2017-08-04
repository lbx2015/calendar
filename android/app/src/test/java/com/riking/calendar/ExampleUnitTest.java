package com.riking.calendar;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.riking.calendar.realm.model.Task;

import org.junit.Test;

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
        t.createTime = new Date();
//        Gson gson = new Gson();
        Gson gson = new GsonBuilder().setDateFormat("yyyyMMddHHmm").create();
        ;
        System.out.print(gson.toJson(t));
    }
}