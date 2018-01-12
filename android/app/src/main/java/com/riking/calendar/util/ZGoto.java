package com.riking.calendar.util;

import android.app.Activity;
import android.content.Intent;

import com.riking.calendar.activity.AnswerCommentsActivity;
import com.riking.calendar.activity.LoginNavigateActivity;
import com.riking.calendar.app.MyApplication;

/**
 * Created by zw.zhang on 2017/11/21.
 */

public class ZGoto {

    public static void goToAnswerComments(String questionAnswerId) {
        Intent i = new Intent(MyApplication.mCurrentActivity, AnswerCommentsActivity.class);
        i.putExtra(CONST.ANSWER_ID, questionAnswerId);
//        i.putExtra(CONST.ANSWER_COMMENT_NUM, answer.commentNum);
        ZGoto.to(i);
    }

    public static void toLoginActivity() {
        Intent i = new Intent(MyApplication.mCurrentActivity, LoginNavigateActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        MyApplication.mCurrentActivity.startActivity(i);
    }

    /**
     * not check login status
     *
     * @param i
     */
    public static void to(Intent i) {
        MyApplication.mCurrentActivity.startActivity(i);
    }

    public static <T extends Activity> void toWithLoginCheck(Class<T> t) {
        //login check
        if (ZPreference.pref.getBoolean(CONST.IS_LOGIN, false)) {
            Activity a = MyApplication.mCurrentActivity;
            a.startActivity(new Intent(a, t));
        } else {
            Intent i = new Intent(MyApplication.mCurrentActivity, LoginNavigateActivity.class);
            ZR.jumpClass = t.getName();
            MyApplication.mCurrentActivity.startActivity(i);
        }

    }

    public static <T extends Activity> void to(Class<T> t) {
        Intent i = new Intent(MyApplication.mCurrentActivity, t);
        MyApplication.mCurrentActivity.startActivity(i);
    }
}
