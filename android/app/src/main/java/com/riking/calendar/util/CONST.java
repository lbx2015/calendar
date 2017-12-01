package com.riking.calendar.util;

/**
 * Created by zw.zhang on 2017/8/7.
 */

public class CONST {
    public static final int CBRC = 0;
    public static final int PBOC = 1;
    public static final byte REPEAT_FLAG_WEEK = 3;
    public static final byte REPEAT_FLAG_WORK_DAY = 1;
    public static final byte REPEAT_FLAG_HOLIDAY = 2;
    public static final byte NOT_REPEAT_FLAG_WEEK = 0;
    public static final String URL_BASE = "http://172.16.64.190:8281/";
    //    public static final String URL_BASE = "http://172.16.32.14:6061/tl-api/";
    public static final String DEFAUT_REALM_DATABASE_NAME = "rkdb";
    public static final byte DELETE = 1;
    public static final byte ADD = 2;
    public static final byte UPDATE = 3;
    public final static int TOTAL_COL = 7;
    public final static int TOTAL_ROW = 6;
    public final static int CURRENT_PAGER_INDEX = 1000;
    public final static String BASE_URL = "http://172.16.64.96:8281/";
    public final static String PREFERENCE_FILE_NAME = "saveInfo";
    // All Shared Preferences Keys
    public static final String IS_LOGIN = "IsLoggedIn";
    public static final String NEED_WELCOME_ACTIVITY = "NEED_WELCOME_ACTIVITY";
    public static final String WHOLE_DAY_EVENT_HOUR = "WHOLE_DAY_EVENT_HOUR";
    public static final String WHOLE_DAY_EVENT_MINUTE = "WHOLE_DAY_EVENT_MINUTE";
    public static final String USER_IMAGE_URL = "USER_IMAGE_URL";
    public static final String USER_ID = "UserId";
    public static final String PHONE_NUMBER = "PhoneNumber";
    public static final String PHONE_SEQ_NUMBER = "phoneSeqNum";
    public static final String USER_NAME = "phoneSeqNum";
    public static final String USER_COMMENTS = "USER_COMMENTS";
    public static final String USER_EMAIL = "USER_EMAIL";
    public static final String USER_PASSWORD = "USER_PASSWORD";
    public static final String USER_DEPT = "USER_DEPT";
    public static final String USER_ADDRESS = "USER_ADDRESS";
    public static final String USER_BIRTHDAY = "USER_BIRTHDAY";
    public static final String USER_SEX = "user_sex";
    //image path external storage
    public static final String IMAGE_PATH = "/rikingImage";
    public static final String REMINDER_TITLE = "Reminder title";
    //Date formats
    public static final String yyyyMMddHHmm = "yyyyMMddHHmm";
    public static final String yyyyMMdd = "yyyyMMdd";
    public static final String birthDayFormat = "yyyy-MM-dd";
    public static final String WEB_URL = "WEB_URL";
    public static final String IMAGE_URL = "IMAGE_URL";
    public static final String INDUSTRY_ID = "INDUSTRY_ID";
    public static final String POSITION_ID = "POSITION_ID";
    public static final String ORDER_REPORTS = "ORDER_REPORTS";
    public static final String DIS_ORDER_REPORTS = "DIS_ORDER_REPORTS";
    public static final String ORDER_REPORTS_CHANGED = "ORDER_REPORTS_CHANGED";
    public  static  final int REQUEST_CODE = 1;
    public static final String EDIT_MODE = "EDIT_MODE";

    //滚动事件
    public enum ScrollDirection {
        UP,
        DOWN,
        SAME
    }
}
