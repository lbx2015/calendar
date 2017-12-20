package com.riking.calendar.interfeet;

/**
 * Created by zw.zhang on 2017/11/20.
 */

public interface SubscribeReport<T> {
    void orderReport(T report);

    void unorderReport(T report);

    boolean isAddedToMyOrder(T report);
     boolean isInEditMode();
}
