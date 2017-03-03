package com.goockr.nakedeyeguard.Base;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextClock;

import com.goockr.nakedeyeguard.R;


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
    protected abstract int getLoyoutId();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLoyoutId());
        setupView();
        ActivityCollector.addActivity(this);
    }


    public void setupView()
    {
        //获取当前时间
        iv_MainWifi=(ImageView)findViewById(R.id.iv_BaseWifi);
        ib_Back=(ImageButton) findViewById(R.id.bt_BaseBack);
        tc_BaseClock=(TextClock) findViewById(R.id.tc_BaseClock);

        //
//        tc_BaseClock.setFormat12Hour(getDateFormate(this));
//        tc_BaseClock.setFormat24Hour(getDateFormate(this));
//
    }
    private String getDateFormate(Context context){
        return Settings.System.getString(context.getContentResolver(),
                Settings.System.DATE_FORMAT);
    }
    //
    public ImageButton getBackBtn(){return ib_Back;}

    //
    public TextClock getTextClock(){return tc_BaseClock;}

    @Override
    protected void onResume() {
        super.onResume();
        IntentFilter intentFilter=new IntentFilter();
        intentFilter.addAction("com.goockr.broadcast.logout");
        receiver=new ForceOfflineReceiver();
        registerReceiver(receiver,intentFilter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (receiver!=null)
        unregisterReceiver(receiver);
        receiver=null;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityCollector.removeActivity(this);
    }

    class ForceOfflineReceiver extends BroadcastReceiver
    {
        @Override
        public void onReceive(final Context context, Intent intent) {

        }
    }
}
