package com.goockr.nakedeyeguard.view.TipsPage;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.widget.ImageButton;

import com.goockr.nakedeyeguard.Base.BaseActivity;
import com.goockr.nakedeyeguard.R;

/**
 * Created by JJT-ssd on 2017/3/2.
 * 提示页
 */
public class TipsActivity extends BaseActivity {

    @Override
    protected int getLoyoutId() {return R.layout.activity_tips;}

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       replaFragment(new TipsWirewayFragment());
    }

    @Override
    public ImageButton getBackBtn() {
        return super.getBackBtn();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    public void replaFragment(Fragment fragment)
    {
        FragmentTransaction transaction=getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fl_TipsLayout,fragment);
        transaction.commit();
    }

}
