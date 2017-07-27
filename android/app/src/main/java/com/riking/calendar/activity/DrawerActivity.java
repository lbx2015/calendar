package com.riking.calendar.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.riking.calendar.R;

/**
 * Created by zw.zhang on 2017/7/7.
 */

public class DrawerActivity extends AppCompatActivity {

    private final String[] items = new String[]{"选项一：", "选项二：", "选项三：",
            "选项4：", "选项5：", "选项6："};
    private DrawerLayout drawerLayout;
    private ListView leftDrawer;
    private ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawer);

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        leftDrawer = (ListView) findViewById(R.id.drawer_left);
        adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, items) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                // 设置背填充内容背景色
                TextView tView = (TextView) super.getView(position,
                        convertView, parent);
                tView.setTextColor(Color.BLACK);
                return super.getView(position, convertView, parent);
            }
        };
        leftDrawer.setAdapter(adapter);
        leftDrawer.setBackgroundColor(Color.WHITE);

        // 设置选择模式为单条选中
        leftDrawer.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

        // 通过代码：根据重力方向打开指定抽屉
        drawerLayout.openDrawer(Gravity.LEFT);
        // 设置抽屉阴影
        // drawerLayout.setDrawerShadow(R.drawable.ic_launcher, Gravity.LEFT);
        // 设置抽屉空余处颜色
        drawerLayout.setScrimColor(Color.BLUE);
        // 设置抽屉锁定模式 LOCK_MODE_LOCKED_OPEN:锁定 无法滑动； 只能通过代码取消锁定
        // drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_OPEN);
    }
}
