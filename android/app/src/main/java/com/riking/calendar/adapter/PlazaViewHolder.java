package com.riking.calendar.adapter;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.lzy.ninegrid.NineGridView;
import com.riking.calendar.R;
import com.riking.calendar.view.CircleImageView;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * Created by xiangzhihong on 2016/3/2 on 15:49.
 */
public class PlazaViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.item_view)
    View itemView;
    @BindView(R.id.item_cator)
    TextView itemCator;
    @BindView(R.id.cardView)
    CardView cardView;
    @BindView(R.id.item_content)
    TextView itemContent;
    @BindView(R.id.item_image)
    CircleImageView itemImage;
    @BindView(R.id.grid_images)
    NineGridView nineGridView;
//    @BindView(R.id.item_count)
//    TextView itemCount;

    public PlazaViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        itemView.setTag(this);
    }

    private void resetView() {

    }
}