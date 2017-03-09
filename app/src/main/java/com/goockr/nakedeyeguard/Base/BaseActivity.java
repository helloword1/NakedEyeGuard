package com.goockr.nakedeyeguard.Base;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextClock;

import com.goockr.nakedeyeguard.BatteryView.BatteryReceiver;
import com.goockr.nakedeyeguard.BatteryView.MyBatteryView;
import com.goockr.nakedeyeguard.FirstUsePage.FirstActivty;
import com.goockr.nakedeyeguard.R;
import com.goockr.nakedeyeguard.Screensaver.ScreensaverActivity;

import java.util.Calendar;

import static com.goockr.nakedeyeguard.App.editor;
import static com.goockr.nakedeyeguard.App.interruptScreen;
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

    private ImageView iv_MainBatteryChangring;
    private Handler timeHandler;

    TextClock tc_BaseClock;
    private ForceOfflineReceiver receiver;
    BatteryReceiver batteryReceiver;

    NetworkReceiverHelper networkReceiverHelper;


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
        iv_MainBatteryChangring=(ImageView)findViewById(R.id.iv_MainBatteryChangring);
        final MyBatteryView view_MainBattery=(MyBatteryView)findViewById(R.id.view_MainBattery);
        //主线程的 handler 接收到 子线程的消息，然后修改TextView的显示
        timeHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                int what = msg.what;
                switch (what) {
                    case 10001:
                        if (msg.arg1==0)
                        {
                            iv_MainBatteryChangring.setVisibility(View.GONE);
                            view_MainBattery.rePaint(Color.WHITE,(int)(view_MainBattery.getWidth()*msg.arg2*0.01f));
                        }
                        else if(msg.arg1==1)
                        {
                            iv_MainBatteryChangring.setVisibility(View.VISIBLE);
                            //根据电量绘画控件的宽度
                            view_MainBattery.rePaint(Color.GREEN,(int)(view_MainBattery.getWidth()*msg.arg2*0.01f));
                        }

                        break;
                }
            }
        };


        registerBroadcast();

    }

    @Override
    protected void onResume() {
        super.onResume();

        isStartScreen=true;
        interruptScreen=true;
        beforTouchTime=Calendar.getInstance().getTimeInMillis();
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (interruptScreen)
                {
                    SystemClock.sleep(2000);
                    nowTime= Calendar.getInstance().getTimeInMillis();
                    if (isStartScreen) startScreensaver();
                }
            }
        }).start();

    }

    @Override
    protected void onPause() {
        super.onPause();
        interruptScreen=false;
        interruptScreen=false;
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

    long longTime=Long.MAX_VALUE;
    private void startScreensaver()
    {
        String scrrenTime = preferences.getString("RestTime","30秒");
        int scrrenTimeInt = restTimeList.indexOf(scrrenTime);

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
           interruptScreen=false;
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
            if (intent.getAction().equals("com.goockr.broadcast.logout"))
            {
                editor.putBoolean("FirstUser",true);
                editor.putString("TimeZone","北京");
                editor.putBoolean("24HourSystem",true);
                editor.commit();
                ActivityCollector.finishAll();
                Intent startIntent=new Intent(context, FirstActivty.class);
                startActivity(startIntent);
            }
        }
    }

    private void registerBroadcast() {

        IntentFilter intentFilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        batteryReceiver=new BatteryReceiver(timeHandler);
        registerReceiver(batteryReceiver, intentFilter);
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
        if (batteryReceiver!=null) unregisterReceiver(batteryReceiver);
    }

}

