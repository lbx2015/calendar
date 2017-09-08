package com.riking.calendar.fragment;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.riking.calendar.R;
import com.riking.calendar.activity.ViewPagerActivity;
import com.riking.calendar.adapter.VocationRecyclerViewAdapter;
import com.riking.calendar.jiguang.Logger;
import com.riking.calendar.pojo.CtryHdayCrcy;
import com.riking.calendar.pojo.CtryHdayCryCondition;
import com.riking.calendar.pojo.GetHolidayModel;
import com.riking.calendar.pojo.HolidayConditionDemo;
import com.riking.calendar.pojo.ModelPropDict;
import com.riking.calendar.retrofit.APIClient;
import com.riking.calendar.retrofit.APIInterface;
import com.riking.calendar.view.SpinnerView;
import com.riking.calendar.view.ZRecyclerView;
import com.riking.calendar.widget.dialog.DatePickerDialog;
import com.riking.calendar.widget.dialog.SearchDialog;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by zw.zhang on 2017/7/11.
 */

public class SecondFragment extends Fragment {
    public Calendar calendar;
    public APIInterface apiInterface;
    public CtryHdayCrcy requestBoday = new CtryHdayCrcy();
    public Callback getMoreVocationCallBack;
    ZRecyclerView recyclerView;

    ViewPagerActivity a;
    View searchView;
    View dateColumn;
    ImageView dateArrow;
    View countryColumn;
    View holidayColumn;
    View concurrencyColumn;
    TextView countryTextView;
    TextView holidayTextView;
    TextView concurrencyTextView;
    TextView dateTextView;
    SpinnerView countrySpinnerView;
    SpinnerView mHolidaySpinnerView;
    SpinnerView mConcurrencySpinnerView;
    List<ModelPropDict> mCountryDatas;
    List<ModelPropDict> mHolidayDatas;
    List<ModelPropDict> mConcurrencyDatas;
    DatePickerDialog dialog;
    View v;
    private SwipeRefreshLayout swipeRefreshLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (v != null) {
            return v;
        }
        calendar = Calendar.getInstance();
        a = (ViewPagerActivity) getActivity();
        v = inflater.inflate(R.layout.second_fragment, container, false);
        dateColumn = v.findViewById(R.id.date_column);
        countryColumn = v.findViewById(R.id.country_column);
        holidayColumn = v.findViewById(R.id.holiday_column);
        concurrencyColumn = v.findViewById(R.id.concurrency_column);
        countryTextView = (TextView) v.findViewById(R.id.country_textview);
        holidayTextView = (TextView) v.findViewById(R.id.holiday_textview);
        concurrencyTextView = (TextView) v.findViewById(R.id.concurrency_textview);
        dateTextView = (TextView) v.findViewById(R.id.date_textview);
        final View empty = v.findViewById(R.id.empty);
        swipeRefreshLayout = (SwipeRefreshLayout) v.findViewById(R.id.swipeRefreshLayout);

