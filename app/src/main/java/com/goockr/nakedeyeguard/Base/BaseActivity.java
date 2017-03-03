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
import android.widget.Toast;

import com.goockr.nakedeyeguard.R;
import com.goockr.nakedeyeguard.SettingPage.WifiPage.MyNetworkStateService;
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
        Intent intents=new Intent(this, MyNetworkStateService.class);
        startService(intents);
    }


    public void setupView()
    {
        //获取当前时间
        iv_MainWifi=(ImageView)findViewById(R.id.iv_BaseWifi);
        ib_Back=(ImageButton) findViewById(R.id.bt_BaseBack);
        tc_BaseClock=(TextClock) findViewById(R.id.tc_BaseClock);
        setWifiIcon();
    }

    //
    public ImageButton getBackBtn(){return ib_Back;}

    //
    public TextClock getTextClock(){return tc_BaseClock;}

    @Override
    protected void onResume() {
        super.onResume();
        registerBroadcast();

    }

    @Override
    protected void onPause() {
        super.onPause();
        if (receiver!=null)
        unregisterReceiver(receiver);
        unregisterReceiver(networkReceiverHelper);
        receiver=null;
        networkReceiverHelper=null;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityCollector.removeActivity(this);
    }

    private void setWifiIcon()
    {
        boolean isNetworkAvailable =  WifiHelper.isNetworkAvailable(this);
        if (isNetworkAvailable)iv_MainWifi.setImageResource(R.drawable.icon_wifi);
        else iv_MainWifi.setImageResource(R.drawable.icon_wifi_10);
    }

    class ForceOfflineReceiver extends BroadcastReceiver
    {
        @Override
        public void onReceive(final Context context, Intent intent) {

        }
    }

    private void registerBroadcast() {

        IntentFilter intentFilter=new IntentFilter();
        intentFilter.addAction("com.goockr.broadcast.logout");
        receiver=new ForceOfflineReceiver();
        registerReceiver(receiver,intentFilter);

        networkReceiverHelper =new NetworkReceiverHelper() {
            @Override
            void onConnected() {
                iv_MainWifi.setImageResource(R.drawable.icon_wifi);
                Toast.makeText(BaseActivity.this,"++++",Toast.LENGTH_SHORT).show();
            }

            @Override
            void onDisConnected() {
                iv_MainWifi.setImageResource(R.drawable.icon_wifi_10);
                Toast.makeText(BaseActivity.this,"----",Toast.LENGTH_SHORT).show();
            }
        };

        IntentFilter intentNet = new IntentFilter();
        registerReceiver(networkReceiverHelper,intentNet);
    }
}

