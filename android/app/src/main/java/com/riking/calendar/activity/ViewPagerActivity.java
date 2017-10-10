package com.riking.calendar.activity;

import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.hyphenate.chatuidemo.ui.ConversationListFragment;
import com.riking.calendar.BuildConfig;
import com.riking.calendar.R;
import com.riking.calendar.fragment.FourthFragment;
import com.riking.calendar.fragment.HomeFragment;
import com.riking.calendar.fragment.SecondFragment;
import com.riking.calendar.fragment.ThirdFragment;
import com.riking.calendar.jiguang.Logger;
import com.riking.calendar.listener.CheckCallBack;
import com.riking.calendar.pojo.AppVersionResult;
import com.riking.calendar.retrofit.APIClient;
import com.riking.calendar.util.AppInnerDownLoder;
import com.riking.calendar.util.DownLoadApk;

/**
 * Created by zw.zhang on 2017/7/11.
 */

public class ViewPagerActivity extends FragmentActivity {
    //Tab 图片
    private final int[] TAB_IMGS = new int[]{R.drawable.home_tab_selector, R.drawable.first_tab_selector, R.drawable.second_tab_selector, R.drawable.third_tab_selector, R.drawable.fourth_tab_selector};
    //Fragment 数组
    private final Fragment[] TAB_FRAGMENTS = new Fragment[]{new HomeFragment(), new SecondFragment(), new ConversationListFragment(), new ThirdFragment(), new FourthFragment()};
    //Tab 数目
    private final int COUNT = 5;
    public TabLayout mTabLayout;
    MyPagerAdapter adapter;
    boolean doubleBackToExitPressedOnce = false;
    private String[] mTitles;
    private AlertDialog.Builder mDialog;

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, getString(R.string.click_again_exit), Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viewpager);
        initViews();

        Window window = getWindow();

// clear FLAG_TRANSLUCENT_STATUS flag:
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

// add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

// finally change the color
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.colorPrimary));
        }

        //to test the download function not use request
//        AppVersionResult u = new AppVersionResult();
//        u.type = "2";
//        u.msg = "test update";
//        u.apkUrl = "http://192.168.23.1:8080/MylocalServer/app_debug.apk";
//        forceUpdate(u);
        APIClient.checkUpdate(new CheckCallBack() {
            @Override
            public void onSuccess(AppVersionResult updateInfo) {
                Logger.d("zzw", "on Success");
                //返回0当前为最新版本，返回1有版本更新，返回2需要强制更新
                if (updateInfo.type.equals("2")) {
                    forceUpdate(updateInfo);
                } else if (updateInfo.type.equals("1")) {
                    normalUpdate(updateInfo);
                }
            }

            @Override
            public void onError() {
            }
        });
    }

    private void initViews() {
        mTitles = getResources().getStringArray(R.array.subTittles);
        final ViewPager pager = (ViewPager) findViewById(R.id.viewPager);
        adapter = new MyPagerAdapter(getSupportFragmentManager());
        pager.setAdapter(adapter);
        pager.setOffscreenPageLimit(3);

        mTabLayout = (TabLayout) findViewById(R.id.tablayout);
        setTabs(mTabLayout, this.getLayoutInflater(), mTitles, TAB_IMGS);

        pager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(mTabLayout));
        mTabLayout.setOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(pager));
    }


    /**
     * @description: 设置添加Tab
     */
    private void setTabs(TabLayout tabLayout, LayoutInflater inflater, String[] tabTitlees, int[] tabImgs) {
        for (int i = 0; i < tabImgs.length; i++) {
            TabLayout.Tab tab = tabLayout.newTab();
            View view = inflater.inflate(R.layout.tab_custom, null);
            tab.setCustomView(view);

            TextView tvTitle = (TextView) view.findViewById(R.id.tv_tab);
            tvTitle.setText(tabTitlees[i]);
            ImageView imgTab = (ImageView) view.findViewById(R.id.img_tab);
            imgTab.setImageResource(tabImgs[i]);
            tabLayout.addTab(tab);
        }
    }

    public void forceUpdate(final AppVersionResult updateInfo) {
        mDialog = new AlertDialog.Builder(this);
        mDialog.setTitle(BuildConfig.APPLICATION_ID + "又更新咯！");
        mDialog.setMessage(updateInfo.msg);
        mDialog.setPositiveButton("立即更新", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Logger.d("zzw", "on click download");
                AppInnerDownLoder.downLoadApk(ViewPagerActivity.this, updateInfo.apkUrl, updateInfo.msg);
            }
        }).setCancelable(false).create().show();
    }

    public void normalUpdate(final AppVersionResult updateInfo) {
        mDialog = new AlertDialog.Builder(this);
        mDialog.setTitle(BuildConfig.APPLICATION_ID + "又更新咯！");
        mDialog.setMessage(updateInfo.msg);
        mDialog.setPositiveButton("立即更新", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                DownLoadApk.download(ViewPagerActivity.this, updateInfo.apkUrl, updateInfo.msg);
            }
        }).setCancelable(true).setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        }).create().show();
    }

    private class MyPagerAdapter extends FragmentPagerAdapter {

        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int pos) {
            return TAB_FRAGMENTS[pos];
        }

        @Override
        public int getCount() {
            return COUNT;
        }
    }
}
