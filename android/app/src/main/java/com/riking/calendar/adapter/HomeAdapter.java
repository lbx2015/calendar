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
import com.riking.calendar.activity.AnswerActivity;
import com.riking.calendar.activity.QuestionActivity;
import com.riking.calendar.activity.TopicActivity;
import com.riking.calendar.listener.ZCallBack;
import com.riking.calendar.listener.ZClickListenerWithLoginCheck;
import com.riking.calendar.pojo.base.ResponseModel;
import com.riking.calendar.pojo.params.HomeParams;
import com.riking.calendar.pojo.params.QAnswerParams;
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
        if (mList.get(position).pushType > 5) {
            return REMMEND_TYPE;
        }
        return super.getItemViewType(position);
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder cellHolder, final int i) {
        if (getItemViewType(i) == REMMEND_TYPE) {
            MyLog.d("onBindViewHolderr at " + i + " and the view type is " + getItemViewType(i));
            RecommendedViewHolder h = (RecommendedViewHolder) cellHolder;
            h.recyclerView.setLayoutManager(new LinearLayoutManager(h.recyclerView.getContext(), LinearLayoutManager.HORIZONTAL, false));
            h.recyclerView.setAdapter(new RecommendedAdapter());

        } else {
            final TQuestionResult r = mList.get(i);
            final HomeViewHolder h = (HomeViewHolder) cellHolder;
            //type 1 : from topic
            if (r.pushType == 1) {
                h.itemCator.setText("来自话题" + r.topicTitle);
                if (StringUtil.isEmpty(r.qaContent)) {
                    h.answerContent.setVisibility(View.GONE);
                } else {
                    h.answerContent.setVisibility(View.VISIBLE);
                    h.answerContent.setText(r.qaContent);
                }
                h.answerTitle.setText(r.tqTitle);
                if (r.coverUrl == null) {
                    h.answerImage.setVisibility(View.GONE);
                } else {
                    h.answerImage.setVisibility(View.VISIBLE);
                    ZR.setAnswerImage(h.answerImage, r.coverUrl);
                }

                //set from image
                ZR.setUserImage(h.fromImage, r.fromImgUrl);

                h.reviewTv.setText(ZR.getNumberString(r.qaCommentNum));
                h.agreeTv.setText(ZR.getNumberString(r.qaAgreeNum));

                if (r.isAgree == 1) {
                    h.agreeTv.setCompoundDrawablesWithIntrinsicBounds(R.drawable.com_icon_zan_p, 0, 0, 0);
                } else {
                    h.agreeTv.setCompoundDrawablesWithIntrinsicBounds(R.drawable.com_icon_zan_n, 0, 0, 0);
                }
                //go to topic on click
                h.itemCator.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent(context, TopicActivity.class);
                        i.putExtra(CONST.TOPIC_ID, r.topicId);
                        ZGoto.to(i);
                    }
                });

                //go to question activity on click
                h.answerTitle.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent(context, QuestionActivity.class);
                        i.putExtra(CONST.QUESTION_ID, r.tqId);
                        ZGoto.to(i);
                    }
                });

                //set agree listener
                setAgreeClick(h.agreeTv, r);

                //go to answer activity on click
                h.answerContent.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent(context, AnswerActivity.class);
                        i.putExtra(CONST.ANSWER_ID, r.tqId);
                        ZGoto.to(i);
                    }
                });
            }


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
                            APIClient.shieldQuestion(p, new ZCallBack<ResponseModel<String>>() {
                                @Override
                                public void callBack(ResponseModel<String> response) {
                                    mList.remove(i);
                                    notifyItemRemoved(i);
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

    private void setAgreeClick(final TextView agreeTv, final TQuestionResult r) {
        agreeTv.setOnClickListener(new ZClickListenerWithLoginCheck() {
            @Override
            public void click(View v) {
                final QAnswerParams p = new QAnswerParams();
                p.questAnswerId = r.qaId;
                //answer agree type
                p.optType = 1;
                if (r.isAgree == 1) {
                    p.enabled = 0;
                } else {
                    p.enabled = 1;
                }
                APIClient.qAnswerAgree(p, new ZCallBack<ResponseModel<String>>() {
                    @Override
                    public void callBack(ResponseModel<String> response) {
                        if (p.enabled == 1) {
                            r.isAgree = 1;
                            r.qaAgreeNum = r.qaAgreeNum + 1;
                            agreeTv.setText(ZR.getNumberString(r.qaAgreeNum));
                            agreeTv.setCompoundDrawablesWithIntrinsicBounds(R.drawable.com_icon_zan_p, 0, 0, 0);
                            Toast.makeText(agreeTv.getContext(), "点赞成功", Toast.LENGTH_SHORT).show();
                        } else {
                            r.isAgree = 0;
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
