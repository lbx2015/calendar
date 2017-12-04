package com.riking.calendar.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.riking.calendar.R;
import com.riking.calendar.util.CONST;
import com.riking.calendar.util.ZPreference;
import com.riking.calendar.view.ZCenterImageView;

/**
 * Created by zw.zhang on 2017/7/24.
 */

public class WelcomeActivity extends AppCompatActivity {
    private ViewPager viewPager;
    private MyViewPagerAdapter myViewPagerAdapter;
    private ZCenterImageView progress1;
    private ZCenterImageView progress2;
    private ZCenterImageView progress3;
    private ZCenterImageView progress4;
    private View enterButton;
    private View dotsLayout;
    //  viewpager change listener
    ViewPager.OnPageChangeListener viewPagerPageChangeListener = new ViewPager.OnPageChangeListener() {

        @Override
        public void onPageSelected(int position) {
            switch (position) {
                case 0: {
                    progress1.setImageDrawable(getDrawable(R.drawable.long_welcome_progress));
                    progress2.setImageDrawable(getDrawable(R.drawable.short_welcome_progress));
                    progress3.setImageDrawable(getDrawable(R.drawable.short_welcome_progress));
                    progress4.setImageDrawable(getDrawable(R.drawable.short_welcome_progress));
                    enterButton.setVisibility(View.GONE);
                    break;
                }
                case 1: {
                    progress2.setImageDrawable(getDrawable(R.drawable.long_welcome_progress));
                    progress1.setImageDrawable(getDrawable(R.drawable.short_welcome_progress));
                    progress3.setImageDrawable(getDrawable(R.drawable.short_welcome_progress));
                    progress4.setImageDrawable(getDrawable(R.drawable.short_welcome_progress));
                    enterButton.setVisibility(View.GONE);
                    break;
                }
                case 2: {
                    progress3.setImageDrawable(getDrawable(R.drawable.long_welcome_progress));
                    progress2.setImageDrawable(getDrawable(R.drawable.short_welcome_progress));
                    progress1.setImageDrawable(getDrawable(R.drawable.short_welcome_progress));
                    progress4.setImageDrawable(getDrawable(R.drawable.short_welcome_progress));
                    enterButton.setVisibility(View.GONE);
                    dotsLayout.setVisibility(View.VISIBLE);
                    break;
                }
                case 3: {
                    progress4.setImageDrawable(getDrawable(R.drawable.long_welcome_progress));
                    progress2.setImageDrawable(getDrawable(R.drawable.short_welcome_progress));
                    progress3.setImageDrawable(getDrawable(R.drawable.short_welcome_progress));
                    progress1.setImageDrawable(getDrawable(R.drawable.short_welcome_progress));
                    enterButton.setVisibility(View.VISIBLE);
                    dotsLayout.setVisibility(View.GONE);
                    enterButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(WelcomeActivity.this, ViewPagerActivity.class);
                            startActivity(intent);
                            //Welcome activity only need once
                            ZPreference.put(CONST.NEED_WELCOME_ACTIVITY, false);
                            finish(); //This closes current activity
                        }
                    });
                    break;
                }
            }
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {

        }

        @Override
        public void onPageScrollStateChanged(int arg0) {

        }
    };
    private int[] layouts;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        Log.d("zzw", this + "on create");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        progress1 = (ZCenterImageView) findViewById(R.id.progress1);
        progress2 = (ZCenterImageView) findViewById(R.id.progress2);
        progress3 = (ZCenterImageView) findViewById(R.id.progress3);
        progress4 = (ZCenterImageView) findViewById(R.id.progress4);
        viewPager = (ViewPager) findViewById(R.id.view_pager);
        enterButton = findViewById(R.id.enter_button);
        dotsLayout = findViewById(R.id.layoutDots);

        // layouts of all welcome sliders
        // add few more layouts if you want
        layouts = new int[]{
                R.layout.welcome_slide1,
                R.layout.welcome_slide2,
                R.layout.welcome_slide3,
                R.layout.welcome_slide4};

        myViewPagerAdapter = new MyViewPagerAdapter();
        viewPager.setAdapter(myViewPagerAdapter);
        viewPager.addOnPageChangeListener(viewPagerPageChangeListener);
    }

    /**
     * View pager adapter
     */
    public class MyViewPagerAdapter extends PagerAdapter {
        private LayoutInflater layoutInflater;

        public MyViewPagerAdapter() {
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = layoutInflater.inflate(layouts[position], container, false);
            container.addView(view);
            return view;
        }

        @Override
        public int getCount() {
            return 4;
        }

        @Override
        public boolean isViewFromObject(View view, Object obj) {
            return view == obj;
        }


        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            View view = (View) object;
            container.removeView(view);
        }
    }
}
