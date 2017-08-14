package com.riking.calendar.fragment;

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
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
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
import com.riking.calendar.widget.dialog.DatePickerDialog;
import com.riking.calendar.widget.dialog.SearchDialog;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
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
    public Calendar calendar;
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
    TextView dateTextView;
    SpinnerView mSpinnerView;
    SpinnerView mHolidaySpinnerView;
    SpinnerView mConcurrencySpinnerView;
    List<ModelPropDict> mCountryDatas;
    List<ModelPropDict> mHolidayDatas;
    List<ModelPropDict> mConcurrencyDatas;
    CtryHdayCrcy requestBoday = new CtryHdayCrcy();
    Callback getMoreVocationCallBack;
    DatePickerDialog dialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        calendar = Calendar.getInstance();
        a = (ViewPagerActivity) getActivity();
        View v = inflater.inflate(R.layout.second_fragment, container, false);
        dateColumn = v.findViewById(R.id.date_column);
        countryColumn = v.findViewById(R.id.country_column);
        holidayColumn = v.findViewById(R.id.holiday_column);
        concurrencyColumn = v.findViewById(R.id.concurrency_column);
        countryTextView = (TextView) v.findViewById(R.id.country_textview);
        holidayTextView = (TextView) v.findViewById(R.id.holiday_textview);
        concurrencyTextView = (TextView) v.findViewById(R.id.concurrency_textview);
        dateTextView = (TextView) v.findViewById(R.id.date_textview);

        mSpinnerView = new SpinnerView((ViewGroup) countryColumn);
        mSpinnerView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // 设置输入框内容
                ModelPropDict dict = mCountryDatas.get(position);
                countryTextView.setText(dict.valu);
                requestBoday.ctryName = dict.ke;
                apiInterface.getMore(requestBoday).enqueue(getMoreVocationCallBack);
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
                requestBoday.hdayName = data.ke;
                Log.d("zzw", "requestBoday: " + requestBoday.hdayName);
                apiInterface.getMore(requestBoday).enqueue(getMoreVocationCallBack);
                mHolidaySpinnerView.mWindow.dismiss();
            }
        });

        mConcurrencySpinnerView = new SpinnerView((ViewGroup) concurrencyColumn);
        mConcurrencySpinnerView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ModelPropDict data = mConcurrencyDatas.get(position);
                concurrencyTextView.setText(data.valu);
                requestBoday.crcy = data.ke;
                apiInterface.getMore(requestBoday).enqueue(getMoreVocationCallBack);
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

        final View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.btnSubmit: {
                        calendar.set(Calendar.YEAR, Integer.parseInt(dialog.wheelDatePicker.year));
                        calendar.set(Calendar.MONTH, Integer.parseInt(dialog.wheelDatePicker.month) - 1);
                        calendar.set(Calendar.DATE, Integer.parseInt(dialog.wheelDatePicker.day) - 1);

                        if (dialog.isDateFilterCleared) {
                            dateTextView.setText(getString(R.string.date));
                            //remove the date filter
                            requestBoday.hdayDate = null;
                        } else {
                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月");
                            dateTextView.setText(sdf.format(calendar.getTime()));
                            if (dialog.isWholeMonth) {
                                requestBoday.hdayDate = new SimpleDateFormat("yyyyMM").format(calendar.getTime());
                            } else {
                                requestBoday.hdayDate = new SimpleDateFormat("yyyyMMdd").format(calendar.getTime());
                            }
                        }

                        apiInterface.getMore(requestBoday).enqueue(getMoreVocationCallBack);
                        dialog.dismiss();
                        break;
                    }
                    case R.id.btnCancel: {
                        dialog.dismiss();
                        break;
                    }
                }
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
                dialog.show();
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
                                recyclerView.setAdapter(new VocationRecyclerViewAdapter(ctryHdayCryCondition._data.content));
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
                mSpinnerView.setAdapter(new MyAdapter(mCountryDatas));
                Log.d("zzw", response.code() + "success " + r);
            }

            @Override
            public void onFailure(Call<HolidayConditionDemo> call, Throwable t) {
                Log.d("zzw", "call2 fail" + t.getMessage());
            }
        });
        CtryHdayCrcy ctryHdayCrcy = new CtryHdayCrcy();
        Gson gson = new Gson();
        Log.d("feiyoulian", "jason: " + gson.toJson(ctryHdayCrcy));
        Call<CtryHdayCryCondition> vocationCalls = apiInterface.getMore(ctryHdayCrcy);

        getMoreVocationCallBack = new Callback<CtryHdayCryCondition>() {
            @Override
            public void onResponse(Call<CtryHdayCryCondition> call, Response<CtryHdayCryCondition> response) {
                if (recyclerView == null) {
                    return;
                }
                CtryHdayCryCondition ctryHdayCryCondition = response.body();
                if (ctryHdayCryCondition == null) {
                    return;
                }
                recyclerView.setAdapter(new VocationRecyclerViewAdapter(ctryHdayCryCondition._data.content));
                Log.d("zzw", "CtryHdayCryCondition success: " + ctryHdayCryCondition);
            }

            @Override
            public void onFailure(Call<CtryHdayCryCondition> call, Throwable t) {
            }
        };

        vocationCalls.enqueue(getMoreVocationCallBack);

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