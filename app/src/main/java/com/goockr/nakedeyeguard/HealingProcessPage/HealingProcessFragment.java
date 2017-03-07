package com.goockr.nakedeyeguard.HealingProcessPage;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.goockr.nakedeyeguard.Base.BaseFragment;
import com.goockr.nakedeyeguard.Model.UserModel;
import com.goockr.nakedeyeguard.R;

import static com.goockr.nakedeyeguard.App.iconDrawable;

/**
 * Created by JJT-ssd on 2017/2/28.
 */

public class HealingProcessFragment extends BaseFragment implements View.OnClickListener {
    View view=null;
    Button bt_HPFSure;
    UserModel userModel;
    ImageView iv_HPFUserIcon;
    TextView tv_HPFUserName;
    @Override
    protected int getLoyoutId() {
        return R.layout.heading_process_fragment;
    }

    @Override
    protected void onCusCreate(View view) {
        setupUI(view);
    }


    private void setupUI(View view) {
        userModel =((HealingProcessActivity)getActivity()).userModel;

        tv_HPFUserName=(TextView)view.findViewById(R.id.tv_HPFUserName);
        iv_HPFUserIcon=(ImageView)view.findViewById(R.id.iv_HPFUserIcon);
        bt_HPFSure=(Button)view.findViewById(R.id.bt_HPFSure);

        bt_HPFSure.setOnClickListener(this);
        tv_HPFUserName.setText(userModel.getUserName());
        setWifiIcon(((HealingProcessActivity)getActivity()).getNetWorkState());
        getBackBtn().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });
        iv_HPFUserIcon.setImageDrawable(iconDrawable);
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
