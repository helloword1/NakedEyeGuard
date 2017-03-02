package com.goockr.nakedeyeguard.FirstUsePage;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.goockr.nakedeyeguard.Base.BaseActivity;
import com.goockr.nakedeyeguard.R;
import com.goockr.nakedeyeguard.SettingPage.WifiPage.WifiActivity;

public class FirstActivty extends BaseActivity {

    @Override
    protected int getLoyoutId() {return R.layout.activity_first_activty;}

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_activty);
        Button bt_FirstSet=(Button)findViewById(R.id.bt_FirstSet);
        bt_FirstSet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(FirstActivty.this, WifiActivity.class);
                startActivity(intent);
            }
        });
    }
}
