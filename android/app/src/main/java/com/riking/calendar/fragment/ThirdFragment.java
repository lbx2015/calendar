package com.riking.calendar.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.riking.calendar.R;
import com.riking.calendar.activity.AddRemindActivity;

/**
 * Created by zw.zhang on 2017/7/11.
 */

public class ThirdFragment extends Fragment implements View.OnClickListener {
    View v;
    private ViewPager mViewPager;
    private View add;
    private View done;
    private MyPagerAdapter mAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (v != null) {
            return v;
        }
        v = inflater.inflate(R.layout.third_fragment, container, false);
        mViewPager = (ViewPager) v.findViewById(R.id.pager);
        mAdapter = new MyPagerAdapter(getChildFragmentManager());
        mViewPager.setAdapter(mAdapter);//给ViewPager设置适配器
        add = v.findViewById(R.id.add);
        done = v.findViewById(R.id.done);
        add.setOnClickListener(this);

        //adding view pager to the slidingTabLayout
        TabLayout topTabLayout = v.findViewById(R.id.top_tab_layout);
        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(topTabLayout));
        topTabLayout.setOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));

        //set custom layout to adding divider
        for (int i = 0; i < 2; i++) {
            TabLayout.Tab tab = topTabLayout.newTab();
            RelativeLayout relativeLayout = (RelativeLayout)
                    getActivity().getLayoutInflater().inflate(R.layout.third_fragment_tab_custom, null);

            TextView tabTextView = (TextView) relativeLayout.findViewById(R.id.tab_title);
            View divider = relativeLayout.findViewById(R.id.divider);
            if (i == 0) {
                divider.setVisibility(View.GONE);
                tabTextView.setText(R.string.reminder);
            } else {
                tabTextView.setText(R.string.to_do);
            }
            tab.setCustomView(relativeLayout);
            topTabLayout.addTab(tab);
        }
        return v;
    }


    public void onClick(View v) {
        int position = mViewPager.getCurrentItem();
        switch (v.getId()) {
            case R.id.add: {
          /*      if (position == 0) {
                    ReminderFragment f = (ReminderFragment) mAdapter.getItem(mViewPager.getCurrentItem());
                    if (cancel != null)
                        cancel.setVisibility(View.VISIBLE);
                    if (done != null)
                        done.setVisibility(View.VISIBLE);
                    if (add != null)
                        add.setVisibility(View.GONE);
                }*/
                startActivity(new Intent(getActivity(), AddRemindActivity.class));
                break;
            }
            case R.id.cancel: {
                done.setVisibility(View.GONE);
                add.setVisibility(View.VISIBLE);
                break;
            }
            case R.id.done: {
                done.setVisibility(View.GONE);
                add.setVisibility(View.VISIBLE);
                break;
            }
        }
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
            switch (position) {
                case 0:
                    return new ReminderFragment();
                case 1:
                    return new TaskFragment();
            }
            return null;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            Log.d("zzw", "instantiateItem: " + position);

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
                    return getString(R.string.reminder);
                case 1:
                    return getString(R.string.to_do);
            }

            return null;
        }

    }
}
