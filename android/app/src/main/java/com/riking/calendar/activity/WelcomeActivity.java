package com.riking.calendar.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.riking.calendar.R;

/**
 * Created by zw.zhang on 2017/7/24.
 */

public class WelcomeActivity extends AppCompatActivity {
    private ViewPager viewPager;
    private MyViewPagerAdapter myViewPagerAdapter;
    private ImageView progress1;
    private ImageView progress2;
    private ImageView progress3;
    private ImageView progress4;
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
                    break;
                }
                case 1: {
                    progress2.setImageDrawable(getDrawable(R.drawable.long_welcome_progress));
                    progress1.setImageDrawable(getDrawable(R.drawable.short_welcome_progress));
                    progress3.setImageDrawable(getDrawable(R.drawable.short_welcome_progress));
                    progress4.setImageDrawable(getDrawable(R.drawable.short_welcome_progress));
                    break;
                }
                case 2: {
                    progress3.setImageDrawable(getDrawable(R.drawable.long_welcome_progress));
                    progress2.setImageDrawable(getDrawable(R.drawable.short_welcome_progress));
                    progress1.setImageDrawable(getDrawable(R.drawable.short_welcome_progress));
                    progress4.setImageDrawable(getDrawable(R.drawable.short_welcome_progress));
                    break;
                }
                case 3: {
                    progress4.setImageDrawable(getDrawable(R.drawable.long_welcome_progress));
                    progress2.setImageDrawable(getDrawable(R.drawable.short_welcome_progress));
                    progress3.setImageDrawable(getDrawable(R.drawable.short_welcome_progress));
                    progress1.setImageDrawable(getDrawable(R.drawable.short_welcome_progress));
                    new Handler().postDelayed(new Runnable() {

                        @Override
                        public void run() {

                            Intent intent = new Intent(WelcomeActivity.this, ViewPagerActivity.class);
                            startActivity(intent);
                            finish(); //This closes current activity
                        }
                    }, 500); //It means 4 seconds
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

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        Log.d("zzw", this + "on create");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        progress1 = (ImageView) findViewById(R.id.progress1);
        progress2 = (ImageView) findViewById(R.id.progress2);
        progress3 = (ImageView) findViewById(R.id.progress3);
        progress4 = (ImageView) findViewById(R.id.progress4);
        viewPager = (ViewPager) findViewById(R.id.view_pager);


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
            ImageView i = new ImageView(container.getContext());
            ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            i.setLayoutParams(params);
            switch (position) {
                case 0: {
                    i.setImageDrawable(getDrawable(R.drawable.lead_welcome1));
                    break;
                }
                case 1: {
                    i.setImageDrawable(getDrawable(R.drawable.lead_welcome2));
                    break;
                }
                case 2: {
                    i.setImageDrawable(getDrawable(R.drawable.lead_welcome3));
                    break;
                }
                case 3: {
                    i.setImageDrawable(getDrawable(R.drawable.lead_welcome4));
                    break;
                }
            }

            container.addView(i);

            return i;
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
