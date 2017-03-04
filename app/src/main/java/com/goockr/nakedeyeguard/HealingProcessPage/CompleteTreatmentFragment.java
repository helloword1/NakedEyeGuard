package com.goockr.nakedeyeguard.HealingProcessPage;

import android.view.View;
import android.widget.ImageButton;

import com.goockr.nakedeyeguard.Base.BaseFragment;
import com.goockr.nakedeyeguard.R;

/**
 * Created by JJT-ssd on 2017/3/1.
 */

public class CompleteTreatmentFragment extends BaseFragment {

    ImageButton ib_COMTFBack;

    @Override
    protected int getLoyoutId() {return R.layout.complete_treatment_fragment;}

    @Override
    protected void onCusCreate(View view) {
        setupUI(view);
    }

    private void setupUI(View view) {

        getBackBtn().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });


        setWifiIcon(((HealingProcessActivity)getActivity()).getNetWorkState());
    }


}
