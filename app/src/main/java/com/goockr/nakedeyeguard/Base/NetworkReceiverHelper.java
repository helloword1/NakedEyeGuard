package com.goockr.nakedeyeguard.Base;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by JJT-ssd on 2017/3/3.
 */

public abstract class NetworkReceiverHelper extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals("ACTION_NO_CONNECTION"))
        {
            onDisConnected();
        }
        else if (intent.getAction().equals("ACTION_CONNECTIONED"))
        {
            onConnected();
        }
    }

    abstract void onConnected() ;
        //连接时的事件

    abstract void onDisConnected();
        //没有连接时的事件

}
