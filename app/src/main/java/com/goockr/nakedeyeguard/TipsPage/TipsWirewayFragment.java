package com.goockr.nakedeyeguard.TipsPage;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.Button;

import com.goockr.nakedeyeguard.Base.BaseFragment;
import com.goockr.nakedeyeguard.HealingProcessPage.HealingProcessActivity;
import com.goockr.nakedeyeguard.R;

/**
 * Created by JJT-ssd on 2017/3/1.
 */

public class TipsWirewayFragment extends BaseFragment implements View.OnClickListener{
    View view=null;
    Button bt_TWFNext;


    @Override
    protected int getLoyoutId() {
        return R.layout.tips_wireway_fragment;
    }

    @Override
    protected void onCusCreate(View view) {
        setupUI(view);
    }

    private void setupUI(View view)
    {
        bt_TWFNext=(Button)view.findViewById(R.id.bt_TWFNext);
        bt_TWFNext.setOnClickListener(this);
        setWifiIcon(((TipsActivity)getActivity()).getNetWorkState());
        getBackBtn().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {

            case R.id.bt_TWFNext:
                replaFragment(new TipsElectrodeFragment());
                break;
        }
    }
    public void replaFragment(Fragment fragment)
    {

        FragmentTransaction transaction=getActivity().getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(
                R.anim.fragment_slide_right_in, R.anim.fragment_slide_left_out,
                R.anim.fragment_slide_left_in, R.anim.fragment_slide_right_out);
        transaction.replace(R.id.fl_TipsLayout,fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}
