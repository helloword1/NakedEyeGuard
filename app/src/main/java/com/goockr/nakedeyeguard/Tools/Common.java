package com.goockr.nakedeyeguard.Tools;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;

import com.goockr.nakedeyeguard.Screensaver.ScreensaverActivity;
import com.kaopiz.kprogresshud.KProgressHUD;

import java.util.Calendar;

import static com.goockr.nakedeyeguard.App.alarmManager;

/**
 * Created by JJT-ssd on 2017/3/2.
 */

public class Common {

    public static void scheduleDismiss(final KProgressHUD hud) {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                hud.dismiss();
            }
        }, 2000);
    }

    /*
    * name:通知名字，作为通知id使用
    * content：通知内容
    * time：倒时时（秒）
    * */
    public static void addLocalNotication(Context content, long time) {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(System.currentTimeMillis()+time*1000);
        Intent intent = new Intent(content, ScreensaverActivity.class);
        PendingIntent pi = PendingIntent.getBroadcast(content, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(),cal.getTimeInMillis()+time*1000, pi);
    }
}
