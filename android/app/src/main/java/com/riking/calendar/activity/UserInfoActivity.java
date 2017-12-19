package com.riking.calendar.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bigkoo.pickerview.listener.CustomListener;
import com.riking.calendar.R;
import com.riking.calendar.bean.DictionaryBean;
import com.riking.calendar.jiguang.Logger;
import com.riking.calendar.listener.ZCallBack;
import com.riking.calendar.pojo.AppUser;
import com.riking.calendar.pojo.Dictionary;
import com.riking.calendar.pojo.UploadImageModel;
import com.riking.calendar.pojo.base.ResponseModel;
import com.riking.calendar.pojo.params.UpdUserParams;
import com.riking.calendar.pojo.resp.AppUserResp;
import com.riking.calendar.retrofit.APIClient;
import com.riking.calendar.retrofit.APIInterface;
import com.riking.calendar.task.LoadUserImageTask;
import com.riking.calendar.util.CONST;
import com.riking.calendar.util.FileUtil;
import com.riking.calendar.util.ZPreference;
import com.riking.calendar.util.image.ImagePicker;
import com.riking.calendar.view.OptionsPickerView;
import com.riking.calendar.widget.EmailAutoCompleteTextView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by zw.zhang on 2017/8/5.
 */

public class UserInfoActivity extends AppCompatActivity implements View.OnClickListener {
    public TextView userName;
    public TextView email;
    public TextView department;
    ImageView myPhoto;
    View userNameRelativeLayout;
    View departmentRelativeLayout;
    View emailRelativeLayout;

    SharedPreferences preference;
    APIInterface apiInterface = APIClient.getClient().create(APIInterface.class);
    View moreUserInfoView;

