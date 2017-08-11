package com.riking.calendar.fragment;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.riking.calendar.R;
import com.riking.calendar.activity.ViewPagerActivity;
import com.riking.calendar.adapter.VocationRecyclerViewAdapter;
import com.riking.calendar.listener.HideShowScrollListener;
import com.riking.calendar.pojo.CtryHdayCrcy;
import com.riking.calendar.pojo.CtryHdayCryCondition;
import com.riking.calendar.pojo.GetHolidayModel;
import com.riking.calendar.pojo.HolidayConditionDemo;
import com.riking.calendar.pojo.ModelPropDict;
import com.riking.calendar.realm.model.Vocation;
import com.riking.calendar.retrofit.APIClient;
import com.riking.calendar.retrofit.APIInterface;
import com.riking.calendar.util.ViewFindUtils;
import com.riking.calendar.util.ZR;
import com.riking.calendar.view.SpinnerView;
import com.riking.calendar.widget.dialog.SearchDialog;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import io.realm.Realm;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by zw.zhang on 2017/7/11.
 */

public class SecondFragment extends Fragment {
    RecyclerView recyclerView;
    Realm realm;
    ViewPagerActivity a;
    View searchView;
    APIInterface apiInterface;
    View dateColumn;
    View countryColumn;
    View holidayColumn;
    View concurrencyColumn;
    TextView countryTextView;
    TextView holidayTextView;
    TextView concurrencyTextView;

    SpinnerView mSpinnerView;
    SpinnerView mHolidaySpinnerView;
    SpinnerView mConcurrencySpinnerView;
    List<ModelPropDict> mCountryDatas;
    List<ModelPropDict> mHolidayDatas;
    List<ModelPropDict> mConcurrencyDatas;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        a = (ViewPagerActivity) getActivity();
        View v = inflater.inflate(R.layout.second_fragment, container, false);
        dateColumn = v.findViewById(R.id.date_column);
        countryColumn = v.findViewById(R.id.country_column);
        holidayColumn = v.findViewById(R.id.holiday_column);
        concurrencyColumn = v.findViewById(R.id.concurrency_column);
        countryTextView = (TextView) v.findViewById(R.id.country_textview);
        holidayTextView = (TextView) v.findViewById(R.id.holiday_textview);
        concurrencyTextView = (TextView) v.findViewById(R.id.concurrency_textview);

        mSpinnerView = new SpinnerView((ViewGroup) countryColumn);

        mSpinnerView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // 设置输入框内容
                ModelPropDict data = mCountryDatas.get(position);
                countryTextView.setText(data.valu);
                // 隐藏popupWindow
                mSpinnerView.mWindow.dismiss();
            }
        });

        mHolidaySpinnerView = new SpinnerView((ViewGroup) holidayColumn);
        mHolidaySpinnerView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ModelPropDict data = mHolidayDatas.get(position);
                holidayTextView.setText(data.valu);
                mHolidaySpinnerView.mWindow.dismiss();
            }
        });

        mConcurrencySpinnerView = new SpinnerView((ViewGroup) concurrencyColumn);
        mConcurrencySpinnerView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ModelPropDict data = mConcurrencyDatas.get(position);
                concurrencyTextView.setText(data.valu);
                mConcurrencySpinnerView.mWindow.dismiss();
            }
        });

        searchView = v.findViewById(R.id.search);
        countryColumn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("zzw", "click country column");
                mSpinnerView.toggle();

            }
        });
        holidayColumn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("zzw", "click country column");
                mHolidaySpinnerView.toggle();
            }
        });
        concurrencyColumn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mConcurrencySpinnerView.toggle();
            }
        });

        searchView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SearchDialog dialog = new SearchDialog(v.getContext());
                dialog.show();
            }
        });
        recyclerView = ViewFindUtils.find(v, R.id.recycler_view);
        recyclerView.addOnScrollListener(new HideShowScrollListener() {
            int marginBottom = (int) ZR.convertDpToPx(a, 48);

            @Override
            public void onHide() {
                LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) recyclerView.getLayoutParams();
                params.setMargins(0, 0, 0, 0);
                recyclerView.setLayoutParams(params);
                a.bottomTabs.setVisibility(View.GONE);
                recyclerView.invalidate();
            }

            @Override
            public void onShow() {
                LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) recyclerView.getLayoutParams();
                params.setMargins(0, 0, 0, marginBottom);
                recyclerView.setLayoutParams(params);
                recyclerView.invalidate();
                a.bottomTabs.setVisibility(View.VISIBLE);
            }
        });
        realm = Realm.getDefaultInstance();
        // Create the Realm instance
        realm = Realm.getDefaultInstance();
        //insert  to realm
        // All writes must be wrapped in a transaction to facilitate safe multi threading
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                // Add a person
                Vocation v = realm.createObject(Vocation.class, UUID.randomUUID().toString());
                v.date = new Date();
                v.country = "China";
                v.currency = "RMB";
                v.name = "春节";

            }
        });

        recyclerView = (RecyclerView) v.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(a.getApplicationContext()));
        List<Vocation> vocations = realm.where(Vocation.class).findAll();
        recyclerView.setItemAnimator(new DefaultItemAnimator());
