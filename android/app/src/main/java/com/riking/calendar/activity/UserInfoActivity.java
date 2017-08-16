package com.riking.calendar.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
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

import com.ldf.calendar.Const;
import com.riking.calendar.R;
import com.riking.calendar.adapter.VocationRecyclerViewAdapter;
import com.riking.calendar.jiguang.Logger;
import com.riking.calendar.pojo.AppUser;
import com.riking.calendar.pojo.GetVerificationModel;
import com.riking.calendar.pojo.UploadImageModel;
import com.riking.calendar.retrofit.APIClient;
import com.riking.calendar.retrofit.APIInterface;
import com.riking.calendar.util.image.ImagePicker;
import com.riking.calendar.widget.EmailAutoCompleteTextView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

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
    View emailRelativeLayout;
    SharedPreferences preference;
    APIInterface apiInterface = APIClient.getClient().create(APIInterface.class);
    View moreUserInfoView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        preference = getSharedPreferences(Const.PREFERENCE_FILE_NAME, MODE_PRIVATE);
        setContentView(R.layout.activity_user_info);
        myPhoto = (ImageView) findViewById(R.id.my_photo);
        findViewById(R.id.back).setOnClickListener(this);
        userName = (TextView) findViewById(R.id.name);
        email = (TextView) findViewById(R.id.email);
        department = (TextView) findViewById(R.id.depart);
        userName.setText(preference.getString(Const.USER_NAME, null));
        email.setText(preference.getString(Const.USER_EMAIL, null));
        department.setText(preference.getString(Const.USER_DEPT, null));
        userNameRelativeLayout = findViewById(R.id.user_name_relative_layout);
        emailRelativeLayout = findViewById(R.id.email_row_relative_layout);
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

        String imageUrl = preference.getString(Const.USER_IMAGE_URL, null);
        if (imageUrl != null && imageUrl.length() > 0) {
            VocationRecyclerViewAdapter.MyTask myTask = new VocationRecyclerViewAdapter.MyTask();
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

        File mFile1 = Environment.getExternalStorageDirectory();

        String fileName = "img1.jpg";

        File mFile2 = new File(mFile1, fileName);
        try {
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

        String sdPath = mFile1.getAbsolutePath().toString() + "/" + fileName;

        Log.d("zzw", "Your IMAGE ABSOLUTE PATH:-" + sdPath);

        File temp = new File(sdPath);

        if (!temp.exists()) {
            Log.d("zzw", "no image file at location :" + sdPath);
        }

        RequestBody reqFile = RequestBody.create(MediaType.parse("image"), mFile2);
        MultipartBody.Part body = MultipartBody.Part.createFormData("mFile", mFile2.getName(), reqFile);
        RequestBody name = RequestBody.create(MediaType.parse("text/plain"), "upload_test");
//        Logger.d("zzw", "userId: " + preference.getString(Const.USER_ID, null));

        apiInterface.postImage(body, preference.getString(Const.USER_ID, null)).enqueue(new Callback<UploadImageModel>() {
            @Override
            public void onResponse(Call<UploadImageModel> call, Response<UploadImageModel> response) {
                UploadImageModel r = response.body();

                if (r != null) {
//                        Log.d("zzw", "upload ok:  " + r.source().readUtf8());
                    SharedPreferences.Editor editor = preference.edit();
                    editor.putString(Const.USER_IMAGE_URL, r._data);
                    editor.commit();
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
        ImagePicker.pickImage(this, "Select your image:");
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
                String name = preference.getString(Const.USER_NAME, "");
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
                        String newName = input.getText().toString();
                        if (newName.length() > 0) {
                            userName.setText(newName);
                            AppUser user = new AppUser();
                            user.name = newName;
                            user.id = preference.getString(Const.USER_ID, null);
                            SharedPreferences.Editor editor = preference.edit();
                            editor.putString(Const.USER_NAME, newName);
                            //save the changes.
                            editor.commit();

                            apiInterface.updateUserInfo(user).enqueue(new Callback<GetVerificationModel>() {
                                @Override
                                public void onResponse(Call<GetVerificationModel> call, Response<GetVerificationModel> response) {
                                    GetVerificationModel user = response.body();
                                    Logger.d("zzw", "update user : " + user);
                                }

                                @Override
                                public void onFailure(Call<GetVerificationModel> call, Throwable t) {
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
                String name = preference.getString(Const.USER_EMAIL, "");
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
                        String emailText = input.getText().toString();
                        if (emailText.length() > 0) {
                            email.setText(emailText);
                            AppUser user = new AppUser();
                            user.email = emailText;
                            user.id = preference.getString(Const.USER_EMAIL, null);
                            SharedPreferences.Editor editor = preference.edit();
                            editor.putString(Const.USER_EMAIL, emailText);
                            //save the changes.
                            editor.commit();

                            apiInterface.updateUserInfo(user).enqueue(new Callback<GetVerificationModel>() {
                                @Override
                                public void onResponse(Call<GetVerificationModel> call, Response<GetVerificationModel> response) {
                                    GetVerificationModel user = response.body();
                                    Logger.d("zzw", "update user : " + user);
                                }

                                @Override
                                public void onFailure(Call<GetVerificationModel> call, Throwable t) {
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
