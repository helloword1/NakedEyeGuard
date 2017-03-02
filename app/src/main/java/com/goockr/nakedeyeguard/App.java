package com.goockr.nakedeyeguard;

import android.app.Application;
import android.content.SharedPreferences;

/**
 * Created by JJT-ssd on 2017/3/2.
 */

public class App extends Application {

    //获取数据初始设置
    public static SharedPreferences preferences;
    public static SharedPreferences.Editor  editor;
    @Override
    public void onCreate() {
        super.onCreate();
        //获取数据初始设置
        preferences = getSharedPreferences("preferences",MODE_PRIVATE);
        editor =preferences.edit();

    }

//    private void initValue()
//    {
//        boolean isFirstUser = preferences.getBoolean("FirstUser",false);
//        if (!isFirstUser)
//        editor.putBoolean("FirstUser",true);
//        editor.commit();
//    }
}
