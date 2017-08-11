package com.goockr.nakedeyeguard.view.FirstUsePage;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.Button;

import com.goockr.nakedeyeguard.Base.ActivityCollector;
import com.goockr.nakedeyeguard.Base.BaseFragment;
import com.goockr.nakedeyeguard.view.MainPage.MainActivity;
import com.goockr.nakedeyeguard.R;
import com.goockr.nakedeyeguard.view.SettingPage.WifiPage.WifiActivity;

/**
 * Created by JJT-ssd on 2017/3/2.
 * 使用条款说明
 */

public class ClauseFragment extends BaseFragment {

    Button bt_AgreeClause;
    @Override
    protected int getLoyoutId() {
        return R.layout.clause_fragment;
    }

    @Override
    protected void onCusCreate(View view) {
        setupUI(view);
    }

    private void setupUI(View view) {
        WifiActivity activity = (WifiActivity) getActivity();
        activity.isHideArrow(true);
        bt_AgreeClause=(Button)view.findViewById(R.id.bt_AgreeClause);
        bt_AgreeClause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                for (Activity activty:ActivityCollector.activities) {
                  if (activty.getClass().equals(FirstActivty.class))
                      activty.finish();
                }
                Intent intent=new Intent(getActivity(), MainActivity.class);
                startActivity(intent);
            }
        });
    }

}
