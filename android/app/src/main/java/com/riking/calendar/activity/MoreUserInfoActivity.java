package com.riking.calendar.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bigkoo.pickerview.listener.CustomListener;
import com.google.gson.Gson;
import com.necer.ncalendar.utils.MyLog;
import com.riking.calendar.R;
import com.riking.calendar.bean.JsonBean;
import com.riking.calendar.jiguang.Logger;
import com.riking.calendar.listener.ZCallBack;
import com.riking.calendar.listener.ZCallBackWithFail;
import com.riking.calendar.listener.ZClickListenerWithLoginCheck;
import com.riking.calendar.pojo.base.ResponseModel;
import com.riking.calendar.pojo.params.UpdUserParams;
import com.riking.calendar.pojo.params.UserParams;
import com.riking.calendar.pojo.resp.AppUserResp;
import com.riking.calendar.pojo.server.Industry;
import com.riking.calendar.retrofit.APIClient;
import com.riking.calendar.retrofit.APIInterface;
import com.riking.calendar.util.CONST;
import com.riking.calendar.util.GetJsonDataUtil;
import com.riking.calendar.util.StringUtil;
import com.riking.calendar.util.ZPreference;
import com.riking.calendar.util.ZToast;
import com.riking.calendar.view.OptionsPickerView;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

/**
 * Created by zw.zhang on 2017/8/5.
 */

