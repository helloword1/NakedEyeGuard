package com.goockr.nakedeyeguard.SettingPage.WifiPage;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.RelativeLayout;

import com.goockr.nakedeyeguard.Base.BaseActivity;
import com.goockr.nakedeyeguard.Model.WifiModel;
import com.goockr.nakedeyeguard.R;

public class WifiActivity extends BaseActivity {

    WifiModel selectWifi=new WifiModel();
    RelativeLayout rl_WifiStateBar;
    @Override
    protected int getLoyoutId() {
        return R.layout.activity_wifi;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        rl_WifiStateBar=(RelativeLayout)findViewById(R.id.rl_WifiStateBar);
        rl_WifiStateBar.setVisibility(View.GONE);
        replaFragment(new WifiConnectFragment());
    }

    public void replaFragment(Fragment fragment)
    {
        FragmentTransaction transaction=getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fl_WifiConnect,fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

}
