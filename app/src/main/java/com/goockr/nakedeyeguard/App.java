package com.goockr.nakedeyeguard;

import android.app.AlarmManager;
import android.app.Application;
import android.content.SharedPreferences;

import java.util.HashMap;
import java.util.Map;

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
    @Override
    public void onCreate() {
        super.onCreate();
        //获取数据初始设置
        preferences = getSharedPreferences("preferences",MODE_PRIVATE);
        editor =preferences.edit();
        alarmManager= (AlarmManager)getSystemService(ALARM_SERVICE);
        initValue();//
    }

    private void initValue()
    {
        //设置默认时区 中国标准时间 (北京)
        final String timeZoneStr = preferences.getString("TimeZone",getString(R.string.北京));
        editor.putString("TimeZone",timeZoneStr);
        editor.commit();
        alarmManager.setTimeZone(timeZoneStr);
        new Thread(new Runnable() {
            @Override
            public void run() {
                timeZoneMap.put(getString(R.string.北京), "北京");
                timeZoneMap.put(getString(R.string.东京), "东京");
                timeZoneMap.put(getString(R.string.东加塔布), "东加塔布");
                timeZoneMap.put(getString(R.string.中途岛), "中途岛");
                timeZoneMap.put(getString(R.string.丹佛), "丹佛");
            }
        }).start();

    }



}
