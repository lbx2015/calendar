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
import com.riking.calendar.fragment.MyFollowTopicFragment;
import com.riking.calendar.fragment.MyFollowerPersonFragment;
import com.riking.calendar.fragment.MyFollowingQuestionFragment;
import com.riking.calendar.util.CONST;

public class MyFollowActivity extends AppCompatActivity { //Fragment 数组
    //viewpager
    private final Fragment[] TAB_FRAGMENTS = new Fragment[]{MyFollowerPersonFragment.newInstance(this), MyFollowTopicFragment.newInstance(this), MyFollowingQuestionFragment.newInstance(this)};
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
        activityTitle = findViewById(R.id.activity_title);
        activityTitle.setText("我的关注");
        Intent i = getIntent();
        userId = i.getStringExtra(CONST.USER_ID);
        init();
    }

    private void init() {
        initViews();
        initEvents();
    }

    private void initViews() {
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
                    return "用户";
                case 1:
                    return "话题";
                case 2:
                    return "问题";
            }
            return null;
        }
    }
}
