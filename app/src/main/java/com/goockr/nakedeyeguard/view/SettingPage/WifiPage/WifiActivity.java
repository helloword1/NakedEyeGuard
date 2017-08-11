package com.goockr.nakedeyeguard.view.SettingPage.WifiPage;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.RelativeLayout;

import com.goockr.nakedeyeguard.Base.BaseActivity;
import com.goockr.nakedeyeguard.R;

public class WifiActivity extends BaseActivity {

    RelativeLayout rl_WifiStateBar;
    @Override
    protected int getLoyoutId() {
        return R.layout.activity_wifi;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        replaFragment(new WifiConnectFragment());
        getBackBtn().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public void setupView() {
        super.setupView();
        rl_WifiStateBar=(RelativeLayout)findViewById(R.id.rl_WifiStateBar);
        rl_WifiStateBar.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    public void replaFragment(Fragment fragment)
    {
        FragmentTransaction transaction=getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.fl_WifiConnect,fragment);
        transaction.commit();
    }
    public void isHideTitle(boolean hide) {
        if(hide){
            getBackBtn().setVisibility(View.GONE);
        }else {
            getBackBtn().setVisibility(View.VISIBLE);
        }

    }

    public void isHideArrow(boolean hide) {
        if (hide) {
            rl_WifiStateBar.setVisibility(View.GONE);
        } else {
            rl_WifiStateBar.setVisibility(View.VISIBLE);
        }
    }
}
