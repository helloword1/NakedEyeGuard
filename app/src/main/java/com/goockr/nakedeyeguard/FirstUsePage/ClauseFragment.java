package com.goockr.nakedeyeguard.FirstUsePage;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.Button;

import com.goockr.nakedeyeguard.Base.ActivityCollector;
import com.goockr.nakedeyeguard.Base.BaseFragment;
import com.goockr.nakedeyeguard.MainPage.MainActivity;
import com.goockr.nakedeyeguard.R;

import static com.goockr.nakedeyeguard.App.editor;

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
        getBackBtn().setVisibility(View.GONE);
        setWifiIcon(true);
        bt_AgreeClause=(Button)view.findViewById(R.id.bt_AgreeClause);
        bt_AgreeClause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor.putBoolean("FirstUser",false);
                editor.commit();
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
