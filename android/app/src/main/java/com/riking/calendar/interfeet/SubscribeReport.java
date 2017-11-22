package com.riking.calendar.interfeet;

import com.riking.calendar.pojo.server.ReportFrequency;

/**
 * Created by zw.zhang on 2017/11/20.
 */

public interface SubscribeReport {
    void orderReport(ReportFrequency report);

    void unorderReport(ReportFrequency report);

    boolean isAddedToMyOrder(ReportFrequency report);
     boolean isInEditMode();
}