    private OptionsPickerView departPickerView;
    private ArrayList<DictionaryBean> cardItem = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        preference = getSharedPreferences(CONST.PREFERENCE_FILE_NAME, MODE_PRIVATE);
        setContentView(R.layout.activity_user_info);
        myPhoto = (ImageView) findViewById(R.id.my_photo);
        findViewById(R.id.back).setOnClickListener(this);
        userName = (TextView) findViewById(R.id.name);
        email = (TextView) findViewById(R.id.email);
        department = (TextView) findViewById(R.id.depart);
        userName.setText(preference.getString(CONST.USER_NAME, ""));
        email.setText(preference.getString(CONST.USER_EMAIL, ""));
        department.setText(preference.getString(CONST.USER_DEPT, ""));
        userNameRelativeLayout = findViewById(R.id.user_name_relative_layout);
        emailRelativeLayout = findViewById(R.id.email_row_relative_layout);
        departmentRelativeLayout = findViewById(R.id.depart_row_relative_layout);
        departmentRelativeLayout.setOnClickListener(this);
        emailRelativeLayout.setOnClickListener(this);
        userNameRelativeLayout.setOnClickListener(this);
        moreUserInfoView = findViewById(R.id.more_user_info_relative_layout);
        moreUserInfoView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(UserInfoActivity.this, MoreUserInfoActivity.class));
            }
        });
        // width and height will be at least 600px long (optional).
        ImagePicker.setMinQuality(600, 600);

        String imageUrl = preference.getString(CONST.USER_IMAGE_URL, null);
        if (imageUrl == null) {
            return;
        }
        String imageName = imageUrl.substring(imageUrl.lastIndexOf('/') + 1);
        if (FileUtil.imageExists(imageName)) {
            Logger.d("zzw", "no need load url: " + imageName);
            Bitmap bitmap = BitmapFactory.decodeFile(FileUtil.getImageFilePath(imageName));
            myPhoto.setImageBitmap(bitmap);

        } else if (imageUrl != null && imageUrl.length() > 0) {
            Logger.d("zzw", " load url: " + imageUrl);
            LoadUserImageTask myTask = new LoadUserImageTask();
            myTask.imageView = myPhoto;
            myTask.execute(imageUrl);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Bitmap bitMap = ImagePicker.getImageFromResult(this, requestCode, resultCode, data);
        if (bitMap != null) {
            Logger.d("zzw", "bit map is not null");
            myPhoto.setImageBitmap(bitMap);
        } else {
            Logger.d("zzw", "bitmap is null");
            return;
        }

//        Bitmap bitMap = BitmapFactory.decodeResource(getResources(), R.drawable.cat_1);

        final File mFile2 = FileUtil.generateImageFile();
        try {
            mFile2.createNewFile();
            FileOutputStream outStream;

            outStream = new FileOutputStream(mFile2);

            bitMap.compress(Bitmap.CompressFormat.JPEG, 100, outStream);

            outStream.flush();

            outStream.close();

        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        String sdPath = mFile2.getAbsolutePath().toString();

        Log.d("zzw", "Your IMAGE ABSOLUTE PATH:-" + sdPath);

        File temp = new File(sdPath);

        if (!temp.exists()) {
            Log.d("zzw", "no image file at location :" + sdPath);
        }

        RequestBody reqFile = RequestBody.create(MediaType.parse("image"), mFile2);
        MultipartBody.Part body = MultipartBody.Part.createFormData("mFile", mFile2.getName(), reqFile);
        RequestBody name = RequestBody.create(MediaType.parse("text/plain"), "upload_test");
//        Logger.d("zzw", "userId: " + preference.getString(CONST.USER_ID, null));

        apiInterface.postImage(body, preference.getString(CONST.USER_ID, null)).enqueue(new Callback<UploadImageModel>() {
            @Override
            public void onResponse(Call<UploadImageModel> call, Response<UploadImageModel> response) {
                final UploadImageModel r = response.body();
                if (r != null) {
                    AppUserResp currentUser = ZPreference.getCurrentLoginUser();
                    currentUser.photoUrl = r._data;
                    ZPreference.saveUserInfoAfterLogin(currentUser);
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            File to = new File(new File(mFile2.getParent()), r._data.substring(r._data.lastIndexOf('/') + 1));
                            mFile2.renameTo(to);
                        }
                    }).start();
                } else {
                    Logger.d("zzw", "upload ok response body is null");
                }
            }

            @Override
            public void onFailure(Call<UploadImageModel> call, Throwable t) {
                Logger.d("zzw", "upload image fail: " + t.getMessage());
            }
        });

       /* InputStream is = ImagePicker.getInputStreamFromResult(this, requestCode, resultCode, data);
        if (is != null) {
            try {
                is.close();
            } catch (IOException ex) {
                // ignore
            }
        } else {
            Logger.d("zzw", "Failed to get input stream!");
        }*/
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void onPickImage(View view) {
        ImagePicker.pickImage(this, "选择用户图片:");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back: {
                onBackPressed();
                break;
            }
            case R.id.user_name_relative_layout: {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle(getString(R.string.name));
                // I'm using fragment here so I'm using getView() to provide ViewGroup
                // but you can provide here any other instance of ViewGroup from your Fragment / Activity
                View viewInflated = LayoutInflater.from(this).inflate(R.layout.edit_user_name_dialog, null, false);
                // Set up the input
                final EditText input = (EditText) viewInflated.findViewById(R.id.input);
                // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
                builder.setView(viewInflated);
                String name = preference.getString(CONST.USER_NAME, "");
                input.setText(name);
                input.setSelection(name.length());

                // Set up the buttons
                builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        Editable editable = input.getText();
                        if (editable == null) {
                            return;
                        }
                        final String newName = input.getText().toString();
                        if (newName.length() > 0) {
                            userName.setText(newName);
                            UpdUserParams user = new UpdUserParams();
                            user.userName = newName;
                            APIClient.modifyUserInfo(user, new ZCallBack<ResponseModel<String>>() {
                                @Override
                                public void callBack(ResponseModel<String> response) {
                                    AppUserResp currentUser = ZPreference.getCurrentLoginUser();
                                    currentUser.userName = newName;
                                    ZPreference.saveUserInfoAfterLogin(currentUser);
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
                break;
            }
            case R.id.email_row_relative_layout: {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle(getString(R.string.job_email));
                // I'm using fragment here so I'm using getView() to provide ViewGroup
                // but you can provide here any other instance of ViewGroup from your Fragment / Activity
                View viewInflated = LayoutInflater.from(this).inflate(R.layout.edit_user_email_dialog, null, false);
                // Set up the input
                final EmailAutoCompleteTextView input = (EmailAutoCompleteTextView) viewInflated.findViewById(R.id.input);
                // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
                builder.setView(viewInflated);
                String name = preference.getString(CONST.USER_EMAIL, "");
                input.setText(name);
                input.setSelection(name.length());

                // Set up the buttons
                builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        Editable editable = input.getText();
                        if (editable == null) {
                            return;
                        }
                        final String emailText = input.getText().toString();
                        if (emailText.length() > 0) {
                            email.setText(emailText);
                            AppUser user = new AppUser();
                            user.email = emailText;
                            user.userId = preference.getString(CONST.USER_ID, null);


                            apiInterface.updateUserInfo(user).enqueue(new ZCallBack<ResponseModel<String>>() {
                                @Override
                                public void callBack(ResponseModel<String> response) {
                                    SharedPreferences.Editor editor = preference.edit();
                                    editor.putString(CONST.USER_EMAIL, emailText);
                                    //save the changes.
                                    editor.commit();
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
                break;
            }
            case R.id.depart_row_relative_layout: {

                if (cardItem.size() > 0) {
                    departPickerView.show();
                    return;
                }
                ArrayList<String> fields = new ArrayList<>();
                fields.add("DEPT");
                apiInterface.getDictionary(fields).enqueue(new ZCallBack<ResponseModel<ArrayList<Dictionary>>>() {
                    @Override
                    public void callBack(ResponseModel<ArrayList<Dictionary>> response) {
                        initCustomOptionPicker();
                        for (Dictionary dictionary : response._data) {
                            DictionaryBean bean = new DictionaryBean(dictionary);
                            cardItem.add(bean);
                        }
                        departPickerView.setPicker(cardItem);//添加数据
                        departPickerView.show();
                        Logger.d("zzw", "depart query ok: " + response._data);
                    }
                });

//                AlertDialog.Builder builder = new AlertDialog.Builder(this);
//                builder.setTitle(getString(R.string.depart));
//                // I'm using fragment here so I'm using getView() to provide ViewGroup
//                // but you can provide here any other instance of ViewGroup from your Fragment / Activity
//                View viewInflated = LayoutInflater.from(this).inflate(R.layout.edit_user_name_dialog, null, false);
//                // Set up the input
//                final AutoCompleteTextView input = (AutoCompleteTextView) viewInflated.findViewById(R.userId.input);
//                // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
//                builder.setView(viewInflated);
//                String name = preference.getString(CONST.USER_DEPT, "");
//                input.setText(name);
//                input.setSelection(name.length());
//
//                // Set up the buttons
//                builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        dialog.dismiss();
//                        Editable editable = input.getText();
//                        if (editable == null) {
//                            return;
//                        }
//                        final String departName = input.getText().toString();
//                        if (departName.length() > 0) {
//                            department.setText(departName);
//                            AppUser user = new AppUser();
//                            user.dept = departName;
//                            user.userId = preference.getString(CONST.USER_ID, null);
//
//
//                            apiInterface.updateUserInfo(user).enqueue(new ZCallBack<ResponseModel<String>>() {
//                                @Override
//                                public void callBack(ResponseModel<String> response) {
//                                    SharedPreferences.Editor editor = preference.edit();
//                                    editor.putString(CONST.USER_DEPT, departName);
//                                    //save the changes.
//                                    editor.commit();
//                                }
//                            });
//                        }
//                    }
//                });
//                builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        dialog.cancel();
//                    }
//                });

//                builder.show();
                break;
            }
        }
    }

    private void initCustomOptionPicker() {
        if (departPickerView != null) {
            return;
        }

        //条件选择器初始化，自定义布局
        /**
         * @description
         *
         * 注意事项：
         * 自定义布局中，id为 optionspicker 或者 timepicker 的布局以及其子控件必须要有，否则会报空指针。
         * 具体可参考demo 里面的两个自定义layout布局。
         */
        departPickerView = new OptionsPickerView.Builder(this, new OptionsPickerView.OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int option2, int options3, View v) {
                //返回的分别是三个级别的选中位置
                final String departName = cardItem.get(options1).getPickerViewText();
                AppUser user = new AppUser();
                user.dept = departName;
                user.userId = preference.getString(CONST.USER_ID, null);
                Logger.d("zzw", "depart selected: " + departName);
                apiInterface.updateUserInfo(user).enqueue(new ZCallBack<ResponseModel<String>>() {
                    @Override
                    public void callBack(ResponseModel<String> response) {
                        SharedPreferences.Editor editor = preference.edit();
                        editor.putString(CONST.USER_DEPT, departName);
                        //save the changes.
                        editor.commit();
                        department.setText(departName);
                    }
                });
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
                                departPickerView.returnData();
                                departPickerView.dismiss();
                            }
                        });

                        ivCancel.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                departPickerView.dismiss();
                            }
                        });
                    }
                })
                .isDialog(true)
                .build();
    }
}