public class MoreUserInfoActivity extends AppCompatActivity {
    private static final int MSG_LOAD_DATA = 0x0001;
    private static final int MSG_LOAD_SUCCESS = 0x0002;
    private static final int MSG_LOAD_FAILED = 0x0003;
    //phone number
    View phoneNumberLayout;
    TextView phoneNumberTv;
    TextView addPhoneNumberTv;
    //email
    View emailLayout;
    TextView emailTv;
    TextView addEmailTv;
    //wechat
    View wechatLayout;
    TextView wechatTv;
    TextView addWechatTv;
    //company
    View companyLayout;
    TextView companyTv;
    TextView addCompanyTv;
    //industry
    View industryLayout;
    TextView industryTv;
    TextView addIndustryTv;
    //position
    View positionLayout;
    TextView positionTv;
    TextView addPositionTv;
    //job place
    View locationLayout;
    TextView locationTv;
    TextView addLocationTv;
    SharedPreferences preference;
    APIInterface apiInterface = APIClient.getClient().create(APIInterface.class);
    //    TextView addressTextView;
//    View addressRelativeLayout;
    //reminderTimeCalendar
    Calendar calendar;
    OptionsPickerView pvOptions;
    AppUserResp currentUser = ZPreference.getCurrentLoginUser();
    ArrayList<Industry> industries;
    ArrayList<Industry> positions;
    TextView emailValidated;
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
                        showPickerView();
                    }
                    break;

                case MSG_LOAD_FAILED:
                    Toast.makeText(MoreUserInfoActivity.this, "Parse Failed", Toast.LENGTH_SHORT).show();
                    break;

            }
        }
    };
    private OptionsPickerView industryPicker;
    private OptionsPickerView positionPicker;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        preference = getSharedPreferences(CONST.PREFERENCE_FILE_NAME, MODE_PRIVATE);
        setContentView(R.layout.activity_more_user_info);
        init();
    /*   addressTextView = (TextView) findViewById(R.id.address);
        addressRelativeLayout = findViewById(R.id.address_relative_layout);
        addressRelativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isLoaded) {
                    //address picker view
                    showPickerView();
                } else {
                    mHandler.sendEmptyMessage(MSG_LOAD_DATA);
                }
               AlertDialog.Builder builder = new AlertDialog.Builder(MoreUserInfoActivity.this);
                builder.setTitle(getString(R.string.address));
                // I'm using fragment here so I'm using getView() to provide ViewGroup
                // but you can provide here any other instance of ViewGroup from your Fragment / Activity
                View viewInflated = LayoutInflater.from(MoreUserInfoActivity.this).inflate(R.layout.edit_user_name_dialog, null, false);
                // Set up the input
                final AutoCompleteTextView input = (AutoCompleteTextView) viewInflated.findViewById(R.userId.input);
                // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
                builder.setView(viewInflated);
                String addressText = preference.getString(CONST.USER_ADDRESS, "");
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
                            user.userId = preference.getString(CONST.USER_ID, null);

                            apiInterface.updateUserInfo(user).enqueue(new ZCallBack<ResponseModel<String>>() {
                                @Override
                                public void callBack(ResponseModel<String> response) {
                                    SharedPreferences.Editor editor = preference.edit();
                                    editor.putString(CONST.USER_ADDRESS, departName);
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

                builder.show();
          }
        });

        addressTextView.setText(preference.getString(CONST.USER_ADDRESS, ""));*/
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    private void showPickerView() {
        if (pvOptions != null) {
            pvOptions.show();
            return;
        }
        final UpdateUserInfoCallBack callBack = new UpdateUserInfoCallBack() {
            @Override
            void newValue(String newValue) {
                UpdUserParams user = new UpdUserParams();
                user.address = newValue;
                MyLog.d("positionId; " + newValue);
                callServerApi2UpdateUserInfo(newValue, user);
            }

            @Override
            void updateSuccess(String newValue) {
                currentUser.address = newValue;
                locationTv.setVisibility(View.VISIBLE);
                addLocationTv.setVisibility(View.GONE);
                locationTv.setText(newValue);
                ZPreference.saveUserInfoAfterLogin(currentUser);
            }
        };
        // 弹出选择器
        pvOptions = new OptionsPickerView.Builder(this, new OptionsPickerView.OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                //返回的分别是三个级别的选中位置
                final String location = options1Items.get(options1).getPickerViewText() +
                        options2Items.get(options1).get(options2) +
                        options3Items.get(options1).get(options2).get(options3);
                if (location.length() > 0) {
                    callBack.newValue(location);
                }
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

    private void initViews() {
        //phone number
        phoneNumberLayout = findViewById(R.id.cell_phone_number_layout);
        phoneNumberTv = findViewById(R.id.cell_phone_nubmer_tv);
        addPhoneNumberTv = findViewById(R.id.add_cell_phone_nubmer_tv);
        //email
        emailValidated = findViewById(R.id.email_validated);
        emailLayout = findViewById(R.id.email_layout);
        emailTv = findViewById(R.id.email_tv);
        addEmailTv = findViewById(R.id.add_email_tv);
        //wechat
        wechatLayout = findViewById(R.id.wechat_layout);
        wechatTv = findViewById(R.id.wechat_tv);
        addWechatTv = findViewById(R.id.add_wechat_tv);
        //company
        companyLayout = findViewById(R.id.company_layout);
        companyTv = findViewById(R.id.company_tv);
        addCompanyTv = findViewById(R.id.add_company_tv);
        //industry
        industryLayout = findViewById(R.id.industry_layout);
        industryTv = findViewById(R.id.industry_tv);
        addIndustryTv = findViewById(R.id.add_industry_tv);
        //position
        positionLayout = findViewById(R.id.position_layout);
        positionTv = findViewById(R.id.position_tv);
        addPositionTv = findViewById(R.id.add_position_tv);
        //job place
        locationLayout = findViewById(R.id.location_layout);
        locationTv = findViewById(R.id.location_tv);
        addLocationTv = findViewById(R.id.add_location_tv);
    }

    //条件选择器初始化，自定义布局
    private void initEvents() {
        //phone number
        phoneNumberLayout.setOnClickListener(new ZClickListenerWithLoginCheck() {
            @Override
            public void click(View v) {
                changeCompanyDialog(3, currentUser.phone, new UpdateUserInfoCallBack() {
                    @Override
                    public void newValue(final String newValue) {
                        if (currentUser.phone.equals(newValue)) {
                            return;
                        }
                        UpdUserParams user = new UpdUserParams();
                        user.phone = newValue;
                        callServerApi2UpdateUserInfo(newValue, user);
                    }

                    @Override
                    void updateSuccess(String newValue) {
                        currentUser.phone = newValue;
                        ZPreference.saveUserInfoAfterLogin(currentUser);
                        phoneNumberTv.setVisibility(View.VISIBLE);
                        addPhoneNumberTv.setVisibility(View.GONE);
                        phoneNumberTv.setText(newValue);
                    }
                });
            }
        });
        //email
        emailLayout.setOnClickListener(new ZClickListenerWithLoginCheck() {
            @Override
            public void click(View v) {
                changeCompanyDialog(1, currentUser.email, new UpdateUserInfoCallBack() {
                    @Override
                    public void newValue(final String newValue) {
                        if (currentUser.email.equals(newValue)) {
                            return;
                        }

                        UpdUserParams user = new UpdUserParams();
                        user.email = newValue;
                        callServerApi2UpdateUserInfo(newValue, user);
                    }

                    @Override
                    void updateSuccess(String newValue) {
                        currentUser.email = newValue;
                        emailValidated.setClickable(true);
                        emailValidated.setEnabled(true);
                        emailValidated.setText("未验证");
                        currentUser.isIdentify = 0;
                        ZPreference.saveUserInfoAfterLogin(currentUser);
                        emailTv.setVisibility(View.VISIBLE);
                        addEmailTv.setVisibility(View.GONE);
                        emailTv.setText(newValue);
                    }
                });
            }
        });
        //wechat
        wechatLayout.setOnClickListener(new ZClickListenerWithLoginCheck() {
            @Override
            public void click(View v) {

            }
        });
        //company
        companyLayout.setOnClickListener(new ZClickListenerWithLoginCheck() {
            @Override
            public void click(View v) {
                changeCompanyDialog(0, currentUser.companyName, new UpdateUserInfoCallBack() {
                    @Override
                    public void newValue(final String newValue) {
                        if (currentUser.companyName.equals(newValue)) {
                            return;
                        }
                        UpdUserParams user = new UpdUserParams();
                        user.companyName = newValue;
                        callServerApi2UpdateUserInfo(newValue, user);
                    }

                    @Override
                    void updateSuccess(String newValue) {
                        currentUser.companyName = newValue;
                        ZPreference.saveUserInfoAfterLogin(currentUser);
                        companyTv.setVisibility(View.VISIBLE);
                        addCompanyTv.setVisibility(View.GONE);
                        companyTv.setText(newValue);
                    }
                });
            }
        });

        //industry
        industryLayout.setOnClickListener(new ZClickListenerWithLoginCheck() {
            @Override
            public void click(View v) {
                setIndustryPicker(currentUser.industryId);
            }
        });

        //position
        positionLayout.setOnClickListener(new ZClickListenerWithLoginCheck() {
            @Override
            public void click(View v) {
                setPositionPicker(currentUser.positionId);
            }
        });
        //job place
        locationLayout.setOnClickListener(new ZClickListenerWithLoginCheck() {
            @Override
            public void click(View v) {
                if (isLoaded) {
                    //address picker view
                    showPickerView();
                } else {
                    mHandler.sendEmptyMessage(MSG_LOAD_DATA);
                }
            }
        });
    }

    private void setPositionPicker(String currentPositionId) {
        MyLog.d("setPositionPicker" + currentPositionId);
        final UpdateUserInfoCallBack callBack = new UpdateUserInfoCallBack() {
            @Override
            void newValue(String newValue) {
                UpdUserParams user = new UpdUserParams();
                user.positionId = newValue;
                MyLog.d("positionId; " + newValue);
                callServerApi2UpdateUserInfo(newValue, user);
            }

            @Override
            void updateSuccess(String newValue) {
                currentUser.positionId = newValue;
                positionTv.setVisibility(View.VISIBLE);
                addPositionTv.setVisibility(View.GONE);
                //reset industry name
                for (Industry i : positions) {
                    if (i.industryId.equals(newValue)) {
                        positionTv.setText(i.name);
                        currentUser.positionName = i.name;
                        break;
                    }
                }

                ZPreference.saveUserInfoAfterLogin(currentUser);
            }
        };

        positionPicker = new OptionsPickerView.Builder(this, new OptionsPickerView.OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int option2, int options3, View v) {
                Industry i = positions.get(options1);
//                final String positionName = i.getPickerViewText();
                callBack.newValue(i.industryId);

            }
        }).setCyclic(true, false, false)
                .setLayoutRes(R.layout.pickerview_department, new CustomListener() {
                    @Override
                    public void customLayout(View v) {
                        final View tvSubmit = v.findViewById(R.id.tv_finish);
                        ImageView ivCancel = (ImageView) v.findViewById(R.id.iv_cancel);
                        tvSubmit.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Logger.d("zzw", "click save");
                                positionPicker.returnData();
                                positionPicker.dismiss();
                            }
                        });

                        ivCancel.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                positionPicker.dismiss();
                            }
                        });
                    }
                })
                .isDialog(true)
                .build();

        if (positions == null || positions.size() == 0) {
            HashMap<String, String> hashMap = new HashMap<>();
            hashMap.put("industryId", currentUser.industryId);
            APIClient.getPositions(hashMap, new ZCallBackWithFail<ResponseModel<ArrayList<Industry>>>() {
                @Override
                public void callBack(ResponseModel<ArrayList<Industry>> response) throws Exception {
                    positions = response._data;
                    if (industries == null) {
                        return;
                    }
                    //only one column industry selector
                    positionPicker.setPicker(positions);
                    positionPicker.show();
                }
            });
        } else {
            positionPicker.setPicker(positions);
            positionPicker.show();
        }

        //set default position
        for (int i = 0; i < positions.size(); i++) {
            Industry industry = positions.get(i);
            if (industry.industryId.equals(currentPositionId)) {
                positionPicker.setSelectOptions(i);
            }
        }
    }

    /**
     * @description 注意事项：
     * 自定义布局中，id为 optionspicker 或者 timepicker 的布局以及其子控件必须要有，否则会报空指针。
     * 具体可参考demo 里面的两个自定义layout布局。
     */
    private void setIndustryPicker(final String currentIndustryId) {
        final UpdateUserInfoCallBack callBack = new UpdateUserInfoCallBack() {
            @Override
            void newValue(String newValue) {
                UpdUserParams user = new UpdUserParams();
                user.industryId = newValue;
                callServerApi2UpdateUserInfo(newValue, user);
            }

            @Override
            void updateSuccess(String newValue) {
                currentUser.industryId = newValue;
                ZPreference.saveUserInfoAfterLogin(currentUser);
                industryTv.setVisibility(View.VISIBLE);
                addIndustryTv.setVisibility(View.GONE);
                //reset industry name
                for (Industry i : industries) {
                    if (i.industryId.equals(newValue)) {
                        industryTv.setText(i.name);
                        break;
                    }
                }
                //clear the user position information
                currentUser.positionId = null;
                positionTv.setVisibility(View.GONE);
                addPositionTv.setVisibility(View.VISIBLE);
            }
        };

        industryPicker = new OptionsPickerView.Builder(this, new OptionsPickerView.OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int option2, int options3, View v) {
                Industry i = industries.get(options1);
//                final String positionName = i.getPickerViewText();
                callBack.newValue(i.industryId);

            }
        }).setCyclic(true, false, false)
                .setLayoutRes(R.layout.pickerview_department, new CustomListener() {
                    @Override
                    public void customLayout(View v) {
                        final View tvSubmit = v.findViewById(R.id.tv_finish);
                        ImageView ivCancel = (ImageView) v.findViewById(R.id.iv_cancel);
                        tvSubmit.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Logger.d("zzw", "click save");
                                industryPicker.returnData();
                                industryPicker.dismiss();
                            }
                        });

                        ivCancel.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                industryPicker.dismiss();
                            }
                        });
                    }
                })
                .isDialog(true)
                .build();

        if (industries == null || industries.size() == 0) {
            APIClient.getIndustries(new ZCallBackWithFail<ResponseModel<ArrayList<Industry>>>() {
                @Override
                public void callBack(ResponseModel<ArrayList<Industry>> response) throws Exception {
                    industries = response._data;
                    if (industries == null) {
                        return;
                    }
                    //only one column industry selector
                    industryPicker.setPicker(industries);
                    industryPicker.show();
                    setDefaultIndustry(currentIndustryId);
                }
            });
        } else {
            industryPicker.setPicker(industries);
            industryPicker.show();
            setDefaultIndustry(currentIndustryId);
        }
    }

    private void setDefaultIndustry(String currentIndustryId) {
        //set default industry
        for (int i = 0; i < industries.size(); i++) {
            Industry industry = industries.get(i);
            if (industry.industryId.equals(currentIndustryId)) {
                industryPicker.setSelectOptions(i);
            }
        }
    }

    /**
     * @param type      0 company name,1 email,3 phone number
     * @param initValue
     * @param callBack
     */
    private void changeCompanyDialog(final int type, String initValue, final UpdateUserInfoCallBack callBack) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        if (type == 0) {
            builder.setTitle("公司名称");
        } else if (type == 1) {
            builder.setTitle("邮箱");
        } else if (type == 1) {
            builder.setTitle("电话号码");
        }
        // I'm using fragment here so I'm using getView() to provide ViewGroup
        // but you can provide here any other instance of ViewGroup from your Fragment / Activity
        View viewInflated;
        if (type == 1) {
            viewInflated = LayoutInflater.from(this).inflate(R.layout.edit_user_email_dialog, null, false);
        } else {
            viewInflated = LayoutInflater.from(this).inflate(R.layout.edit_user_name_dialog, null, false);
        }

        // Set up the input
        final EditText input = (EditText) viewInflated.findViewById(R.id.input);
        // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
        builder.setView(viewInflated);
        input.setText(initValue == null ? "" : initValue);
        input.setSelection(initValue == null ? 0 : initValue.length());
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
                    //phone
                    if (type == 3) {
                        if (!StringUtil.isMobileNO(newComments)) {
                            ZToast.toast("电话号码不正确");
                            return;
                        }
                    }
                    callBack.newValue(newComments);
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

    private void initData() {
        //phone number
        if (StringUtil.isEmpty(currentUser.phone)) {
            phoneNumberTv.setVisibility(View.GONE);
            addPhoneNumberTv.setVisibility(View.VISIBLE);
        } else {
            phoneNumberTv.setText(currentUser.phone);
            phoneNumberTv.setVisibility(View.VISIBLE);
            addPhoneNumberTv.setVisibility(View.GONE);
        }
        //email
        if (StringUtil.isEmpty(currentUser.email)) {
            emailValidated.setVisibility(View.GONE);
            emailTv.setVisibility(View.GONE);
            addEmailTv.setVisibility(View.VISIBLE);
        } else {
            emailValidated.setVisibility(View.VISIBLE);

            if (currentUser.isIdentify == 0) {
                emailValidated.setClickable(true);
                emailValidated.setEnabled(true);
                emailValidated.setText("未验证");
            } else {
                emailValidated.setClickable(false);
                emailValidated.setEnabled(false);
                emailValidated.setText("已验证");
            }

            emailTv.setText(currentUser.email);
            emailTv.setVisibility(View.VISIBLE);
            addEmailTv.setVisibility(View.GONE);
        }
        //wechat
        if (StringUtil.isEmpty(currentUser.wechatNickName)) {
            wechatTv.setVisibility(View.GONE);
            addWechatTv.setVisibility(View.VISIBLE);
        } else {
            wechatTv.setText(currentUser.wechatNickName);
            wechatTv.setVisibility(View.VISIBLE);
            addWechatTv.setVisibility(View.GONE);
        }

        //company
        if (StringUtil.isEmpty(currentUser.companyName)) {
            companyTv.setVisibility(View.GONE);
            addCompanyTv.setVisibility(View.VISIBLE);
        } else {
            companyTv.setText(currentUser.companyName);
            companyTv.setVisibility(View.VISIBLE);
            addCompanyTv.setVisibility(View.GONE);
        }

        //industry
        if (StringUtil.isEmpty(currentUser.industryName)) {
            industryTv.setVisibility(View.GONE);
            addIndustryTv.setVisibility(View.VISIBLE);
        } else {
            industryTv.setText(currentUser.industryName);
            industryTv.setVisibility(View.VISIBLE);
            addIndustryTv.setVisibility(View.GONE);
        }

        //position
        if (StringUtil.isEmpty(currentUser.positionName)) {
            positionTv.setVisibility(View.GONE);
            addPositionTv.setVisibility(View.VISIBLE);
        } else {
            positionTv.setText(currentUser.positionName);
            positionTv.setVisibility(View.VISIBLE);
            addPositionTv.setVisibility(View.GONE);
        }

        //job place
        if (StringUtil.isEmpty(currentUser.address)) {
            locationTv.setVisibility(View.GONE);
            addLocationTv.setVisibility(View.VISIBLE);
        } else {
            locationTv.setText(currentUser.address);
            locationTv.setVisibility(View.VISIBLE);
            addLocationTv.setVisibility(View.GONE);
        }

        loadIndustries();
        loadPostions();
    }

    public void clickInvalidateEmail(View view) {
        UserParams userParams = new UserParams();
        userParams.email = currentUser.email;
        APIClient.sendEmailVerifyCode(userParams, new ZCallBack<ResponseModel<String>>() {
            @Override
            public void callBack(ResponseModel<String> response) {
                MoreUserInfoActivity.this.startActivityForResult(new Intent(MoreUserInfoActivity.this, InputEmailVerifyCodeActivity.class), CONST.VERIFY_EMAIL);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Make sure the request was successful
        if (resultCode != RESULT_CANCELED && requestCode == CONST.VERIFY_EMAIL) {
            currentUser.isIdentify = data.getIntExtra(CONST.EMAIL_VALIDATE, 0);
            ZPreference.saveUserInfoAfterLogin(currentUser);
            emailValidated.setClickable(false);
            emailValidated.setEnabled(false);
            emailValidated.setText("已验证");
        }
    }

    private void loadPostions() {
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("industryId", currentUser.industryId);
        APIClient.getPositions(hashMap, new ZCallBackWithFail<ResponseModel<ArrayList<Industry>>>() {
            @Override
            public void callBack(ResponseModel<ArrayList<Industry>> response) throws Exception {
                positions = response._data;
            }
        });
    }

    private void loadIndustries() {
        APIClient.getIndustries(new ZCallBackWithFail<ResponseModel<ArrayList<Industry>>>() {
            @Override
            public void callBack(ResponseModel<ArrayList<Industry>> response) throws Exception {
                industries = response._data;
            }
        });
    }

    private void init() {
        initViews();
        initEvents();
        initData();
    }

    public void clickBack(final View view) {
        onBackPressed();
    }

    public abstract class UpdateUserInfoCallBack {
        //call the method when user complete the input and click ok
        abstract void newValue(String newValue);

        abstract void updateSuccess(String newValue);

        //call server
        public void callServerApi2UpdateUserInfo(final String newValue, UpdUserParams user) {
            APIClient.modifyUserInfo(user, new ZCallBack<ResponseModel<String>>() {
                @Override
                public void callBack(ResponseModel<String> response) {
                    updateSuccess(newValue);
                    ZToast.toast("信息更新成功！");
                }
            });
        }
    }

}
