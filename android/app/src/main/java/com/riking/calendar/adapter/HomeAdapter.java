package com.riking.calendar.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.necer.ncalendar.utils.MyLog;
import com.riking.calendar.R;
import com.riking.calendar.activity.AnswerCommentsActivity;
import com.riking.calendar.activity.TopicActivity;
import com.riking.calendar.activity.UserActivity;
import com.riking.calendar.activity.WriteAnswerActivity;
import com.riking.calendar.listener.ZCallBack;
import com.riking.calendar.listener.ZClickListenerWithLoginCheck;
import com.riking.calendar.pojo.base.ResponseModel;
import com.riking.calendar.pojo.params.HomeParams;
import com.riking.calendar.pojo.params.QAnswerParams;
import com.riking.calendar.pojo.params.TQuestionParams;
import com.riking.calendar.pojo.server.TQuestionResult;
import com.riking.calendar.retrofit.APIClient;
import com.riking.calendar.util.CONST;
import com.riking.calendar.util.StringUtil;
import com.riking.calendar.util.ZGoto;
import com.riking.calendar.util.ZR;
import com.riking.calendar.util.ZToast;
import com.riking.calendar.viewholder.HomeViewHolder;
import com.riking.calendar.viewholder.RecommendedViewHolder;
import com.riking.calendar.widget.dialog.ShareBottomDialog;

import java.util.ArrayList;
import java.util.List;

public class HomeAdapter extends RecyclerView.Adapter {

    public static final int REMMEND_TYPE = 2;
    public List<TQuestionResult> mList;
    private Context context;

