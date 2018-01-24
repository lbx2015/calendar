package com.riking.calendar.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.necer.ncalendar.view.SimpleDividerDecorationWithLastFillWidth;
import com.necer.ncalendar.view.SimpleDividerDecorationWithOnlyLastFillWidth;
import com.riking.calendar.R;
import com.riking.calendar.activity.SearchActivity;
import com.riking.calendar.adapter.SearchNewsAdapter;
import com.riking.calendar.adapter.SearchPersonAdapter;
import com.riking.calendar.adapter.SearchQuestionAdapter;
import com.riking.calendar.adapter.SearchReportsAdapter;
import com.riking.calendar.adapter.SearchTopicAdapter;
import com.riking.calendar.interfeet.PerformInputSearch;
import com.riking.calendar.interfeet.SubscribeReport;
import com.riking.calendar.listener.ZCallBack;
import com.riking.calendar.listener.ZCallBackWithFail;
import com.riking.calendar.pojo.base.ResponseModel;
import com.riking.calendar.pojo.params.SearchParams;
import com.riking.calendar.pojo.params.SubscribeReportParam;
import com.riking.calendar.pojo.server.ColligateSearchResult;
import com.riking.calendar.pojo.server.ReportResult;
import com.riking.calendar.retrofit.APIClient;
import com.riking.calendar.util.ZR;

/**
 * Created by zw.zhang on 2017/7/17.
 */

public class SearchColligateFragment extends Fragment implements SubscribeReport<ReportResult>, PerformInputSearch {
    View v;
    SearchReportsAdapter searchReportAdapter;
    SearchTopicAdapter searchTopicAdapter;
    SearchPersonAdapter searchPersonAdapter;
    SearchNewsAdapter searchNewsAdapter;
    SearchQuestionAdapter searchQuestionAdater;
    String searchCondition;
    RecyclerView reportRecyclerView;
    RecyclerView topicRecyclerView;
    RecyclerView relationRecyclerView;
    RecyclerView newsRecyclerView;
    RecyclerView questionRecyclerView;
    TextView reportTitleTv;
    TextView topicTitleTv;
    TextView relationTitleTv;
    TextView newsTitleTv;
    TextView questionTitleTv;

    View moreReport;
    View moreTopic;
    View moreRelation;
    View moreNews;

    SearchActivity a;

    public static SearchColligateFragment instance(SearchActivity a) {
        SearchColligateFragment f = new SearchColligateFragment();
        f.a = a;
        return f;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (v != null) return v;
        v = inflater.inflate(R.layout.search_colligate_fragment, container, false);
        init();
        return v;
    }

    private void init() {
        initViews();
        initEvents();
    }

    private void initViews() {
        reportRecyclerView = v.findViewById(R.id.report_recycler_view);
        topicRecyclerView = v.findViewById(R.id.topic_recycler_view);
        relationRecyclerView = v.findViewById(R.id.relation_recycler_view);
        newsRecyclerView = v.findViewById(R.id.news_recycler_view);
        questionRecyclerView = v.findViewById(R.id.question_recycler_view);

        reportTitleTv = v.findViewById(R.id.report_title_tv);
        topicTitleTv = v.findViewById(R.id.topic_title_tv);
        relationTitleTv = v.findViewById(R.id.relation_title_tv);
        newsTitleTv = v.findViewById(R.id.news_title_tv);
        questionTitleTv = v.findViewById(R.id.question_title_tv);

        moreReport = v.findViewById(R.id.more_report);
        moreTopic = v.findViewById(R.id.more_topic);
        moreRelation = v.findViewById(R.id.more_relation);
        moreNews = v.findViewById(R.id.more_news);
    }

