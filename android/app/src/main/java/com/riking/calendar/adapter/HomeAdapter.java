package com.riking.calendar.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.necer.ncalendar.utils.MyLog;
import com.riking.calendar.R;
import com.riking.calendar.activity.AnswerActivity;
import com.riking.calendar.activity.QuestionActivity;
import com.riking.calendar.activity.TopicActivity;
import com.riking.calendar.app.MyApplication;
import com.riking.calendar.listener.ZCallBack;
import com.riking.calendar.listener.ZClickListenerWithLoginCheck;
import com.riking.calendar.pojo.base.ResponseModel;
import com.riking.calendar.pojo.params.HomeParams;
import com.riking.calendar.retrofit.APIClient;
import com.riking.calendar.util.CONST;
import com.riking.calendar.util.ZGoto;
import com.riking.calendar.viewholder.HomeViewHolder;
import com.riking.calendar.viewholder.RecommendedViewHolder;
import com.riking.calendar.widget.dialog.ShareBottomDialog;

import java.util.ArrayList;
import java.util.List;


public class HomeAdapter extends RecyclerView.Adapter {

    public static final int REMMEND_TYPE = 2;
    private Context context;
    private List<String> mList;

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
        if (position == 5) {
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
            final HomeViewHolder h = (HomeViewHolder) cellHolder;
            if (i % 2 == 1) {
                h.itemCator.setText("来自话题G01报表");
            } else {
                h.itemCator.setText("Lucy关注了问题");
            }
            RequestOptions options = new RequestOptions();
            Glide.with(h.itemImage.getContext()).load(R.drawable.img_user_head)
                    .apply(options.fitCenter())
                    .into(h.itemImage);
            h.itemCator.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    context.startActivity(new Intent(context, TopicActivity.class));
                    Intent i = new Intent(context, TopicActivity.class);
                    i.putExtra(CONST.TOPIC_ID, "1");
                    ZGoto.to(i);
                }
            });

            h.title.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ZGoto.to(QuestionActivity.class);
//                    context.startActivity(new Intent(context, QuestionActivity.class));
                }
            });
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

            h.itemContent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MyApplication.mCurrentActivity.startActivity(new Intent(context, AnswerActivity.class));
                }
            });

        }

    }


    @Override
    public int getItemCount() {
        return mList.size();
    }

    public void add(String s) {
        mList.add(s);
        notifyDataSetChanged();
    }

    public void clear() {
        mList.clear();
        notifyDataSetChanged();
    }
}
