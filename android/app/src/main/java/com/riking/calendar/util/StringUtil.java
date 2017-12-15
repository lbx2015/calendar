package com.riking.calendar.util;

import android.text.TextUtils;

/**
 * Created by zw.zhang on 2017/8/25.
 */

public class StringUtil {
    /**
     * 判断手机格式是否正确
     *
     * @param mobiles
     * @return 移动：134、135、136、137、138、139、150、151、157(TD)、158、159、187、188
     * 联通：130、131、132、152、155、156、185、186
     * 电信：133、153、180、189、（1349卫通）
     * 总结起来就是第一位必定为1，第二位必定为3或5或8，其他位置的可以为0-9
     */
    public static boolean isMobileNO(String mobiles) {
        String phone = mobiles.replaceAll("[^\\d]", "");
        //"[1]"代表第1位为数字1，"[358]"代表第二位可以为3、5、8中的一个，"\\d{9}"代表后面是可以是0～9的数字，有9位。
        String telRegex = "[1][34578]\\d{9}";
        if (TextUtils.isEmpty(phone)) return false;
        else return phone.trim().matches(telRegex);
    }

    public static String getPhoneNumber(String mobiles) {
        return mobiles.replaceAll("[^\\d]", "");
    }

    public static boolean isMobileNO(CharSequence mobiles) {
        if (mobiles == null) {
            return false;
        }
        return isMobileNO(mobiles.toString());
    }

    public static boolean isEmpty(String s) {
        if (s == null) {
            return true;
        }
        String str = s.trim();

        if (str.equals("") || str.length() == 0) {
            return true;
        }
        return false;

    }

    public static boolean isHttpUrl(String s){
     return s.startsWith("http");
    }
}