    private void initEvents() {
        //report
        setRecyclerView(reportRecyclerView);
        searchReportAdapter = new SearchReportsAdapter(this);
        RecyclerView.AdapterDataObserver reportObserver = new RecyclerView.AdapterDataObserver() {
            @Override
            public void onChanged() {
                super.onChanged();
                if (searchReportAdapter.getItemCount() < 1) {
                    reportRecyclerView.setVisibility(View.GONE);
                    reportTitleTv.setVisibility(View.GONE);
                    moreReport.setVisibility(View.GONE);
                } else {
                    reportRecyclerView.setVisibility(View.VISIBLE);
                    reportTitleTv.setVisibility(View.VISIBLE);
                    moreReport.setVisibility(View.VISIBLE);
                }

            }
        };
        searchReportAdapter.registerAdapterDataObserver(reportObserver);
        reportRecyclerView.setAdapter(searchReportAdapter);
        reportObserver.onChanged();

        //topic
        setRecyclerViewWithLastDivider(topicRecyclerView);
        searchTopicAdapter = new SearchTopicAdapter();
        RecyclerView.AdapterDataObserver topicObserver = new RecyclerView.AdapterDataObserver() {
            @Override
            public void onChanged() {
                super.onChanged();
                if (searchTopicAdapter.getItemCount() < 1) {
                    topicRecyclerView.setVisibility(View.GONE);
                    topicTitleTv.setVisibility(View.GONE);
                    moreTopic.setVisibility(View.GONE);
                } else {
                    topicRecyclerView.setVisibility(View.VISIBLE);
                    topicTitleTv.setVisibility(View.VISIBLE);
                    moreTopic.setVisibility(View.VISIBLE);
                }

            }
        };
        searchTopicAdapter.registerAdapterDataObserver(topicObserver);
        topicRecyclerView.setAdapter(searchTopicAdapter);
        topicObserver.onChanged();

        //relation
        setRecyclerViewWithLastDivider(relationRecyclerView);
        searchPersonAdapter = new SearchPersonAdapter(getContext());

        RecyclerView.AdapterDataObserver personObserver = new RecyclerView.AdapterDataObserver() {
            @Override
            public void onChanged() {
                super.onChanged();
                if (searchPersonAdapter.getItemCount() < 1) {
                    relationRecyclerView.setVisibility(View.GONE);
                    relationTitleTv.setVisibility(View.GONE);
                    moreRelation.setVisibility(View.GONE);
                } else {
                    relationRecyclerView.setVisibility(View.VISIBLE);
                    relationTitleTv.setVisibility(View.VISIBLE);
                    moreRelation.setVisibility(View.VISIBLE);
                }

            }
        };
        searchPersonAdapter.registerAdapterDataObserver(personObserver);
        relationRecyclerView.setAdapter(searchPersonAdapter);
        personObserver.onChanged();

        //news
        setRecyclerView(newsRecyclerView);
        searchNewsAdapter = new SearchNewsAdapter(getContext());

        RecyclerView.AdapterDataObserver newsObserver = new RecyclerView.AdapterDataObserver() {
            @Override
            public void onChanged() {
                super.onChanged();
                if (searchNewsAdapter.getItemCount() < 1) {
                    newsRecyclerView.setVisibility(View.GONE);
                    newsTitleTv.setVisibility(View.GONE);
                    moreNews.setVisibility(View.GONE);
                } else {
                    newsRecyclerView.setVisibility(View.VISIBLE);
                    newsTitleTv.setVisibility(View.VISIBLE);
                    moreNews.setVisibility(View.VISIBLE);
                }

            }
        };
        searchNewsAdapter.registerAdapterDataObserver(newsObserver);
        newsRecyclerView.setAdapter(searchNewsAdapter);
        newsObserver.onChanged();


        //questions
        LinearLayoutManager manager = new LinearLayoutManager(getActivity(),
                LinearLayoutManager.VERTICAL, false);
        questionRecyclerView.setLayoutManager(manager);
        questionRecyclerView.setHasFixedSize(true);
        //adding dividers.
        questionRecyclerView.addItemDecoration(new SimpleDividerDecorationWithLastFillWidth(ZR.getDrawable(R.drawable.recycler_view_divider), (int) ZR.convertDpToPx(15)));
        searchQuestionAdater = new SearchQuestionAdapter();
        RecyclerView.AdapterDataObserver questionObserver = new RecyclerView.AdapterDataObserver() {
            @Override
            public void onChanged() {
                super.onChanged();
                if (searchQuestionAdater.getItemCount() < 1) {
                    questionRecyclerView.setVisibility(View.GONE);
                    questionTitleTv.setVisibility(View.GONE);
                } else {
                    questionRecyclerView.setVisibility(View.VISIBLE);
                    questionTitleTv.setVisibility(View.VISIBLE);
                }
            }
        };
        searchQuestionAdater.registerAdapterDataObserver(questionObserver);
        questionRecyclerView.setAdapter(searchQuestionAdater);
        questionObserver.onChanged();
        setOnClickListener();
    }

