package com.riking.calendar.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatRadioButton;
import android.text.Editable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.riking.calendar.R;
import com.riking.calendar.bean.DictionaryBean;
import com.riking.calendar.jiguang.Logger;
import com.riking.calendar.listener.ZCallBack;
import com.riking.calendar.listener.ZClickListenerWithLoginCheck;
import com.riking.calendar.pojo.UploadImageModel;
import com.riking.calendar.pojo.base.ResponseModel;
import com.riking.calendar.pojo.params.UpdUserParams;
import com.riking.calendar.pojo.resp.AppUserResp;
import com.riking.calendar.retrofit.APIClient;
import com.riking.calendar.retrofit.APIInterface;
import com.riking.calendar.util.CONST;
import com.riking.calendar.util.FileUtil;
import com.riking.calendar.util.ZGoto;
import com.riking.calendar.util.ZPreference;
import com.riking.calendar.util.ZR;
import com.riking.calendar.util.ZToast;
import com.riking.calendar.util.image.ImagePicker;

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
    ImageView myPhoto;
    View userNameRelativeLayout;
    TextView sexTextView;
    View sexRelativeLayout;
    AppUserResp user;
    APIInterface apiInterface = APIClient.getClient().create(APIInterface.class);

    View introductionRelative;

    private ArrayList<DictionaryBean> cardItem = new ArrayList<>();

    public void clickBack(final View view) {
        onBackPressed();
    }

    protected void onRestart() {
        super.onRestart();
        if (!ZPreference.isLogin()) {
            ZGoto.toLoginActivity();
            finish();
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        user = ZPreference.getCurrentLoginUser();
        setContentView(R.layout.activity_user_info);
        init();

        // width and height will be at least 600px long (optional).
        ImagePicker.setMinQuality(600, 600);


        ZR.setCircleUserImage(myPhoto, user.photoUrl);
      /*  String imageName = imageUrl.substring(imageUrl.lastIndexOf('/') + 1);
        if (FileUtil.imageExists(imageName)) {
            Logger.d("zzw", "no need load url: " + imageName);
            Bitmap bitmap = BitmapFactory.decodeFile(FileUtil.getImageFilePath(imageName));
            myPhoto.setImageBitmap(bitmap);

        } else if (imageUrl != null && imageUrl.length() > 0) {
            Logger.d("zzw", " load url: " + imageUrl);
            LoadUserImageTask myTask = new LoadUserImageTask();
            myTask.imageView = myPhoto;
            myTask.execute(imageUrl);
        }*/
    }

    private void init() {
        initViews();
        initEvents();
        setData();
    }

    private void setData() {
        setSex();
    }

    private void setSex() {
        if (user.sex == 1) {
            sexTextView.setText(getString(R.string.male));
        } else {
            sexTextView.setText(getString(R.string.female));
        }
    }

    private void initViews() {
        introductionRelative = findViewById(R.id.user_introduction_relative);
        myPhoto = (ImageView) findViewById(R.id.my_photo);
        userName = (TextView) findViewById(R.id.name);
        //sex
        sexTextView = (TextView) findViewById(R.id.sex);
        sexRelativeLayout = findViewById(R.id.sex_relative_layout);

        userName.setText(user.userName);
        userNameRelativeLayout = findViewById(R.id.user_name_relative_layout);
    }

    private void initEvents() {
        userNameRelativeLayout.setOnClickListener(this);
        sexRelativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(UserInfoActivity.this);
                builder.setTitle(getString(R.string.sex));
                // I'm using fragment here so I'm using getView() to provide ViewGroup
                // but you can provide here any other instance of ViewGroup from your Fragment / Activity
                View viewInflated = LayoutInflater.from(UserInfoActivity.this).inflate(R.layout.edit_sex_dialog, null, false);
                // set up radio buttons
                final AppCompatRadioButton maleButton = (AppCompatRadioButton) viewInflated.findViewById(R.id.male_button);
                final AppCompatRadioButton femaleButton = (AppCompatRadioButton) viewInflated.findViewById(R.id.female_button);

                // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
                builder.setView(viewInflated);
                if (user.sex == 1) {
                    maleButton.setChecked(true);
                } else {
                    femaleButton.setChecked(true);
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
                        if (user.sex != newSex) {
                            final UpdUserParams user = new UpdUserParams();
                            user.sex = newSex;
                            APIClient.modifyUserInfo(user, new ZCallBack<ResponseModel<String>>() {
                                @Override
                                public void callBack(ResponseModel<String> response) {
                                    AppUserResp currentUser = ZPreference.getCurrentLoginUser();
                                    currentUser.sex = user.sex;
                                    ZPreference.saveUserInfoAfterLogin(currentUser);
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

        introductionRelative.setOnClickListener(new ZClickListenerWithLoginCheck() {
            @Override
            public void click(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(UserInfoActivity.this);
                builder.setTitle(getString(R.string.user_comments));
                // I'm using fragment here so I'm using getView() to provide ViewGroup
                // but you can provide here any other instance of ViewGroup from your Fragment / Activity
                View viewInflated = LayoutInflater.from(UserInfoActivity.this).inflate(R.layout.edit_user_name_dialog, null, false);
                // Set up the input
                final EditText input = (EditText) viewInflated.findViewById(R.id.input);
                // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
                builder.setView(viewInflated);
                input.setText(user.descript == null ? "" : user.descript);
                input.setSelection(user.descript == null ? 0 : user.descript.length());
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

                            UpdUserParams user = new UpdUserParams();
                            user.descript = newComments;

                            APIClient.modifyUserInfo(user, new ZCallBack<ResponseModel<String>>() {
                                @Override
                                public void callBack(ResponseModel<String> response) {
                                    AppUserResp currentUser = ZPreference.getCurrentLoginUser();
                                    currentUser.descript = newComments;
                                    ZPreference.saveUserInfoAfterLogin(currentUser);
                                    Intent i = new Intent();
                                    i.putExtra(CONST.USER_COMMENTS, newComments);
                                    setResult(CONST.REQUEST_CODE, i);
                                    ZToast.toast("信息更新成功！");
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
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_CANCELED) {
            //do nothing on operation cancelled
            return;
        }

        Bitmap bitMap = ImagePicker.getImageFromResult(this, requestCode, resultCode, data);
        if (bitMap != null) {
            Logger.d("zzw", "bit map is not null");
            ZR.setCircleUserImage(myPhoto, bitMap);
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

        apiInterface.postImage(body, user.userId).enqueue(new Callback<UploadImageModel>() {
            @Override
            public void onResponse(Call<UploadImageModel> call, Response<UploadImageModel> response) {
                final UploadImageModel r = response.body();
                if (r != null) {
                    AppUserResp currentUser = ZPreference.getCurrentLoginUser();
                    currentUser.photoUrl = r._data;
                    ZPreference.saveUserInfoAfterLogin(currentUser);

                    //update user image in user info fragment
                    Intent i = new Intent();
                    i.putExtra(CONST.USER_IMAGE_URL, r._data);
                    setResult(CONST.REQUEST_CODE, i);
                    ZR.setCircleUserImage(myPhoto, r._data);
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
                input.setText(user.userName);
                input.setSelection(user.userName.length());

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
                                    Intent i = new Intent();
                                    i.putExtra(CONST.USER_NAME, newName);
                                    setResult(CONST.REQUEST_CODE, i);
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
        }
    }
}
