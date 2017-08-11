package com.goockr.nakedeyeguard.Tools;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.goockr.nakedeyeguard.R;


import static android.content.Context.WIFI_SERVICE;

/**
 * Created by CMQ-SSD on 2016/11/14.
 */

public class WifiRssiTool {

  //  public TextView wifiTextView;
    public ImageView wifiImageView;

    private static WifiRssiTool instance = null;

    // wifi相关
    IntentFilter wifiIntentFilter;   // wifi监听器

    private final String NETWORK_STATE_CHANGE = "android.net.conn.CONNECTIVITY_CHANGE";

    public static WifiRssiTool getInstance() {
        if (instance == null) {
            synchronized (WifiRssiTool.class) {
                if (instance == null) {
                    instance = new WifiRssiTool();

                }
            }
        }
        return instance;
    }


    public void create() {
        // wifi
        wifiIntentFilter = new IntentFilter();
        wifiIntentFilter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);
        wifiIntentFilter.addAction(NETWORK_STATE_CHANGE);
        wifiIntentFilter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);
        wifiIntentFilter.addAction(WifiManager.RSSI_CHANGED_ACTION);


    }

    // 声明wifi消息处理过程
    private BroadcastReceiver wifiIntentReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            String action = intent.getAction();
            Bundle extras = intent.getExtras();

//            1.         WIFI_STATE_DISABLED ： WIFI 不能使用，其值是： 1 。
//            2.         WIFI_STATE_DISABLING ： WIFI 正在关闭中，由于 WIFI 关闭是需要这一个过程，这个状态就表示 WIFI 正在关闭的过程中，其值是： 0 。
//            3.         WIFI_STATE_ENABLED ： WIFI 可以使用，其值是： 3 。
//            4.         WIFI_STATE_ENABLING ： WIFI 正在开启中，其道理同 WIFI_STATE_DISABLING ，其值是： 2 。
//           5.          WIFI_STATE_UNKNOWN ：未知网卡状态，当手机或程序出现了一些错误引起 WIFI 不可用时会是这个状态，其值是： 4 。
//            iv_MainWifi.setImageResource(R.drawable.icon_nowifi3);
//        } else {
//            iv_MainWifi.setImageResource(R.drawable.icon_wifi1);

            if (WifiManager.WIFI_STATE_CHANGED_ACTION.equals(action)) {//这个监听wifi的打开与关闭，与wifi的连接无关
                int wifiState = intent.getIntExtra(WifiManager.EXTRA_WIFI_STATE, -1);

                switch (wifiState) {
                    case WifiManager.WIFI_STATE_DISABLED:
                        Log.v("WIFI状态", "wifiState:WIFI_STATE_DISABLED");
                        wifiImageView.setImageResource(R.drawable.icon_nowifi3);
                        break;
                    case WifiManager.WIFI_STATE_DISABLING:
                        Log.v("WIFI状态", "wifiState:WIFI_STATE_DISABLING");
                        break;
                    case WifiManager.WIFI_STATE_ENABLED:
                        wifiImageView.setImageResource(R.drawable.icon_wifi1);

                        Log.v("WIFI状态", "wifiState:WIFI_STATE_ENABLED");
                        break;
                    case WifiManager.WIFI_STATE_ENABLING:
                        Log.v("WIFI状态", "wifiState:WIFI_STATE_ENABLING");
                        break;
                    case WifiManager.WIFI_STATE_UNKNOWN:
                        Log.v("WIFI状态", "wifiState:WIFI_STATE_UNKNOWN");
                        break;
                    //
                }
            }
        }
    };

    public void  registerReceiver()
    {
      // MyApplication.getInstance().registerReceiver(wifiIntentReceiver, wifiIntentFilter);
        Object app= App.getInstances();

       if (app!=null)
       {
           App.getInstances() .registerReceiver(wifiIntentReceiver, wifiIntentFilter);
       }
    }

    public void  unregisterReceiver()
    {
        App.getInstances().unregisterReceiver(wifiIntentReceiver);
    }


}















