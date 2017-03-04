package com.goockr.nakedeyeguard.Screensaver;

import android.app.KeyguardManager;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.os.PowerManager;

/**
 * Created by JJT-ssd on 2017/3/4.
 */


public class ScreensaverReceiver extends Service {

    @Override
    public IBinder onBind(Intent intent) {
        // TODO Auto-generated method stub
        return null;
    }

    public void onCreate() {
        // 屏蔽系统的屏保
        KeyguardManager manager = (KeyguardManager) getSystemService(Context.KEYGUARD_SERVICE);
        KeyguardManager.KeyguardLock lock = manager.newKeyguardLock("KeyguardLock");
        lock.disableKeyguard();


        // 注册一个监听屏幕开启和关闭的广播
        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_SCREEN_ON);
        filter.addAction(Intent.ACTION_SCREEN_OFF);
        registerReceiver(screenReceiver, filter);
    }

    BroadcastReceiver screenReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            // TODO Auto-generated method stub
            String action = intent.getAction();
            if (action.equals(Intent.ACTION_SCREEN_ON)) {

            } else if (action.equals(Intent.ACTION_SCREEN_OFF)) {//如果接受到关闭屏幕的广播
                //开启屏幕唤醒，常亮
                acquire(ScreensaverReceiver.this);
                Intent intent2 = new Intent(ScreensaverReceiver.this,
                        ScreensaverActivity.class);
//                intent2.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent2);
                release();
            }
        }
    };

    public void onDestroy() {
        unregisterReceiver(screenReceiver);
    };


    private  PowerManager.WakeLock wakeLock;
    /**开启 保持屏幕唤醒*/
    public  void acquire(Context context) {
        PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        wakeLock = pm.newWakeLock(PowerManager.SCREEN_BRIGHT_WAKE_LOCK|PowerManager.ON_AFTER_RELEASE, "PowerManagerWakeLock");
        wakeLock.acquire();
    }

    /**关闭 保持屏幕唤醒*/
    public  void release() {
        if (wakeLock != null) {
            wakeLock.release();
            wakeLock = null;
        }
    }

}