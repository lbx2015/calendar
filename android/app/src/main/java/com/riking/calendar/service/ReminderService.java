package com.riking.calendar.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import com.ldf.calendar.Const;
import com.riking.calendar.R;
import com.riking.calendar.activity.MainActivity;

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
                Intent intent2 = new Intent(ReminderService.this, MainActivity.class);
                PendingIntent pendingIntent = PendingIntent.getActivity(getApplication(), 0, intent2, 0);
                Notification notify = new NotificationCompat.Builder(getApplication())
                        .setSmallIcon(R.drawable.cat_1)
                        .setTicker(reminderTitle)
                        .setContentTitle(reminderTitle)
                        .setStyle(new NotificationCompat.BigTextStyle().bigText(reminderTitle))
                        .setContentIntent(pendingIntent)
                        .setAutoCancel(true)
                        .build();
                manager.notify(1, notify);
            }
        }).start();
        return super.onStartCommand(intent, flags, startId);
    }
}