    private void setOnClickListener() {
        moreReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                a.mViewPager.setCurrentItem(1);
            }
        });
        moreTopic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                a.mViewPager.setCurrentItem(2);
            }
        });
        moreRelation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                a.mViewPager.setCurrentItem(3);
            }
        });
        moreNews.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                a.mViewPager.setCurrentItem(4);
            }
        });
    }

    private void setRecyclerView(RecyclerView mRecyclerView) {
        LinearLayoutManager manager = new LinearLayoutManager(getActivity(),
                LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(manager);
        mRecyclerView.setHasFixedSize(true);
        //adding dividers.
        mRecyclerView.addItemDecoration(new SimpleDividerDecorationWithLastFillWidth(ZR.getDrawable(R.drawable.recycler_view_divider)));
    }

    private void setRecyclerViewWithLastDivider(RecyclerView mRecyclerView) {
        LinearLayoutManager manager = new LinearLayoutManager(getActivity(),
                LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(manager);
        mRecyclerView.setHasFixedSize(true);
        //adding dividers.
        mRecyclerView.addItemDecoration(new SimpleDividerDecorationWithOnlyLastFillWidth(ZR.getDrawable(R.drawable.recycler_view_divider)));

    }

    private void loadData(final int page) {
/*        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mPullToLoadView.setComplete();
                if (page > 3) {
                    Toast.makeText(getContext(), "没有更多数据了",
                            Toast.LENGTH_SHORT).show();
                    isHasLoadedAll = true;
                    return;
                }
                List<ReportFrequency> data = new ArrayList<>();
                for (int i = 0; i <= 15; i++) {
                    ReportFrequency frequency = new ReportFrequency("dfldlfjdklsf", "fldsjfkld", "dklfdla", "dfjla", "djfladj");
                    data.add(frequency);
                }
                mAdapter.setData(data);
                isLoading = false;
                nextPage = page + 1;
            }
        }, 1000)*/
        search(searchCondition);
    }

    public void orderReport(ReportResult report) {
        Activity ac = getActivity();
        if (ac instanceof SearchActivity) {
            SearchActivity searchActivity = (SearchActivity) ac;
            searchActivity.saveToRealm();
        }
        SubscribeReportParam a = new SubscribeReportParam();
        a.reportIds = report.reportId;
        a.subscribeType = 1;
        APIClient.updateUserReportRelById(a, new ZCallBack<ResponseModel<String>>() {
            @Override
            public void callBack(ResponseModel<String> response) {

            }
        });
    }

    public void unorderReport(ReportResult report) {
        Activity ac = getActivity();
        if (ac instanceof SearchActivity) {
            SearchActivity searchActivity = (SearchActivity) ac;
            searchActivity.saveToRealm();
        }
        SubscribeReportParam a = new SubscribeReportParam();
        a.reportIds = report.reportId;
        a.subscribeType = 0;

        APIClient.updateUserReportRelById(a, new ZCallBack<ResponseModel<String>>() {
            @Override
            public void callBack(ResponseModel<String> response) {

            }
        });
    }

    @Override
    public boolean isAddedToMyOrder(ReportResult report) {
        return false;
    }

    @Override
    public boolean isInEditMode() {
        return true;
    }

    @Override
    public void search(String searchCondition) {
        this.searchCondition = searchCondition;
        if (TextUtils.isEmpty(searchCondition)) {
            return;
        }

        SearchParams params = new SearchParams();
        params.keyWord = searchCondition;
        //search reports
        params.objType = 1;
        APIClient.colligateSearch(params, new ZCallBackWithFail<ResponseModel<ColligateSearchResult>>() {
            @Override
            public void callBack(ResponseModel<ColligateSearchResult> response) throws Exception {
                if (failed) {

                } else {
                    ColligateSearchResult result = response._data;
                    if (result.reports.size() > 2) {
                        searchReportAdapter.setData(result.reports.subList(0, 2));
                        moreReport.setVisibility(View.VISIBLE);
                    } else {
                        searchReportAdapter.setData(result.reports);
                        moreReport.setVisibility(View.GONE);
                    }

                    if (result.topics.size() > 2) {
                        searchTopicAdapter.setData(result.topics.subList(0, 2));
                        moreTopic.setVisibility(View.VISIBLE);
                    } else {
                        searchTopicAdapter.setData(result.topics);
                        moreTopic.setVisibility(View.GONE);
                    }


                    if (result.users.size() > 2) {
                        searchPersonAdapter.setData(result.users.subList(0, 2));
                        moreRelation.setVisibility(View.VISIBLE);
                    } else {
                        searchPersonAdapter.setData(result.users);
                        moreRelation.setVisibility(View.GONE);
                    }


                    if (result.news.size() > 2) {
                        searchNewsAdapter.setData(result.news.subList(0, 2));
                        moreNews.setVisibility(View.VISIBLE);
                    } else {
                        searchNewsAdapter.setData(result.news);
                        moreNews.setVisibility(View.GONE);
                    }


                    searchQuestionAdater.setData(result.questions);
                }
            }
        });

    }
}
