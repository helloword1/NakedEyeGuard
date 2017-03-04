package com.goockr.nakedeyeguard.Base;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextClock;

import com.goockr.nakedeyeguard.R;
import com.goockr.nakedeyeguard.Tools.Common;
import com.goockr.nakedeyeguard.Tools.WifiHelper;


/**
 * Created by JJT-ssd on 2017/2/3.
 */

public abstract class BaseActivity extends AppCompatActivity {

    //返回按钮
    private ImageButton ib_Back;
    //时期显示
    private ImageView iv_MainWifi;
    //
    TextClock tc_BaseClock;
    private ForceOfflineReceiver receiver;
    NetworkReceiverHelper networkReceiverHelper;
    protected abstract int getLoyoutId();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLoyoutId());
        setupView();
        ActivityCollector.addActivity(this);
        Common.addLocalNotication(this,10);
    }


    public void setupView()
    {
        //获取当前时间
        iv_MainWifi=(ImageView)findViewById(R.id.iv_BaseWifi);
        ib_Back=(ImageButton) findViewById(R.id.bt_BaseBack);
        tc_BaseClock=(TextClock) findViewById(R.id.tc_BaseClock);
        registerBroadcast();
    }

    //
    public ImageButton getBackBtn(){return ib_Back;}

    //
    public TextClock getTextClock(){return tc_BaseClock;}

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityCollector.removeActivity(this);
        if (receiver!=null) receiver.unregisterReceiver();
        if (networkReceiverHelper!=null) networkReceiverHelper.unregisterReceiver();
        receiver=null;
        networkReceiverHelper=null;
    }

    private void setWifiIcon()
    {
        boolean isNetworkAvailable =  WifiHelper.isNetworkAvailable(this);
        if (isNetworkAvailable)iv_MainWifi.setImageResource(R.drawable.icon_wifi);
        else iv_MainWifi.setImageResource(R.drawable.icon_wifi_10);
    }

    class ForceOfflineReceiver extends BroadcastReceiver
    {
        Context mContext;

        public ForceOfflineReceiver(Context mContext)
        {
            IntentFilter intentFilter=new IntentFilter();
            intentFilter.addAction("com.goockr.broadcast.logout");
            this.mContext=mContext;
            mContext.registerReceiver(this,intentFilter);
        }

        public  void unregisterReceiver()
        {
            mContext.unregisterReceiver(this);
        }
        @Override
        public void onReceive(final Context context, Intent intent) {

        }
    }

    private void registerBroadcast() {

        networkReceiverHelper= new NetworkReceiverHelper() {
            @Override
            void onConnected() {
                iv_MainWifi.setImageResource(R.drawable.icon_wifi);
            }

            @Override
            void onDisConnected() {
                iv_MainWifi.setImageResource(R.drawable.icon_wifi_10);
            }
        };
        receiver=new ForceOfflineReceiver(this);
        networkReceiverHelper.registerReceiver(this);
    }
}

