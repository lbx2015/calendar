package com.riking.calendar.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by zw.zhang on 2017/8/3.
 */

public class DateUtil {
    /**
     * @param date yyyyMMdd
     * @param time HHmm
     * @return
     */
    public static Date get(String date, String time) {
        Date dateTime = null;
        try {
            dateTime = new SimpleDateFormat("yyyyMMddHHmm").parse(date + time);
        } catch (ParseException e) {
            Debug.Handle(e);
        } finally {
            return dateTime;
        }

    }
}