        // Configure the refreshing colors
        swipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        countrySpinnerView = new SpinnerView((ViewGroup) countryColumn);
        countrySpinnerView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // 设置输入框内容
                ModelPropDict dict = mCountryDatas.get(position);
                if (countryTextView.getText().toString().equals(dict.valu)) {
                    countrySpinnerView.dismiss();
                    //do nothing for the same condition
                    return;
                }
                countryTextView.setText(dict.valu);
                requestBoday.ctryName = dict.ke;
                requestBoday.pindex = 1;
                apiInterface.getMore(requestBoday).enqueue(getMoreVocationCallBack);
                // 隐藏popupWindow
                countrySpinnerView.dismiss();
            }
        });

        mHolidaySpinnerView = new SpinnerView((ViewGroup) holidayColumn);
        mHolidaySpinnerView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ModelPropDict data = mHolidayDatas.get(position);
                if (holidayTextView.getText().toString().equals(data.valu)) {
                    mHolidaySpinnerView.dismiss();
                    //do nothing for the same condition
                    return;
                }
                holidayTextView.setText(data.valu);
                requestBoday.hdayName = data.ke;
                Log.d("zzw", "requestBoday: " + requestBoday.hdayName);
                requestBoday.pindex = 1;
                apiInterface.getMore(requestBoday).enqueue(getMoreVocationCallBack);
                mHolidaySpinnerView.dismiss();
            }
        });

        mConcurrencySpinnerView = new SpinnerView((ViewGroup) concurrencyColumn);
        mConcurrencySpinnerView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ModelPropDict data = mConcurrencyDatas.get(position);
                if (concurrencyTextView.getText().toString().equals(data.valu)) {
                    mConcurrencySpinnerView.dismiss();
                    //do nothing for the same condition
                    return;
                }
                concurrencyTextView.setText(data.valu);
                requestBoday.crcy = data.ke;
                requestBoday.pindex = 1;
                apiInterface.getMore(requestBoday).enqueue(getMoreVocationCallBack);
                mConcurrencySpinnerView.dismiss();
            }
        });

        searchView = v.findViewById(R.id.search);

        final View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.btnSubmit: {
                        calendar.set(Calendar.YEAR, Integer.parseInt(dialog.wheelDatePicker.year));

                        //whole month
                        if (dialog.isWholeMonth) {
                            calendar.set(Calendar.MONTH, Integer.parseInt(dialog.wheelDatePicker.month) - 1);
                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月");
                            dateTextView.setText(sdf.format(calendar.getTime()));
                            requestBoday.hdayDate = new SimpleDateFormat("yyyyMM").format(calendar.getTime());
                        }
                        //whole year
                        else {
                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy年");
                            dateTextView.setText(sdf.format(calendar.getTime()));
                            requestBoday.hdayDate = new SimpleDateFormat("yyyy").format(calendar.getTime());
                        }
                        requestBoday.pindex = 1;
                        apiInterface.getMore(requestBoday).enqueue(getMoreVocationCallBack);
                        dialog.dismiss();
                        dateArrow.setRotation(0);
                        break;
                    }
                    case R.id.btnCancel: {
                        dialog.dismiss();
                        dateArrow.setRotation(0);
                        break;
                    }
                }
            }
        };

        dateArrow = (ImageView) v.findViewById(R.id.date_arrow);
        final ImageView countryArrow = (ImageView) v.findViewById(R.id.country_arrow);
        final ImageView currencyArrow = (ImageView) v.findViewById(R.id.currency_arrow);
        final ImageView holidayArrow = (ImageView) v.findViewById(R.id.holiday_arrow);
        final DialogInterface.OnDismissListener dismissListener = new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                //arrow down
                dateArrow.setRotation(0);
            }
        };
        dateColumn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dialog == null) {
                    dialog = new DatePickerDialog(getContext());
                    dialog.btnSubmit.setOnClickListener(listener);
                    dialog.btnCancel.setOnClickListener(listener);
                }

                dialog.setOnDismissListener(dismissListener);
                dialog.show();
                dateArrow.setRotation(180);
            }
        });


        //change the arrow icon to arrow down icon
        countrySpinnerView.dismissListener = new SpinnerView.DismissListener() {
            @Override
            public void onDismiss() {
                countrySpinnerView.mWindow.dismiss();
                countryArrow.setRotation(0);
            }
        };

        mConcurrencySpinnerView.dismissListener = new SpinnerView.DismissListener() {
            @Override
            public void onDismiss() {
                mConcurrencySpinnerView.mWindow.dismiss();
                currencyArrow.setRotation(0);
            }
        };

        mHolidaySpinnerView.dismissListener = new SpinnerView.DismissListener() {
            @Override
            public void onDismiss() {
                mHolidaySpinnerView.mWindow.dismiss();
                holidayArrow.setRotation(0);
            }
        };


        countryColumn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("zzw", "click country column");
                if (countrySpinnerView.mWindow != null && countrySpinnerView.mWindow.isShowing()) {
                    Logger.d("zzw", "arrow down");
                    countrySpinnerView.dismiss();
                    countryArrow.setRotation(0);
                    ;
                } else {
                    countrySpinnerView.clickArrow();
                    Logger.d("zzw", "arrow left");
                    countryArrow.setRotation(180);
                }
            }
        });

        holidayColumn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("zzw", "click country column");
                if (mHolidaySpinnerView.mWindow != null && mHolidaySpinnerView.mWindow.isShowing()) {
                    mHolidaySpinnerView.dismiss();
                    holidayArrow.setRotation(0);
                } else {
                    mHolidaySpinnerView.clickArrow();
                    holidayArrow.setRotation(180);
                }
            }
        });
        concurrencyColumn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mConcurrencySpinnerView.mWindow != null && mConcurrencySpinnerView.mWindow.isShowing()) {
                    mConcurrencySpinnerView.dismiss();
                    currencyArrow.setRotation(0);
                } else {
                    mConcurrencySpinnerView.clickArrow();
                    currencyArrow.setRotation(180);
                }
            }
        });

        searchView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final SearchDialog dialog = new SearchDialog(v.getContext());
                dialog.searchClickListener = new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.d("zzw", "click search Button");
                        final CtryHdayCrcy searchRequest = new CtryHdayCrcy();
                        searchRequest.queryParam = dialog.editText.getText().toString();
                        apiInterface.getVagueQuery(searchRequest).enqueue(new Callback<CtryHdayCryCondition>() {
                            @Override
                            public void onResponse(Call<CtryHdayCryCondition> call, Response<CtryHdayCryCondition> response) {
                                if (recyclerView == null) {
                                    return;
                                }
                                CtryHdayCryCondition ctryHdayCryCondition = response.body();
                                if (ctryHdayCryCondition == null) {
                                    Toast.makeText(getContext(), getString(R.string.no_found), Toast.LENGTH_SHORT);
                                    return;
                                } else {
                                    holidayTextView.setText(mHolidayDatas.get(0).valu);
                                    countryTextView.setText(mCountryDatas.get(0).valu);
                                    concurrencyTextView.setText(mConcurrencyDatas.get(0).valu);
                                    requestBoday = new CtryHdayCrcy();
                                }

                                VocationRecyclerViewAdapter adapter = new VocationRecyclerViewAdapter(ctryHdayCryCondition._data.content);
                                adapter.setFootViewText("加载中。。。", SecondFragment.this);
                                recyclerView.setAdapter(adapter);
                            }

                            @Override
                            public void onFailure(Call<CtryHdayCryCondition> call, Throwable t) {

                            }
                        });
                        dialog.dismiss();
                    }
                };
                dialog.show();
            }
        });
        recyclerView = (ZRecyclerView) v.findViewById(R.id.recycler_view);
        ((TextView) ((LinearLayout) empty).getChildAt(1)).setText("没有找到数据");
        recyclerView.emptyViewCallBack = new ZRecyclerView.EmptyViewCallBack() {
            @Override
            public void onEmpty() {
                empty.setVisibility(View.VISIBLE);
            }

            @Override
            public void onNotEmpty() {
                empty.setVisibility(View.GONE);
            }
        };
        recyclerView.setLayoutManager(new LinearLayoutManager(a));
