package com.goockr.nakedeyeguard.SettingPage;

import android.os.Bundle;
import android.view.View;

import com.goockr.nakedeyeguard.Base.BaseActivity;
import com.goockr.nakedeyeguard.R;

public class AboutActivity extends BaseActivity {

    @Override
    protected int getLoyoutId() {return R.layout.activity_about;}

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getBackBtn().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(R.anim.fragment_slide_left_in, R.anim.fragment_slide_right_out);
            }
        });
    }

}
