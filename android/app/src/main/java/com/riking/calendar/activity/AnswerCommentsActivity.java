package com.riking.calendar.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.necer.ncalendar.utils.MyLog;
import com.riking.calendar.R;
import com.riking.calendar.activity.base.ZActivity;
import com.riking.calendar.adapter.AnswerCommentListAdapter;
import com.riking.calendar.adapter.AnswerReplyListAdapter;
import com.riking.calendar.listener.ZCallBack;
import com.riking.calendar.listener.ZCallBackWithFail;
import com.riking.calendar.listener.ZClickListenerWithLoginCheck;
import com.riking.calendar.pojo.base.ResponseModel;
import com.riking.calendar.pojo.params.CommentParams;
import com.riking.calendar.pojo.params.QAnswerParams;
import com.riking.calendar.pojo.server.NCReply;
import com.riking.calendar.pojo.server.QAComment;
import com.riking.calendar.pojo.server.QuestionAnswer;
import com.riking.calendar.retrofit.APIClient;
import com.riking.calendar.util.CONST;
import com.riking.calendar.util.ZPreference;
import com.riking.calendar.util.ZR;
import com.riking.calendar.util.ZToast;
import com.riking.calendar.view.KeyboardEditText;

import java.util.List;

/**
 * Created by zw.zhang on 2017/7/24.
 * answer comments page
 */

public class AnswerCommentsActivity extends ZActivity<AnswerCommentListAdapter> {
    TextView publicButton;
    KeyboardEditText writeComment;
    ImageView answerIcon;
    boolean isOpened = false;
    //0 reply news ,1 rely comment,2 reply reply
    int commentFlag = 0;
    QAComment replyComment;
    NCReply replyReply;
    String commentContent;
    AnswerReplyListAdapter replyListAdapter;
    RecyclerView replyRecyclerView;
    private String answerId;
    private int answerCommentsNum;
    private TextView activityTitle;
    private TextView questionTitleTv;
    private TextView answerContentTv;

    //override this method if u need change the layout
    public void setLayout() {
        setContentView(R.layout.activity_comments);
    }

    @Override
    public AnswerCommentListAdapter getAdapter() {
        return new AnswerCommentListAdapter(this);
    }

    public void initViews() {
        //set data
        Intent i = getIntent();
        answerId = i.getStringExtra(CONST.ANSWER_ID);
        answerCommentsNum = i.getIntExtra(CONST.ANSWER_COMMENT_NUM, 0);

        activityTitle = findViewById(R.id.activity_title);
        activityTitle.setText(Html.fromHtml("<html><body><font color=#666666>" + "评论" + " </font> <font color=#999999>" + (answerCommentsNum) + " </font> </body><html>"));
        answerIcon = findViewById(R.id.icon_answer);
        publicButton = findViewById(R.id.public_button);
        writeComment = findViewById(R.id.write_comment);
        questionTitleTv = findViewById(R.id.title);
        answerContentTv = findViewById(R.id.content);
    }

