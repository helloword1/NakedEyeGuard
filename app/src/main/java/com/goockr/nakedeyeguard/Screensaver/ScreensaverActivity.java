package com.goockr.nakedeyeguard.Screensaver;

import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.goockr.nakedeyeguard.R;

import static com.goockr.nakedeyeguard.App.preferences;

public class ScreensaverActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_screensaver);
        final CircleProgress cp_ScrenSaver=(CircleProgress)findViewById(R.id.cp_ScrenSaver);
        cp_ScrenSaver.startAnim();
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

}
