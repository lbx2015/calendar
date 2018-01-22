package com.riking.calendar.activity;

import android.content.BroadcastReceiver;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.necer.ncalendar.utils.MyLog;
import com.riking.calendar.BuildConfig;
import com.riking.calendar.R;
import com.riking.calendar.fragment.HomeFragment;
import com.riking.calendar.fragment.MyNewsFragment;
import com.riking.calendar.fragment.UserInfoFragment;
import com.riking.calendar.fragment.WorkFragment;
import com.riking.calendar.jiguang.Logger;
import com.riking.calendar.listener.CheckCallBack;
import com.riking.calendar.pojo.AppVersionResult;
import com.riking.calendar.retrofit.APIClient;
import com.riking.calendar.util.AppInnerDownLoder;
import com.riking.calendar.util.DownLoadApk;
import com.riking.calendar.util.StatusBarUtil;
import com.riking.calendar.util.ZR;

/**
 * Created by zw.zhang on 2017/7/11.
 */

public class ViewPagerActivity extends AppCompatActivity {
    //huanxin start
    protected static final String TAG = "ViewPagerActivity";
    //huanxin end
    //Tab 图片
    private final int[] TAB_IMGS = new int[]{R.drawable.home_tab_selector, R.drawable.second_tab_selector, R.drawable.third_tab_selector, R.drawable.fourth_tab_selector};
    //    private final int[] TAB_IMGS = new int[]{R.drawable.home_tab_selector, R.drawable.first_tab_selector, R.drawable.second_tab_selector, R.drawable.third_tab_selector, R.drawable.fourth_tab_selector};
    //Fragment 数组
    private final Fragment[] TAB_FRAGMENTS = new Fragment[]{new HomeFragment(), new WorkFragment(), new MyNewsFragment(), new UserInfoFragment()};
    //    private final Fragment[] TAB_FRAGMENTS = new Fragment[]{new HomeFragment(), new WorkFragment(), new ConversationListFragment(), new UserInfoFragment()};
    //    private final Fragment[] TAB_FRAGMENTS = new Fragment[]{new HomeFragment(), new PlazaFragment(), new WorkFragment(), new ConversationListFragment(), new FourthFragment()};
    //Tab 数目
    private final int COUNT = TAB_FRAGMENTS.length;
    // user logged into another device
    public boolean isConflict = false;
    public TabLayout mTabLayout;
    public MyPagerAdapter adapter;
    boolean doubleBackToExitPressedOnce = false;
    ViewPager pager;
    // textview for unread message count
    private TextView unreadLabel;

    // textview for unread event message
    private int currentTabIndex;
    // user account was removed
    private boolean isCurrentAccountRemoved = false;
    private String[] mTitles;
    private AlertDialog.Builder mDialog;
    private android.app.AlertDialog.Builder exceptionBuilder;
    private boolean isExceptionDialogShow = false;
    private BroadcastReceiver internalDebugReceiver;
    private BroadcastReceiver broadcastReceiver;
    private LocalBroadcastManager broadcastManager;

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        // Save the user's current state
        // Check carefully what you're adding into the savedInstanceState before saving it
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.clear();
    }

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

        View decorView = getWindow().getDecorView();
// Hide both the navigation bar and the status bar.
// SYSTEM_UI_FLAG_FULLSCREEN is only available on Android 4.1 and higher, but as
// a general rule, you should design your app to hide the status bar whenever you
// hide the navigation bar.
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
        decorView.setSystemUiVisibility(uiOptions);

  /*      Window window = getWindow();

// clear FLAG_TRANSLUCENT_STATUS flag:
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

// add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

// finally change the color
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.colorPrimary));
        }*/

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
                if (updateInfo.enforce == 1) {
                    forceUpdate(updateInfo);
                } else if (updateInfo.enforce == 0) {
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
        pager = (ViewPager) findViewById(R.id.viewPager);
        adapter = new MyPagerAdapter(getSupportFragmentManager());
        pager.setAdapter(adapter);
        pager.setOffscreenPageLimit(3);

        mTabLayout = (TabLayout) findViewById(R.id.tablayout);
        pager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(mTabLayout));
        mTabLayout.setOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(pager));
        setTabs(mTabLayout, this.getLayoutInflater(), mTitles, TAB_IMGS);

        //adding page change listener
        pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position == 3) {
                    Window w = getWindow();
                    // in Activity's onCreate() for instance
                    w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
                    w.clearFlags(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
                    //set transparent background for the status bar
                    StatusBarUtil.setTransparent(ViewPagerActivity.this);
                } else {
                    Window w = getWindow(); // in Activity's onCreate() for instance
                    w.clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS | WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
                    w.setStatusBarColor(ZR.getColor(R.color.color_489dfff));
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    /**
     * @description: 设置添加Tab
     */
    private void setTabs(TabLayout tabLayout, LayoutInflater inflater, String[] tabTitlees, int[] tabImgs) {
        for (int i = 0; i < tabImgs.length; i++) {
            TabLayout.Tab tab = tabLayout.newTab();
            View view = inflater.inflate(R.layout.tab_custom, null);
            tab.setCustomView(view);
            if (i == 2) {
                MyLog.d(tabTitlees[i]);
                unreadLabel = (TextView) view.findViewById(R.id.unread_msg_number);
            }
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
        mDialog.setMessage(updateInfo.remark);
        mDialog.setPositiveButton("立即更新", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Logger.d("zzw", "on click download");
                AppInnerDownLoder.downLoadApk(ViewPagerActivity.this, updateInfo.url, updateInfo.remark);
            }
        }).setCancelable(false).create().show();
    }

    public void normalUpdate(final AppVersionResult updateInfo) {
        mDialog = new AlertDialog.Builder(this);
        mDialog.setTitle(BuildConfig.APPLICATION_ID + "又更新咯！");
        mDialog.setMessage(updateInfo.remark);
        mDialog.setPositiveButton("立即更新", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                DownLoadApk.download(ViewPagerActivity.this, updateInfo.url, updateInfo.remark);
            }
        }).setCancelable(true).setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        }).create().show();
    }
/*
    @Override
    protected void onStop() {
        EMClient.getInstance().chatManager().removeMessageListener(messageListener);
        EMClient.getInstance().removeClientListener(clientListener);
        DemoHelper sdkHelper = DemoHelper.getInstance();
        sdkHelper.popActivity(this);

        super.onStop();
    }*/

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
