package com.goockr.nakedeyeguard.TipsPage;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.widget.FrameLayout;

import com.goockr.nakedeyeguard.Base.BaseActivity;
import com.goockr.nakedeyeguard.R;

public class TipsActivity extends BaseActivity {

    FrameLayout fl_TipsLayout;

    @Override
    protected int getLoyoutId() {return R.layout.activity_tips;}

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupUI();
       replaFragment(new TipsWirewayFragment());
    }


    private void setupUI()
    {

    }

    public void replaFragment(Fragment fragment)
    {
        FragmentTransaction transaction=getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fl_TipsLayout,fragment);
        transaction.commit();
    }

}
