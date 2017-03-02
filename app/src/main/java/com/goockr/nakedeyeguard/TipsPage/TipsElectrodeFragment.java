package com.goockr.nakedeyeguard.TipsPage;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.goockr.nakedeyeguard.Base.BaseFragment;
import com.goockr.nakedeyeguard.R;

/**
 * Created by JJT-ssd on 2017/3/1.
 */

public class TipsElectrodeFragment extends BaseFragment implements View.OnClickListener{
    View view=null;
    Button bt_TEFNext;
    ImageButton ib_TEFBack;

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
        ib_TEFBack=(ImageButton)view.findViewById(R.id.ib_TEFBack);
        bt_TEFNext=(Button)view.findViewById(R.id.bt_TEFNext);
        ib_TEFBack.setOnClickListener(this);
        bt_TEFNext.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.ib_TEFBack:
                getActivity().getSupportFragmentManager().popBackStack();
                break;
            case R.id.bt_TEFNext:
                replaFragment(new TipsLastFragment());
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