//        recyclerView.addItemDecoration(new DividerItemDecoration(a, LinearLayout.VERTICAL));
//        recyclerView.setAdapter(new VocationRecyclerViewAdapter(vocations));
        apiInterface = APIClient.getClient().create(APIInterface.class);

        GetHolidayModel mode = new GetHolidayModel();

        Call<ResponseBody> call = apiInterface.getHolidays(mode);
        call.enqueue(new retrofit2.Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                ResponseBody r = response.body();

                try {
                    if (r == null) {
                        Log.d("zzw", "r is null");
                        return;
                    }
                    String s = r.source().readUtf8();
                    String s2 = s.replace("\\", "");
                    int i = s2.indexOf("}");
                    int l = s2.lastIndexOf("}");
//                    Gson gson = new Gson();
//                    ReminderModel m;
//                    m = gson.fromJson(s2.substring(10, i + 1), ReminderModel.class);
                    Log.d("zzw", response.code() + "success " + s2);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.d("zzw", "fail" + t.getMessage());
            }
        });

        Call<HolidayConditionDemo> call2 = apiInterface.getParams();
        call2.enqueue(new Callback<HolidayConditionDemo>() {
            @Override
            public void onResponse(Call<HolidayConditionDemo> call, Response<HolidayConditionDemo> response) {
                HolidayConditionDemo r = response.body();
                // 模拟数据
                mCountryDatas = r._data.ctryName;
                mHolidayDatas = r._data.hdayName;
                mConcurrencyDatas = r._data.crcy;
                ModelPropDict allCountry = new ModelPropDict();
                allCountry.valu = getString(R.string.country);
                mCountryDatas.add(0, allCountry);
                ModelPropDict allHoliday = new ModelPropDict();
                allHoliday.valu = getString(R.string.vocation);
                mHolidayDatas.add(0, allHoliday);
                ModelPropDict allConcurrency = new ModelPropDict();
                allConcurrency.valu = getString(R.string.currency);
                mConcurrencyDatas.add(0, allConcurrency);
                mConcurrencySpinnerView.setAdapter(new MyAdapter(mConcurrencyDatas));
                mHolidaySpinnerView.setAdapter(new MyAdapter(mHolidayDatas));
                mSpinnerView.setAdapter(new MyAdapter(mCountryDatas));
               /* try {
                    if (r == null) {
                        Log.d("zzw", "r is null");
                        return;
                    }

                    String s = r.source().readUtf8();
                    String s2 = s.replace("\\", "");
                    int i = s2.indexOf("}");
                    int l = s2.lastIndexOf("}");*/
                Log.d("zzw", response.code() + "success " + r);
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
            }

            @Override
            public void onFailure(Call<HolidayConditionDemo> call, Throwable t) {
                Log.d("zzw", "fail" + t.getMessage());
            }
        });

        Call<CtryHdayCryCondition> vocationCalls = apiInterface.getMore(new CtryHdayCrcy());
        vocationCalls.enqueue(new Callback<CtryHdayCryCondition>() {
            @Override
            public void onResponse(Call<CtryHdayCryCondition> call, Response<CtryHdayCryCondition> response) {
                CtryHdayCryCondition ctryHdayCryCondition = response.body();
                recyclerView.setAdapter(new VocationRecyclerViewAdapter(ctryHdayCryCondition._data.content));
                Log.d("zzw", "CtryHdayCryCondition success: " + ctryHdayCryCondition);
            }

            @Override
            public void onFailure(Call<CtryHdayCryCondition> call, Throwable t) {

            }
        });

        return v;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        realm.close();
    }

    class MyAdapter extends BaseAdapter {
        List<ModelPropDict> data;

        public MyAdapter(List<ModelPropDict> data) {
            this.data = data;
        }

        @Override
        public int getCount() {
            if (data != null) {
                return data.size();
            }
            return 0;
        }

        @Override
        public Object getItem(int position) {
            if (data != null) {
                return data.get(position);
            }
            return null;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;
            if (convertView == null) {
                // 加载布局
                convertView = View.inflate(getContext(), R.layout.item, null);
                // 初始化holder
                holder = new ViewHolder();
                // 初始化item的view
                holder.tvTitle = (TextView) convertView.findViewById(R.id.tv_title);
                // 设置标记
                convertView.setTag(holder);
            } else {
                // 复用
                holder = (ViewHolder) convertView.getTag();
            }
            // 给view设置数据
            final ModelPropDict d = data.get(position);
            holder.tvTitle.setText(d.valu);
         /*   holder.ivDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mDatas.remove(data);
                    // UI刷新
                    notifyDataSetChanged();
                }
            });*/
            return convertView;
        }
    }

    class ViewHolder {
        TextView tvTitle;
    }
}