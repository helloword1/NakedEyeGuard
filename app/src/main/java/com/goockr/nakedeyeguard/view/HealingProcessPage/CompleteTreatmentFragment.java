package com.goockr.nakedeyeguard.view.HealingProcessPage;

import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.goockr.nakedeyeguard.Base.BaseFragment;
import com.goockr.nakedeyeguard.data.Model.UserDetailBean;
import com.goockr.nakedeyeguard.R;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * Created by JJT-ssd on 2017/3/1.
 * 完成治疗
 */

public class CompleteTreatmentFragment extends BaseFragment implements View.OnClickListener {

    ImageButton ib_COMTFBack;
    UserDetailBean userInfo;
    ImageView iv_COMTFUserIcon;
    TextView tv_COMTFUserName;
    HealingProcessActivity activity;

    @Override
    protected int getLoyoutId() {
        return R.layout.complete_treatment_fragment;
    }

    @Override
    protected void onCusCreate(View view) {
        setupUI(view);
    }

    private void setupUI(View view) {
        userInfo = ((HealingProcessActivity) getActivity()).userInfo;
        tv_COMTFUserName = (TextView) view.findViewById(R.id.tv_COMTFUserName);
        iv_COMTFUserIcon = (ImageView) view.findViewById(R.id.iv_COMTFUserIcon);
        ib_COMTFBack = (ImageButton) view.findViewById(R.id.ib_COMTFBack);

        ib_COMTFBack.setOnClickListener(this);
        activity = (HealingProcessActivity) getActivity();
        activity.getBackBtn().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.finish();
            }
        });
        tv_COMTFUserName.setText(userInfo.getName());
        ImageLoader.getInstance().displayImage(userInfo.getHead_image(), iv_COMTFUserIcon);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ib_COMTFBack://返回
                activity.finish();
                break;
        }
    }
}
