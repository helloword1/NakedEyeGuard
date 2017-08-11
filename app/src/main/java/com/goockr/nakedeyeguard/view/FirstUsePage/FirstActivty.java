package com.goockr.nakedeyeguard.view.FirstUsePage;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.goockr.nakedeyeguard.Base.BaseActivity;
import com.goockr.nakedeyeguard.R;
import com.goockr.nakedeyeguard.view.SettingPage.WifiPage.WifiActivity;

/**
 * Created by JJT-ssd on 2017/3/2.
 * 第一次使用时启动
 */
public class FirstActivty extends BaseActivity {
    RelativeLayout rl_FirstStateBar;
    private static final String TAG = "FirstActivty";

    @Override
    protected int getLoyoutId() {
        return R.layout.activity_first_activty;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Button bt_FirstSet = (Button) findViewById(R.id.bt_FirstSet);
        rl_FirstStateBar = (RelativeLayout) findViewById(R.id.rl_FirstStateBar);
        bt_FirstSet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: bt_FirstSet1111");
                Intent intent = new Intent(FirstActivty.this, WifiActivity.class);
                startActivity(intent);
            }
        });


    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        super.onKeyUp(keyCode, event);
        return super.onKeyUp(keyCode, event);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        super.onKeyDown(keyCode, event);
        return super.onKeyDown(keyCode, event);
    }

    public void isHideTitle(boolean hide) {
        if (hide) {
            getBackBtn().setVisibility(View.GONE);
        } else {
            getBackBtn().setVisibility(View.VISIBLE);
        }

    }

    public void isHideArrow(boolean hide) {
        if (hide) {
            rl_FirstStateBar.setVisibility(View.GONE);
        } else {
            rl_FirstStateBar.setVisibility(View.VISIBLE);
        }

    }
}
