package com.goockr.nakedeyeguard;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import com.goockr.nakedeyeguard.FirstUsePage.FirstActivty;
import com.goockr.nakedeyeguard.MainPage.MainActivity;

import static com.goockr.nakedeyeguard.App.preferences;

public class StartActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        final boolean isFirstUser = preferences.getBoolean("FirstUser",true);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (isFirstUser)//判断是否注册过
                {
                    Intent intentFirst=new Intent(StartActivity.this, FirstActivty.class);
                    startActivity(intentFirst);
                    finish();
                }
                else {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Intent intentMain=new Intent(StartActivity.this, MainActivity.class);
                            startActivity(intentMain);
                            finish();
                        }
                    },2000);

                }


            }
        },1500);
    }
}
