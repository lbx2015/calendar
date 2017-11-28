package com.riking.calendar.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.riking.calendar.R;

import java.util.ArrayList;
import java.util.List;

import ss.com.bannerslider.banners.Banner;
import ss.com.bannerslider.banners.DrawableBanner;
import ss.com.bannerslider.banners.RemoteBanner;
import ss.com.bannerslider.events.OnBannerClickListener;
import ss.com.bannerslider.views.BannerSlider;

/**
 * Created by zw.zhang on 2017/7/11.
 */

public class HomeFragment extends Fragment {
    //Fragment 数组
    private final Fragment[] TAB_FRAGMENTS = new Fragment[]{new TopicFragment(), new FinanceNewsFragment()};
    View v;
    private ViewPager mViewPager;
    private MyPagerAdapter mAdapter;
    private BannerSlider bannerSlider;
    CollapsingToolbarLayout collapsingToolbarLayout;
    AppBarLayout appBarLayout;
    LinearLayout search;

    private void setupBannerSlider() {
        bannerSlider = (BannerSlider) v.findViewById(R.id.banner_slider1);
        addBanners();

        bannerSlider.setOnBannerClickListener(new OnBannerClickListener() {
            @Override
            public void onClick(int position) {
                Toast.makeText(getContext(), "Banner with position " + String.valueOf(position) + " clicked!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void addBanners() {
        List<Banner> remoteBanners = new ArrayList<>();
        //Add banners using image urls
   /*     remoteBanners.add(new RemoteBanner(
                "https://assets.materialup.com/uploads/dcc07ea4-845a-463b-b5f0-4696574da5ed/preview.jpg"
        ));*/
        remoteBanners.add(new DrawableBanner(R.drawable.banner));
        remoteBanners.add(new DrawableBanner(R.drawable.profile3));
        remoteBanners.add(new DrawableBanner(R.drawable.profilegoat));
        remoteBanners.add(new DrawableBanner(R.drawable.banner));
        remoteBanners.add(new DrawableBanner(R.drawable.profile));
        bannerSlider.setBanners(remoteBanners);

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (v != null) {
            return v;
        }
        v = inflater.inflate(R.layout.home_fragment, container, false);
        mViewPager = (ViewPager) v.findViewById(R.id.pager);
        mAdapter = new MyPagerAdapter(getChildFragmentManager());
        mViewPager.setAdapter(mAdapter);//给ViewPager设置适配器
        TabLayout tabLayout = (TabLayout) v.findViewById(R.id.top_tab_layout);
        tabLayout.setupWithViewPager(mViewPager);
        appBarLayout = v.findViewById(R.id.appbar);
        search = v.findViewById(R.id.search);
        setupBannerSlider();
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
