package com.riking.calendar.adapter;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.necer.ncalendar.utils.MyLog;
import com.riking.calendar.R;
import com.riking.calendar.viewholder.RecommendedViewHolder;

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
        if (viewType == REMMEND_TYPE) {
            RecyclerView recyclerView = new RecyclerView(viewGroup.getContext());
            recyclerView.setId(R.id.recycler_view);
            return new RecommendedViewHolder(recyclerView);
        } else {
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(
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
    public void onBindViewHolder(RecyclerView.ViewHolder cellHolder, int i) {
        if (getItemViewType(i) == REMMEND_TYPE) {
            MyLog.d("onBindViewHolderr at " + i + " and the view type is " + getItemViewType(i));
            RecommendedViewHolder h = (RecommendedViewHolder) cellHolder;
            h.recyclerView.setLayoutManager(new LinearLayoutManager(h.recyclerView.getContext(), LinearLayoutManager.HORIZONTAL, false));
            h.recyclerView.setAdapter(new RecommendedAdapter());

        } else {
            HomeViewHolder h = (HomeViewHolder) cellHolder;
            if (getItemCount() % 2 == 1) {
                h.itemCator.setText("热门回答");
            } else {
                h.itemCator.setText("知乎回答");
            }
            h.itemImage.setBorderWidth(2);
            h.itemImage.setBorderColor(h.itemImage.getResources().getColor(R.color.colorPrimary));
            RequestOptions options = new RequestOptions();
            Glide.with(h.itemImage.getContext()).load(R.drawable.img_user_head)
                    .apply(options.fitCenter())
                    .into(h.itemImage);
            h.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//             context.startActivity(new Intent(context, DetailsActivity.class));
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
