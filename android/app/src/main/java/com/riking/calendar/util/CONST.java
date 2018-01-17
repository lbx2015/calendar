package com.riking.calendar.util;

/**
 * Created by zw.zhang on 2017/8/7.
 */

public class CONST {
    public static final byte REPEAT_FLAG_WEEK = 3;
    public static final byte REPEAT_FLAG_WORK_DAY = 1;
    public static final byte REPEAT_FLAG_HOLIDAY = 2;
    public static final byte NOT_REPEAT_FLAG_WEEK = 0;
//    public static final String TL_API_TEST = "http://172.16.64.177:8281/";
//        public static final String TL_API_TEST = "http://172.16.64.96:8281/";
    //    public static final String TL_API_TEST = "http://172.16.64.199:8281/";
//        public static final String TL_API_TEST = "http://172.16.64.177:6061/tl-api-test/";
    public static final String TL_API_TEST = "http://118.31.187.217:6061/tl-api-test/";
    //        public static final String TL_API_TEST = "http://172.16.64.96:8281/";
    public static final String TL_API_DEV = "http://172.16.64.190:8281/";
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
    public static final String EMAIL_VALIDATE = "EMAIL_VALIDATE";
    public static final String USER_EMAIL = "USER_EMAIL";
    public static final String USER_PASSWORD = "USER_PASSWORD";
    public static final String USER_DEPT = "USER_DEPT";
    public static final String USER_ADDRESS = "USER_ADDRESS";
    public static final String USER_BIRTHDAY = "USER_BIRTHDAY";
    public static final String USER_SEX = "user_sex";
    //image path external storage
    public static final String IMAGE_PATH = "/rikingImage";
    public static final String REMINDER_TITLE = "Reminder title";
    public static final String REMINDER_ID = "REMIND_ID";
    //Date formats
    public static final String yyyyMMddHHmm = "yyyyMMddHHmm";
    public static final String yyyy_mm_dd_hh_mm = "yyyy-MM-dd HH:mm";
    public static final String YYYYMMDDHHMMSSSSS = "yyyyMMddHHmmssSSS";
    public static final String yyyyMMdd = "yyyyMMdd";
    public static final String birthDayFormat = "yyyy-MM-dd";
    public static final String WEB_URL = "WEB_URL";
    public static final String IMAGE_URL = "IMAGE_URL";
    public static final String INDUSTRY_ID = "INDUSTRY_ID";
    public static final String POSITION_ID = "POSITION_ID";
    public static final String ORDER_REPORTS = "ORDER_REPORTS";
    public static final String DIS_ORDER_REPORTS = "DIS_ORDER_REPORTS";
    public static final String ORDER_REPORTS_CHANGED = "ORDER_REPORTS_CHANGED";
    public static final int REQUEST_CODE = 1;
    public static final int REQUEST_CODE_ADD_REMINDER = 1;
    public static final String EDIT_MODE = "EDIT_MODE";
    public static final String USER_REAL_NAME = "USER_REAL_NAME";
    //The whole information of logining user in gson string
    public static final String CURRENT_LOGIN_USER = "CURRENT_LOGIN_USER";
    public static final String NEWS_ID = "NEWS_ID";
    public static final String CHECK_NOT_LOGIN_ON_CLICK = "CHECK_NOT_LOGIN_ON_CLICK";
    public static final String QUESTION_ID = "QUESTION_ID";
    public static final String TOPIC_ID = "TOPIC_ID";
    public static final String ANSWER_ID = "ANSWER_ID";
    public static final String MY_FOLLOW = "MY_FOLLOW";
    public static final String ANSWER_COMMENT_NUM = "ANSWER_COMMENT_NUM";
    public static final String COMMENT_NUM = "COMMENT_NUM";
    public static final String SEARCH_CONDITION = "SEARCH_CONDITION";

    public static final int UPDATE_USER_INFO_REQUES = 1111;
    public static final int VERIFY_EMAIL = 1112;
    public static final String ACTIVITY_NAME = "ACTIVITY_NAME";
    public static final String QUESTION_TITLE = "QUESTION_TITLE";
    public static final String LOGINING = "LOGINING";


    /** 被邀请回答的邀请 */
    public static final int NOTICE_OPT_ANSWERINVITE = 1;

    /** 问题回答 */
    public static final int NOTICE_OPT_QUESTION_ANSWER = 2;

    /** 问题回答被点赞 */
    public static final int NOTICE_OPT_QA_AGREEOR = 3;

    /** 问题回答被收藏 */
    public static final int NOTICE_OPT_QA_COLLECT = 4;

    /** 问题被关注 */
    public static final int NOTICE_OPT_QUESTION_FOLLOW = 5;

    /** 被关注的用户 */
    public static final int NOTICE_OPT_USER_FOLLOW = 6;

    /** 问题回答的被评论 */
    public static final int NOTICE_OPT_QANSWER_COMMENT = 7;

    /**回答类   评论被点赞 */
    public static final int NOTICE_OPT_ANSWER_COMMENT_AGREE = 8;

    /**资讯类   评论被点赞 */
    public static final int NOTICE_OPT_NEWS_COMMENT_AGREE = 9;

    /**回答类   评论的回复和回复的被回复 */
    public static final int NOTICE_OPT_ANSWER_COMMENT_REPLY = 10;

    /**资讯类  评论的回复和回复的被回复 */
    public static final int NOTICE_OPT_NEWS_COMMENT_REPLY = 11;

    //滚动事件
    public enum ScrollDirection {
        UP,
        DOWN,
        SAME
    }
}
