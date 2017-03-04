package com.goockr.nakedeyeguard.Screensaver;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.goockr.nakedeyeguard.R;

public class ScreensaverActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_screensaver);
        CircleProgress cp_ScrenSaver=(CircleProgress)findViewById(R.id.cp_ScrenSaver);
        cp_ScrenSaver.startAnim();
    }
}
