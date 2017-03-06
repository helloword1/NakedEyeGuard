package com.goockr.nakedeyeguard.HealingProcessPage;

import android.content.Intent;
import android.os.CountDownTimer;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.goockr.nakedeyeguard.Base.BaseFragment;
import com.goockr.nakedeyeguard.R;
import com.goockr.nakedeyeguard.TipsPage.TipsActivity;
import com.mikhaellopez.circularprogressbar.CircularProgressBar;

import static com.goockr.nakedeyeguard.Tools.DateExtension.dateLong2String;

/**
 * Created by JJT-ssd on 2017/2/28.
 */

public class CourseOfTreatmentFragment extends BaseFragment implements View.OnClickListener,CompoundButton.OnCheckedChangeListener {

    ImageButton bt_CTFBack;
    ImageButton ib_CTFReduce;
    ImageButton ib_CTFAdd;
    ImageButton ib_CTFTip;
    CheckBox cb_CTFStart;
    ProgressBar pb_CTFStrength;
    CircularProgressBar cpb_CTFProgress;
    TextView tv_CTFStrength;
    TextView tv_CTFTimer;

    @Override
    protected int getLoyoutId() {return R.layout.course_treatment_fragment;}

    @Override
    protected void onCusCreate(View view) {
        setupUI(view);
    }


    private void setupUI(View view) {
        tv_CTFStrength=(TextView)view.findViewById(R.id.tv_CTFStrength);
        pb_CTFStrength=(ProgressBar)view.findViewById(R.id.pb_CTFStrength);
        cpb_CTFProgress=(CircularProgressBar)view.findViewById(R.id.cpb_CTFProgress);
        tv_CTFTimer=(TextView)view.findViewById(R.id.tv_CTFTimer);
        bt_CTFBack=(ImageButton)view.findViewById(R.id.bt_CTFBack);
        ib_CTFReduce=(ImageButton)view.findViewById(R.id.ib_CTFReduce);
        ib_CTFAdd=(ImageButton)view.findViewById(R.id.ib_CTFAdd);
        ib_CTFTip=(ImageButton)view.findViewById(R.id.ib_CTFTip);
        cb_CTFStart=(CheckBox)view.findViewById(R.id.cb_CTFStart);

        bt_CTFBack.setOnClickListener(this);
        ib_CTFTip.setOnClickListener(this);
        ib_CTFReduce.setOnClickListener(this);
        ib_CTFAdd.setOnClickListener(this);
        cb_CTFStart.setOnCheckedChangeListener(this);
        progressBarState(pb_CTFStrength.getProgress());
        setWifiIcon(((HealingProcessActivity)getActivity()).getNetWorkState());
        getBackBtn().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.bt_CTFBack:
                getActivity().finish();
                break;
            case R.id.ib_CTFReduce:
                int proValueReduce= pb_CTFStrength.getProgress();
                if (20<=proValueReduce)
                {
                    pb_CTFStrength.setProgress(proValueReduce-20);
                    tv_CTFStrength.setText("强度:"+String.valueOf(proValueReduce-20)+"%");
                }
                progressBarState(pb_CTFStrength.getProgress());
                break;
            case R.id.ib_CTFAdd:
                int proValueAdd= pb_CTFStrength.getProgress();
                if (proValueAdd<100)
                {
                    pb_CTFStrength.setProgress(proValueAdd+20);
                    tv_CTFStrength.setText("强度:"+String.valueOf(proValueAdd+20)+"%");
                }
                progressBarState(pb_CTFStrength.getProgress());
                break;
            case R.id.ib_CTFTip:
                Intent intent=new Intent(getActivity(), TipsActivity.class);
                startActivity(intent);
                break;

        }
    }

    private void progressBarState(int proValue)
    {
        if (0<proValue)
            ib_CTFReduce.setBackgroundResource(R.drawable.ctf_btn_reduce);
        else  ib_CTFReduce.setBackgroundResource(R.drawable.btn_reduce_disable);
        if (proValue<100)
            ib_CTFAdd.setBackgroundResource(R.drawable.ctf_btn_add);
        else ib_CTFAdd.setBackgroundResource(R.drawable.btn_add_disable);
    }

    CountDownTimer countDownTimer = null;
    boolean isPause=false;
    long allTime=5000;
    long startTime=5000;
    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {


        switch (buttonView.getId())
        {
            case R.id.cb_CTFStart:
                if (isChecked)
                {
                    if (!isPause) firstStart();
                  //  else secondStart();

                }else
                {
                    if (countDownTimer!=null) {
                        countDownTimer.cancel();
                        isPause=true;
                    }
                    cb_CTFStart.setBackgroundResource(R.drawable.ctf_btn_start);

                }
                break;
        }
    }

    private void firstStart()
    {
        cb_CTFStart.setBackgroundResource(R.drawable.btn_pause);
        tv_CTFTimer.setText(dateLong2String(allTime));
        final long timeCal =isPause?startTime:allTime;
        final int[] i = {1};
        countDownTimer= new CountDownTimer(timeCal+50,1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                if (i[0] ==1)
                {
                    i[0] =0;
                    return;
                }
                startTime=millisUntilFinished;
                Log.e("millisUntilFinished=",String.valueOf(millisUntilFinished));
                float finishScond = (timeCal-millisUntilFinished-50);
                float pro =(finishScond/(timeCal-50))*100;
                cpb_CTFProgress.setProgress(pro);
                tv_CTFTimer.setText(dateLong2String(millisUntilFinished));
            }
            @Override
            public void onFinish() {
                isPause=false;
                tv_CTFTimer.setText("00:00");
                cpb_CTFProgress.setProgress(100);
                cb_CTFStart.setBackgroundResource(R.drawable.ctf_btn_start);
                cb_CTFStart.setChecked(false);
                replaFragment(new CompleteTreatmentFragment());
            }
        };
        countDownTimer.start();
    }


    private void secondStart()
    {
        cb_CTFStart.setBackgroundResource(R.drawable.btn_pause);
        tv_CTFTimer.setText(dateLong2String(startTime));
        final int[] i = {1};
        countDownTimer= new CountDownTimer(startTime+50,1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                if (i[0] ==1)
                {
                    i[0] =0;
                    return;
                }
                Log.e("onTick=",String.valueOf(millisUntilFinished));
                float finishScond = (startTime-millisUntilFinished-50);
                float pro =((finishScond+(allTime-startTime))/(allTime-50))*100;
                cpb_CTFProgress.setProgress(pro);
                tv_CTFTimer.setText(dateLong2String(millisUntilFinished));
            }
            @Override
            public void onFinish() {
                isPause=false;
                tv_CTFTimer.setText("00:00");
                cpb_CTFProgress.setProgress(100);
                cb_CTFStart.setBackgroundResource(R.drawable.ctf_btn_start);
                cb_CTFStart.setChecked(false);
                replaFragment(new CompleteTreatmentFragment());
            }
        };
        countDownTimer.start();
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
