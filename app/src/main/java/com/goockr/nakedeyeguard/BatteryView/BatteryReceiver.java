package com.goockr.nakedeyeguard.BatteryView;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.BatteryManager;
import android.os.Handler;
import android.os.Message;

/**
 * Created by JJT-ssd on 2017/3/9.
 * 电池电量变化检测
 */

public class BatteryReceiver extends BroadcastReceiver {


    private Handler timeHandler;
    private int mBatteryLevel;
    private int mBatteryScale;
    private int isCharging=0;//1为充电状态 .。。
    public BatteryReceiver(Handler timeHandler)
    {
        this.timeHandler=timeHandler;
    }
    @Override
    public void onReceive(Context context, Intent intent) {

        //判断它是否是为电量变化的Broadcast Action
        if(Intent.ACTION_BATTERY_CHANGED.equals(intent.getAction())){
            Message message = Message.obtain();
            int status=intent.getIntExtra("status", BatteryManager.BATTERY_STATUS_UNKNOWN);//     表示是充电状态
            if(status==BatteryManager.BATTERY_STATUS_CHARGING) isCharging=1;
            else isCharging=0;
            message.what = 10001;
            //获取当前电量
            mBatteryLevel = intent.getIntExtra("level", 0);
            //电量的总刻度
            mBatteryScale = intent.getIntExtra("scale", 100);
            message.arg1= isCharging;
            message.arg2 = mBatteryLevel;
            timeHandler.sendMessage(message);
        }
    }
}