    public void setListenerToRootView() {
        final View activityRootView = getWindow().getDecorView().findViewById(android.R.id.content);
        activityRootView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) writeComment.getLayoutParams();
                int heightDiff = activityRootView.getRootView().getHeight() - activityRootView.getHeight();
                if (heightDiff > 100) { // 99% of the time the height diff will be due to a keyboard.
                    //keyboard is up
                    params.leftMargin = (int) ZR.convertDpToPx(15);
                    answerIcon.setVisibility(View.GONE);
                    if (isOpened == false) {
                        //Do two things, make the view top visible and the editText smaller
                    }
                    isOpened = true;
                } else if (isOpened == true) {
                    //keyboard is down
                    params.leftMargin = (int) ZR.convertDpToPx(5);
                    writeComment.clearFocus();
                    answerIcon.setVisibility(View.VISIBLE);
                    isOpened = false;
                }
            }
        });
    }

    public void initEvents() {
        setListenerToRootView();
        setPublicClickListener();
        writeComment.setOnKeyboardListener(new KeyboardEditText.KeyboardListener() {
            @Override
            public void onStateChanged(KeyboardEditText keyboardEditText, boolean showing) {
                LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) writeComment.getLayoutParams();
                if (showing) {
                    params.leftMargin = (int) ZR.convertDpToPx(15);
                    answerIcon.setVisibility(View.GONE);
                } else {
                    params.leftMargin = (int) ZR.convertDpToPx(5);
                    writeComment.clearFocus();
                    answerIcon.setVisibility(View.VISIBLE);
                }
            }
        });
        writeComment.setShowSoftInputOnFocus(true);
        writeComment.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().trim().length() > 0) {
                    commentContent = s.toString().trim();
                    publicButton.setTextColor(ZR.getColor(R.color.color_489dfff));
                    publicButton.setEnabled(true);
                } else {
                    publicButton.setTextColor(ZR.getColor(R.color.color_cccccc));
                    publicButton.setEnabled(false);
                }
            }
        });
    }

    private void setPublicClickListener() {
        publicButton.setOnClickListener(new ZClickListenerWithLoginCheck() {
            @Override
            public void click(View v) {
                if (replyComment != null || replyReply != null) {
                    CommentParams params = new CommentParams();
                    params.content = commentContent;
                    //answer reply reply
                    params.objType = 1;
                    if (replyComment != null) {
                        params.toUserId = replyComment.userId;
                        params.commentId = replyComment.qACommentId;

                        if (ZPreference.getUserId().equals(replyComment.userId)) {
                            ZToast.toast("自己不能回复自己");
                            //reset to null
                            replyComment = null;
                            return;
                        }
                        //reset to null
                        replyComment = null;

                    } else if (replyReply != null) {
                        params.commentId = replyReply.commentId;
                        params.toUserId = replyReply.fromUser.userId;
                        params.replyId = replyReply.replyId;

                        if (ZPreference.getUserId().equals(replyReply.fromUser.userId)) {
                            ZToast.toast("自己不能回复自己");
                            //reset to null
                            replyComment = null;
                            return;
                        }
                        //reset to null
                        replyReply = null;
                    }

                    MyLog.d("commentReply:" + params.toString());

                    APIClient.commentReply(params, new ZCallBackWithFail<ResponseModel<NCReply>>() {
                        @Override
                        public void callBack(ResponseModel<NCReply> response) throws Exception {
                            MyLog.d("comment reply : " + failed + " replyRecyclerView visibility: " + (replyRecyclerView.getVisibility() == View.VISIBLE));
                            if (failed) {

                            } else {
                                if (response._data == null) {
                                    return;
                                }
                                writeComment.setText("");
                                if (replyListAdapter.mList == null || replyListAdapter.mList.isEmpty()) {
                                    loadData(1);
                                } else {
                                    MyLog.d("reply list adapter mlist size before : " + replyListAdapter.mList.size());
                                    replyListAdapter.mList.add(0, response._data);
                                    MyLog.d("reply list adapter mlist size after : " + replyListAdapter.mList.size());
                                    replyListAdapter.notifyItemInserted(0);
//                                replyRecyclerView.scrollToPosition(0);
                                }
                                Toast.makeText(AnswerCommentsActivity.this, "添加成功", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
                //reply answer
                else {
                    QAnswerParams p = new QAnswerParams();
                    p.questAnswerId = answerId;
                    p.content = commentContent;
                    p.optType = 1;
                    APIClient.qACommentPub(p, new ZCallBack<ResponseModel<QAComment>>() {
                        @Override
                        public void callBack(ResponseModel<QAComment> response) {
                            emptyLayout.setVisibility(View.GONE);
                            mPullToLoadView.setVisibility(View.VISIBLE);
                            writeComment.setText("");
                            mAdapter.mList.add(0, response._data);
                            mAdapter.notifyItemInserted(0);
                            mRecyclerView.scrollToPosition(0);
//                            activityTitle.setText("评论" + mAdapter.getItemCount());
                            activityTitle.setText(Html.fromHtml("<html><body><font color=#666666>" + "评论" + " </font> <font color=#999999>" + (mAdapter.getItemCount()) + " </font> </body><html>"));
                            Toast.makeText(AnswerCommentsActivity.this, "添加成功", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
    }

    public void loadData(final int page) {
        loadAnswerComments(page);
       /* new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (page > 3) {
                    Toast.makeText(CommentsActivity.this, "没有更多数据了",
                            Toast.LENGTH_SHORT).show();
                    isHasLoadedAll = true;
                    return;
                }
                for (int i = 0; i <= 15; i++) {
                    mAdapter.add(i + "");
                }

                isLoading = false;
                nextPage = page + 1;
            }
        }, 1);*/
    }

    private void loadAnswerComments(final int page) {
        QAnswerParams params = new QAnswerParams();
        params.questAnswerId = answerId;
        APIClient.qACommentList(params, new ZCallBackWithFail<ResponseModel<QuestionAnswer>>() {
            @Override
            public void callBack(ResponseModel<QuestionAnswer> response) {
                if (failed) {
                    mPullToLoadView.setComplete();
                    isLoading = false;

                } else {
                    QuestionAnswer answer = response._data;
                    List<QAComment> comments = answer.qaCommentList;
                    questionTitleTv.setText(answer.title);
                    answerContentTv.setText(answer.content);
//                isLoading = false;
//                if (comments.size() == 0) {
//                    ZToast.toast("没有更多数据了");
//                    return;
//                }
                    if (comments != null) {
//                        activityTitle.setText("评论" + comments.size());
                        activityTitle.setText(Html.fromHtml("<html><body><font color=#666666>" + "评论" + " </font> <font color=#999999>" + (comments.size()) + " </font> </body><html>"));
                    }
//                mAdapter.addAll(comments);
//                nextPage = page + 1;

                    setData2Adapter(comments);
                }
            }
        });
    }

    public void clickBack(final View view) {
        onBackPressed();
    }

    public void clickWriteComment(QAComment c, AnswerReplyListAdapter adapter, RecyclerView replyRecyclerView) {
        this.replyRecyclerView = replyRecyclerView;
        replyListAdapter = adapter;
        commentFlag = 1;
        replyComment = c;
        showKeyBoard(c.userName);
    }

    public void clickReply(final NCReply reply, AnswerReplyListAdapter adapter, RecyclerView replyRecyclerView) {
        this.replyRecyclerView = replyRecyclerView;
        replyListAdapter = adapter;
        commentFlag = 2;
        replyReply = reply;

        showKeyBoard(reply.fromUser.userName);
    }

    public void showKeyBoard(String userName) {
        writeComment.setHint("回复" + userName);
        writeComment.performClick();
        writeComment.requestFocus();
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(writeComment.getWindowToken(), 0);
        new Handler().postDelayed(new Runnable() {
            public void run() {
                writeComment.dispatchTouchEvent(MotionEvent.obtain(SystemClock.uptimeMillis(), SystemClock.uptimeMillis(), MotionEvent.ACTION_DOWN, 0, 0, 0));
                writeComment.dispatchTouchEvent(MotionEvent.obtain(SystemClock.uptimeMillis(), SystemClock.uptimeMillis(), MotionEvent.ACTION_UP, 0, 0, 0));
            }
        }, 200);
    }

}
