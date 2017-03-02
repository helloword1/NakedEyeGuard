package com.goockr.nakedeyeguard.SettingPage.WifiPage;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;

import com.goockr.nakedeyeguard.Base.BaseActivity;
import com.goockr.nakedeyeguard.Model.WifiModel;
import com.goockr.nakedeyeguard.R;

public class WifiActivity extends BaseActivity {

    WifiModel selectWifi=new WifiModel();

    @Override
    protected int getLoyoutId() {
        return R.layout.activity_wifi;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
