package com.goockr.nakedeyeguard.Base;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.wifi.WifiManager;
import android.os.BatteryManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextClock;
import android.widget.TextView;

import com.goockr.nakedeyeguard.R;
import com.goockr.nakedeyeguard.Tools.NetUtils;
import com.goockr.nakedeyeguard.Tools.NoDoubleClickUtils;
import com.goockr.nakedeyeguard.Tools.PowerOffDialog;
import com.goockr.nakedeyeguard.reciver.NetBroadCastReciver;
import com.goockr.nakedeyeguard.view.FirstUsePage.FirstActivty;
import com.goockr.nakedeyeguard.widget.BatteryView.MyBatteryView;
import com.goockr.nakedeyeguard.widget.LockScreen.LockScreen;
import com.goockr.nakedeyeguard.widget.LockScreen.ScreenTouchReceiver;
import com.goockr.nakedeyeguard.widget.Screensaver.ScreensaverActivity;

import java.io.Serializable;
import java.util.Calendar;

import static com.goockr.nakedeyeguard.Tools.App.editor;
import static com.goockr.nakedeyeguard.Tools.App.interruptScreen;
import static com.goockr.nakedeyeguard.Tools.App.isStartScreen;
import static com.goockr.nakedeyeguard.Tools.App.networkState;
import static com.goockr.nakedeyeguard.Tools.App.preferences;
import static com.goockr.nakedeyeguard.Tools.App.restTimeList;


/**
 * Created by JJT-ssd on 2017/2/3.
 */

public abstract class BaseActivity extends AppCompatActivity {

    //返回按钮
    private ImageButton ib_Back;
    //时期显示
    public ImageView iv_MainWifi;

    //电池百分比显示
    private TextView tv_power;


    private ImageView iv_MainBatteryChangring;
    private Handler timeHandler;

    TextClock tc_BaseClock;
    private ForceOfflineReceiver receiver;
    private BatteryReceiver batteryReceiver;

    NetworkReceiverHelper networkReceiverHelper;
    public ProgressBar pbView;

