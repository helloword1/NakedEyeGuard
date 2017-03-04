package com.goockr.nakedeyeguard.Base;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextClock;

import com.goockr.nakedeyeguard.R;
import com.goockr.nakedeyeguard.Screensaver.ScreensaverActivity;

import java.util.Calendar;

import static com.goockr.nakedeyeguard.App.isStartScreen;
import static com.goockr.nakedeyeguard.App.networkState;
import static com.goockr.nakedeyeguard.App.preferences;
import static com.goockr.nakedeyeguard.App.restTimeList;


/**
 * Created by JJT-ssd on 2017/2/3.
 */

public abstract class BaseActivity extends AppCompatActivity {

    //返回按钮
    private ImageButton ib_Back;
    //时期显示
    private ImageView iv_MainWifi;


    TextClock tc_BaseClock;
    private ForceOfflineReceiver receiver;
    NetworkReceiverHelper networkReceiverHelper;
    Thread screenThread;
    boolean isInterrupt=false;
    protected abstract int getLoyoutId();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLoyoutId());
        setupView();
        ActivityCollector.addActivity(this);

    }

    @Override
    protected void onResume() {
        super.onResume();
        isInterrupt=false;
        isStartScreen=true;
        beforTouchTime=Calendar.getInstance().getTimeInMillis();
    }

    @Override
    protected void onPause() {
        super.onPause();
        //isInterrupt=false;
        //screenThread=null;
    }

    public void setupView()
    {
        //获取当前时间
        iv_MainWifi=(ImageView)findViewById(R.id.iv_BaseWifi);
        ib_Back=(ImageButton) findViewById(R.id.bt_BaseBack);
        tc_BaseClock=(TextClock) findViewById(R.id.tc_BaseClock);
        registerBroadcast();


        screenThread= new Thread(new Runnable() {
            @Override
            public void run() {
                while (!isInterrupt)
                {
                    SystemClock.sleep(2000);
                    nowTime= Calendar.getInstance().getTimeInMillis();
                    if (isStartScreen) startScreensaver();
                }

            }
        });
       // screenThread.start();
    }

    //
    public ImageButton getBackBtn(){return ib_Back;}

    //
    public TextClock getTextClock(){return tc_BaseClock;}

    public boolean getNetWorkState() {return networkState;}

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityCollector.removeActivity(this);
        if (receiver!=null) receiver.unregisterReceiver();
        if (networkReceiverHelper!=null) networkReceiverHelper.unregisterReceiver();
        receiver=null;
        networkReceiverHelper=null;
    }

    long nowTime=0;
    long beforTouchTime=0;
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch(event.getActionMasked()){
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_POINTER_DOWN:
            case MotionEvent.ACTION_POINTER_UP:
            case MotionEvent.ACTION_MOVE:
                beforTouchTime = Calendar.getInstance().getTimeInMillis();
                break;
        }
        return false;
    }
    String scrrenTime = preferences.getString("RestTime","30秒");
    int scrrenTimeInt = restTimeList.indexOf(scrrenTime);
    long longTime=Long.MAX_VALUE;
    private void startScreensaver()
    {
        switch (scrrenTimeInt)
        {
            case 0:
                longTime=10000;
                break;
            case 1:
                longTime=30*1000;
                break;
            case 2:
                longTime=60*1000;
                break;
            case 3:
                longTime=3*60*1000;
                break;
            case 4:
                longTime=5*60*1000;
                break;
            case 5:
                longTime=10*60*1000;
                break;
            case 6:
                longTime=30*60*1000;
                break;
            case 7:
                longTime=60*60*1000;
                break;
            case 8:
                longTime=Long.MAX_VALUE;
                break;
        }
        Long ABS =nowTime-beforTouchTime;
       if (Math.abs(ABS)>longTime)
       {
           isStartScreen=false;
           Intent intentScreen = new Intent(BaseActivity.this,ScreensaverActivity.class);
           startActivity(intentScreen);
       }
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

        //设置Intent的Action属性

        networkReceiverHelper= new NetworkReceiverHelper() {
            @Override
            void onConnected() {
                iv_MainWifi.setImageResource(R.drawable.icon_wifi);
                networkState=true;
            }

            @Override
            void onDisConnected() {
                iv_MainWifi.setImageResource(R.drawable.icon_wifi_10);
                networkState=false;
            }
        };
        receiver=new ForceOfflineReceiver(this);
        networkReceiverHelper.registerReceiver(this);
    }
}

