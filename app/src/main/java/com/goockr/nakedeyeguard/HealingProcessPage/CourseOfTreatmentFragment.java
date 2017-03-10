package com.goockr.nakedeyeguard.HealingProcessPage;

import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.goockr.nakedeyeguard.Base.BaseFragment;
import com.goockr.nakedeyeguard.Http.HttpHelper;
import com.goockr.nakedeyeguard.Model.TreatmentModel;
import com.goockr.nakedeyeguard.Model.UserModel;
import com.goockr.nakedeyeguard.R;
import com.goockr.nakedeyeguard.TipsPage.TipsActivity;
import com.goockr.nakedeyeguard.Tools.Common;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.mikhaellopez.circularprogressbar.CircularProgressBar;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;

import static com.goockr.nakedeyeguard.Tools.Common.replaFragment;
import static com.goockr.nakedeyeguard.Tools.DateExtension.dateLong2String;

/**
 * Created by JJT-ssd on 2017/2/28.
 */

public class CourseOfTreatmentFragment extends BaseFragment implements View.OnClickListener,CompoundButton.OnCheckedChangeListener,Runnable {

    ImageButton bt_CTFBack;
    ImageButton ib_CTFReduce;
    ImageButton ib_CTFAdd;
    ImageButton ib_CTFTip;
    CheckBox cb_CTFStart;
    ProgressBar pb_CTFStrength;
    CircularProgressBar cpb_CTFProgress;
    TextView tv_CTFStrength;
    TextView tv_CTFTimer;
    UserModel userModel;
    TreatmentModel treatmentModel;

    //倒计时
    private Thread myCountDownThread;
    private Handler timeHandler;
    private boolean mflag;

    private int mCount = 10;
    private float allTime = 10.0f;
    boolean isTreatmenting=false;//判断是否治疗中

    @Override
    protected int getLoyoutId() {
        return R.layout.course_treatment_fragment;
    }

    @Override
    protected void onCusCreate(View view) {
        setupUI(view);
        initTherad();
    }


