package com.riking.calendar.pojo;

import java.util.ArrayList;

/**
 * Created by zw.zhang on 2017/8/10.
 */

public class HolidayMode {
    public ArrayList<ModelPropDict> hdayName;
    public ArrayList<ModelPropDict> crcy;
    public ArrayList<ModelPropDict> ctryName;

    @Override
    public String toString() {
        return "HolidayMode{" +
                "hdayNmae=" + hdayName +
                ", crcy=" + crcy +
                ", ctryName=" + ctryName +
                '}';
    }
}
