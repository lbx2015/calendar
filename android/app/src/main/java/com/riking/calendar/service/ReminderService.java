package com.riking.calendar.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import com.ldf.calendar.Const;
import com.riking.calendar.R;
import com.riking.calendar.activity.ViewPagerActivity;

/**
 * Created by zw.zhang on 2017/7/10.
 */

public class ReminderService extends Service {
    public String reminderTitle;

    @Override
    public void onCreate() {
        // TODO Auto-generated method stub
        super.onCreate();
        Log.d("zzw", "Service got created");
        Toast.makeText(this, "ServiceClass.onCreate()", Toast.LENGTH_LONG).show();
    }

    @Override
    public IBinder onBind(Intent arg0) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
    }

    @Override
    public void onStart(Intent intent, int startId) {
        // TODO Auto-generated method stub
        super.onStart(intent, startId);
        reminderTitle = intent.getExtras().getString(Const.REMINDER_TITLE);
        Toast.makeText(this, reminderTitle + " ServiceClass.onStart()", Toast.LENGTH_LONG).show();
        Log.d("zzw", reminderTitle + "Service got started");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(ReminderService.this, "onStartCommand got started", Toast.LENGTH_LONG).show();
                        Log.d("zzw", "onStartCommand got started");
                    }
                });
                NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                Intent intent2 = new Intent(ReminderService.this, ViewPagerActivity.class);
                PendingIntent pendingIntent = PendingIntent.getActivity(getApplication(), 0, intent2, 0);
                // Each element then alternates between vibrate, sleep, vibrate, sleep...
                long[] pattern1 = {0, 100, 1000, 300, 200, 100, 500, 200, 100, 100, 1000, 300, 200, 100, 500, 200, 100};
                Notification notify = new NotificationCompat.Builder(getApplication())
                        .setSmallIcon(R.mipmap.launcher_icon)
                        .setTicker(reminderTitle)
                        .setContentTitle(reminderTitle)
                        .setStyle(new NotificationCompat.BigTextStyle().bigText(reminderTitle))
//                        .setContentIntent(pendingIntent)
                        .setAutoCancel(true)
                        // Each element then alternates between vibrate, sleep, vibrate, sleep...
                        .setVibrate(pattern1)
                        .setOnlyAlertOnce(true)
//                        .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                        .setSound(Uri.parse("android.resource://" + getApplicationContext().getPackageName() + "/" + R.raw.nice_ringtone_2017))
                        .build();
                /**
                 * 手机处于锁屏状态时， LED灯就会不停地闪烁， 提醒用户去查看手机,下面是绿色的灯光一 闪一闪的效果
                 */
                notify.ledARGB = Color.GREEN;// 控制 LED 灯的颜色，一般有红绿蓝三种颜色可选
                notify.ledOnMS = 1000;// 指定 LED 灯亮起的时长，以毫秒为单位
                notify.ledOffMS = 1000;// 指定 LED 灯暗去的时长，也是以毫秒为单位
                notify.flags = Notification.FLAG_SHOW_LIGHTS;// 指定通知的一些行为，其中就包括显示
                manager.notify(1, notify);
            }
        }).start();
        return super.onStartCommand(intent, flags, startId);
    }
}
