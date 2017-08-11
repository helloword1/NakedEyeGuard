package com.goockr.nakedeyeguard;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.goockr.nakedeyeguard.view.FirstUsePage.FirstActivty;
import com.goockr.nakedeyeguard.view.MainPage.MainActivity;

import static com.goockr.nakedeyeguard.Tools.App.preferences;

/**
 * Created by JJT-ssd on 2017/3/2.
 * 启动页
 */
public class StartActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        final boolean isFirstUser = preferences.getBoolean("FirstUser",true);
        if (isFirstUser)//判断是否注册过
        {

            Intent intentFirst=new Intent(StartActivity.this, FirstActivty.class);
            startActivity(intentFirst);
            finish();
        }
        else {

            Intent intentMain=new Intent(StartActivity.this, MainActivity.class);
            startActivity(intentMain);
            finish();

        }

    }



}
