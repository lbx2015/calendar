package com.riking.calendar.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.riking.calendar.R;
import com.riking.calendar.activity.ReportsSelectActivity;
import com.riking.calendar.app.MyApplication;
import com.riking.calendar.listener.ZCallBackWithFail;
import com.riking.calendar.pojo.AppUser;
import com.riking.calendar.pojo.base.ResponseModel;
import com.riking.calendar.pojo.server.Industry;
import com.riking.calendar.retrofit.APIClient;
import com.riking.calendar.util.CONST;
import com.riking.calendar.util.Preference;

import java.util.ArrayList;
import java.util.List;


public class PositionAdapter extends RecyclerView.Adapter<PositionViewHolder> {

    //position list, position and industry have the same data structure.
    public List<Industry> mList;
    public Long industryId;
    private Context context;

    public PositionAdapter(Context context) {
        this.context = context;
        mList = new ArrayList<>();
    }

    @Override
    public PositionViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(
                R.layout.industry_item, viewGroup, false);
        return new PositionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final PositionViewHolder h, final int i) {
        h.positionName.setText(mList.get(i).name);
        h.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (h.checkImage.getVisibility() == View.VISIBLE) {
                    h.checkImage.setVisibility(View.GONE);
                    h.positionName.setTextColor(h.positionName.getResources().getColor(R.color.black_deep));
                } else {
                    h.checkImage.setVisibility(View.VISIBLE);
                    h.positionName.setTextColor(h.positionName.getResources().getColor(R.color.holidayColor));
                }
                Intent it = new Intent(MyApplication.mCurrentActivity, ReportsSelectActivity.class);
                MyApplication.mCurrentActivity.startActivity(it);

                AppUser result = new AppUser();
                result.positionId = mList.get(i).id;
                result.id = (Preference.pref.getString(CONST.USER_ID, ""));
                APIClient.updateUserInfo(result, new ZCallBackWithFail<ResponseModel<String>>() {
                    @Override
                    public void callBack(ResponseModel<String> response) {

                    }
                });
            }
        });
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

}
