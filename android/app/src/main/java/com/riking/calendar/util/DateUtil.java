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
     * return ture if date1 is before date2
     * The compare will ignore the reminderTimeCalendar information.
     *
     * @param date1
     * @param date2
     * @return
     */
    public static boolean before(Date date1, Date date2) {
        SimpleDateFormat s = new SimpleDateFormat(CONST.yyyyMMdd);
        return s.format(date1).compareTo(s.format(date2)) <= 0;
    }

    /**
     * @param weekflag 传入的是周几
     * @param dateTime 传入的是时间戳（设置当天的年月日+从选择框拿来的时分秒）
     * @return 返回起始闹钟时间的时间戳
     */
    public static long getRepeatReminderTime(int weekflag, long dateTime) {
        long time = 0;
        //weekflag == 0表示是按天为周期性的时间间隔或者是一次行的，weekfalg非0时表示每周几的闹钟并以周为时间间隔
        if (weekflag != 0) {
            Calendar c = Calendar.getInstance();
            int week = c.get(Calendar.DAY_OF_WEEK);
            if (1 == week) {
                week = 7;
            } else if (2 == week) {
                week = 1;
            } else if (3 == week) {
                week = 2;
            } else if (4 == week) {
                week = 3;
            } else if (5 == week) {
                week = 4;
            } else if (6 == week) {
                week = 5;
            } else if (7 == week) {
                week = 6;
            }

            if (weekflag == week) {
                if (dateTime > System.currentTimeMillis()) {
                    time = dateTime;
                } else {
                    time = dateTime + 7 * 24 * 3600 * 1000;
                }
            } else if (weekflag > week) {
                time = dateTime + (weekflag - week) * 24 * 3600 * 1000;
            } else if (weekflag < week) {
                time = dateTime + (weekflag - week + 7) * 24 * 3600 * 1000;
            }
        } else {
            if (dateTime > System.currentTimeMillis()) {
                time = dateTime;
            } else {
                time = dateTime + 24 * 3600 * 1000;
            }
        }
        return time;
    }

    /**
     * @param date yyyyMMdd
     * @param time HHmm
     * @return
     */
    public static Date get(String date, String time) {
        Date dateTime = null;
        try {
            String numberRefined = (date + time).replaceAll("[^\\d]", "");
            dateTime = new SimpleDateFormat("yyyyMMddHHmm").parse(numberRefined);
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

    public static String getCustonFormatTime(long timeInMillis, String s) {
        return new SimpleDateFormat(s).format(new Date(timeInMillis));
    }

    public static String date2String(Date time, String pattern) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);
        String reqTimeStamp;
        reqTimeStamp = dateFormat.format(time);
        return reqTimeStamp;
    }

    public static Date StringFormatMS(String time, String pattern) throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);
        Date reqTimeStamp;

        reqTimeStamp = dateFormat.parse(time);

        return reqTimeStamp;
    }
}