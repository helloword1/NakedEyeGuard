package com.goockr.nakedeyeguard.HealingProcessPage;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.Button;

import com.goockr.nakedeyeguard.Base.BaseFragment;
import com.goockr.nakedeyeguard.R;

/**
 * Created by JJT-ssd on 2017/2/28.
 */

public class HealingProcessFragment extends BaseFragment implements View.OnClickListener {
   View view=null;
    Button bt_HPFSure;

    @Override
    protected int getLoyoutId() {
        return R.layout.heading_process_fragment;
    }

    @Override
    protected void onCusCreate(View view) {
        setupUI(view);
    }


    private void setupUI(View view) {


        bt_HPFSure=(Button)view.findViewById(R.id.bt_HPFSure);

        bt_HPFSure.setOnClickListener(this);
        setWifiIcon(((HealingProcessActivity)getActivity()).getNetWorkState());
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
            case  R.id.bt_HPFSure:
                replaFragment(new CourseOfTreatmentFragment());
                break;
        }
    }


    public void replaFragment(Fragment fragment)
    {

        FragmentTransaction transaction=getActivity().getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(
            R.anim.fragment_slide_right_in, R.anim.fragment_slide_left_out,
            R.anim.fragment_slide_left_in, R.anim.fragment_slide_right_out);
        transaction.replace(R.id.fl_HPFLayout,fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}
