package com.riking.calendar.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.hyphenate.EMClientListener;
import com.hyphenate.EMMessageListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chatuidemo.Constant;
import com.hyphenate.chatuidemo.DemoHelper;
import com.hyphenate.chatuidemo.db.InviteMessgeDao;
import com.hyphenate.chatuidemo.ui.ContactListFragment;
import com.hyphenate.chatuidemo.ui.ConversationListFragment;
import com.hyphenate.chatuidemo.ui.GroupsActivity;
import com.hyphenate.easeui.utils.EaseCommonUtils;
import com.necer.ncalendar.utils.MyLog;
import com.riking.calendar.BuildConfig;
import com.riking.calendar.R;
import com.riking.calendar.fragment.FourthFragment;
import com.riking.calendar.fragment.HomeFragment;
import com.riking.calendar.fragment.PlazaFragment;
import com.riking.calendar.fragment.WorkFragment;
import com.riking.calendar.jiguang.Logger;
import com.riking.calendar.listener.CheckCallBack;
import com.riking.calendar.pojo.AppVersionResult;
import com.riking.calendar.retrofit.APIClient;
import com.riking.calendar.util.AppInnerDownLoder;
import com.riking.calendar.util.DownLoadApk;

import java.util.List;

/**
 * Created by zw.zhang on 2017/7/11.
 */

public class ViewPagerActivity extends AppCompatActivity {
    //huanxin start
    protected static final String TAG = "ViewPagerActivity";
    //huanxin end
    //Tab 图片
    private final int[] TAB_IMGS = new int[]{R.drawable.home_tab_selector, R.drawable.first_tab_selector, R.drawable.second_tab_selector, R.drawable.third_tab_selector, R.drawable.fourth_tab_selector};
    //Fragment 数组
    private final Fragment[] TAB_FRAGMENTS = new Fragment[]{new HomeFragment(), new PlazaFragment(), new WorkFragment(), new ConversationListFragment(), new FourthFragment()};
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
    EMClientListener clientListener = new EMClientListener() {
        @Override
        public void onMigrate2x(boolean success) {
            Toast.makeText(ViewPagerActivity.this, "onUpgradeFrom 2.x to 3.x " + (success ? "success" : "fail"), Toast.LENGTH_LONG).show();
            if (success) {
                refreshUIWithMessage();
            }
        }
    };
    EMMessageListener messageListener = new EMMessageListener() {

        @Override
        public void onMessageReceived(List<EMMessage> messages) {
            // notify new message
            for (EMMessage message : messages) {
                DemoHelper.getInstance().getNotifier().onNewMsg(message);
            }
            refreshUIWithMessage();
        }

        @Override
        public void onCmdMessageReceived(List<EMMessage> messages) {
            refreshUIWithMessage();
        }

        @Override
        public void onMessageRead(List<EMMessage> messages) {
        }

        @Override
        public void onMessageDelivered(List<EMMessage> message) {
        }

        @Override
        public void onMessageRecalled(List<EMMessage> messages) {
            refreshUIWithMessage();
        }

        @Override
        public void onMessageChanged(EMMessage message, Object change) {
        }
    };
    // textview for unread event message
    private TextView unreadAddressLable;
    private Button[] mTabs;
    private ContactListFragment contactListFragment;
    private Fragment[] fragments;
    private int index;
    private int currentTabIndex;
    // user account was removed
    private boolean isCurrentAccountRemoved = false;
    private InviteMessgeDao inviteMessgeDao;
    private String[] mTitles;
    private AlertDialog.Builder mDialog;
    private android.app.AlertDialog.Builder exceptionBuilder;
    private boolean isExceptionDialogShow = false;
    private BroadcastReceiver internalDebugReceiver;
    private ConversationListFragment conversationListFragment;
    private BroadcastReceiver broadcastReceiver;
    private LocalBroadcastManager broadcastManager;

