package com.goockr.nakedeyeguard;

import android.app.AlarmManager;
import android.app.Application;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;

import com.goockr.nakedeyeguard.Http.HttpHelper;
import com.goockr.nakedeyeguard.Tools.WifiHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by JJT-ssd on 2017/3/2.
 */

public class App extends Application {

    //获取数据初始设置
    public static SharedPreferences preferences;
    public static SharedPreferences.Editor  editor;
    //时区
    public static AlarmManager alarmManager;
    public static Map timeZoneMap=new HashMap();
    public static ArrayList<String> restTimeList = new ArrayList<>();

    public static boolean isStartScreen=true;
    //
    public static boolean networkState=false;
    public static WifiHelper wifiHelper;

    public static Drawable iconDrawable=null;
    @Override
    public void onCreate() {
        super.onCreate();
        //获取数据初始设置
        initValue();//
//        String apkRoot="chmod 777 "+getPackageCodePath();
//        SystemManager.RootCommand(apkRoot);

    }

    private void initValue()
    {
        preferences = getSharedPreferences("preferences",MODE_PRIVATE);
        editor =preferences.edit();
        alarmManager= (AlarmManager)getSystemService(ALARM_SERVICE);
        wifiHelper=new WifiHelper(getApplicationContext());
        HttpHelper.httpInits();
//        Intent intentService =new Intent(this, ScreensaverReceiver.class);
//        startService(intentService);

        //亮度设置
        //int brightness =preferences.getInt("Brightness",100);
       // Settings.System.putInt(getContentResolver(), Settings.System.SCREEN_BRIGHTNESS,brightness);


        android.provider.Settings.System.putString(getContentResolver(),android.provider.Settings.System.TIME_12_24, "24");
        //设置默认时区 中国标准时间 (北京)
        final String timeZoneStr = preferences.getString("TimeZone","北京");
        new Thread(new Runnable() {
            @Override
            public void run() {
                timeZoneMap.put(getString(R.string.北京), "北京");
                timeZoneMap.put(getString(R.string.东京), "东京");
                timeZoneMap.put(getString(R.string.东加塔布), "东加塔布");
                timeZoneMap.put(getString(R.string.中途岛), "中途岛");
                timeZoneMap.put(getString(R.string.丹佛), "丹佛");

                for(Map.Entry entry:(Set<Map.Entry>)timeZoneMap.entrySet()){
                    if(timeZoneStr.equals(entry.getValue()))
                    {
                        alarmManager.setTimeZone((String)entry.getKey());
                        editor.putString("TimeZone",timeZoneStr);

                    }
                }
                restTimeList.add("10秒");
                restTimeList.add("30秒");
                restTimeList.add("1分钟");
                restTimeList.add("3分钟");
                restTimeList.add("5分钟");
                restTimeList.add("10分钟");
                restTimeList.add("30分钟");
                restTimeList.add("1小时");
                restTimeList.add("从不");
                editor.putString("RestTime","30秒");
                editor.commit();

            }
        }).start();

    }



}
