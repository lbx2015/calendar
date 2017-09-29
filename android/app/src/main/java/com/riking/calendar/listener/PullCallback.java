package com.riking.calendar.listener;

public interface PullCallback {

    void onLoadMore();

    void onRefresh();

    boolean isLoading();

    boolean hasLoadedAllItems();

}
