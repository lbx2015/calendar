package com.riking.calendar.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.riking.calendar.R;
import com.riking.calendar.activity.PositionSelectActivity;
import com.riking.calendar.app.MyApplication;
import com.riking.calendar.listener.ZCallBackWithFail;
import com.riking.calendar.pojo.AppUser;
import com.riking.calendar.pojo.base.ResponseModel;
import com.riking.calendar.pojo.server.Industry;
import com.riking.calendar.retrofit.APIClient;
import com.riking.calendar.util.CONST;
import com.riking.calendar.util.Preference;
import com.riking.calendar.viewholder.OneTextViewHolder;

import java.util.ArrayList;
import java.util.List;


public class IndustryAdapter extends RecyclerView.Adapter<OneTextViewHolder> {

    public List<Industry> mList;
    private Context context;

    public IndustryAdapter(Context context) {
        this.context = context;
        mList = new ArrayList<>();
    }

    @Override
    public OneTextViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(
                R.layout.industry_item, viewGroup, false);
        return new OneTextViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final OneTextViewHolder h, int i) {
        final Industry industry = mList.get(i);
        h.textView.setText(industry.name);
        h.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MyApplication.mCurrentActivity, PositionSelectActivity.class);
                i.putExtra(CONST.INDUSTRY_ID,industry.id);
                //go to position select page
                MyApplication.mCurrentActivity.startActivity(i);
                AppUser result = new AppUser();
                result.industryId = industry.id;
                result.isGuide = "1";
                result.userId = (Preference.pref.getString(CONST.USER_ID, ""));
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
