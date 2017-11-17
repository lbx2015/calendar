package com.riking.calendar.viewholder;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.riking.calendar.R;
import com.riking.calendar.view.CircleImageView;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * Created by xiangzhihong on 2016/3/2 on 15:49.
 */
public class HomeViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.item_view)
    public View itemView;
    @BindView(R.id.item_cator)
    public TextView itemCator;
    @BindView(R.id.cardView)
    public CardView cardView;
    @BindView(R.id.item_content)
    public TextView itemContent;
    @BindView(R.id.item_image)
    public CircleImageView itemImage;
    @BindView(R.id.fromPhone)
    public TextView fromPhone;

    @BindView(R.id.more_action)
    public TextView moreAction;
//    @BindView(R.id.item_count)
//    TextView itemCount;

    public HomeViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        itemView.setTag(this);
    }

    private void resetView() {

    }
}
