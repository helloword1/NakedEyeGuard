package com.goockr.nakedeyeguard.TipsPage;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

import com.goockr.nakedeyeguard.R;

/**
 * Created by JJT-ssd on 2017/3/1.
 */

public class TipsWirewayFragment extends Fragment implements View.OnClickListener{
    View view=null;
    Button bt_TWFNext;
    ImageButton bt_TWFBack;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.tips_wireway_fragment,container,false);
        setupUI();
        return view;
    }

    private void setupUI()
    {
        bt_TWFNext=(Button)view.findViewById(R.id.bt_TWFNext);
        bt_TWFBack=(ImageButton) view.findViewById(R.id.bt_TWFBack);
        bt_TWFNext.setOnClickListener(this);
        bt_TWFBack.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.bt_TWFBack:
                getActivity().finish();
                break;
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