//        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) recyclerView.getLayoutParams();
//        final int marginBottom = a.bottomTabs.getMeasuredHeight();
//        params.setMargins(0, 0, 0, marginBottom);
//        recyclerView.setLayoutParams(params);
//        List<Vocation> vocations = realm.where(Vocation.class).findAll();
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
                    Log.d("zzw", response.code() + "success " + s2);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.d("zzw", "call fail" + t.getMessage());
            }
        });

        Call<HolidayConditionDemo> call2 = apiInterface.getParams();
        call2.enqueue(new Callback<HolidayConditionDemo>() {
            @Override
            public void onResponse(Call<HolidayConditionDemo> call, Response<HolidayConditionDemo> response) {
                HolidayConditionDemo r = response.body();
                if (r == null) {
                    return;
                }
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
                countrySpinnerView.setAdapter(new MyAdapter(mCountryDatas));
                Log.d("zzw", response.code() + "success " + r);
            }

            @Override
            public void onFailure(Call<HolidayConditionDemo> call, Throwable t) {
                Log.d("zzw", "call2 fail" + t.getMessage());
            }
        });

        requestBoday.hdayDate = new SimpleDateFormat("yyyy").format(calendar.getTime());
        Gson gson = new Gson();
        Log.d("zzw", "jason: " + gson.toJson(requestBoday));
        final Call<CtryHdayCryCondition> vocationCalls = apiInterface.getMore(requestBoday);

        getMoreVocationCallBack = new Callback<CtryHdayCryCondition>() {
            @Override
            public void onResponse(Call<CtryHdayCryCondition> call, Response<CtryHdayCryCondition> response) {
                swipeRefreshLayout.setRefreshing(false);
                if (recyclerView == null) {
                    return;
                }
                CtryHdayCryCondition ctryHdayCryCondition = response.body();
                if (ctryHdayCryCondition == null) {
                    return;
                }
                VocationRecyclerViewAdapter adapter = new VocationRecyclerViewAdapter(ctryHdayCryCondition._data.content);
                adapter.setFootViewText("加载中。。。", SecondFragment.this);
                recyclerView.setAdapter(adapter);

                Log.d("zzw", "CtryHdayCryCondition success: " + ctryHdayCryCondition);
            }

            @Override
            public void onFailure(Call<CtryHdayCryCondition> call, Throwable t) {
                swipeRefreshLayout.setRefreshing(false);
            }
        };

        vocationCalls.enqueue(getMoreVocationCallBack);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //first page
                requestBoday.pindex = 1;
                apiInterface.getMore(requestBoday).enqueue(getMoreVocationCallBack);
            }
        });


        return v;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
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