    protected abstract int getLoyoutId();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLoyoutId());
        setupView();
        ActivityCollector.addActivity(this);
        if (mainKeyEn) {
            sendBroadcast(new Intent(ScreenTouchReceiver.TOUCHACTION));
        }

    }


    @Override
    protected void onRestart() {
        super.onRestart();
        if (mainKeyEn) {
            sendBroadcast(new Intent(ScreenTouchReceiver.TOUCHACTION));
        }
    }

    public void setupView() {
        //获取当前时间
        iv_MainWifi = (ImageView) findViewById(R.id.iv_BaseWifi);
        ib_Back = (ImageButton) findViewById(R.id.bt_BaseBack);
        tc_BaseClock = (TextClock) findViewById(R.id.tc_BaseClock);
        tv_power = (TextView) findViewById(R.id.tv_powerpercen);
        pbView = (ProgressBar) findViewById(R.id.pbView);

        if (preferences.getBoolean("PROGRESSBAR", false)) {
            pbView.setVisibility(View.VISIBLE);
        } else {
            pbView.setVisibility(View.GONE);
        }
        tc_BaseClock.setFormat24Hour("hh:mm");
        final MyBatteryView view_MainBattery = (MyBatteryView) findViewById(R.id.view_MainBattery);
        final TextView tv_power = (TextView) findViewById(R.id.tv_powerpercen);
        iv_MainBatteryChangring = (ImageView) findViewById(R.id.iv_MainBatteryChangring);

//         //主线程的 handler 接收到 子线程的消息，然后修改TextView的显示
        timeHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                int what = msg.what;
                switch (what) {
                    case 10001:
                        if (msg.arg1 == 0) {
                            iv_MainBatteryChangring.setVisibility(View.GONE);
                            //view_MainBattery.rePaint(Color.WHITE, (int) (44 * msg.arg2 * 0.01f));

                            if (msg.arg2 > 20) {
                                //根据电量绘画控件的宽度
                                view_MainBattery.rePaint(Color.WHITE, (int) (44 * msg.arg2 * 0.01f));
                            } else {
                                //根据电量绘画控件的宽度
                                view_MainBattery.rePaint(Color.RED, (int) (44 * msg.arg2 * 0.01f));
                            }

                        } else if (msg.arg1 == 1) {
                            iv_MainBatteryChangring.setVisibility(View.VISIBLE);


                            if (msg.arg2 > 20) {
                                //根据电量绘画控件的宽度
                                view_MainBattery.rePaint(Color.GREEN, (int) (44 * msg.arg2 * 0.01f));
                            } else {
                                //根据电量绘画控件的宽度
                                view_MainBattery.rePaint(Color.RED, (int) (44 * msg.arg2 * 0.01f));
                            }


                        }
                        tv_power.setText(msg.arg2 + "%");
                        break;
                }
            }
        };

        IntentFilter intentFilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        batteryReceiver = new BatteryReceiver();
        registerReceiver(batteryReceiver, intentFilter);

        getBackBtn().setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (NoDoubleClickUtils.isFastClick()) {
                    return;
                }
                finish();
            }
        });
    }

    /**
     * 跳转到对应的Activity
     */
    public <T> void showActivity(Class<T> activityCls) {
        showActivity(activityCls, null);
    }

    /**
     * 跳转到对应的Activity
     */
    public <T> void showActivity(Class<T> activityCls, Bundle extras) {
        Intent intent = new Intent(this, activityCls);
        if (null == extras) {
            startActivity(intent);
        } else {
            intent.putExtras(extras);
            startActivity(intent);
        }
    }

    /**
     * 跳转到对应的Activity
     */

    public <T> void showActivity(Class<T> activityCls, String key, Serializable obj) {
        Intent intent = new Intent(this, activityCls);
        intent.putExtra(key, obj);
        startActivity(intent);
    }

    /**
     * 跳转到对应的Activity
     */
    public <T> void showActivityByLoginFilter(Class<T> activityCls,
                                              Bundle extras) {
        Intent intent = new Intent(this, activityCls);
        if (null != extras) {
            intent.putExtras(extras);
        }
        showActivity(activityCls, extras);
    }

    @Override
    protected void onResume() {
        super.onResume();
        isStartScreen = true;
        interruptScreen = true;
        beforTouchTime = Calendar.getInstance().getTimeInMillis();
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (interruptScreen) {
                    SystemClock.sleep(2000);
                    nowTime = Calendar.getInstance().getTimeInMillis();
                    if (isStartScreen) startScreensaver();
                }
            }
        }).start();
        registerBroadcast();

    }

    @Override
    protected void onPause() {
        super.onPause();
        interruptScreen = false;
        interruptScreen = false;
    }


    long nowTime = 0;
    long beforTouchTime = 0;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_POINTER_DOWN:
            case MotionEvent.ACTION_POINTER_UP:
            case MotionEvent.ACTION_MOVE:
                beforTouchTime = Calendar.getInstance().getTimeInMillis();
                break;
        }
        return false;
    }

    long longTime = Long.MAX_VALUE;

    private void startScreensaver() {
        String scrrenTime = preferences.getString("RestTime", "30秒");
        int scrrenTimeInt = restTimeList.indexOf(scrrenTime);
        switch (scrrenTimeInt) {
            case 0:
                longTime = 10000;
                break;
            case 1:
                longTime = 30 * 1000;
                break;
            case 2:
                longTime = 60 * 1000;
                break;
            case 3:
                longTime = 3 * 60 * 1000;
                break;
            case 4:
                longTime = 5 * 60 * 1000;
                break;
            case 5:
                longTime = 10 * 60 * 1000;
                break;
            case 6:
                longTime = 30 * 60 * 1000;
                break;
            case 7:
                longTime = 60 * 60 * 1000;
                break;
            case 8:
                longTime = Long.MAX_VALUE;
                break;
        }

        Long ABS = nowTime - beforTouchTime;
        if (Math.abs(ABS) > longTime) {
            isStartScreen = false;
            interruptScreen = false;
            Intent intentScreen = new Intent(BaseActivity.this, ScreensaverActivity.class);
            startActivity(intentScreen);
        }
    }


    class ForceOfflineReceiver extends BroadcastReceiver {
        Context mContext;

        public ForceOfflineReceiver(Context mContext) {
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction("com.goockr.broadcast.logout");
            this.mContext = mContext;
            mContext.registerReceiver(this, intentFilter);
        }

        public void unregisterReceiver() {
            mContext.unregisterReceiver(this);
        }

        @Override
        public void onReceive(final Context context, Intent intent) {
            if (intent.getAction().equals("com.goockr.broadcast.logout")) {
                editor.putBoolean("FirstUser", true);
                editor.putString("TimeZone", "北京");
                editor.putBoolean("24HourSystem", true);
                editor.commit();
                ActivityCollector.finishAll();
                Intent startIntent = new Intent(context, FirstActivty.class);
                startActivity(startIntent);
            }
        }
    }

    //监听WiFi变化
    public void registerBroadcast() {
        //注册WiFi广播
        BroadcastReceiver receiver=new NetBroadCastReciver();
        //注册BroadCastReciver,设置监听的频道。就是filter中的
        IntentFilter filter=new IntentFilter();
        filter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);
        filter.addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION);
        filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(receiver, filter);

        String SSID = NetUtils.getCurWifi();
