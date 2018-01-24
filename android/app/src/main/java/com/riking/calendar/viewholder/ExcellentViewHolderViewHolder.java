package com.riking.calendar.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.riking.calendar.R;
import com.riking.calendar.view.CircleImageView;
import com.riking.calendar.viewholder.base.ZUserBaseViewHolder;
import com.riking.calendar.viewholder.base.ZViewHolder;

import butterknife.BindView;
import butterknife.ButterKnife;


public class ExcellentViewHolderViewHolder extends ZUserBaseViewHolder {

    @BindView(R.id.divider)
    public View divider;

    public boolean invited;

    public ExcellentViewHolderViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        itemView.setTag(this);
    }
}
