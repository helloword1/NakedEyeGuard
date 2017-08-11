package com.goockr.nakedeyeguard.Base;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by JJT-ssd on 2017/3/1.
 */

public abstract class BaseFragment extends Fragment {
    View view=null;
//    //返回按钮
//    private ImageButton bt_BaseFragmentBack;
//    //时期显示
//    private ImageView iv_BaseFragmenWifi;

    protected abstract int getLoyoutId();
    protected abstract void onCusCreate(View view);

    private Handler timeHandler;
//    BatteryReceiver batteryReceiver;
//
//    ImageView iv_MainWifi;

    @Override
    public void onResume() {
        super.onResume();

//        String SSID = NetUtils.getCurWifi();
//        if (SSID.equals("0x") || SSID.equals("<unknown ssid>")) {
//            iv_MainWifi.setImageResource(R.drawable.icon_nowifi3);
//        } else {
//            iv_MainWifi.setImageResource(R.drawable.icon_wifi1);
//        }

    }

    /**
     *注销广播
     * */
    @Override
    public void onDestroyView() {
       // getActivity().unregisterReceiver(batteryReceiver);
        super.onDestroyView();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view =inflater.inflate(getLoyoutId(),container,false);
        setupView();
        onCusCreate(view);
        return view;
    }

    public void setupView()
    {
//
//        iv_MainWifi=(ImageView)view.findViewById(R.id.iv_BaseFragmenWifi) ;//??????
//        //获取当前时间
//       iv_BaseFragmenWifi=(ImageView)view.findViewById(R.id.iv_BaseFragmenWifi);//??????
//        bt_BaseFragmentBack=(ImageButton) view.findViewById(R.id.bt_BaseFragmentBack);
//        final TextView tv_power=(TextView)view.findViewById(R.id.tv_Fragmentpowerpercen);
//        final MyBatteryView iv_FragmenBattery=(MyBatteryView) view.findViewById(R.id.iv_FragmenBattery);
//        final ImageView iv_FragmentBatteryChangring=(ImageView)view.findViewById(R.id.iv_FragmentBatteryChangring);
//
//        //主线程的 handler 接收到 子线程的消息，然后修改TextView的显示
//        timeHandler = new Handler() {
//            @Override
//            public void handleMessage(Message msg) {
//                super.handleMessage(msg);
//                int what = msg.what;
//                switch (what) {
//                    case 10001:
//                        if (msg.arg1==0)
//                        {
//                            iv_FragmentBatteryChangring.setVisibility(View.GONE);
//                            //iv_FragmenBattery.rePaint(Color.WHITE,(int)(44*msg.arg2*0.01f));
//
//                            if (msg.arg2>20)
//                            {
//                                //根据电量绘画控件的宽度
//                                iv_FragmenBattery.rePaint(Color.WHITE, (int) (44 * msg.arg2 * 0.01f));
//                            }else {
//                                //根据电量绘画控件的宽度
//                                iv_FragmenBattery.rePaint(Color.RED, (int) (44 * msg.arg2 * 0.01f));
//                            }
//
//                        }
//                        else if(msg.arg1==1)
//                        {
//                            iv_FragmentBatteryChangring.setVisibility(View.VISIBLE);
//                            //根据电量绘画控件的宽度
//                           // iv_FragmenBattery.rePaint(Color.GREEN,(int)(44*msg.arg2*0.01f));
//
//                            if (msg.arg2>20)
//                            {
//                                //根据电量绘画控件的宽度
//                                iv_FragmenBattery.rePaint(Color.GREEN, (int) (44 * msg.arg2 * 0.01f));
//                            }else {
//                                //根据电量绘画控件的宽度
//                                iv_FragmenBattery.rePaint(Color.RED, (int) (44 * msg.arg2 * 0.01f));
//                            }
//
//                        }
//
//                        tv_power.setText(msg.arg2+"%");
//
//
//                        break;
//                }
//            }
//        };
//
//        IntentFilter intentFilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
//        batteryReceiver=new BatteryReceiver(timeHandler);
//        getActivity().registerReceiver(batteryReceiver, intentFilter);
    }

//    //设置标题
//    public ImageButton getBackBtn(){return bt_BaseFragmentBack;}
//
//    public void setWifiIcon(boolean isNetWork)
//    {
//        if (isNetWork) iv_BaseFragmenWifi.setImageResource(R.drawable.icon_wifi);
//        else iv_BaseFragmenWifi.setImageResource(R.drawable.icon_wifi_10);
//    }

}
