package com.riking.calendar.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.riking.calendar.R;
import com.riking.calendar.adapter.base.ZAdater;
import com.riking.calendar.pojo.server.QAnswerResult;
import com.riking.calendar.util.DateUtil;
import com.riking.calendar.util.ZR;
import com.riking.calendar.view.CircleImageView;
import com.riking.calendar.viewholder.base.ZViewHolder;

import butterknife.BindView;
import butterknife.ButterKnife;

//answer comment adapter
public class MyCollectAnswerAdapter extends ZAdater<MyCollectAnswerAdapter.MyViewHolder, QAnswerResult> {
    @Override
    public void onBindVH(MyViewHolder h, int i) {
        QAnswerResult appUser = mList.get(i);
        ZR.setUserName(h.itemCator, appUser.userName, appUser.grade);
        ZR.setUserImage(h.fromImage, appUser.photoUrl);
        h.questionTitle.setText(appUser.title);
        h.answerContent.setText(appUser.content);
        h.timeTv.setText(DateUtil.showTime(appUser.createdTime));
    }

    @Override
    public MyViewHolder onCreateVH(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(
                R.layout.my_collect_answer_item, viewGroup, false);
        return new MyCollectAnswerAdapter.MyViewHolder(view);
    }

    class MyViewHolder extends ZViewHolder {
        @BindView(R.id.item_cator)
        public TextView itemCator;
        @BindView(R.id.answer_content)
        public TextView answerContent;
        @BindView(R.id.answer_image)
        public ImageView answerImage;
        @BindView(R.id.from_image)
        public CircleImageView fromImage;

        @BindView(R.id.answer_title)
        public TextView questionTitle;
        @BindView(R.id.review_number)
        public TextView firstTextIcon;
        @BindView(R.id.agree_number)
        public TextView secondTextIcon;
        @BindView(R.id.time)
        public TextView timeTv;

        public MyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setTag(this);
        }
    }
}
