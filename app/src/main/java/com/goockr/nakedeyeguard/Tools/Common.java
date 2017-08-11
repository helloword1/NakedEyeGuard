package com.goockr.nakedeyeguard.Tools;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;

import com.goockr.nakedeyeguard.R;
import com.goockr.nakedeyeguard.widget.Screensaver.ScreensaverActivity;
import com.kaopiz.kprogresshud.KProgressHUD;

import java.util.Calendar;

import static com.goockr.nakedeyeguard.Tools.App.alarmManager;

/**
 * Created by JJT-ssd on 2017/3/2.
 */
public class Common {

    public  static String defaultLight="138";

    public  static String musicName="abc.mp3";

    public static  String musicPath= Environment.getExternalStorageDirectory()+"/"+musicName;

    public static  String StartStateKey="StartStateKey";

    //0xFE+0x05+0x01+0x00+0x06+0xFF
    public static byte[] Heart= {(byte) 0xFE,(byte) 0x05, (byte) 0x01, (byte) 0x00, (byte) 0x06, (byte) 0xFF};

    public static void scheduleDismiss(final KProgressHUD hud) {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                if (hud != null) hud.dismiss();//改

            }
        }, 2000);
    }

    /*
    * name:通知名字，作为通知id使用
    * content：通知内容
    * time：倒时时（秒）
    * */
    public static void addLocalNotication(Context content, long time) {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(System.currentTimeMillis()+time*1000);
        Intent intent = new Intent(content, ScreensaverActivity.class);
        PendingIntent pi = PendingIntent.getBroadcast(content, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(),cal.getTimeInMillis()+time*1000, pi);
    }


    /**
     * 切换Fragment
     * @param activity
     * @param fragment
     * @param Rid
     * @param isAdd
     */
    public static void replaFragment(FragmentActivity activity, Fragment fragment, int Rid, boolean isAdd)
    {

        FragmentTransaction transaction=activity.getSupportFragmentManager().beginTransaction();

        if (isAdd)
        {
            transaction.setCustomAnimations(
                    R.anim.fragment_slide_right_in, R.anim.fragment_slide_left_out,
                    R.anim.fragment_slide_left_in, R.anim.fragment_slide_right_out);
            transaction.addToBackStack(null);
        }
        //transaction.replace(Rid,fragment);
        transaction.add(Rid,fragment);



        transaction.commit();

    }

    public static void replaFragment(FragmentActivity activity, Fragment newfragment, int Rid, boolean isAdd,Fragment currentFragment)
    {

        FragmentTransaction transaction=activity.getSupportFragmentManager().beginTransaction();

        if (isAdd)
        {
            transaction.setCustomAnimations(
                    R.anim.fragment_slide_right_in, R.anim.fragment_slide_left_out,
                    R.anim.fragment_slide_left_in, R.anim.fragment_slide_right_out);
            transaction.addToBackStack(null);
        }
        //transaction.replace(Rid,fragment);
        transaction.add(Rid,newfragment);

        transaction.hide(currentFragment);


        transaction.commit();

    }


    /*****保存用户数据的key******/

      public  static String ScreenLightKey="ScreenLightKey";
}
