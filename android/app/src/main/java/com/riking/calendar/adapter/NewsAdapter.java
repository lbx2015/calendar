package com.riking.calendar.adapter;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.riking.calendar.R;

/**
 * Created by zw.zhang on 2017/7/12.
 */

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.MyViewHolder> {


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.news_cardview_row, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        holder.personName.setText("dfldksflkds");
        holder.personAge.setText("CardView_cardBackgroundColor 设置背景色\n" +
                "CardView_cardCornerRadius 设置圆角大小\n" +
                "CardView_cardElevation 设置z轴阴影\n" +
                "CardView_cardMaxElevation 设置z轴最大高度值\n" +
                "CardView_cardUseCompatPadding 是否使用CompadPadding\n" +
                "CardView_cardPreventCornerOverlap 是否使用PreventCornerOverlap\n" +
                "CardView_contentPadding 内容的padding\n" +
                "CardView_contentPaddingLeft 内容的左padding\n" +
                "CardView_contentPaddingTop 内容的上padding\n" +
                "CardView_contentPaddingRight 内容的右padding\n" +
                "CardView_contentPaddingBottom 内容的底padding  l");
//        holder.personPhoto.setImageResource(R.drawable.default_user_icon);
        RequestOptions options = new RequestOptions();
        Glide.with(holder.personPhoto.getContext()).load(R.drawable.default_user_icon)
                .apply(options.centerCrop())
                .into(holder.personPhoto);
//        holder.cv.setBackgroundColor(Color.WHITE);
    }

    @Override
    public int getItemCount() {
        return 100;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView personName;
        TextView personAge;
        ImageView personPhoto;
        CardView cv;

        public MyViewHolder(View view) {
            super(view);
            cv = (CardView) itemView.findViewById(R.id.cv);
            personName = (TextView) itemView.findViewById(R.id.person_name);
            personAge = (TextView) itemView.findViewById(R.id.person_age);
            personPhoto = (ImageView) itemView.findViewById(R.id.person_photo);
        }
    }
}
