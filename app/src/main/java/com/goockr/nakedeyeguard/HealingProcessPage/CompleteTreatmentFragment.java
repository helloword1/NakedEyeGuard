package com.goockr.nakedeyeguard.HealingProcessPage;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.goockr.nakedeyeguard.R;

/**
 * Created by JJT-ssd on 2017/3/1.
 */

public class CompleteTreatmentFragment extends Fragment implements View.OnClickListener{
    View view=null;
    ImageButton ib_COMTFBack;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view =inflater.inflate(R.layout.complete_treatment_fragment,container,false);
        setupUI();
        return view;
    }

    private void setupUI() {

        ib_COMTFBack=(ImageButton)view.findViewById(R.id.ib_COMTFBack);
        ib_COMTFBack.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.ib_COMTFBack:
                getActivity().finish();
                break;
        }
    }
}
