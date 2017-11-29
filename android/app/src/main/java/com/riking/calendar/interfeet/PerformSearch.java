package com.riking.calendar.interfeet;

import com.riking.calendar.pojo.server.ReportFrequency;

/**
 * Created by zw.zhang on 2017/11/20.
 */

public interface PerformSearch {
    void performSearchByLocalHistory(String searchCondition);
    void localSearchConditionIsEmpty();

}
