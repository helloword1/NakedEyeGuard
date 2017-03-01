package com.goockr.nakedeyeguard.TipsPage;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

import com.goockr.nakedeyeguard.R;

/**
 * Created by JJT-ssd on 2017/3/1.
 */

public class TipsLastFragment extends Fragment implements View.OnClickListener{
    View view=null;
    Button bt_TLFNext;
    ImageButton ib_TLFBack;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.tips_last_fragment,container,false);
        setupUI();
        return view;
    }

    private void setupUI()
    {
        bt_TLFNext=(Button)view.findViewById(R.id.bt_TLFNext);
        ib_TLFBack=(ImageButton) view.findViewById(R.id.ib_TLFBack);
        bt_TLFNext.setOnClickListener(this);
        ib_TLFBack.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.ib_TLFBack:
                getActivity().getSupportFragmentManager().popBackStack();
                break;
            case R.id.bt_TLFNext:
                getActivity().finish();
                break;
        }
    }

}
