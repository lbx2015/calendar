package com.riking.calendar.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.flyco.tablayout.CommonTabLayout;
import com.flyco.tablayout.listener.CustomTabEntity;
import com.riking.calendar.R;
import com.riking.calendar.pojo.TabEntity;
import com.riking.calendar.util.ViewFindUtils;

import java.util.ArrayList;

public class CommonTabActivity extends AppCompatActivity {
    private Context mContext = this;
    private String[] mTitles = {"首页", "消息", "联系人", "更多"};
    private ArrayList<CustomTabEntity> mTabEntities = new ArrayList<>();
    private View mDecorView;
    private CommonTabLayout mTabLayout_8;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_common_tab);

        for (int i = 0; i < mTitles.length; i++) {
            mTabEntities.add(new TabEntity(mTitles[i], 0, 0));
        }

        mDecorView = getWindow().getDecorView();
        /** indicator圆角色块 */
        mTabLayout_8 = ViewFindUtils.find(mDecorView, R.id.tl_8);
        mTabLayout_8.setTabData(mTabEntities);
        mTabLayout_8.setCurrentTab(2);
    }
}