    private void setupUI(View view) {
        tv_CTFStrength = (TextView) view.findViewById(R.id.tv_CTFStrength);
        pb_CTFStrength = (ProgressBar) view.findViewById(R.id.pb_CTFStrength);
        cpb_CTFProgress = (CircularProgressBar) view.findViewById(R.id.cpb_CTFProgress);
        tv_CTFTimer = (TextView) view.findViewById(R.id.tv_CTFTimer);
        bt_CTFBack = (ImageButton) view.findViewById(R.id.bt_CTFBack);
        ib_CTFReduce = (ImageButton) view.findViewById(R.id.ib_CTFReduce);
        ib_CTFAdd = (ImageButton) view.findViewById(R.id.ib_CTFAdd);
        ib_CTFTip = (ImageButton) view.findViewById(R.id.ib_CTFTip);
        cb_CTFStart = (CheckBox) view.findViewById(R.id.cb_CTFStart);

        bt_CTFBack.setOnClickListener(this);
        ib_CTFTip.setOnClickListener(this);
        ib_CTFReduce.setOnClickListener(this);
        ib_CTFAdd.setOnClickListener(this);
        cb_CTFStart.setOnCheckedChangeListener(this);
        progressBarState(pb_CTFStrength.getProgress());
        setWifiIcon(((HealingProcessActivity) getActivity()).getNetWorkState());
        getBackBtn().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isTreatmenting)isTreatment();
                else  getActivity().getSupportFragmentManager().popBackStack();
            }
        });

        userModel = ((HealingProcessActivity)getActivity()).userModel;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_CTFBack:
                if (isTreatmenting)isTreatment();
                else getActivity().finish();
                break;
            case R.id.ib_CTFReduce:
                int proValueReduce = pb_CTFStrength.getProgress();
                if (20 <= proValueReduce) {
                    pb_CTFStrength.setProgress(proValueReduce - 20);
                    tv_CTFStrength.setText("强度:" + String.valueOf(proValueReduce - 20) + "%");
                }
                progressBarState(pb_CTFStrength.getProgress());
                break;
            case R.id.ib_CTFAdd:
                int proValueAdd = pb_CTFStrength.getProgress();
                if (proValueAdd < 100) {
                    pb_CTFStrength.setProgress(proValueAdd + 20);
                    tv_CTFStrength.setText("强度:" + String.valueOf(proValueAdd + 20) + "%");
                }
                progressBarState(pb_CTFStrength.getProgress());
                break;
            case R.id.ib_CTFTip:
                if (isTreatmenting)isTreatment();
                else {
                    Intent intent = new Intent(getActivity(), TipsActivity.class);
                    startActivity(intent);
                }
                break;
        }
    }

    private void progressBarState(int proValue) {
        if (0 < proValue)
            ib_CTFReduce.setBackgroundResource(R.drawable.ctf_btn_reduce);
        else ib_CTFReduce.setBackgroundResource(R.drawable.btn_reduce_disable);
        if (proValue < 100)
            ib_CTFAdd.setBackgroundResource(R.drawable.ctf_btn_add);
        else ib_CTFAdd.setBackgroundResource(R.drawable.btn_add_disable);
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

        switch (buttonView.getId()) {
            case R.id.cb_CTFStart:
                if (isChecked) cb_CTFStart.setBackgroundResource(R.drawable.btn_pause);
                else cb_CTFStart.setBackgroundResource(R.drawable.ctf_btn_start);
                if (!myCountDownThread.isAlive())
                {
                    //开始计时器或者是重启计时器，设置标记为true
                    mflag = true;
                    //判断是否是第一次启动，如果是不是第一次启动，那么状态就是Thread.State.TERMINATED
                    //不是的话，就需要重新的初始化，因为之前的已经结束了。
                    //并且要判断这个mCount 是否为-1，如果是的话，说名上一次的计时已经完成了，那么要重新设置。
                    if (myCountDownThread.getState() == Thread.State.TERMINATED)
                    {
                        myCountDownThread = new Thread(this);
                        if (mCount == -1) mCount = 10;
                        {
                            //未完成上次治疗，从上次暂停状态开始
                            pauseOrRestartTreatment(1);
                        }
                    }
                    else firstTreatment();
                } else {
                    if (!isTreatmenting)return;
                    //暂停计时器，设置标记为false
                    pauseOrRestartTreatment(2);
                    mflag = false;
                    //不可以使用 stop 方法，会报错，java.lang.UnsupportedOperationException
                    //mThread.stop();
                }

                break;
        }
    }

    private void initTherad() {
        //主线程的 handler 接收到 子线程的消息，然后修改TextView的显示
        timeHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                int what = msg.what;
                switch (what) {
                    case 1:
                        int untilFinishedTime = msg.arg1;
                        tv_CTFTimer.setText(dateLong2String(untilFinishedTime*1000));
                        float pro =(allTime - untilFinishedTime)/allTime;
                        BigDecimal b = new BigDecimal(pro);
                        float f1 = b.setScale(4,BigDecimal.ROUND_HALF_UP).floatValue();
                        cpb_CTFProgress.setProgress(f1*100);
                        if (untilFinishedTime<=0&&isTreatmenting==true) finishTreatment();//完成治疗
                        break;
                }
            }
        };
        //子线程的初始化
        myCountDownThread = new Thread(this);
    }

    @Override
    public void run() {
        //子线程必须要设置这个标记mflag和倒计时数。
        while (mflag && mCount >= 0) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            //每间隔 一秒钟 发送 一个Message 给主线程的 handler让主线程的hanlder 来修改UI
            //注意 这里的 message可以是通过obtain来获取 这样节省内存，它会自动的看有没有可以复用的，就不重复创建了
            Message message = Message.obtain();
            message.what = 1;
            message.arg1 = mCount;
            timeHandler.sendMessage(message);
            mCount--;
        }
    }

    //开始治疗
    private void firstTreatment()
    {
        Map<String,String> map= new HashMap();
        map.put("c_pad_id", HttpHelper.deviceId);
        map.put("user_id",userModel.getId());
        map.put("number",userModel.getTreatmentTimes());

        HttpHelper.httpPost(HttpHelper.getStartTreat(), map, new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                Toast.makeText(getActivity(),"请检查网络", Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onResponse(String response, int id) {
                JSONObject dataObj=null;
                try {
                    dataObj=new JSONObject(response);
                    treatmentModel=new TreatmentModel();
                    treatmentModel.setTreatmentId(dataObj.getString("object_id"));
                    treatmentModel.setTreatmentState(dataObj.getString("state"));
                    treatmentModel.setTreatmentRemainTime(dataObj.getString("remain_time"));
                    //第一次开始
                    allTime= mCount=Integer.valueOf(treatmentModel.getTreatmentRemainTime())/1000;
                    myCountDownThread.start();
                    isTreatmenting=true;
                } catch (JSONException e) {
                    Toast.makeText(getActivity(),"Error", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    //2表示暂停 ,1表示开始
    private void pauseOrRestartTreatment(final int state)
    {
        Map<String,String> map= new HashMap();
        map.put("object_id", treatmentModel.getTreatmentId());
        map.put("state",String.valueOf(state));
        map.put("remain_time",String.valueOf(mCount*1000));

        HttpHelper.httpPost(HttpHelper.getPauseTreat(), map, new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                Toast.makeText(getActivity(),"请检查网络", Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onResponse(String response, int id) {
                JSONObject dataObj=null;
                try {
                    dataObj=new JSONObject(response);
                    treatmentModel.setTreatmentId(dataObj.getString("object_id"));
                    treatmentModel.setTreatmentState(dataObj.getString("state"));
                    if (state==1)  myCountDownThread.start();
                } catch (JSONException e) {
                    Toast.makeText(getActivity(),"Error", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }
    //3完成治疗
    private void finishTreatment()
    {
        Map<String,String> map= new HashMap();
        map.put("object_id", treatmentModel.getTreatmentId());
        map.put("state",String.valueOf(3));
        map.put("finish_time",String.valueOf(Calendar.getInstance().getTimeInMillis()));

        HttpHelper.httpPost(HttpHelper.getPauseTreat(), map, new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                Toast.makeText(getActivity(),"请检查网络", Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onResponse(String response, int id) {
                JSONObject dataObj=null;
                try {
                    dataObj=new JSONObject(response);
//                    Log.e("response++=",response);
//                    treatmentModel.setTreatmentId(dataObj.getString("object_id"));
//                    treatmentModel.setTreatmentUserId(dataObj.getString("user_id"));
//                    treatmentModel.setTreatmentTimes(dataObj.getString("number"));
//                    treatmentModel.setTreatmentState(dataObj.getString("state"));
//                    treatmentModel.setTreatmentStartTime(dataObj.getString("start_time"));
//                    treatmentModel.setTreatmentPauseTime(dataObj.getString("pause_time"));
//                    treatmentModel.setTreatmentRemainTime(dataObj.getString("remain_time"));
                    mflag = false;
                    cb_CTFStart.setBackgroundResource(R.drawable.ctf_btn_start);
                    cb_CTFStart.setChecked(false);
                    isTreatmenting=false;
                    //replaFragment(new CompleteTreatmentFragment());
                    replaFragment(getActivity(),new CompleteTreatmentFragment(),R.id.fl_HPFLayout,true);
                } catch (JSONException e) {
                    Toast.makeText(getActivity(),"Error", Toast.LENGTH_SHORT).show();
                }


            }
        });
    }

    private void isTreatment()
    {
        final MaterialDialog treatmentDialog =  new MaterialDialog.Builder(getActivity())
                .customView(R.layout.treatmenting_dialog,true)
                .show();
        treatmentDialog.setCanceledOnTouchOutside(false);
        View treatmentView = treatmentDialog.getCustomView();

        Button bt_TreatmentCancle = (Button) treatmentView.findViewById(R.id.bt_TreatmentCancle);
        Button bt_TreatmentSure = (Button) treatmentView.findViewById(R.id.bt_TreatmentSure);
        bt_TreatmentCancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                treatmentDialog.dismiss();
            }
        });
        bt_TreatmentSure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                treatmentDialog.dismiss();
                Map<String,String> map= new HashMap();
                map.put("object_id", treatmentModel.getTreatmentId());
                map.put("state",String.valueOf(3));
                map.put("finish_time",String.valueOf(Calendar.getInstance().getTimeInMillis()));

                final TextView tv_Reset = new TextView(getActivity());
                tv_Reset.setTextColor(Color.WHITE);
                tv_Reset.setTextSize(18);
                final KProgressHUD  restHUD= KProgressHUD.create(getActivity())
                        .setCustomView(tv_Reset);

                HttpHelper.httpPost(HttpHelper.getPauseTreat(), map, new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        tv_Reset.setText("请检查网络！");
                        restHUD.show();
                        Common.scheduleDismiss(restHUD);
                    }
                    @Override
                    public void onResponse(String response, int id) {
                        JSONObject dataObj=null;
                        try {
                            dataObj=new JSONObject(response);
                            isTreatmenting=false;
                            mflag = false;
                            cb_CTFStart.setBackgroundResource(R.drawable.ctf_btn_start);
                            cb_CTFStart.setChecked(false);

                            mCount=0;
                            myCountDownThread = new Thread(CourseOfTreatmentFragment.this);
                            tv_Reset.setText("已结束本疗程！");
                            cpb_CTFProgress.setProgress(100);
                            tv_CTFTimer.setText("00:00");
                            restHUD.show();
                            Common.scheduleDismiss(restHUD);
                        } catch (JSONException e) {

                            tv_Reset.setText("结束失败！");
                            restHUD.show();
                            Common.scheduleDismiss(restHUD);
                        }


                    }
                });

            }
        });
    }

//    public void replaFragment(Fragment fragment) {
//
//        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
//        transaction.setCustomAnimations(
//                R.anim.fragment_slide_right_in, R.anim.fragment_slide_left_out,
//                R.anim.fragment_slide_left_in, R.anim.fragment_slide_right_out);
//        transaction.replace(R.id.fl_HPFLayout, fragment);
//        transaction.addToBackStack(null);
//        transaction.commit();
//    }

    @Override
    public void onPause() {
        super.onPause();
        mflag = false;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }


}