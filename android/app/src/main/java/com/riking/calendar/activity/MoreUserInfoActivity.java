package com.riking.calendar.activity;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatRadioButton;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.ldf.calendar.Const;
import com.riking.calendar.R;
import com.riking.calendar.bean.JsonBean;
import com.riking.calendar.jiguang.Logger;
import com.riking.calendar.listener.ZCallBack;
import com.riking.calendar.pojo.AppUser;
import com.riking.calendar.pojo.base.ResponseModel;
import com.riking.calendar.retrofit.APIClient;
import com.riking.calendar.retrofit.APIInterface;
import com.riking.calendar.util.GetJsonDataUtil;
import com.riking.calendar.view.OptionsPickerView;
import com.riking.calendar.widget.dialog.BirthdayPickerDialog;

import org.json.JSONArray;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by zw.zhang on 2017/8/5.
 */

public class MoreUserInfoActivity extends AppCompatActivity implements View.OnClickListener {
    private static final int MSG_LOAD_DATA = 0x0001;
    private static final int MSG_LOAD_SUCCESS = 0x0002;
    private static final int MSG_LOAD_FAILED = 0x0003;
    SharedPreferences preference;
    APIInterface apiInterface = APIClient.getClient().create(APIInterface.class);
    TextView sexTextView;
    TextView addressTextView;
    TextView birthDayTextView;
    TextView commentsTextView;
    View commentsRelativeLayout;
    View addressRelativeLayout;
    View birthDayRelative;
    View sexRelativeLayout;
    BirthdayPickerDialog datePickerDialog;
    //reminderTimeCalendar
    Calendar calendar;
    OptionsPickerView pvOptions;
    private ArrayList<JsonBean> options1Items = new ArrayList<>();
    private ArrayList<ArrayList<String>> options2Items = new ArrayList<>();
    private ArrayList<ArrayList<ArrayList<String>>> options3Items = new ArrayList<>();
    private Thread thread;
    private boolean isLoaded = false;
    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_LOAD_DATA:
                    if (thread == null) {//如果已创建就不再重新创建子线程了

//                        Toast.makeText(MoreUserInfoActivity.this, "Begin Parse Data", Toast.LENGTH_SHORT).show();
                        thread = new Thread(new Runnable() {
                            @Override
                            public void run() {
                                // 写子线程中的操作,解析省市区数据
                                initJsonData();
                            }
                        });
                        thread.start();
                    }
                    break;

                case MSG_LOAD_SUCCESS:
//                    Toast.makeText(MoreUserInfoActivity.this, "Parse Succeed", Toast.LENGTH_SHORT).show();
                    isLoaded = true;
                    if (isLoaded) {
                        ShowPickerView();
                    }
                    break;

