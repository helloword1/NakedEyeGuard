package com.goockr.nakedeyeguard.view.TipsPage;

import android.view.View;
import android.widget.Button;

import com.goockr.nakedeyeguard.Base.BaseFragment;
import com.goockr.nakedeyeguard.view.HealingProcessPage.CourseOfTreatmentFragment;
import com.goockr.nakedeyeguard.R;

import static com.goockr.nakedeyeguard.Tools.Common.replaFragment;

/**
 * Created by JJT-ssd on 2017/3/1.
 */

public class TipsLastFragment extends BaseFragment implements View.OnClickListener{
    View view=null;
    Button bt_TLFNext;
    TipsActivity mActivity;

    @Override
    protected int getLoyoutId() {
        return R.layout.tips_last_fragment;
    }

    @Override
    protected void onCusCreate(View view) {
        setupUI(view);
    }

    private void setupUI(View view)
    {
        bt_TLFNext=(Button)view.findViewById(R.id.bt_TLFNext);
        bt_TLFNext.setOnClickListener(this);
        mActivity = (TipsActivity) getActivity();
        mActivity.getBackBtn().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mActivity.getSupportFragmentManager().popBackStack();
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {

            case R.id.bt_TLFNext:
                replaFragment(getActivity(),new CourseOfTreatmentFragment(),R.id.fl_HPFLayout,true);
                break;
        }
    }

}
