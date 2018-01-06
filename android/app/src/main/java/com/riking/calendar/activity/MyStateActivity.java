package com.riking.calendar.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.riking.calendar.R;
import com.riking.calendar.fragment.AnswerCommentsFragment;
import com.riking.calendar.fragment.MyAnswersFragment;
import com.riking.calendar.fragment.MyDynamicQuestionFragment;
import com.riking.calendar.util.CONST;
import com.riking.calendar.util.ZPreference;

/**
 * Created by zw.zhang on 2017/7/24.
 * answer comments page
 */

public class MyStateActivity extends AppCompatActivity { //Fragment 数组
    //viewpager
    private final Fragment[] TAB_FRAGMENTS = new Fragment[]{AnswerCommentsFragment.newInstance(this), MyAnswersFragment.newInstance(this), MyDynamicQuestionFragment.newInstance(this)};
    public String userId;
    TabLayout tabLayout;
    private ViewPager mViewPager;
    private MyPagerAdapter mAdapter;
    private TextView activityTitle;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        Log.d("zzw", this + "on create");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_state);

        Intent i = getIntent();
        userId = i.getStringExtra(CONST.USER_ID);
        init();

        if (ZPreference.getUserId().equals(userId)) {
            activityTitle.setText("我的动态");
        } else {
            if (i.getIntExtra(CONST.USER_SEX, 0) == 0) {
                activityTitle.setText("她的动态");
            } else {
                activityTitle.setText("他的动态");
            }
        }
    }

    private void init() {
        initViews();
        initEvents();
    }

    private void initViews() {
        activityTitle = findViewById(R.id.activity_title);
        mViewPager = (ViewPager) findViewById(R.id.pager);
        tabLayout = (TabLayout) findViewById(R.id.top_tab_layout);
        tabLayout.setupWithViewPager(mViewPager);
    }

    private void initEvents() {
        mAdapter = new MyPagerAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(mAdapter);//给ViewPager设置适配器
    }

    private void loadData(final int page) {
    }

    public void clickBack(final View view) {
        onBackPressed();
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
                    return "评论";
                case 1:
                    return "回答";
                case 2:
                    return "提问";
            }

            return null;
        }
    }
}
