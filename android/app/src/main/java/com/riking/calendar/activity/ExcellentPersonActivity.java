package com.riking.calendar.activity;

import android.view.View;
import android.widget.TextView;

import com.necer.ncalendar.utils.MyLog;
import com.necer.ncalendar.view.SimpleDividerDecorationWithoutLastItem;
import com.riking.calendar.R;
import com.riking.calendar.activity.base.ZActivity;
import com.riking.calendar.adapter.ExcellentPersonAnswerAdapter;
import com.riking.calendar.listener.ZCallBack;
import com.riking.calendar.listener.ZCallBackWithFail;
import com.riking.calendar.listener.ZClickListenerWithLoginCheck;
import com.riking.calendar.pojo.base.ResponseModel;
import com.riking.calendar.pojo.params.TQuestionParams;
import com.riking.calendar.pojo.params.TopicParams;
import com.riking.calendar.pojo.server.QAnswerResult;
import com.riking.calendar.pojo.server.Topic;
import com.riking.calendar.retrofit.APIClient;
import com.riking.calendar.util.CONST;
import com.riking.calendar.util.ZR;
import com.riking.calendar.util.ZToast;

import java.util.List;

import static com.riking.calendar.util.CONST.TOPIC_ID;

/**
 * Created by zw.zhang on 2017/7/24.
 */

public class ExcellentPersonActivity extends ZActivity<ExcellentPersonAnswerAdapter> { //Fragment 数组
    public View followButton;
    public TextView followTv;
    public Topic topic;
    public String topicId;
    public String userName;
    public String answerUserId;
    public TextView answerNumberTv;
    private TextView topicTitle;
    private TextView followNumberTv;
    private TextView topicContent;
    private TextView expandButtonTv;
    private TextView activityTitle;

    @Override
    public void setLayout() {
        setContentView(R.layout.excellent_answer_activity);
        topicId = getIntent().getStringExtra(TOPIC_ID);
        userName = getIntent().getStringExtra(CONST.USER_NAME);
        answerUserId = getIntent().getStringExtra(CONST.USER_ID);
    }

    @Override
    public ExcellentPersonAnswerAdapter getAdapter() {
        return new ExcellentPersonAnswerAdapter();
    }

    @Override
    public void loadData(int page) {
        loadTopicById();
        loadAnswers(page);
        loadAnswersNumber();
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

    private void loadAnswersNumber() {
        TopicParams params = new TopicParams();
        params.topicId = topicId;
        params.userId = answerUserId;
        APIClient.getQAnswerSize(params, new ZCallBackWithFail<ResponseModel<String>>() {
            @Override
            public void callBack(ResponseModel<String> response) throws Exception {
                loadComplete();
                if (failed) {

                } else {
                    answerNumberTv.setText(userName + "在此话题下有" + response._data + "个回答");
                }
            }
        });
    }

    private void loadAnswers(final int page) {
        TopicParams params = new TopicParams();
        params.topicId = topicId;
        params.userId = answerUserId;
        params.pindex = page;
        APIClient.getUserAnswerResult(params, new ZCallBackWithFail<ResponseModel<List<QAnswerResult>>>() {
            @Override
            public void callBack(ResponseModel<List<QAnswerResult>> response) throws Exception {
                loadComplete();
                if (failed) {

                } else {
                    List<QAnswerResult> results = response._data;
                    setData2Adapter(page, results);
                }

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

    public void initViews() {
        activityTitle = findViewById(R.id.activity_title);
        answerNumberTv = findViewById(R.id.answer_number_tv);
        expandButtonTv = findViewById(R.id.id_expand_textview);
        topicContent = findViewById(R.id.id_source_textview);
        followNumberTv = findViewById(R.id.follow_number_tv);
        topicTitle = findViewById(R.id.topic_title);
        followButton = findViewById(R.id.follow_button);
        followTv = findViewById(R.id.follow_text);
    }

    public void initEvents() {
        activityTitle.setText(userName + "的回答");
        setFollowClickListener();
        mRecyclerView.addItemDecoration(new SimpleDividerDecorationWithoutLastItem(ZR.getDrawable(R.drawable.recycler_view_divider), (int) ZR.convertDpToPx(15)));
    }

    public void clickBack(final View view) {
        onBackPressed();
    }

}