//        if (SSID.equals("0x") || SSID.equals("<unknown ssid>")) {
//            iv_MainWifi.setImageResource(R.drawable.icon_nowifi3);
//        } else {
//            iv_MainWifi.setImageResource(R.drawable.icon_wifi1);
//        }
    }

    //
    public ImageButton getBackBtn() {
        return ib_Back;
    }

    //
    public TextClock getTextClock() {
        return tc_BaseClock;
    }

    public boolean getNetWorkState() {
        return networkState;
    }

    public void jumpActivity(Context context, Activity activity) {
        Intent intent = new Intent(context, activity.getClass());
        startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (dialog != null) {
            dialog.dismiss();
        }
        ActivityCollector.removeActivity(this);
        if (receiver != null) receiver.unregisterReceiver();
        if (networkReceiverHelper != null) networkReceiverHelper.unregisterReceiver();
        receiver = null;
        networkReceiverHelper = null;
        if (batteryReceiver != null) unregisterReceiver(batteryReceiver);

    }

    private long firstpress = 0;
    private boolean mainKeyEn = true;

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        Log.i("baseact", event.toString());
        if (mainKeyEn) {
            if (event.getScanCode() == 158) {
                if (System.currentTimeMillis() - firstpress > 2000) {

                } else {
//                    startActivity(new Intent(this,ScreensaverActivity.class));
                    firstpress = 0;
                    startActivity(new Intent(this, LockScreen.Controller.class));
                }
            }
        }
        return false;
    }

    private AlertDialog dialog;

    /**
     * 弹出关机对话框
     */

    public void popDialog() {
        PowerOffDialog.show(this, new PowerOffDialog.PowerOffDialogCallBack() {
            @Override
            public void powerOffCancle() {
                MainKeyEnable(true);
            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        Log.i("baseact", event.toString());
        if (mainKeyEn) {
            if (event.getScanCode() == 158) {
                if (firstpress == 0) {
                    firstpress = System.currentTimeMillis();
                }
                if (System.currentTimeMillis() - firstpress > 2000) {
                    sendBroadcast(new Intent(ScreenTouchReceiver.SCREENON));
                    popDialog();
                    mainKeyEn = false;
                    firstpress = 0;
                }
            }
        }
        return false;
    }

    public void MainKeyEnable(boolean enable) {
        this.mainKeyEn = enable;
    }


    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        Log.i("setting", "dispatchTouchEvent");
        initScreen();
        return super.dispatchTouchEvent(ev);
    }

    /**
     * 控制[屏幕亮度和休眠,发送广播
     */
    protected void initScreen() {
        if (mainKeyEn) {
            sendBroadcast(new Intent(ScreenTouchReceiver.TOUCHACTION));
        }
    }

    private class BatteryReceiver extends BroadcastReceiver {


        private int mBatteryLevel;
        private int mBatteryScale;
        private int isCharging = 0;//1为充电状态 .。。

        @Override
        public void onReceive(Context context, Intent intent) {

            //判断它是否是为电量变化的Broadcast Action
            if (Intent.ACTION_BATTERY_CHANGED.equals(intent.getAction())) {
                Message message = Message.obtain();
                int status = intent.getIntExtra("status", BatteryManager.BATTERY_STATUS_UNKNOWN);//     表示是充电状态
                if (status == BatteryManager.BATTERY_STATUS_CHARGING) isCharging = 1;
                else isCharging = 0;
                message.what = 10001;
                //获取当前电量
                mBatteryLevel = intent.getIntExtra("level", 0);
                //电量的总刻度
                mBatteryScale = intent.getIntExtra("scale", 100);
                message.arg1 = isCharging;
                message.arg2 = mBatteryLevel;
                timeHandler.sendMessage(message);
            }
        }
    }
}

