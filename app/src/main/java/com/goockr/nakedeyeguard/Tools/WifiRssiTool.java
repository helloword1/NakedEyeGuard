package com.goockr.nakedeyeguard.Tools;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.WifiManager;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;


import com.tencent.calldemo.MyApplication;
import com.tencent.calldemo.R;

import static android.content.Context.WIFI_SERVICE;

/**
 * Created by CMQ-SSD on 2016/11/14.
 */

public class WifiRssiTool {

    public TextView wifiTextView;
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


//    public void getWifiInfo() {
//        WifiManager wifi_service = (WifiManager) getSystemService(WIFI_SERVICE);
//        WifiInfo wifiInfo = wifi_service.getConnectionInfo();
//    }

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
            int wifi_state = intent.getIntExtra("wifi_state", 0);
            int level = Math.abs(((WifiManager) MyApplication.getInstance().getSystemService(WIFI_SERVICE)).getConnectionInfo().getRssi());


            String TAG="TAG";
            String action = intent.getAction();
            Log.d(TAG, "action: " + action);
            if (action.equals(NETWORK_STATE_CHANGE)) {

                Log.d(TAG, "action: " + action);
            } else if (action.equals(WifiManager.WIFI_STATE_CHANGED_ACTION)) {
//                int wifistate = intent.getIntExtra(
//                        WifiManager.EXTRA_WIFI_STATE,
//                        WifiManager.WIFI_STATE_DISABLED);
//
//                if (wifistate == WifiManager.WIFI_STATE_DISABLED) {
//                    btnWifi.setBackgroundResource(R.drawable.selector_wifi_0);
//                } else if (wifistate == WifiManager.WIFI_STATE_ENABLED) {
//                    updateWifiStrength();
//                }

                switch (wifi_state) {
                    case WifiManager.WIFI_STATE_DISABLING:
                  //      wifiTextView.setText("wifi等級："+level+"");
                        wifiImageView.setBackground(null);
                        break;
                    case WifiManager.WIFI_STATE_DISABLED:
                   //     wifiTextView.setText("wifi等級："+level+"");
                        wifiImageView.setBackground(null);
                        break;
                    case WifiManager.WIFI_STATE_ENABLING:
                    //    wifiTextView.setText("wifi等級："+level+"");
                        wifiImageView.setBackground(null);
                        break;
                    case WifiManager.WIFI_STATE_ENABLED:
                     //   wifiTextView.setText("wifi等級："+level+"");
                        wifiImageView.setBackground(null);
                        break;
                    case WifiManager.WIFI_STATE_UNKNOWN:
                     //   wifiTextView.setText("wifi等級："+level+"");
                        wifiImageView.setBackground(null);
                        break;
                }

            } else if (action.equals(WifiManager.RSSI_CHANGED_ACTION)) {
                Log.d(TAG, "action: " + action);
               // wifiTextView.setText("wifi等級："+level+"");

                int level_1=65;
                int level_2=74;
                int level_3=83;
                int level_4=89;

                if (level<level_1)
                {

                    wifiImageView.setBackground(MyApplication.getInstance().getResources().getDrawable(R.drawable.wifi_1));
                }else if ((level>level_1||level==level_1)&&(level<level_2)){
                    wifiImageView.setBackground(MyApplication.getInstance().getResources().getDrawable(R.drawable.wifi_2));
                }else if ((level>level_2||level==level_2)&&(level<level_3)){
                    wifiImageView.setBackground(MyApplication.getInstance().getResources().getDrawable(R.drawable.wifi_3));
                }else if ((level>level_3||level==level_3)&&(level<level_4)){
                    wifiImageView.setBackground(MyApplication.getInstance().getResources().getDrawable(R.drawable.wifi_4));
                }else if (level>level_4){

                }

            }




        }
    };

    public void  registerReceiver()
    {
      // MyApplication.getInstance().registerReceiver(wifiIntentReceiver, wifiIntentFilter);
        Object app= MyApplication.getInstance();

       if (app!=null)
       {
           MyApplication.getInstance() .registerReceiver(wifiIntentReceiver, wifiIntentFilter);
       }
    }

    public void  unregisterReceiver()
    {
        MyApplication.getInstance().unregisterReceiver(wifiIntentReceiver);
    }


}















