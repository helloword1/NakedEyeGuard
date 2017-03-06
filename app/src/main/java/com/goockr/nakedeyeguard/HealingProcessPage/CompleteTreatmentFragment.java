package com.goockr.nakedeyeguard.HealingProcessPage;

import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.goockr.nakedeyeguard.Base.BaseFragment;
import com.goockr.nakedeyeguard.Model.UserModel;
import com.goockr.nakedeyeguard.R;

import static com.goockr.nakedeyeguard.App.iconDrawable;

/**
 * Created by JJT-ssd on 2017/3/1.
 */

public class CompleteTreatmentFragment extends BaseFragment implements View.OnClickListener{

    ImageButton ib_COMTFBack;
    UserModel userModel;
    ImageView iv_COMTFUserIcon;
    TextView tv_COMTFUserName;
    @Override
    protected int getLoyoutId() {return R.layout.complete_treatment_fragment;}

    @Override
    protected void onCusCreate(View view) {
        setupUI(view);
    }

    private void setupUI(View view) {
        userModel =((HealingProcessActivity)getActivity()).userModel;
        tv_COMTFUserName=(TextView)view.findViewById(R.id.tv_COMTFUserName);
        iv_COMTFUserIcon=(ImageView)view.findViewById(R.id.iv_COMTFUserIcon);
        ib_COMTFBack=(ImageButton) view.findViewById(R.id.ib_COMTFBack);

        ib_COMTFBack.setOnClickListener(this);
        getBackBtn().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {getActivity().getSupportFragmentManager().popBackStack();}
        });
        setWifiIcon(((HealingProcessActivity)getActivity()).getNetWorkState());
        tv_COMTFUserName.setText(userModel.getUserName());
        iv_COMTFUserIcon.setImageDrawable(iconDrawable);
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
