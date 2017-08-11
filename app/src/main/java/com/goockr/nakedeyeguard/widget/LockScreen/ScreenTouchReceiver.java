package com.goockr.nakedeyeguard.widget.LockScreen;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.provider.Settings;
import android.util.Log;

import com.goockr.nakedeyeguard.Tools.ACache;
import com.goockr.nakedeyeguard.Tools.App;
import com.goockr.nakedeyeguard.Tools.Common;

/**
 * Created by LZJ on 2017/5/31.
 */

public class ScreenTouchReceiver extends BroadcastReceiver {
    public static final String TOUCHACTION = "com.goockr.nakeeyeguard.screentouch";
    public static final String TOUCHACTIONCAN = "com.goockr.nakeeyeguard.screentouchcancle";
    public static final String SCREENON = "com.goockr.nakeeyeguard.screenon";
    public static final String SCREENBRIGHTNESS = "screenbrightness";
    public static final String SCREENDARKTIME = "screendarktime";
    public static  int DARKMIN = 20;
    private static int curMin = DARKMIN;
    private static boolean isRunning = false;
    private static boolean isCancle = false;
    private Thread timeThread = null;

    private Runnable timeRunnable = new Runnable() {
        @Override
        public void run() {
            while (isRunning){
                curMin--;
                if (curMin==DARKMIN/2){
                    if (!isCancle){
                       // Settings.System.putInt(App.getContext().getContentResolver(), Settings.System.SCREEN_BRIGHTNESS,0);
                    }
                }
                if (curMin==0){
                    String curbrghtness = ACache.get(App.getContext()).getAsString(SCREENBRIGHTNESS);

                    if (curbrghtness==null||curbrghtness.equals("")){
                        curbrghtness = Common.defaultLight;
                        ACache.get(App.getContext()).put(SCREENBRIGHTNESS,Common.defaultLight);
                    }
                    isRunning = false;
                    timeThread = null;
                    isCancle = false;
                    curMin = DARKMIN;
                    Settings.System.putInt(App.getContext().getContentResolver(), Settings.System.SCREEN_BRIGHTNESS, Integer.valueOf(curbrghtness));
                    App.getContext().startActivity(new Intent(App.getContext(),LockScreen.Controller.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                }
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    };

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(TOUCHACTION)){
            if (isRunning==false){
                isRunning = true;
                isCancle = false;
                curMin = DARKMIN;
                timeThread = new Thread(timeRunnable);
                timeThread.start();
            }else {
                curMin = DARKMIN;

                String curbrightness = ACache.get(App.getContext()).getAsString(SCREENBRIGHTNESS);
                if(curbrightness!=null){
                    if(Integer.valueOf(curbrightness)>255||Integer.valueOf(curbrightness)<0){
                        curbrightness = "40";
                    }
                }else {
                    curbrightness = "40";
                }

                //Settings.System.putInt(App.getContext().getContentResolver(), Settings.System.SCREEN_BRIGHTNESS, Integer.valueOf(curbrightness));
            }
        }
        if (intent.getAction().equals(TOUCHACTIONCAN)){
            curMin = DARKMIN;
            isCancle = true;
            isRunning = false;
            String curbrightness = ACache.get(App.getContext()).getAsString(SCREENBRIGHTNESS);
            if(curbrightness!=null){
                if(Integer.valueOf(curbrightness)>255||Integer.valueOf(curbrightness)<0){
                    curbrightness = Common.defaultLight;
                }
            }else {
                curbrightness = Common.defaultLight;
            }
            Settings.System.putInt(App.getContext().getContentResolver(), Settings.System.SCREEN_BRIGHTNESS, Integer.valueOf(curbrightness));
        }

        if (intent.getAction().equals(Intent.ACTION_SCREEN_ON)||intent.getAction().equals(SCREENON)){
            Log.i("setting","on");
            String curbrightness = ACache.get(App.getContext()).getAsString(SCREENBRIGHTNESS);
            if(curbrightness!=null){
                if(Integer.valueOf(curbrightness)>255||Integer.valueOf(curbrightness)<0){
                    curbrightness = Common.defaultLight;
                }
            }else {
                curbrightness = Common.defaultLight;
            }
            Settings.System.putInt(App.getContext().getContentResolver(), Settings.System.SCREEN_BRIGHTNESS, Integer.valueOf(curbrightness));
        }
    }
}
