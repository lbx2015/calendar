package com.riking.calendar.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerTabStrip;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.riking.calendar.R;

/**
 * Created by zw.zhang on 2017/7/11.
 */

public class ThirdFragment extends Fragment {
    private ViewPager mViewPager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.third_fragment, container, false);

        mViewPager = (ViewPager) v.findViewById(R.id.pager);
        MyPagerAdapter mAdapter = new MyPagerAdapter(getChildFragmentManager());
        mViewPager.setAdapter(mAdapter);//给ViewPager设置适配器
        return v;
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
        public boolean isViewFromObject(View view, Object object) {
            return view == object;//官方推荐写法
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return new ReminderFragment();
                case 1:
                    return new ToDoFragment();
            }
            return null;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            return super.instantiateItem(container, position);
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView(container);//删除页卡
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "提醒";
                case 1:
                    return "代办";
            }

            return null;
        }

    }
}
