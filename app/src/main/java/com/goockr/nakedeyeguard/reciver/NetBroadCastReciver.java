package com.goockr.nakedeyeguard.reciver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.goockr.nakedeyeguard.Base.BaseActivity;
import com.goockr.nakedeyeguard.R;

public class NetBroadCastReciver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        BaseActivity baseActivity = (BaseActivity) context;
        //此处是主要代码，
        //如果是在开启wifi连接和有网络状态下
        if (ConnectivityManager.CONNECTIVITY_ACTION.equals(intent.getAction())) {
            ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo info = intent.getParcelableExtra(ConnectivityManager.EXTRA_NETWORK_INFO);
            if (NetworkInfo.State.CONNECTED == info.getState()) {
                //连接状态
                Log.e("pzf", "有网络连接");
                baseActivity.iv_MainWifi.setImageResource(R.drawable.icon_wifi1);
            } else {
                Log.e("pzf", "无网络连接");
                baseActivity.iv_MainWifi.setImageResource(R.drawable.icon_nowifi3);

            }
        }
    }
}