    public HomeAdapter(Context context) {
        this.context = context;
        mList = new ArrayList<>();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view;
        if (viewType == REMMEND_TYPE) {
            view = LayoutInflater.from(viewGroup.getContext()).inflate(
                    R.layout.topic_suggestion_item, viewGroup, false);
            return new RecommendedViewHolder(view);
        } else {
            view = LayoutInflater.from(viewGroup.getContext()).inflate(
                    R.layout.home_item, viewGroup, false);
            return new HomeViewHolder(view);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (mList.get(position).pushType == 6 || mList.get(position).pushType == 7) {
            return REMMEND_TYPE;
        }
        return super.getItemViewType(position);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder cellHolder, int i) {
        final TQuestionResult r = mList.get(i);

        if (getItemViewType(i) == REMMEND_TYPE) {
            MyLog.d("onBindViewHolderr at " + i + " and the view type is " + getItemViewType(i));
            RecommendedViewHolder h = (RecommendedViewHolder) cellHolder;
            h.recyclerView.setLayoutManager(new LinearLayoutManager(h.recyclerView.getContext(), LinearLayoutManager.HORIZONTAL, false));
            RecommendedAdapter adapter = new RecommendedAdapter();
            adapter.type = r.pushType;
            switch (r.pushType) {
                //interesting topics
                case 6: {
                    h.titleTV.setText("你可能感兴趣的话题");
                    adapter.topicResults = r.topicResults;
                    break;
                }
                //interesting person
                case 7: {
                    h.titleTV.setText("你可能感兴趣的人");
                    adapter.appUserResults = r.appUserResults;
                    break;
                }
            }
            h.recyclerView.setAdapter(adapter);

        } else {
            final HomeViewHolder h = (HomeViewHolder) cellHolder;
            //type 1 : from topic
            if (r.pushType == 1) {
                h.itemCator.setText("来自话题" + r.topicTitle);
                setAnswerAgreeAndComment(h, r);
            }
            //followed user agree answer
            else if (r.pushType == 2) {
                h.itemCator.setText(r.userName + "攒了回答");
                setAnswerAgreeAndComment(h, r);
            }
            //followed user follow question
            else if (r.pushType == 3) {
                h.itemCator.setText(r.userName + "关注了问题");
                setQuestionFollowAndReply(h, r);
            }
            //followed user answer a question
            else if (r.pushType == 4) {
                h.itemCator.setText(r.userName + "回答了问题");
                setAnswerAgreeAndComment(h, r);
            } else if (r.pushType == 5) {
                h.itemCator.setText(r.userName + "收藏了问题");
                setAnswerAgreeAndComment(h, r);
            }

            //set the answer data
            setAnswerData(h, r);


            h.moreAction.setOnClickListener(new View.OnClickListener() {
                @Override
                @SuppressLint("RestrictedApi")
                public void onClick(View v) {
                    final ShareBottomDialog shareBottomDialog = new ShareBottomDialog(h.moreAction.getContext());
                    shareBottomDialog.shieldButton.setOnClickListener(new ZClickListenerWithLoginCheck() {
                        @Override
                        public void click(View v) {
                            shareBottomDialog.dismiss();
                            HomeParams p = new HomeParams();
                            p.enabled = 0;//屏蔽
                            p.objId = r.qaId;
                            APIClient.shieldQuestion(p, new ZCallBack<ResponseModel<String>>() {
                                @Override
                                public void callBack(ResponseModel<String> response) {
                                    //remove the layout position
                                    mList.remove(h.getLayoutPosition());
                                    notifyItemRemoved(h.getLayoutPosition());
                                }
                            });
                        }
                    });
                    shareBottomDialog.show();
          /*          //Creating the instance of PopupMenu
                    final PopupMenu popup = new PopupMenu(context, h.moreAction, Gravity.RIGHT);
                    //Inflating the Popup using xml file
                    popup.getMenuInflater().inflate(R.menu.popup_menu, popup.getMenu());
                    //noinspection RestrictedApi
                    MenuPopupHelper menuHelper = new MenuPopupHelper(context, (MenuBuilder) popup.getMenu(), h.moreAction);
                    menuHelper.setForceShowIcon(true);

                    //registering popup with OnMenuItemClickListener
                    popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            switch (item.getItemId()) {
                                case R.userId.share: {
                                    new ShareBottomDialog(h.moreAction.getContext()).show();
//                                    Intent textIntent = new Intent(Intent.ACTION_SEND);
//                                    textIntent.setType("text/plain");
//                                    textIntent.putExtra(Intent.EXTRA_TEXT, "http://www.baidu.com");
//                                    MyApplication.mCurrentActivity.startActivity(Intent.createChooser(textIntent, "分享到..."));
                                    break;
                                }
                                case R.userId.prevent: {
                                    break;
                                }
                            }
                            return true;
                        }
                    });
                    popup.show();*/
                }
            });
        }
    }

    private void setAnswerData(final HomeViewHolder h, final TQuestionResult r) {
        //set answer content
        if (StringUtil.isEmpty(r.qaContent)) {
            h.answerContent.setVisibility(View.GONE);
        } else {
            h.answerContent.setVisibility(View.VISIBLE);
            h.answerContent.setText(r.qaContent);
        }
        //set question title
        h.questionTitle.setText(r.tqTitle);

        //set answer cover image
        if (StringUtil.isEmpty(r.coverUrl)) {
            h.answerImage.setVisibility(View.GONE);
        } else {
            h.answerImage.setVisibility(View.VISIBLE);
            ZR.setImage(h.answerImage, r.coverUrl);
        }

        //set from image
        ZR.setUserImage(h.fromImage, r.fromImgUrl);

        //go to topic on click
        h.itemCator.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //go to topic detail on click
                if (r.pushType == 1) {
                    Intent i = new Intent(context, TopicActivity.class);
                    i.putExtra(CONST.TOPIC_ID, r.topicId);
                    ZGoto.to(i);
                } else if ((r.pushType == 2 || r.pushType == 3 || r.pushType == 4 || r.pushType == 5)) {
                    Intent i = new Intent(h.itemCator.getContext(), UserActivity.class);
                    i.putExtra(CONST.USER_ID, r.userId);
                    ZGoto.to(i);
                }
            }
        });

        //go to question activity on click
        ZR.setRequestClickListener(h.questionTitle, r.tqId);

        //go to answer activity on click
        ZR.setAnswerClickListener(h.answerContent, r.qaId);
    }


    private void setQuestionFollowAndReply(final HomeViewHolder h, final TQuestionResult r) {
        //set the follow number of the question
        h.firstTextIcon.setText(ZR.getNumberString(r.qfollowNum));
        //set the answer number of the question
        h.secondTextIcon.setText(ZR.getNumberString(r.qanswerNum));

        //follow icon
        if (r.status == 1) {
            h.firstTextIcon.setCompoundDrawablesWithIntrinsicBounds(R.drawable.com_icon_follow_p, 0, 0, 0);
        } else {
            h.firstTextIcon.setCompoundDrawablesWithIntrinsicBounds(R.drawable.com_icon_follow_n, 0, 0, 0);
        }

        h.secondTextIcon.setCompoundDrawablesWithIntrinsicBounds(R.drawable.com_icon_answer, 0, 0, 0);

        //set follow listener
        setFollowQuestionClick(h.firstTextIcon, r);

        //go to comment list activity on cick
        h.secondTextIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(h.secondTextIcon.getContext(), WriteAnswerActivity.class);
                i.putExtra(CONST.ANSWER_ID, r.qaId);
                i.putExtra(CONST.QUESTION_TITLE,r.tqTitle);
                ZGoto.to(i);
            }
        });
    }

    private void setAnswerAgreeAndComment(final HomeViewHolder h, final TQuestionResult r) {
        //set the answer comment number
        h.firstTextIcon.setText(ZR.getNumberString(r.qaCommentNum));
        //set the answer agree number
        h.secondTextIcon.setText(ZR.getNumberString(r.qaAgreeNum));

        if (r.status == 1) {
            h.secondTextIcon.setCompoundDrawablesWithIntrinsicBounds(R.drawable.com_icon_zan_p, 0, 0, 0);
        } else {
            h.secondTextIcon.setCompoundDrawablesWithIntrinsicBounds(R.drawable.com_icon_zan_n, 0, 0, 0);
        }

        h.firstTextIcon.setCompoundDrawablesWithIntrinsicBounds(R.drawable.com_icon_comment, 0, 0, 0);

        //set agree listener
        setAgreeClick(h.secondTextIcon, r);

        //go to comment list activity on cick
        h.firstTextIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(h.firstTextIcon.getContext(), AnswerCommentsActivity.class);
                i.putExtra(CONST.ANSWER_ID, r.qaId);
                i.putExtra(CONST.ANSWER_COMMENT_NUM, r.qaCommentNum);
                ZGoto.to(i);
            }
        });
    }

    private void setFollowQuestionClick(final TextView followTv, final TQuestionResult r) {
        final TQuestionParams params = new TQuestionParams();
        params.attentObjId = r.tqId;
        //question
        params.objType = 1;
        //followed
        if (r.status == 1) {
            params.enabled = 0;
        } else {
            params.enabled = 1;
        }
        followTv.setOnClickListener(new ZClickListenerWithLoginCheck() {
            @Override
            public void click(View v) {
                APIClient.follow(params, new ZCallBack<ResponseModel<String>>() {
                    @Override
                    public void callBack(ResponseModel<String> response) {
                        r.status = params.enabled;
                        if (r.status == 1) {
                            followTv.setCompoundDrawablesWithIntrinsicBounds(R.drawable.com_icon_follow_p, 0, 0, 0);
                            ZToast.toast("关注成功");
                        } else {
                            followTv.setCompoundDrawablesWithIntrinsicBounds(R.drawable.com_icon_follow_n, 0, 0, 0);
                            ZToast.toast("取消关注");
                        }
                    }
                });
            }
        });
    }

    private void setAgreeClick(final TextView agreeTv, final TQuestionResult r) {
        agreeTv.setOnClickListener(new ZClickListenerWithLoginCheck() {
            @Override
            public void click(View v) {
                final QAnswerParams p = new QAnswerParams();
                p.questAnswerId = r.qaId;
                //answer agree type
                p.optType = 1;
                if (r.status == 1) {
                    p.enabled = 0;
                } else {
                    p.enabled = 1;
                }
                APIClient.qAnswerAgree(p, new ZCallBack<ResponseModel<String>>() {
                    @Override
                    public void callBack(ResponseModel<String> response) {
                        if (p.enabled == 1) {
                            r.status = 1;
                            r.qaAgreeNum = r.qaAgreeNum + 1;
                            agreeTv.setText(ZR.getNumberString(r.qaAgreeNum));
                            agreeTv.setCompoundDrawablesWithIntrinsicBounds(R.drawable.com_icon_zan_p, 0, 0, 0);
                            Toast.makeText(agreeTv.getContext(), "点赞成功", Toast.LENGTH_SHORT).show();
                        } else {
                            r.status = 0;
                            r.qaAgreeNum = r.qaAgreeNum - 1;
                            agreeTv.setText(ZR.getNumberString(r.qaAgreeNum));
                            ZToast.toast("取消点赞");
                            agreeTv.setCompoundDrawablesWithIntrinsicBounds(R.drawable.com_icon_zan_n, 0, 0, 0);
                        }
                    }
                });
            }
        });
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public void add(TQuestionResult s) {
        mList.add(s);
        notifyDataSetChanged();
    }

    public void clear() {
        mList.clear();
        notifyDataSetChanged();
    }
}
