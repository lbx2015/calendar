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
import com.flyco.tablayout.listener.OnTabSelectListener;
import com.riking.calendar.R;
import com.riking.calendar.fragment.FirstFragment;
import com.riking.calendar.fragment.FourthFragment;
import com.riking.calendar.fragment.SecondFragment;
import com.riking.calendar.fragment.ThirdFragment;
import com.riking.calendar.pojo.TabEntity;
import com.riking.calendar.util.ViewFindUtils;

import java.util.ArrayList;

/**
 * Created by zw.zhang on 2017/7/11.
 */

public class ViewPagerActivity extends FragmentActivity {
    public CommonTabLayout bottomTabs;
    MyPagerAdapter adapter;
    private String[] mTitles;
    private ArrayList<CustomTabEntity> mTabEntities = new ArrayList<>();
    private View mDecorView;
    private int[] mIconUnselectIds = {
            R.drawable.work_page_unselected, R.drawable.holiday_page_unselected,
            R.drawable.remind_page_unselected, R.drawable.me_page_unselected};
    private int[] mIconSelectIds = {
            R.drawable.work_page_selected, R.drawable.holiday_page_selected,
            R.drawable.remind_page_selected, R.drawable.me_page_selected};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viewpager);
        final ViewPager pager = (ViewPager) findViewById(R.id.viewPager);
        adapter = new MyPagerAdapter(getSupportFragmentManager());
        pager.setAdapter(adapter);
        pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                bottomTabs.setCurrentTab(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        mTitles = getResources().getStringArray(R.array.subTittles);

        for (int i = 0; i < mTitles.length; i++) {
            mTabEntities.add(new TabEntity(mTitles[i], mIconSelectIds[i], mIconUnselectIds[i]));
        }

        mDecorView = getWindow().getDecorView();
        /** indicator圆角色块 */
        bottomTabs = ViewFindUtils.find(mDecorView, R.id.tl_3);
        bottomTabs.setTabData(mTabEntities);
        bottomTabs.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelect(int position) {
                pager.setCurrentItem(position);
            }

            @Override
            public void onTabReselect(int position) {

            }
        });


        Window window = getWindow();

// clear FLAG_TRANSLUCENT_STATUS flag:
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

// add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

// finally change the color
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.colorPrimary));
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
                    return new SecondFragment();
                case 2:
                    return new ThirdFragment();
                case 3:
                    return new FourthFragment();
                default:
                    return new ThirdFragment();
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
