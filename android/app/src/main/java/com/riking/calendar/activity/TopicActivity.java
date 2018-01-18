package com.riking.calendar.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.necer.ncalendar.utils.MyLog;
import com.riking.calendar.R;
import com.riking.calendar.fragment.ExcellentAnswererFragment;
import com.riking.calendar.fragment.HotAnswerOfTopicFragment;
import com.riking.calendar.fragment.QuestionsFragment;
import com.riking.calendar.listener.ZCallBack;
import com.riking.calendar.listener.ZClickListenerWithLoginCheck;
import com.riking.calendar.pojo.base.ResponseModel;
import com.riking.calendar.pojo.params.TQuestionParams;
import com.riking.calendar.pojo.params.TopicParams;
import com.riking.calendar.pojo.server.Topic;
import com.riking.calendar.retrofit.APIClient;
import com.riking.calendar.util.ZR;
import com.riking.calendar.util.ZToast;

import static com.riking.calendar.util.CONST.TOPIC_ID;

/**
 * Created by zw.zhang on 2017/7/24.
 */

public class TopicActivity extends AppCompatActivity { //Fragment 数组
    private final Fragment[] TAB_FRAGMENTS = new Fragment[]{HotAnswerOfTopicFragment.newInstance(this), QuestionsFragment.newInstance(this), ExcellentAnswererFragment.newInstance(this)};
    public View followButton;
    public TextView followTv;
    public Topic topic;
    public String topicId;
    private ViewPager mViewPager;
    private TextView topicTitle;
    private MyPagerAdapter mAdapter;
    private TextView followNumberTv;
    private TextView topicContent;
    private TextView expandButtonTv;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        Log.d("zzw", this + "on create");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.topic_activity);
        topicId = getIntent().getStringExtra(TOPIC_ID);
        init();
    }

    private void init() {
        initViews();
        initEvents();
        loadTopicById();
        setFollowClickListener();

    }

    private void setFollowClickListener() {
        followButton.setOnClickListener(new ZClickListenerWithLoginCheck() {
            @Override
            public void click(View v) {
                //adding null protection
                if (topic == null) {
                    return;
                }
                final TQuestionParams params = new TQuestionParams();
                params.attentObjId = topic.topicId;
                //topic
                params.objType = 2;
                //followed
                if (topic.isFollow == 1) {
                    params.enabled = 0;
                } else {
                    params.enabled = 1;
                }

                APIClient.follow(params, new ZCallBack<ResponseModel<String>>() {
                    @Override
                    public void callBack(ResponseModel<String> response) {
                        topic.isFollow = params.enabled;
                        if (topic.isFollow == 1) {
                            ZToast.toast("关注成功");
                        } else {
                            ZToast.toast("取消关注");
                        }
                        updateFollowButton();
                    }
                });
            }
        });

    }

    private void loadTopicById() {
        TopicParams params = new TopicParams();
        params.topicId = topicId;

        APIClient.getTopic(params, new ZCallBack<ResponseModel<Topic>>() {
            @Override
            public void callBack(ResponseModel<Topic> response) {
                topic = response._data;
                updateFollowButton();
                //set follow number
                followNumberTv.setText(topic.followNum + "人关注");
                //set topic title
                topicTitle.setText(topic.title);
                //set topic content
                topicContent.setText(topic.content);
                if (topicContent.getLineCount() <= 3) {
                    MyLog.d("expand button set gone");
                    expandButtonTv.setVisibility(View.GONE);
                }
            }
        });
    }


    private void updateFollowButton() {
        //followed
        if (topic.isFollow == 1) {
            followTv.setText("已关注");
            followTv.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
            followTv.setTextColor(ZR.getColor(R.color.color_bbbbbb));
            followButton.setBackground(ZR.getDrawable(R.drawable.follow_button_gray));
        } else {
            followTv.setText("关注");
            followTv.setTextColor(ZR.getColor(R.color.white));
            followTv.setCompoundDrawablesWithIntrinsicBounds(R.drawable.com_btn_icon_plus_w, 0, 0, 0);
            followTv.setCompoundDrawablePadding((int) ZR.convertDpToPx(5));
            followButton.setBackground(ZR.getDrawable(R.drawable.follow_button_border));
        }
    }

    private void initViews() {
        expandButtonTv = findViewById(R.id.id_expand_textview);
        topicContent = findViewById(R.id.id_source_textview);
        followNumberTv = findViewById(R.id.follow_number_tv);
        topicTitle = findViewById(R.id.topic_title);
        followButton = findViewById(R.id.follow_button);
        followTv = findViewById(R.id.follow_text);
        mViewPager = (ViewPager) findViewById(R.id.pager);
    }

    private void initEvents() {
        mAdapter = new MyPagerAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(mAdapter);//给ViewPager设置适配器
        TabLayout tabLayout = (TabLayout) findViewById(R.id.top_tab_layout);
        tabLayout.setupWithViewPager(mViewPager);
    }


    public void clickBack(final View view) {
        onBackPressed();
    }

    //ViewPager适配器
    class MyPagerAdapter extends FragmentPagerAdapter {

        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            return TAB_FRAGMENTS.length;//页卡数
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
                    return "精华";
                case 1:
                    return "问题";
                case 2:
                    return "优秀回答者";
            }

            return null;
        }

    }

}
