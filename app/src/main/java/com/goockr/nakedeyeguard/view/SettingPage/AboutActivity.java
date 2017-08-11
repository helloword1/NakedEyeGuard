package com.goockr.nakedeyeguard.view.SettingPage;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.BatteryManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.goockr.nakedeyeguard.Base.BaseActivity;
import com.goockr.nakedeyeguard.widget.BatteryView.MyBatteryView;
import com.goockr.nakedeyeguard.R;
import com.goockr.nakedeyeguard.Tools.PowerOffDialog;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * 关于
 */
public class AboutActivity extends BaseActivity {

    private ImageView iv_MainBatteryChangring;
    private Handler timeHandler;
    BatteryReceiver batteryReceiver;
    @Override
    protected int getLoyoutId() {return R.layout.activity_about;}

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        timeHandler = new Handler();
        getBackBtn().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(R.anim.fragment_slide_left_in, R.anim.fragment_slide_right_out);
            }
        });

        iv_MainBatteryChangring = (ImageView) findViewById(R.id.iv_MainBatteryChangring);
        final MyBatteryView view_MainBattery = (MyBatteryView) findViewById(R.id.view_MainBattery);
        final TextView tv_power=(TextView)findViewById(R.id.tv_powerpercen);

        //主线程的 handler 接收到 子线程的消息，然后修改TextView的显示
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
                            if (msg.arg2>20)
                            {
                                //根据电量绘画控件的宽度
                                view_MainBattery.rePaint(Color.WHITE, (int) (44 * msg.arg2 * 0.01f));
                            }else {
                                //根据电量绘画控件的宽度
                                view_MainBattery.rePaint(Color.RED, (int) (44 * msg.arg2 * 0.01f));
                            }

                        } else if (msg.arg1 == 1) {
                            iv_MainBatteryChangring.setVisibility(View.VISIBLE);
                            //根据电量绘画控件的宽度
                            //view_MainBattery.rePaint(Color.GREEN, (int) (44 * msg.arg2 * 0.01f));
                            if (msg.arg2>20)
                            {
                                //根据电量绘画控件的宽度
                                view_MainBattery.rePaint(Color.GREEN, (int) (44 * msg.arg2 * 0.01f));
                            }else {
                                //根据电量绘画控件的宽度
                                view_MainBattery.rePaint(Color.RED, (int) (44 * msg.arg2 * 0.01f));
                            }

                        }
                        tv_power.setText(msg.arg2+"%");
                        break;
                }
            }
        };

        IntentFilter intentFilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        batteryReceiver=new BatteryReceiver();
        registerReceiver(batteryReceiver, intentFilter);

        initUI();

    }

    private void initUI() {


        String id=getMac();
        TextView mac_tv=(TextView)findViewById(R.id.activity_about_mac_tv);
        mac_tv.setText(id);

//        TextView version_tv=(TextView)findViewById(R.id.activity_about_mac_tv);
//        version_tv.setText("1.0.0");


    }


    private AlertDialog dialog;
    @Override
    public void popDialog() {
//        AlertDialog.Builder builder = new AlertDialog.Builder(this);
//        builder.setTitle("关机");
//        builder.setMessage(R.string.poweroff);
//        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                Process Proc = null;
//                try {
//                    Proc = Runtime.getRuntime().exec("./system/bin/shutdown.sh");
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//        });
//        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                dialog.dismiss();
//                MainKeyEnable(true);
//            }
//        });
//        builder.setCancelable(false);
//        dialog = builder.show();

        PowerOffDialog.show(this,new PowerOffDialog.PowerOffDialogCallBack() {
            @Override
            public void powerOffCancle() {
                MainKeyEnable(true);
            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (dialog!=null){
            dialog.dismiss();
        }
        if (batteryReceiver != null) unregisterReceiver(batteryReceiver);
    }

    private class BatteryReceiver extends BroadcastReceiver {

        private int mBatteryLevel;
        private int mBatteryScale;
        private int isCharging=0;//1为充电状态 .。。

        @Override
        public void onReceive(Context context, Intent intent) {

            //判断它是否是为电量变化的Broadcast Action
            if(Intent.ACTION_BATTERY_CHANGED.equals(intent.getAction())){
                Message message = Message.obtain();
                int status=intent.getIntExtra("status", BatteryManager.BATTERY_STATUS_UNKNOWN);//     表示是充电状态
                if(status==BatteryManager.BATTERY_STATUS_CHARGING) isCharging=1;
                else isCharging=0;
                message.what = 10001;
                //获取当前电量
                mBatteryLevel = intent.getIntExtra("level", 0);
                //电量的总刻度
                mBatteryScale = intent.getIntExtra("scale", 100);
                message.arg1= isCharging;
                message.arg2 = mBatteryLevel;
                timeHandler.sendMessage(message);
            }
        }
    }

    String getMac() {
//        String macSerial = null;
//        String str = "";
//        try {
//            Process pp = Runtime.getRuntime().exec(
//                    "cat /proc/cpuinfo");
//            InputStreamReader ir = new InputStreamReader(pp.getInputStream());
//            LineNumberReader input = new LineNumberReader(ir);
//
//
//            for (; null != str;) {
//                str = input.readLine();
//                if (str != null) {
//                    macSerial = str.trim();// 去空格
//                    break;
//                }
//            }
//        } catch (IOException ex) {
//            // 赋予默认值
//            ex.printStackTrace();
//        }
//        return macSerial;

        InputStream is = null;
        try {
            is = new FileInputStream("/proc/cpuinfo");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        //new一个StringBuffer用于字符串拼接
        StringBuffer sb = new StringBuffer();
        String line = null;
        try {
            //当输入流内容读取完毕时
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
            //记得关闭流数据 节约内存消耗
            is.close();
            reader.close();
            String result = sb.toString();
            result = result.split("Serial")[1].split(":")[1].trim();


            return result;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return "";
    }

}
