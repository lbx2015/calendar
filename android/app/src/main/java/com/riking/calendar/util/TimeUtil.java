package com.riking.calendar.util;

import android.os.Handler;
import android.os.Message;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by zw.zhang on 2017/8/24.
 */

public class TimeUtil {
    TextView textView;
    private int time = 60;
    private Timer timer;
    private String btnText = "重新获取验证码";
    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case 1:

                    if (time > 0) {
                        textView.setText(time + "秒后重新发送");
                        textView.setTextSize(14);
                    } else {

                        timer.cancel();
                        textView.setText(btnText);
                        textView.setEnabled(true);
                        textView.setTextSize(14);
                    }

                    break;

                default:
                    break;
            }

        }
    };

    public TimeUtil(TextView t) {
        super();
        this.textView = t;
    }

    public void RunTimer() {
        timer = new Timer();
        TimerTask task = new TimerTask() {

            @Override
            public void run() {
                time--;
                Message msg = handler.obtainMessage();
                msg.what = 1;
                handler.sendMessage(msg);
            }
        };
        timer.schedule(task, 100, 1000);
    }
}
