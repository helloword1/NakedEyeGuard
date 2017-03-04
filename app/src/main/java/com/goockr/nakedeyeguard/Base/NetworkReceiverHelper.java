package com.goockr.nakedeyeguard.Base;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by JJT-ssd on 2017/3/3.
 */

public abstract class NetworkReceiverHelper extends BroadcastReceiver {

    private ConnectivityManager connectivityManager;
    private NetworkInfo info;

    Context mContext;
    NetworkReceiverHelper networkReceiverHelper;
    public void registerReceiver(Context mContext)
    {
        IntentFilter intentNet = new IntentFilter();
        intentNet.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        mContext.registerReceiver(this,intentNet);
        this.mContext=mContext;
    }

    public  void unregisterReceiver()
    {
        mContext.unregisterReceiver(this);
    }
    @Override
    public void onReceive(Context context, Intent intent)
    {
        //当网络发生变化时
        String action = intent.getAction();
        if (action.equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
            connectivityManager = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
            info = connectivityManager.getActiveNetworkInfo();
            if(info != null && info.isAvailable()) {
                onConnected();
            } else {
                onDisConnected();
            }
        }
    }
    //连接时的事件
    abstract void onConnected() ;

    abstract void onDisConnected();
    //没有连接时的事件

}
