package com.goockr.nakedeyeguard.FirstUsePage;

import android.graphics.Color;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.goockr.nakedeyeguard.Base.BaseFragment;
import com.goockr.nakedeyeguard.R;
import com.kaopiz.kprogresshud.KProgressHUD;

/**
 * Created by JJT-ssd on 2017/3/2.
 */

public class BindingFragment extends BaseFragment {

    Button bt_CompleteBinding;
    @Override
    protected int getLoyoutId() {
        return R.layout.binding_fragment;
    }

    @Override
    protected void onCusCreate(View view) {
        setupUI(view);
    }
    private void setupUI(View view)
    {
        getBackBtn().setVisibility(View.GONE);
        setWifiIcon(true);
        bt_CompleteBinding=(Button)view.findViewById(R.id.bt_CompleteBinding);
        bt_CompleteBinding.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final boolean isBinding=true;
                TextView tv_Reset = new TextView(getActivity());

                tv_Reset.setTextColor(Color.WHITE);
                tv_Reset.setTextSize(18);
                if (isBinding)
                {
                    tv_Reset.setText("您已成功激活设备！");

                }else {
                    tv_Reset.setText("未能成功激活设备，请确认是否已在微信服务号成功注册！");
                }
                final KProgressHUD restHUD= KProgressHUD.create(getActivity())
                        .setCustomView(tv_Reset)
                        .show();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        restHUD.dismiss();
                        if (isBinding)
                        {
                            replaFragment(new ClauseFragment());
                        }
                    }
                }, 2000);
            }
        });
    }
    public void replaFragment(Fragment fragment)
    {

        FragmentTransaction transaction=getActivity().getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(
                R.anim.fragment_slide_right_in, R.anim.fragment_slide_left_out,
                R.anim.fragment_slide_left_in, R.anim.fragment_slide_right_out);
        transaction.replace(R.id.fl_WifiConnect,fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}
