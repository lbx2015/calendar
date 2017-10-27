package com.riking.calendar.activity;

import android.content.SharedPreferences;
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
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.riking.calendar.R;
import com.riking.calendar.fragment.CreateReminderFragment;
import com.riking.calendar.fragment.FinanceNewsFragment;
import com.riking.calendar.fragment.HomeFragment;
import com.riking.calendar.fragment.TopicFragment;
import com.riking.calendar.realm.model.Reminder;
import com.riking.calendar.realm.model.Task;
import com.riking.calendar.retrofit.APIClient;
import com.riking.calendar.retrofit.APIInterface;
import com.riking.calendar.util.CONST;

import java.text.SimpleDateFormat;
import java.util.Date;

import io.realm.Realm;

/**
 * Created by zw.zhang on 2017/7/24.
 */

public class TopicActivity extends AppCompatActivity { //Fragment 数组
    private final Fragment[] TAB_FRAGMENTS = new Fragment[]{new TopicFragment(), new TopicFragment()};
    private ViewPager mViewPager;
    private MyPagerAdapter mAdapter;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        Log.d("zzw", this + "on create");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.topic_activity);
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mAdapter = new MyPagerAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(mAdapter);//给ViewPager设置适配器
        TabLayout tabLayout = (TabLayout) findViewById(R.id.top_tab_layout);
        tabLayout.setupWithViewPager(mViewPager);
    }



    //ViewPager适配器
    class MyPagerAdapter extends FragmentPagerAdapter {

        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            return 2;//页卡数
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
                    return "话题动态";
                case 1:
                    return "行业资讯";
            }

            return null;
        }

    }

}