                case MSG_LOAD_FAILED:
                    Toast.makeText(MoreUserInfoActivity.this, "Parse Failed", Toast.LENGTH_SHORT).show();
                    break;

            }
        }
    };

    private void ShowPickerView() {
        if (pvOptions != null) {
            pvOptions.show();
            return;
        }
        // 弹出选择器
        pvOptions = new OptionsPickerView.Builder(this, new OptionsPickerView.OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                //返回的分别是三个级别的选中位置
                final String departName = options1Items.get(options1).getPickerViewText() +
                        options2Items.get(options1).get(options2) +
                        options3Items.get(options1).get(options2).get(options3);
                if (departName.length() > 0) {
                    AppUser user = new AppUser();
                    user.address = departName;
                    user.id = preference.getString(Const.USER_ID, null);

                    apiInterface.updateUserInfo(user).enqueue(new ZCallBack<ResponseModel<String>>() {
                        @Override
                        public void callBack(ResponseModel<String> response) {
                            SharedPreferences.Editor editor = preference.edit();
                            editor.putString(Const.USER_ADDRESS, departName);
                            //save the changes.
                            editor.commit();
                            addressTextView.setText(departName);
                        }
                    });
                }
                Toast.makeText(MoreUserInfoActivity.this, departName, Toast.LENGTH_SHORT).show();
            }
        })

                .setTitleText("城市选择")
                .setDividerColor(getResources().getColor(R.color.color_background_b6b6b6))
                .setTextColorCenter(getResources().getColor(R.color.color_323232)) //设置选中项文字颜色
                .setContentTextSize(16)
                .setLineSpacingMultiplier(2f)
                .setCyclic(true, false, false)
                .build();

        /*pvOptions.setPicker(options1Items);//一级选择器
        pvOptions.setPicker(options1Items, options2Items);//二级选择器*/
        pvOptions.setPicker(options1Items, options2Items, options3Items);//三级选择器
        pvOptions.show();
    }

    private void initJsonData() {//解析数据

        /**
         * 注意：assets 目录下的Json文件仅供参考，实际使用可自行替换文件
         * 关键逻辑在于循环体
         *
         * */
        String JsonData = new GetJsonDataUtil().getJson(this, "province.json");//获取assets目录下的json文件数据

        ArrayList<JsonBean> jsonBean = parseData(JsonData);//用Gson 转成实体

        /**
         * 添加省份数据
         *
         * 注意：如果是添加的JavaBean实体，则实体类需要实现 IPickerViewData 接口，
         * PickerView会通过getPickerViewText方法获取字符串显示出来。
         */
        options1Items = jsonBean;

        for (int i = 0; i < jsonBean.size(); i++) {//遍历省份
            ArrayList<String> CityList = new ArrayList<>();//该省的城市列表（第二级）
            ArrayList<ArrayList<String>> Province_AreaList = new ArrayList<>();//该省的所有地区列表（第三极）

            for (int c = 0; c < jsonBean.get(i).getCityList().size(); c++) {//遍历该省份的所有城市
                String CityName = jsonBean.get(i).getCityList().get(c).getName();
                CityList.add(CityName);//添加城市

                ArrayList<String> City_AreaList = new ArrayList<>();//该城市的所有地区列表

                //如果无地区数据，建议添加空字符串，防止数据为null 导致三个选项长度不匹配造成崩溃
                if (jsonBean.get(i).getCityList().get(c).getArea() == null
                        || jsonBean.get(i).getCityList().get(c).getArea().size() == 0) {
                    City_AreaList.add("");
                } else {

                    for (int d = 0; d < jsonBean.get(i).getCityList().get(c).getArea().size(); d++) {//该城市对应地区所有数据
                        String AreaName = jsonBean.get(i).getCityList().get(c).getArea().get(d);

                        City_AreaList.add(AreaName);//添加该城市所有地区数据
                    }
                }
                Province_AreaList.add(City_AreaList);//添加该省所有地区数据
            }

            /**
             * 添加城市数据
             */
            options2Items.add(CityList);

            /**
             * 添加地区数据
             */
            options3Items.add(Province_AreaList);
        }

        mHandler.sendEmptyMessage(MSG_LOAD_SUCCESS);

    }


    public ArrayList<JsonBean> parseData(String result) {//Gson 解析
        ArrayList<JsonBean> detail = new ArrayList<>();
        try {
            JSONArray data = new JSONArray(result);
            Gson gson = new Gson();
            for (int i = 0; i < data.length(); i++) {
                JsonBean entity = gson.fromJson(data.optJSONObject(i).toString(), JsonBean.class);
                detail.add(entity);
            }
        } catch (Exception e) {
            e.printStackTrace();
            mHandler.sendEmptyMessage(MSG_LOAD_FAILED);
        }
        return detail;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        preference = getSharedPreferences(Const.PREFERENCE_FILE_NAME, MODE_PRIVATE);
        setContentView(R.layout.activity_more_user_info);
        sexTextView = (TextView) findViewById(R.id.sex);
        addressTextView = (TextView) findViewById(R.id.address);
        birthDayTextView = (TextView) findViewById(R.id.birthday);
        commentsTextView = (TextView) findViewById(R.id.comments);
        addressRelativeLayout = findViewById(R.id.address_relative_layout);
        birthDayRelative = findViewById(R.id.birthday_relative_layout);
        sexRelativeLayout = findViewById(R.id.sex_relative_layout);


        final View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.btnSubmit: {
                        calendar = Calendar.getInstance();
                        calendar.set(Calendar.YEAR, Integer.parseInt(datePickerDialog.wheelDatePicker.year));
                        calendar.set(Calendar.MONTH, Integer.parseInt(datePickerDialog.wheelDatePicker.month) - 1);
                        calendar.set(Calendar.DATE, Integer.parseInt(datePickerDialog.wheelDatePicker.day) - 1);
                        AppUser user = new AppUser();
                        user.id = preference.getString(Const.USER_ID, null);
                        user.birthday = new SimpleDateFormat(Const.yyyyMMdd).format(calendar.getTime());
                        apiInterface.updateUserInfo(user).enqueue(new ZCallBack<ResponseModel<String>>() {
                            @Override
                            public void callBack(ResponseModel<String> response) {
                                Logger.d("zzw", "request success");
                                SharedPreferences.Editor editor = preference.edit();
                                String birthDay = new SimpleDateFormat(Const.birthDayFormat).format(calendar.getTime());
                                editor.putString(Const.USER_BIRTHDAY, birthDay);
                                editor.commit();
                                birthDayTextView.setText(birthDay);
                            }
                        });
                        datePickerDialog.dismiss();
                        break;
                    }
                    case R.id.btnCancel: {
                        datePickerDialog.dismiss();
                        break;
                    }
                }
            }
        };

        birthDayRelative.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (datePickerDialog == null) {
                    datePickerDialog = new BirthdayPickerDialog(MoreUserInfoActivity.this);
                    datePickerDialog.btnSubmit.setOnClickListener(listener);
                    datePickerDialog.btnCancel.setOnClickListener(listener);
                }
                datePickerDialog.show();
            }
        });

        sexRelativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MoreUserInfoActivity.this);
                builder.setTitle(getString(R.string.sex));
                // I'm using fragment here so I'm using getView() to provide ViewGroup
                // but you can provide here any other instance of ViewGroup from your Fragment / Activity
                View viewInflated = LayoutInflater.from(MoreUserInfoActivity.this).inflate(R.layout.edit_sex_dialog, null, false);
                // set up radio buttons
                final AppCompatRadioButton maleButton = (AppCompatRadioButton) viewInflated.findViewById(R.id.male_button);
                final AppCompatRadioButton femaleButton = (AppCompatRadioButton) viewInflated.findViewById(R.id.female_button);

                // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
                builder.setView(viewInflated);
                final int sex = preference.getInt(Const.USER_SEX, 0);
                if (sex == 1) {
                    if (!maleButton.isChecked()) {
                        maleButton.toggle();
                    }
                }

                // Set up the buttons
                builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        int newSex = -1;
                        if (maleButton.isChecked()) {
                            //male
                            newSex = 1;
                        } else {
                            //female
                            newSex = 0;
                        }
                        if (sex != newSex) {
                            final AppUser user = new AppUser();
                            user.id = preference.getString(Const.USER_ID, null);
                            user.sex = newSex;

                            apiInterface.updateUserInfo(user).enqueue(new ZCallBack<ResponseModel<String>>() {
                                @Override
                                public void callBack(ResponseModel<String> response) {
                                    SharedPreferences.Editor editor = preference.edit();
                                    editor.putInt(Const.USER_SEX, user.sex);
                                    editor.commit();

                                    if (user.sex == 1) {
                                        sexTextView.setText(getString(R.string.male));
                                    } else {
                                        sexTextView.setText(getString(R.string.female));
                                    }
                                }
                            });
                        }
                    }
                });
                builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                builder.show();

            }
        });
        addressRelativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isLoaded) {
                    //address picker view
                    ShowPickerView();
                } else {
                    mHandler.sendEmptyMessage(MSG_LOAD_DATA);
                }
                /*AlertDialog.Builder builder = new AlertDialog.Builder(MoreUserInfoActivity.this);
                builder.setTitle(getString(R.string.address));
                // I'm using fragment here so I'm using getView() to provide ViewGroup
                // but you can provide here any other instance of ViewGroup from your Fragment / Activity
                View viewInflated = LayoutInflater.from(MoreUserInfoActivity.this).inflate(R.layout.edit_user_name_dialog, null, false);
                // Set up the input
                final AutoCompleteTextView input = (AutoCompleteTextView) viewInflated.findViewById(R.id.input);
                // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
                builder.setView(viewInflated);
                String addressText = preference.getString(Const.USER_ADDRESS, "");
                input.setText(addressText);
                input.setSelection(addressText.length());

                // Set up the buttons
                builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        Editable editable = input.getText();
                        if (editable == null) {
                            return;
                        }
                        final String departName = input.getText().toString();
                        if (departName.length() > 0) {
                            AppUser user = new AppUser();
                            user.address = departName;
                            user.id = preference.getString(Const.USER_ID, null);

                            apiInterface.updateUserInfo(user).enqueue(new ZCallBack<ResponseModel<String>>() {
                                @Override
                                public void callBack(ResponseModel<String> response) {
                                    SharedPreferences.Editor editor = preference.edit();
                                    editor.putString(Const.USER_ADDRESS, departName);
                                    //save the changes.
                                    editor.commit();
                                    addressTextView.setText(departName);
                                }
                            });
                        }
                    }
                });
                builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                builder.show();*/
            }
        });

        addressTextView.setText(preference.getString(Const.USER_ADDRESS, ""));
        commentsTextView.setText(preference.getString(Const.USER_COMMENTS, ""));
        birthDayTextView.setText(preference.getString(Const.USER_BIRTHDAY, ""));
        commentsRelativeLayout = findViewById(R.id.comments_relative_layout);
        commentsRelativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MoreUserInfoActivity.this);
                builder.setTitle(getString(R.string.user_comments));
                // I'm using fragment here so I'm using getView() to provide ViewGroup
                // but you can provide here any other instance of ViewGroup from your Fragment / Activity
                View viewInflated = LayoutInflater.from(MoreUserInfoActivity.this).inflate(R.layout.edit_user_name_dialog, null, false);
                // Set up the input
                final EditText input = (EditText) viewInflated.findViewById(R.id.input);
                // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
                builder.setView(viewInflated);
                input.setText(preference.getString(Const.USER_COMMENTS, ""));
                input.setSelection(preference.getString(Const.USER_COMMENTS, "").length());
                // Set up the buttons
                builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        Editable editable = input.getText();
                        if (editable == null) {
                            return;
                        }
                        final String newComments = input.getText().toString();
                        if (newComments.length() > 0) {

                            AppUser user = new AppUser();
                            user.remark = newComments;
                            user.id = preference.getString(Const.USER_ID, null);


                            apiInterface.updateUserInfo(user).enqueue(new ZCallBack<ResponseModel<String>>() {
                                @Override
                                public void callBack(ResponseModel<String> response) {
                                    SharedPreferences.Editor editor = preference.edit();
                                    editor.putString(Const.USER_COMMENTS, newComments);
                                    //save the changes.
                                    editor.commit();
                                    commentsTextView.setText(newComments);
                                }
                            });
                        }
                    }
                });
                builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                builder.show();
            }
        });

        if (preference.getInt(Const.USER_SEX, 1) == 1) {
            sexTextView.setText(getString(R.string.male));
        } else {
            sexTextView.setText(getString(R.string.female));
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        findViewById(R.id.back).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back: {
                onBackPressed();
                break;
            }
        }
    }
}
