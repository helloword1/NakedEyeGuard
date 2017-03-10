package com.goockr.nakedeyeguard.TipsPage;

import android.view.View;
import android.widget.Button;

import com.goockr.nakedeyeguard.Base.BaseFragment;
import com.goockr.nakedeyeguard.HealingProcessPage.HealingProcessActivity;
import com.goockr.nakedeyeguard.R;

import static com.goockr.nakedeyeguard.Tools.Common.replaFragment;

/**
 * Created by JJT-ssd on 2017/3/1.
 */

public class TipsElectrodeFragment extends BaseFragment implements View.OnClickListener{
    View view=null;
    Button bt_TEFNext;


    @Override
    protected int getLoyoutId() {
        return R.layout.tips_electrode_fragment;
    }

    @Override
    protected void onCusCreate(View view) {
        setupUI(view);
    }

    private void setupUI(View view)
    {
        bt_TEFNext=(Button)view.findViewById(R.id.bt_TEFNext);
        bt_TEFNext.setOnClickListener(this);
        setWifiIcon(((HealingProcessActivity)getActivity()).getNetWorkState());
        getBackBtn().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.bt_TEFNext:
                replaFragment(getActivity(),new TipsLastFragment(),R.id.fl_HPFLayout,true);
                break;
        }
    }

}
