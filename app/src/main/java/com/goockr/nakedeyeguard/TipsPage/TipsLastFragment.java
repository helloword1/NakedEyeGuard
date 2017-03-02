package com.goockr.nakedeyeguard.TipsPage;

import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.goockr.nakedeyeguard.Base.BaseFragment;
import com.goockr.nakedeyeguard.R;

/**
 * Created by JJT-ssd on 2017/3/1.
 */

public class TipsLastFragment extends BaseFragment implements View.OnClickListener{
    View view=null;
    Button bt_TLFNext;
    ImageButton ib_TLFBack;

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
