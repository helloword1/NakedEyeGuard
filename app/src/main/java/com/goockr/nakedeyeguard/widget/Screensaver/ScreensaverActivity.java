package com.goockr.nakedeyeguard.widget.Screensaver;

import android.os.Bundle;
import android.os.PowerManager;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.goockr.nakedeyeguard.R;

import static com.goockr.nakedeyeguard.Tools.App.preferences;

/**
* 屏保页面
* */

public class ScreensaverActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_screensaver);
        final CircleProgress cp_ScrenSaver=(CircleProgress)findViewById(R.id.cp_ScrenSaver);
        cp_ScrenSaver.startAnim();
        //开启屏幕唤醒，常亮
        acquire();
        //点亮 屏幕
        Settings.System.putInt(getContentResolver(), Settings.System.SCREEN_BRIGHTNESS,10);
        findViewById(R.id.view_ScreenSaver).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                Settings.System.putInt(getContentResolver(), Settings.System.SCREEN_BRIGHTNESS,preferences.getInt("Brightness",100));
                cp_ScrenSaver.stopAnim();
            }
        });
    }

    private  PowerManager.WakeLock wakeLock;
    /**开启 保持屏幕唤醒*/
    public  void acquire() {
        PowerManager pm = (PowerManager)getSystemService(POWER_SERVICE);
        wakeLock = pm.newWakeLock(
                PowerManager.ACQUIRE_CAUSES_WAKEUP |
                        PowerManager.SCREEN_DIM_WAKE_LOCK |
                        PowerManager.ON_AFTER_RELEASE, "SimpleTimer");

        wakeLock.acquire();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        release();
    }

    /**关闭 保持屏幕唤醒*/
    public  void release() {
        if (wakeLock != null) {
            wakeLock.release();
            wakeLock = null;
        }
    }
}
