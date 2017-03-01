package com.goockr.nakedeyeguard.HealingProcessPage;

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
 * Created by JJT-ssd on 2017/2/28.
 */

public class HealingProcessFragment extends Fragment implements View.OnClickListener {
   View view=null;
    ImageButton bt_HPFBack;
    Button bt_HPFSure;
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view =inflater.inflate(R.layout.heading_process_fragment,container,false);
        setupUI();
        return view;
    }

    private void setupUI() {

        bt_HPFBack=(ImageButton)view.findViewById(R.id.bt_HPFBack);
        bt_HPFSure=(Button)view.findViewById(R.id.bt_HPFSure);
        bt_HPFBack.setOnClickListener(this);
        bt_HPFSure.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.bt_HPFBack:
                getActivity().finish();
                break;
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
