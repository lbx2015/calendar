package com.riking.calendar.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
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

    public static String getWeekNoOfYear(Calendar c) {
        return null;
    }

    /**
     * @param weekDay 1 present of monday and 7 present sunday
     * @return
     */
    public static String getWeekNameInChinese(int weekDay) {
        switch (weekDay) {
            case 1: {
                return "星期一";
            }
            case 2: {
                return "星期二";
            }
            case 3: {
                return "星期三";
            }
            case 4: {
                return "星期四";
            }
            case 5: {
                return "星期五";
            }
            case 6: {
                return "星期六";
            }
            case 7: {
                return "星期日";
            }
        }
        return null;
    }
}