package com.riking.calendar.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import com.riking.calendar.R;
import com.riking.calendar.activity.MainActivity;
import com.riking.calendar.jiguang.Logger;

/**
 * Created by zw.zhang on 2017/7/10.
 */

public class AlarmService extends Service {
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
        Toast.makeText(this, "ServiceClass.onStart()", Toast.LENGTH_LONG).show();
        Log.d("zzw", "Service got started");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, final int startId) {
        Logger.d("zzw", "startId: " + startId);
        new Thread(new Runnable() {
            @Override
            public void run() {
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(AlarmService.this, "onStartCommand got started", Toast.LENGTH_LONG).show();
                        Log.d("zzw", "onStartCommand got started");
                    }
                });
                NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                Intent intent2 = new Intent(AlarmService.this, MainActivity.class);
                PendingIntent pendingIntent = PendingIntent.getActivity(getApplication(), 0, intent2, 0);
                // Start without a delay
// Each element then alternates between vibrate, sleep, vibrate, sleep...
                long[] pattern1 = {0, 100, 1000, 300, 200, 100, 500, 200, 100};
                Notification notify = new NotificationCompat.Builder(getApplication())
                        .setSmallIcon(R.drawable.cat_1)
                        .setTicker("您的***项目即将到期，请及时处理！")
                        .setContentTitle("项目到期提醒")
                        .setStyle(new NotificationCompat.BigTextStyle().bigText("此处注明的是有关需要提醒项目的某些重要内容"))
                        .setContentIntent(pendingIntent)
//                        .setAutoCancel(true)
                        .setDefaults(Notification.DEFAULT_ALL)
                        .setVibrate(pattern1)
                        .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                        // High priority.
                        .setPriority(NotificationCompat.PRIORITY_MAX)

                        // Public visibility means that the notification will appear even with screen locked.
                        .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)

                        // Setting a category for notification.
                        .setCategory(NotificationCompat.CATEGORY_ALARM)

                        // Keep the notification alive until you manually dismiss it
                        .setOngoing(true)

                        .build();
                manager.notify(startId, notify);
            }
        }).start();
        return super.onStartCommand(intent, flags, startId);
    }
}
