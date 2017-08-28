package com.riking.calendar.adapter;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.riking.calendar.R;
import com.riking.calendar.fragment.SecondFragment;
import com.riking.calendar.pojo.CtryHdayCrcy;
import com.riking.calendar.pojo.CtryHdayCryCondition;
import com.riking.calendar.task.LoadUserImageTask;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by zw.zhang on 2017/7/12.
 */

public class VocationRecyclerViewAdapter extends RecyclerView.Adapter<VocationRecyclerViewAdapter.MyViewHolder> {
    //两个final int类型表示ViewType的两种类型
    private final int NORMAL_TYPE = 0;
    private final int FOOT_TYPE = 1111;
    public boolean lastPage;
    SecondFragment fragment;
    private List<CtryHdayCrcy> vocationList;
    private Boolean isFootView = false;//是否添加了FootView
    private String footViewText = "";//FootView的内容

    public VocationRecyclerViewAdapter(List<CtryHdayCrcy> r) {
        this.vocationList = r;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.vocation_row, parent, false);
        View footView = LayoutInflater.from(parent.getContext()).inflate(R.layout.foot_view, parent, false);
        if (viewType == FOOT_TYPE) return new MyViewHolder(footView, FOOT_TYPE);

        return new MyViewHolder(itemView, NORMAL_TYPE);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        if (isFootView && (getItemViewType(position) == FOOT_TYPE)) {
            if (lastPage) {
                //last page
                holder.tvFootView.setText("没有了。");
                return;
            }
            holder.tvFootView.setText(footViewText);
            fragment.requestBoday.pindex++;
            fragment.apiInterface.getMore(fragment.requestBoday).enqueue(new Callback<CtryHdayCryCondition>() {
                @Override
                public void onResponse(Call<CtryHdayCryCondition> call, Response<CtryHdayCryCondition> response) {
                    CtryHdayCryCondition ctryHdayCryCondition = response.body();
                    if (ctryHdayCryCondition == null) {
                        return;
                    }

                    if (ctryHdayCryCondition._data.last) {
                        holder.tvFootView.setText("没有了。");
                        lastPage = true;
                    }
                    vocationList.addAll(ctryHdayCryCondition._data.content);
                    notifyItemRangeInserted(vocationList.size(), ctryHdayCryCondition._data.content.size());
                    Log.d("zzw", "CtryHdayCryCondition success: " + ctryHdayCryCondition);
                }

                @Override
                public void onFailure(Call<CtryHdayCryCondition> call, Throwable t) {
                }
            });
        } else {
            CtryHdayCrcy r = vocationList.get(position);
            holder.date.setText(r.hdayDate);
            holder.country.setText(r.ctryNameValue);
            holder.currency.setText(r.crcy);
            holder.vocation.setText(r.hdayNameValue);
            LoadUserImageTask myTask = new LoadUserImageTask();
            myTask.imageView = holder.countryImage;
            myTask.execute(r.flagUrl);
        }
    }

    //创建一个方法来设置footView中的文字
    public void setFootViewText(String footViewText, SecondFragment fragment) {
        isFootView = true;
        this.footViewText = footViewText;
        this.fragment = fragment;
    }

    @Override
    public int getItemCount() {
        //The last item is foot view
        if (vocationList.size() > 1 && isFootView) {
            return vocationList.size() + 1;
        }
        return vocationList.size();
    }

    @Override
    public int getItemViewType(int position) {
        //The last item is foot view
        if (position == vocationList.size()) {
            return FOOT_TYPE;
        }
        return NORMAL_TYPE;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView date, country, currency, vocation;
        public ImageView countryImage;
        public TextView tvFootView;//footView的TextView属于独自的一个layout

        public MyViewHolder(View view, int viewType) {
            super(view);
            if (viewType == NORMAL_TYPE) {
                date = (TextView) view.findViewById(R.id.date);
                country = (TextView) view.findViewById(R.id.country);
                currency = (TextView) view.findViewById(R.id.currency);
                vocation = (TextView) view.findViewById(R.id.vocation);
                countryImage = (ImageView) view.findViewById(R.id.country_image);
            } else if (viewType == FOOT_TYPE) {
                tvFootView = (TextView) itemView;
            }
        }
    }
}
