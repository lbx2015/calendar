package com.riking.calendar.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import com.riking.calendar.R;
import com.riking.calendar.listener.ZCallBackWithFail;
import com.riking.calendar.pojo.AppUser;
import com.riking.calendar.pojo.base.ResponseModel;
import com.riking.calendar.retrofit.APIClient;
import com.riking.calendar.util.CONST;
import com.riking.calendar.util.StatusBarUtil;
import com.riking.calendar.util.StringUtil;
import com.riking.calendar.util.ZR;

/**
 * Created by zw.zhang on 2017/8/14.
 */

public class InputCellPhoneNumberActivity extends AppCompatActivity implements TextWatcher {
    EditText phoneNumber;
    View enterButton;
    boolean buttonAvailable;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_input_phone_number);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window w = getWindow(); // in Activity's onCreate() for instance
            w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }

        //set translucent background for the status bar
        StatusBarUtil.setTranslucent(this);
        phoneNumber = (EditText) findViewById(R.id.phone_number);
        //adding text change listener
        phoneNumber.addTextChangedListener(InputCellPhoneNumberActivity.this);
        phoneNumber.addTextChangedListener(new PhoneNumberFormattingTextWatcher() {
            //we need to know if the user is erasing or inputing some new character
            private boolean backspacingFlag = false;
            //we need to block the :afterTextChanges method to be called again after we just replaced the EditText text
            private boolean editedFlag = false;
            //we need to mark the cursor position and restore it after the edition
            private int cursorComplement;

            //text before changed
            private String phoneNumberBeforeChanged;
            private String phoneNumberAfterChanged;

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                //we store the cursor local relative to the end of the string in the EditText before the edition
                cursorComplement = s.length() - phoneNumber.getSelectionStart();
                //we check if the user ir inputing or erasing a character
                if (count > after) {
                    backspacingFlag = true;
                } else {
                    backspacingFlag = false;
                }
                phoneNumberBeforeChanged = s.toString().replaceAll("[^\\d]", "");
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // nothing to do here =D
            }

            @Override
            public void afterTextChanged(Editable s) {
                String string = s.toString();
                //what matters are the phone digits beneath the mask, so we always work with a raw string with only digits
                String phone = string.replaceAll("[^\\d]", "");

                //if the text was just edited, :afterTextChanged is called another time... so we need to verify the flag of edition
                //if the flag is false, this is a original user-typed entry. so we go on and do some magic
                if (!editedFlag) {

                    //we start verifying the worst case, many characters mask need to be added
                    //example: 999999999 <- 6+ digits already typed
                    // masked: (999) 999-999
                    if (phone.length() >= 7 && !backspacingFlag) {
                        //we will edit. next call on this textWatcher will be ignored
                        editedFlag = true;
                        //here is the core. we substring the raw digits and add the mask as convenient
                        String ans = phone.substring(0, 3) + " " + phone.substring(3, 7) + " " + phone.substring(7);
                        phoneNumber.setText(ans);
                        //we deliver the cursor to its original position relative to the end of the string
                        phoneNumber.setSelection(phoneNumber.getText().length() - cursorComplement);

                        //we end at the most simple case, when just one character mask is needed
                        //example: 99999 <- 3+ digits already typed
                        // masked: (999) 99
                    } else if (phone.length() >= 3 && !backspacingFlag) {
                        editedFlag = true;
                        String ans = phone.substring(0, 3) + " " + phone.substring(3);
                        phoneNumber.setText(ans);
                        phoneNumber.setSelection(phoneNumber.getText().length() - cursorComplement);
                    }else  if (backspacingFlag) {
                        if (phoneNumberBeforeChanged.equals(s.toString().replaceAll("[^\\d]", ""))) {
                            //delete the phone number ignore the blanks
                            phoneNumber.setText(s.toString().substring(0,s.length()-1));
                            phoneNumber.setSelection(phoneNumber.getText().length() - cursorComplement);
                        }
                    }
                    // We just edited the field, ignoring this cicle of the watcher and getting ready for the next
                } else {
                    editedFlag = false;
                }
            }
        });
        enterButton = findViewById(R.id.enter_button);
        enterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!buttonAvailable) {
                    return;
                }

                Editable number = phoneNumber.getText();
                if (number == null) {
                    Toast.makeText(phoneNumber.getContext(), "电话号码不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                final String phoneDigits = number.toString();
                if (!StringUtil.isMobileNO(phoneDigits)) {
                    Toast.makeText(phoneNumber.getContext(), "电话号码格式不正确", Toast.LENGTH_SHORT).show();
                    return;
                }
                final AppUser user = new AppUser();
                user.telephone = phoneDigits;
                user.phoneSeqNum = ZR.getDeviceId();
                final ProgressDialog dialog = new ProgressDialog(InputCellPhoneNumberActivity.this);
                dialog.setMessage("正在加载中");
                dialog.show();
                APIClient.getVarificationCode(user, new ZCallBackWithFail<ResponseModel<AppUser>>() {
                    @Override
                    public void callBack(ResponseModel<AppUser> u) {
                        dialog.dismiss();
                        if (failed) {

                        } else {
                            Intent i = new Intent(InputCellPhoneNumberActivity.this, InputVerifyCodeActivity.class);
                            i.putExtra(CONST.PHONE_NUMBER, phoneDigits);
                            //Success get the verify code
                            startActivity(i);
                        }
                    }
                });
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    public void clickBackNav(View v) {
        onBackPressed();
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (StringUtil.isMobileNO(s)) {
            buttonAvailable = true;
            //enable the button
            enterButton.setBackground(getDrawable(R.drawable.new_get_verify_code_back_ground_enabled));
        } else {
            buttonAvailable = false;
            enterButton.setBackground(getDrawable(R.drawable.new_get_verify_code_back_ground));
        }
    }

    @Override
    public void afterTextChanged(Editable s) {

    }
}