    /**
     * update the total unread count
     */
    public void updateUnreadAddressLable() {
        runOnUiThread(new Runnable() {
            public void run() {
                int count = getUnreadAddressCountTotal();
                if (count > 0) {
                    unreadAddressLable.setVisibility(View.VISIBLE);
                } else {
                    unreadAddressLable.setVisibility(View.INVISIBLE);
                }
            }
        });

    }

    /**
     * get unread event notification count, including application, accepted, etc
     *
     * @return
     */
    public int getUnreadAddressCountTotal() {
        int unreadAddressCountTotal = 0;
        unreadAddressCountTotal = inviteMessgeDao.getUnreadMessagesCount();
        return unreadAddressCountTotal;
    }

    /**
     * get unread message count
     *
     * @return
     */
    public int getUnreadMsgCountTotal() {
        return EMClient.getInstance().chatManager().getUnreadMsgsCount();
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (!isConflict && !isCurrentAccountRemoved) {
            //not show the unread message count for the moment
            updateUnreadLabel();
//            updateUnreadAddressLable();
        }

        // unregister this event listener when this activity enters the
        // background
        DemoHelper sdkHelper = DemoHelper.getInstance();
        sdkHelper.pushActivity(this);

        EMClient.getInstance().chatManager().addMessageListener(messageListener);
    }

    /**
     * update unread message count
     */
    public void updateUnreadLabel() {
        int count = getUnreadMsgCountTotal();
        if (unreadLabel != null) {
            if (count > 0) {
                unreadLabel.setText(String.valueOf(count));
                unreadLabel.setVisibility(View.VISIBLE);
            } else {
                unreadLabel.setVisibility(View.GONE);
            }
        }
    }

    private void refreshUIWithMessage() {
        MyLog.d("refreshUIwithMessage");
        new Exception().printStackTrace();
        runOnUiThread(new Runnable() {
            public void run() {
                // refresh unread count
                updateUnreadLabel();
                Fragment f = TAB_FRAGMENTS[3];
                if (f instanceof ConversationListFragment) {
                    ((ConversationListFragment) f).refresh();
                }
//                if (currentTabIndex == 0) {
                // refresh conversation list
//                    if (conversationListFragment != null) {
//                        conversationListFragment.refresh();
//                    }
//                }
            }
        });
    }

    private void registerBroadcastReceiver() {
        broadcastManager = LocalBroadcastManager.getInstance(this);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Constant.ACTION_CONTACT_CHANAGED);
        intentFilter.addAction(Constant.ACTION_GROUP_CHANAGED);
        broadcastReceiver = new BroadcastReceiver() {

            @Override
            public void onReceive(Context context, Intent intent) {
                updateUnreadLabel();
                updateUnreadAddressLable();
                if (currentTabIndex == 0) {
                    // refresh conversation list
                    if (conversationListFragment != null) {
                        conversationListFragment.refresh();
                    }
                } else if (currentTabIndex == 1) {
                    if (contactListFragment != null) {
                        contactListFragment.refresh();
                    }
                }
                String action = intent.getAction();
                if (action.equals(Constant.ACTION_GROUP_CHANAGED)) {
                    if (EaseCommonUtils.getTopActivity(ViewPagerActivity.this).equals(GroupsActivity.class.getName())) {
                        GroupsActivity.instance.onResume();
                    }
                }

            }
        };
        broadcastManager.registerReceiver(broadcastReceiver, intentFilter);
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
        pager = (ViewPager) findViewById(R.id.viewPager);
        adapter = new MyPagerAdapter(getSupportFragmentManager());
        pager.setAdapter(adapter);
        pager.setOffscreenPageLimit(3);

        mTabLayout = (TabLayout) findViewById(R.id.tablayout);
        pager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(mTabLayout));
        mTabLayout.setOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(pager));
        setTabs(mTabLayout, this.getLayoutInflater(), mTitles, TAB_IMGS);
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

    @Override
    protected void onStop() {
        EMClient.getInstance().chatManager().removeMessageListener(messageListener);
        EMClient.getInstance().removeClientListener(clientListener);
        DemoHelper sdkHelper = DemoHelper.getInstance();
        sdkHelper.popActivity(this);

        super.onStop();
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
