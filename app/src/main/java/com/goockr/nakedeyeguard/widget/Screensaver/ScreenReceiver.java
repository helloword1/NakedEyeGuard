package com.goockr.nakedeyeguard.widget.Screensaver;

import android.app.KeyguardManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

/**
 * Created by JJT-ssd on 2017/3/4.
 */


public class ScreenReceiver extends BroadcastReceiver {

    Context mContext;
    public void registerReceiver(Context context )
    {
        this.mContext=context;
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Intent.ACTION_SCREEN_ON);
        intentFilter.addAction(Intent.ACTION_SCREEN_OFF);
        try {
            context.registerReceiver(this,intentFilter);
        }catch (Exception e){

        }
    }

    public void unregisterReceiver()
    {
        mContext.unregisterReceiver(this);
    }


    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO Auto-generated method stub
        String action = intent.getAction();
        if (action.equals(Intent.ACTION_SCREEN_ON)) {
            System.out.println("ACTION_SCREEN_ON");
        } else if (action.equals(Intent.ACTION_SCREEN_OFF)) {//如果接受到关闭屏幕的广播

            //屏蔽系统屏保
            KeyguardManager mKeyguardManager = (KeyguardManager)context.getSystemService(Context.KEYGUARD_SERVICE);
            KeyguardManager.KeyguardLock mKeyguardLock = mKeyguardManager.newKeyguardLock("");
            mKeyguardLock.disableKeyguard();//解锁屏幕，也就是 关闭 屏幕 锁定 功能
            Intent intent2 = new Intent(context, ScreensaverActivity.class);
            context.startActivity(intent2);
        }
    }

}