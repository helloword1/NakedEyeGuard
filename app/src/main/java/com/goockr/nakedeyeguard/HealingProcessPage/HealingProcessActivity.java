package com.goockr.nakedeyeguard.HealingProcessPage;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;

import com.goockr.nakedeyeguard.Base.BaseActivity;
import com.goockr.nakedeyeguard.R;

public class HealingProcessActivity extends BaseActivity {


    @Override
    protected int getLoyoutId() {return R.layout.activity_healing_process;}

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupUI();
        replaFragment(new HealingProcessFragment());
    }


    protected void setupUI() {

    }

    public void replaFragment(Fragment fragment)
    {
        FragmentTransaction transaction=getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fl_HPFLayout,fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

}
