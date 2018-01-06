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

    public static String getReminderTimeShowString(String date, String time) {
        SimpleDateFormat sdf = new SimpleDateFormat(CONST.yyyy_mm_dd_hh_mm);
        return sdf.format(get(date, time));
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
        if (time == null) {
            return null;
        }
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

    public static String showTime(Date date) {
        return DateUtil.showTime(date, CONST.yyyy_mm_dd_hh_mm);
    }

    /**
     * 显示时间，如果与当前时间差别小于一天，则自动用**秒(分，小时)前，如果大于一天则用format规定的格式显示
     *
     * @param ctime  时间
     * @param format 格式 格式描述:例如:yyyy-MM-dd yyyy-MM-dd HH:mm:ss
     * @return
     * @author wxy
     */
    public static String showTime(Date ctime, String format) {
        //System.out.println("当前时间是："+new Timestamp(System.currentTimeMillis()));


        //System.out.println("发布时间是："+df.format(ctime).toString());
        String r = "";
        if (ctime == null) return r;
        if (format == null) format = "MM-dd HH:mm";

        long nowtimelong = System.currentTimeMillis();

        long ctimelong = ctime.getTime();
        long result = Math.abs(nowtimelong - ctimelong);

        if (result < 60000) {// 一分钟内
            r = "刚刚";
        } else if (result >= 60000 && result < 3600000) {// 一小时内
            long seconds = result / 60000;
            r = seconds + "分钟前";
        } else if (result >= 3600000 && result < 86400000) {// 一天内
            long seconds = result / 3600000;
            r = seconds + "小时前";
        } else if (result >= 86400000 && result < 259200000) {// 三天内
            long seconds = result / 86400000;
            r = seconds + "天前";
        } else {// 日期格式
            format = "yyyy-MM-dd HH:mm";
            SimpleDateFormat df = new SimpleDateFormat(format);
            r = df.format(ctime).toString();
        }
        return r;
    }
}