package com.goockr.nakedeyeguard.HealingProcessPage;

import android.os.Handler;
import android.os.Message;
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

    TextView tv_BeforeRight;
    TextView tv_BeforeLeft;
    TextView tv_BeforeGlsRight;
    TextView tv_BeforeGlsLeft;
    TextView tv_NowNakedRight;
    TextView tv_NowNakedLeft;
    TextView tv_NowGlsRight;
    TextView tv_NowGlsLeft;
    @Override
    protected int getLoyoutId() {
        return R.layout.heading_process_fragment;
    }

    @Override
    protected void onCusCreate(View view) {
        setupUI(view);
    }


    private void setupUI(View view) {

        ((HealingProcessActivity)getActivity()).setHandler(mHandler);
        userModel =((HealingProcessActivity)getActivity()).userModel;

        tv_HPFUserName=(TextView)view.findViewById(R.id.tv_HPFUserName);
        iv_HPFUserIcon=(ImageView)view.findViewById(R.id.iv_HPFUserIcon);
        bt_HPFSure=(Button)view.findViewById(R.id.bt_HPFSure);

        tv_BeforeRight=(TextView)view.findViewById(R.id.tv_BeforeRight);
        tv_BeforeLeft=(TextView)view.findViewById(R.id.tv_BeforeLeft);
        tv_BeforeGlsRight=(TextView)view.findViewById(R.id.tv_BeforeGlsRight);
        tv_BeforeGlsLeft=(TextView)view.findViewById(R.id.tv_BeforeGlsLeft);
        tv_NowNakedRight=(TextView)view.findViewById(R.id.tv_NowNakedRight);
        tv_NowNakedLeft=(TextView)view.findViewById(R.id.tv_NowNakedLeft);
        tv_NowGlsRight=(TextView)view.findViewById(R.id.tv_NowGlsRight);
        tv_NowGlsLeft=(TextView)view.findViewById(R.id.tv_NowGlsLeft);
        setData();

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


    public Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            int what = msg.what;
            switch (what) {
                case 22222:
                    setData();
                    break;
            }
        }
    };

    private void setData()
    {
        tv_BeforeRight.setText(userModel.getBeforeRight());
        tv_BeforeLeft.setText(userModel.getBeforeLeft());
        tv_BeforeGlsRight.setText(userModel.getBeforeGlsRight());
        tv_BeforeGlsLeft.setText(userModel.getBeforeGlsLeft());
        tv_NowNakedRight.setText(userModel.getNowNakedRight());
        tv_NowNakedLeft.setText(userModel.getNowNakedLeft());
        tv_NowGlsRight.setText(userModel.getNowGlsRight());
        tv_NowGlsLeft.setText(userModel.getNowGlsLeft());
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
