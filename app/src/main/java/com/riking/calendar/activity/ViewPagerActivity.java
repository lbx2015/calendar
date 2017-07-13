package com.riking.calendar.activity;

import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.flyco.tablayout.CommonTabLayout;
import com.flyco.tablayout.listener.CustomTabEntity;
import com.riking.calendar.R;
import com.riking.calendar.fragment.FirstFragment;
import com.riking.calendar.fragment.SecondFragment;
import com.riking.calendar.fragment.ThirdFragment;
import com.riking.calendar.pojo.TabEntity;
import com.riking.calendar.util.ViewFindUtils;

import java.util.ArrayList;

/**
 * Created by zw.zhang on 2017/7/11.
 */

public class ViewPagerActivity extends FragmentActivity {
    private String[] mTitles = {"工作台", "节假日", "提醒", "个人中心"};
    private ArrayList<CustomTabEntity> mTabEntities = new ArrayList<>();
    private View mDecorView;
    private CommonTabLayout mTabLayout_8;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viewpager);
        ViewPager pager = (ViewPager) findViewById(R.id.viewPager);
        pager.setAdapter(new MyPagerAdapter(getSupportFragmentManager()));

        for (int i = 0; i < mTitles.length; i++) {
            mTabEntities.add(new TabEntity(mTitles[i], 0, 0));
        }

        mDecorView = getWindow().getDecorView();
        /** indicator圆角色块 */
        mTabLayout_8 = ViewFindUtils.find(mDecorView, R.id.tl_8);
        mTabLayout_8.setTabData(mTabEntities);
        mTabLayout_8.setCurrentTab(2);


        Window window = getWindow();

// clear FLAG_TRANSLUCENT_STATUS flag:
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

// add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

// finally change the color
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.colorPrimaryDark));
        }
    }

    private class MyPagerAdapter extends FragmentPagerAdapter {

        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int pos) {
            switch (pos) {

                case 0:
                    return new FirstFragment();
                case 1:
                    return SecondFragment.newInstance("SecondFragment, Instance 1");
                case 2:
                    return ThirdFragment.newInstance("ThirdFragment, Instance 1");
                case 3:
                    return ThirdFragment.newInstance("ThirdFragment, Instance 2");
                default:
                    return ThirdFragment.newInstance("ThirdFragment, Default");
            }
        }

        @Override
        public int getCount() {
            return 4;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return "Page" + position;
        }
    }
}
