package com.riking.calendar.activity;

import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.riking.calendar.R;
import com.riking.calendar.adapter.LocalSearchConditionAdapter;
import com.riking.calendar.adapter.RecommededSearchConditionsAdapter;
import com.riking.calendar.fragment.ExcellentAnswererFragment;
import com.riking.calendar.fragment.HotAnswerOfTopicFragment;
import com.riking.calendar.fragment.QuestionsFragment;
import com.riking.calendar.interfeet.PerformSearch;
import com.riking.calendar.realm.model.SearchConditions;
import com.riking.calendar.util.CONST;
import com.riking.calendar.util.Preference;
import com.riking.calendar.util.ZDB;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;
import io.realm.Sort;

/**
 * Created by zw.zhang on 2017/7/11.
 */

public class SearchActivity extends AppCompatActivity implements PerformSearch {

    //viewpager
    private final Fragment[] TAB_FRAGMENTS = new Fragment[]{new QuestionsFragment(), new HotAnswerOfTopicFragment(), new QuestionsFragment(), new ExcellentAnswererFragment()};
    public String reportSearchCondition;
    View localSearchTitle;
    LocalSearchConditionAdapter localSearchConditionAdapter;
    EditText editText;
    RecyclerView recyclerView;
    RecyclerView recommendedRecyclerView;
    ImageView clearSearchInputImage;
    LinearLayout localLinearLayout;
    private ViewPager mViewPager;
    private MyPagerAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        init();
    }

    void init() {
        initViews();
        initEvents();
    }

    public void clickClearLocalSearchConditions(final View v) {
        ZDB.Instance.getRealm().executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.where(SearchConditions.class).findAll().deleteAllFromRealm();
            }
        });
    }

    public void clickCancel(View view) {
        onBackPressed();
    }

    private void initEvents() {
        mAdapter = new MyPagerAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(mAdapter);//给ViewPager设置适配器
        setRecyclerView();
        setRecommendedRecyclerView();
        clearSearchInputImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editText.setText("");
            }
        });
    }

    private void setRecyclerView() {
        //set layout manager for the recycler view.
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        //set adapters
        RealmResults<SearchConditions> realmResults = ZDB.Instance.getRealm().where(SearchConditions.class).findAllSorted("updateTime", Sort.DESCENDING);
        localSearchConditionAdapter = new LocalSearchConditionAdapter(this, realmResults);
        //register realm change listener
        ZDB.Instance.getRealm().addChangeListener(new RealmChangeListener<Realm>() {
            @Override
            public void onChange(Realm realm) {
                localSearchConditionAdapter.notifyDataSetChanged();
            }
        });
        recyclerView.setAdapter(localSearchConditionAdapter);
    }

    private void setRecommendedRecyclerView() {
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        recommendedRecyclerView.setLayoutManager(layoutManager);
        List<String> list = new ArrayList<>();
        list.add("ldjfkl");
        list.add("ldjfkl");
        list.add("ldjfkl");
        list.add("ldjfkl");
        list.add("ldjfkl");
        RecommededSearchConditionsAdapter adapter = new RecommededSearchConditionsAdapter(this, list);
        recommendedRecyclerView.setAdapter(adapter);
    }

    private void initViews() {
        mViewPager = (ViewPager) findViewById(R.id.pager);
        final TabLayout tabLayout = (TabLayout) findViewById(R.id.top_tab_layout);
        tabLayout.setupWithViewPager(mViewPager);
        localLinearLayout = findViewById(R.id.local_search_linear_layout);
        recommendedRecyclerView = findViewById(R.id.recommend_search_conditions);
        recyclerView = findViewById(R.id.recycler_view);
        clearSearchInputImage = findViewById(R.id.cancel_search_button);
        localSearchTitle = findViewById(R.id.local_search_title);
        editText = findViewById(R.id.search_edit_view);
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 0) {
                    reportSearchCondition = s.toString();
                    performSearch();
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            saveToRealm();
                        }
                    }, 1000);
                    clearSearchInputImage.setVisibility(View.VISIBLE);
                    localLinearLayout.setVisibility(View.GONE);
                    tabLayout.setVisibility(View.VISIBLE);
                    mViewPager.setVisibility(View.VISIBLE);
                } else {
                    clearSearchInputImage.setVisibility(View.GONE);
                }
            }
        });
    }

    public void performSearch() {
        HashMap<String, String> reportName = new LinkedHashMap<>(1);
        reportName.put("reportName", reportSearchCondition);
        reportName.put("userId", Preference.pref.getString(CONST.USER_ID, ""));
      /*  APIClient.getReportByName(reportName, new ZCallBack<ResponseModel<List<ReportFrequency>>>() {
            @Override
            public void callBack(ResponseModel<List<ReportFrequency>> response) {
                reportsOrderAdapter.mList = response._data;
                recyclerView.setAdapter(reportsOrderAdapter);
            }
        });*/
    }

    @Override
    public void performSearchByLocalHistory(String searchCondition) {
        reportSearchCondition = searchCondition;
        performSearch();
        saveToRealm();
    }

    public void localSearchConditionIsEmpty() {
    }

    public void saveToRealm() {
        if (reportSearchCondition.trim().length() == 0) return;
        ZDB.Instance.getRealm().executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                SearchConditions s = new SearchConditions();
                s.name = reportSearchCondition.trim();
                s.updateTime = new Date();
                SearchConditions c = realm.copyToRealmOrUpdate(s);
            }
        });
    }

    //ViewPager适配器
    class MyPagerAdapter extends FragmentPagerAdapter {

        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            return TAB_FRAGMENTS.length;//页卡数
        }

        @Override
        public Fragment getItem(int position) {
            Log.d("zzw", "getItem: " + position);
            return TAB_FRAGMENTS[position];
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            Log.d("zzw", "instantiateItem: " + position);

            return super.instantiateItem(container, position);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "报表";
                case 1:
                    return "话题";
                case 2:
                    return "人脉";
                case 3:
                    return "资讯";
            }

            return null;
        }
    }
}
