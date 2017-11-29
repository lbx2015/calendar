package com.riking.calendar.viewholder;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.riking.calendar.R;
import com.riking.calendar.view.CircleImageView;

import butterknife.BindView;
import butterknife.ButterKnife;


public class OneTextViewHolder extends RecyclerView.ViewHolder {
    @BindView(R.id.text_view)
    public TextView textView;
    public View divider;

    public OneTextViewHolder(View itemView) {
        super(itemView);
        textView = itemView.findViewById(R.id.text_view);
        divider = itemView.findViewById(R.id.divider);
        itemView.setTag(this);
    }
}
