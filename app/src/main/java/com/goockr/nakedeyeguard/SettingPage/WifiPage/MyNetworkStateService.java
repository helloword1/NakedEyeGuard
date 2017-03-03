package com.goockr.nakedeyeguard.SettingPage.WifiPage;

/**
 * Created by JJT-ssd on 2017/3/3.
 */

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.IBinder;
import android.text.TextUtils;

/**
 * 网络发生变化时的service
 */
public class MyNetworkStateService extends Service {

    private ConnectivityManager connectivityManager;
    private NetworkInfo info;
    public static final String ACTION_NO_CONNECTION = "ACTION_NO_CONNECTION"; //网络没有连接
    public static final String ACTION_CONNECTIONED = "ACTION_CONNECTIONED";   //网络连接


    private BroadcastReceiver broadcastReceiver=new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            //当网络发生变化时
            String action = intent.getAction();
            if (action.equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
                connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
                info = connectivityManager.getActiveNetworkInfo();
                if(info != null && info.isAvailable()) {
                    sendNetworkStateBroadCast(ACTION_CONNECTIONED);
                } else {
                    sendNetworkStateBroadCast(ACTION_NO_CONNECTION);
                }
            }
        }
    };

    /**
     * 发送本地广播通知网络状态
     * @param action
     */
    private void sendNetworkStateBroadCast(String action) {
        if (TextUtils.equals(ACTION_CONNECTIONED, action) ||
                TextUtils.equals(ACTION_NO_CONNECTION, action)) {
            Intent intent = new Intent(action);
            sendBroadcast(intent);
        }
    }


    public MyNetworkStateService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        IntentFilter intentFilter=new IntentFilter();
        intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(broadcastReceiver,intentFilter);
    }

    @Override
    public void onDestroy() {
        unregisterReceiver(broadcastReceiver);
        super.onDestroy();
    }
}