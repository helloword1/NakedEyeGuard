package com.goockr.nakedeyeguard.view.HealingProcessPage;

import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
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
import com.goockr.nakedeyeguard.net.Http.HttpHelper;
import com.goockr.nakedeyeguard.data.Model.TreatmentBean;
import com.goockr.nakedeyeguard.data.Model.UserBean;
import com.goockr.nakedeyeguard.data.Model.UserDetailBean;
import com.goockr.nakedeyeguard.R;
import com.goockr.nakedeyeguard.view.TipsPage.TipsActivity;
import com.goockr.nakedeyeguard.Tools.Agreement;
import com.goockr.nakedeyeguard.Tools.App;
import com.goockr.nakedeyeguard.Tools.Common;
import com.goockr.nakedeyeguard.Tools.DialogHelper;
import com.goockr.nakedeyeguard.Tools.HudHelper;
import com.goockr.nakedeyeguard.Tools.NoDoubleClickUtils;
import com.goockr.nakedeyeguard.Tools.SerialPortHelper;
import com.goockr.nakedeyeguard.Tools.ToastUtils;
import com.goockr.nakedeyeguard.Tools.TreatmentEvent;
import com.goockr.nakedeyeguard.Tools.WifiHelper;
import com.goockr.nakedeyeguard.net.callBack.TreatmentCallback;
import com.goockr.nakedeyeguard.net.callBack.UserCallback;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.mikhaellopez.circularprogressbar.CircularProgressBar;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;

import static com.goockr.nakedeyeguard.Tools.DateExtension.dateLong2String;

/**
 * Created by JJT-ssd on 2017/2/28.
 * 治疗过程
 */

public class CourseOfTreatmentFragment extends BaseFragment implements View.OnClickListener, CompoundButton.OnCheckedChangeListener, Runnable {

    public static final int ISRUN = 1;
    public static final int ISPAUSE = 2;
    public static final int ISSTOP = 3;

    private static int state = 3;
    App app;
    ImageButton bt_CTFBack;
    ImageButton ib_CTFReduce;
    ImageButton ib_CTFAdd;
    ImageButton ib_CTFTip;
    CheckBox cb_CTFStart;
    ProgressBar pb_CTFStrength;
    CircularProgressBar cpb_CTFProgress;
    TextView tv_CTFStrength;
    TextView tv_CTFTimer;

    TextView debug_tv;

    UserBean userBean;
    UserDetailBean userInfo;
    TreatmentBean treatmentInfo;//理疗信息与参数
    boolean ischange = false;

    //拔出治疗线
    Button bt_PullOutHeadphones;

    HealingProcessActivity activity;
    //倒计时
    private Thread myCountDownThread;
    private static Handler timeHandler;
    public static boolean mflag;
    private static int mCount = 10;
    private float allTime = 10.0f;
    boolean isTreatmenting = false;//判断是否治疗中

    //实际开始运行，其他值存在启动时延时问题。这个值是实际理疗是否在进行
    private static boolean isRunning = false;
    private Handler handler;

    DialogHelper dialogHelper;

    HudHelper hudHelper;

    public DialogHelper getDialogHelper() {

        if (dialogHelper == null) {
            dialogHelper = new DialogHelper();
        }
        return dialogHelper;
    }

    public HudHelper getHudHelper() {

        if (hudHelper == null) {
            hudHelper = new HudHelper();
        }

        return hudHelper;
    }

    @Override
    protected int getLoyoutId() {
        return R.layout.course_treatment_fragment;
    }

    @Override
    protected void onCusCreate(View view) {
        handler = new Handler();
        setupUI(view);
        initTherad();
        //  App.getInstances().isWorking=true;

    }


    /**
     * 开启线程读串口数据
     */
    private void startReadThread() {
        //region开启线程读串口数据
        app.portHelper.setOnReceivedListener(new SerialPortHelper.OnReceivedListener() {
            @Override
            public void onReceived(final int[] bytes) {
                switch (bytes[1]) {
                    case 129://0x81开始运行指令回复

                        CourseOfTreatmentFragment.state = ISRUN;
                        App.getInstances().isWorking = true;
                        isRunning = true;

                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                getDialogHelper().updateTitleDelayHide("开启理疗成功", 800, new DialogHelper.DialogHelperCallBack() {
                                    @Override
                                    public void hideSuccess() {
                                        dialogHelper = null;
                                    }
                                });

                            }
                        });

                        break;
                    case 130://0x82 设置电流强度指令回复
                        if (bytes[2] != 0) return;

                        if (bytes[3] < 0 || bytes[3] > 40) return;

                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                intensity = bytes[3];
                                pb_CTFStrength.setProgress(bytes[3] * 25);
                                //tv_CTFStrength.setText("强度:" + String.valueOf((bytes[3] * 25)/10) + "%");
                                tv_CTFStrength.setText("强度:" + doubleToString((bytes[3] * 25) / 10.0) + "%");

//                                String str="";
//
//                                for (int i=0;i<bytes.length;i++)
//                                {
//                                    str=str+bytes[i]+",";
//                                }
//                                Log.v("Course","收到数据："+str);
                                //tv_CTFStrength.setText(str);
                            }
                        });
                        break;
                    case 131://0x83 查询指令回复
                        break;
                    case 134://0x86 结束运行指令回复
                        App.getInstances().isWorking = false;
                        isRunning = false;
                        break;
                    case 133://0x85 心跳指令回复

                        Log.v("心跳指令回复", "心跳指令回复");

                        break;
                    default:
                        break;
                }
            }
        });
    }

    private void setupUI(View view) {

        bt_PullOutHeadphones = (Button) view.findViewById(R.id.bt_PullOutHeadphones);
        bt_PullOutHeadphones.setOnClickListener(this);

        tv_CTFStrength = (TextView) view.findViewById(R.id.tv_CTFStrength);
        pb_CTFStrength = (ProgressBar) view.findViewById(R.id.pb_CTFStrength);
        cpb_CTFProgress = (CircularProgressBar) view.findViewById(R.id.cpb_CTFProgress);
        tv_CTFTimer = (TextView) view.findViewById(R.id.tv_CTFTimer);
        bt_CTFBack = (ImageButton) view.findViewById(R.id.bt_CTFBack);
        ib_CTFReduce = (ImageButton) view.findViewById(R.id.ib_CTFReduce);
        ib_CTFAdd = (ImageButton) view.findViewById(R.id.ib_CTFAdd);
        ib_CTFTip = (ImageButton) view.findViewById(R.id.ib_CTFTip);
        cb_CTFStart = (CheckBox) view.findViewById(R.id.cb_CTFStart);

        debug_tv = (TextView) view.findViewById(R.id.course_treatment_debug_tv);

        bt_CTFBack.setOnClickListener(this);
        ib_CTFTip.setOnClickListener(this);
        ib_CTFReduce.setOnClickListener(this);
        ib_CTFAdd.setOnClickListener(this);
        cb_CTFStart.setOnCheckedChangeListener(this);


        progressBarState(pb_CTFStrength.getProgress());
        activity = (HealingProcessActivity) getActivity();
        activity.getBackBtn().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isTreatmenting) isTreatment();
                else {
                    activity.getSupportFragmentManager().popBackStack();
                }
            }
        });
        userBean = ((HealingProcessActivity) getActivity()).userBean;
        // userInfo = ((HealingProcessActivity) getActivity()).userInfo;
        loadData();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        app.portHelper.releaseReadThread();
    }

    int intensity = 0;//电流强度


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_CTFBack:
                // app.portHelper.sendData(Agreement.END);
                if (isTreatmenting) isTreatment();
                else getActivity().finish();
                break;
            case R.id.ib_CTFReduce://减小电流强都，最小单位5%
                if (NoDoubleClickUtils.isFastClick()) {
                    return;
                } else if (!isRunning) {
                    //  Toast.makeText(getActivity(), "请在理疗开始后才进行操作", Toast.LENGTH_SHORT).show();
                    getHudHelper().hudHide();
                    getHudHelper().hudShowTip(getActivity(), "请在理疗开始后才进行操作", 800);

                    return;
                } else {

                    minv();
                }


                break;
            case R.id.ib_CTFAdd://增加电流强度，最小单位5%
                if (NoDoubleClickUtils.isFastClick()) {
                    return;
                } else if (!isRunning) {
                    // Toast.makeText(getActivity(), "请在理疗开始后才进行操作", Toast.LENGTH_SHORT).show();
                    getHudHelper().hudHide();
                    getHudHelper().hudShowTip(getActivity(), "请在理疗开始后才进行操作", 500);
                    return;
                } else {
                    addv();
                }

                break;
            case R.id.ib_CTFTip:
                if (isTreatmenting) isTreatment();
                else {
                    Intent intent = new Intent(getActivity(), TipsActivity.class);
                    startActivity(intent);
                }
                break;

            case R.id.bt_PullOutHeadphones://治疗线提醒
                if (isTreatmenting)
                    disConnectLine();
                break;

        }
    }


    private void minv() {

        int marginLevel = 25;

        int proValueReduce = pb_CTFStrength.getProgress();
        if (marginLevel <= proValueReduce) {

            //设置电流大小
            intensity = (proValueReduce - marginLevel) / marginLevel;
            if (intensity < 0) return;

//            pb_CTFStrength.setProgress(proValueReduce - marginLevel);
//            tv_CTFStrength.setText("强度:" + doubleToString((proValueReduce - marginLevel)/10.0) + "%");
            app.portHelper.sendData(Agreement.setCurrentIntensity(intensity));
            //  debug(Agreement.setCurrentIntensity(intensity));
        }
        progressBarState(pb_CTFStrength.getProgress());
    }

    private void addv() {

        int marginLevel = 25;
        int proValueAdd = pb_CTFStrength.getProgress();
        if (proValueAdd < 1000) {
            int a = proValueAdd + marginLevel;

            //设置电流大小
            intensity = (proValueAdd + marginLevel) / marginLevel;
            if (intensity > 40) return;

//            pb_CTFStrength.setProgress(proValueAdd + marginLevel);
//            tv_CTFStrength.setText("强度:" + doubleToString((proValueAdd + marginLevel)/10.0) + "%");
            app.portHelper.sendData(Agreement.setCurrentIntensity(intensity));
            //debug(Agreement.setCurrentIntensity(intensity));
        }
        progressBarState(pb_CTFStrength.getProgress());
    }

    private void progressBarState(int proValue) {
        if (0 < proValue)
            ib_CTFReduce.setBackgroundResource(R.drawable.ctf_btn_reduce);
        else ib_CTFReduce.setBackgroundResource(R.drawable.btn_reduce_disable);
        if (proValue < 1000)
            ib_CTFAdd.setBackgroundResource(R.drawable.ctf_btn_add);
        else ib_CTFAdd.setBackgroundResource(R.drawable.btn_add_disable);
    }

    private long startTime = 0;

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

        switch (buttonView.getId()) {

            case R.id.cb_CTFStart:

                cb_CTFStart.setClickable(false);
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        cb_CTFStart.setClickable(true);
                    }
                }, 1000);

                Log.v("onCheckedChanged", isChecked + ""); //函数名
                if (state == ISSTOP || state == ISPAUSE) {
                    //开始计时器或者是重启计时器，设置标记为true
                    mflag = true;

                    //判断是否是第一次启动，如果是不是第一次启动，那么状态就是Thread.State.TERMINATED
                    //不是的话，就需要重新的初始化，因为之前的已经结束了。
                    //并且要判断这个mCount 是否为-1，如果是的话，说名上一次的计时已经完成了，那么要重新设置。
                    if (state == ISPAUSE) {
                        myCountDownThread = new Thread(this);
                        if (mCount == -1) mCount = 10;
                        {
                            if (isFinish) {
                                isFinish = false;
                                return;
                            }

                            getDialogHelper().dialogShow(getActivity(), "正在为您开启理疗...");

                            TreatmentEvent.getInstance().postApproveNoti(0);
                            UserDetailBean.TreatmentBean treatmentBean = userInfo.getTreatment();
                            //未完成上次治疗，从上次暂停状态开始
                            //设置继续
                            if (treatmentBean.getType().equals("A")) {
                                app.portHelper.sendData(Agreement.START(1, intensity));
                            } else if (treatmentBean.getType().equals("B")) {
                                app.portHelper.sendData(Agreement.START(2, intensity));
                            } else if (treatmentBean.getType().equals("C")) {
                                app.portHelper.sendData(Agreement.START(3, intensity));
                            } else {
                                app.portHelper.sendData(Agreement.START(1, intensity));
                            }

                            // App.getInstances().isWorking=true;
                            pauseOrRestartTreatment(1);
                        }
                    } else if (state == ISSTOP) {
                        // App.getInstances().isWorking=true;
                        firstTreatment();
                    }
                } else if (state == ISRUN) {
                    if (!isTreatmenting) return;
                    //暂停计时器，设置标记为false
                    mflag = false;

                    getDialogHelper().dialogShow(getActivity(), "正在暂停理疗...");

                    //设置暂停
                    app.portHelper.sendData(Agreement.END);
                    pauseOrRestartTreatment(2);
                }
                break;
        }
    }

    private void initTherad() {

        app = (App) getActivity().getApplication();
        startReadThread();
        //主线程的 handler 接收到 子线程的消息，然后修改TextView的显示
        timeHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);

                if (getActivity() == null) return;

                int what = msg.what;
                switch (what) {
                    case 1:
                        int untilFinishedTime = msg.arg1;
                        tv_CTFTimer.setText(dateLong2String(untilFinishedTime * 1000));
                        float pro = (allTime - untilFinishedTime) / allTime;
                        BigDecimal b = new BigDecimal(pro);
                        float f1 = b.setScale(4, BigDecimal.ROUND_HALF_UP).floatValue();
                        cpb_CTFProgress.setProgress(f1 * 100);
                        if (untilFinishedTime <= 0 && isTreatmenting == true)
                            finishTreatment();//完成治疗
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
            if (mflag) mCount--;
            //每间隔 一秒钟 发送 一个Message 给主线程的 handler让主线程的hanlder 来修改UI
            //注意 这里的 message可以是通过obtain来获取 这样节省内存，它会自动的看有没有可以复用的，就不重复创建了
            Message message = Message.obtain();
            message.what = 1;
            message.arg1 = mCount;
            timeHandler.sendMessage(message);

        }
    }


    /**
     * 监测治疗线的状态
     *
     * @return false表示断开连接 true表示连接正常
     */
    private boolean isConnectLineState = false;

    public boolean isConnectLineState() {
        return isConnectLineState;
    }

    public void setConnectLineState(boolean connectLineState) {
        isConnectLineState = connectLineState;
    }

    /**
     * 治疗线状态的监测
     */
    private void disConnectLine() {
        boolean isConnect = false;
        if (!isConnect)//断开连接处理
        {
            setConnectLineState(false);
            if (!mflag) return;//不在治疗过程直接不处理
            //在治疗过程,断开治疗处理
            cb_CTFStart.setChecked(false);

        } else //连接状态处理
        {
            setConnectLineState(true);
        }

    }

    /**
     * 开始治疗
     */
    private void firstTreatment() {

        if (!WifiHelper.isNetworkAvailable(getActivity())) {
            final MaterialDialog voiceDialog = new MaterialDialog.Builder(getActivity())
                    .customView(R.layout.nonet_tip, true)
                    .show();
            voiceDialog.setCanceledOnTouchOutside(false);
            View voiceView = voiceDialog.getCustomView();
            Button bt_NoMoneyKonw = (Button) voiceView.findViewById(R.id.bt_nonet);
            bt_NoMoneyKonw.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    voiceDialog.dismiss();
                    getActivity().finish();
                }
            });
            return;
        }

        getDialogHelper().dialogShow(getActivity(), "正在为您开启理疗...");


        Map<String, String> map = new HashMap();
        map.put("c_pad_id", HttpHelper.deviceId);
        map.put("user_id", userBean.getId());
        map.put("number", String.valueOf(userInfo.getTreatment().getNumber()));

        HttpHelper.httpPost(HttpHelper.getStartTreat(), map, new TreatmentCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {

                startFiale("开启理疗失败,请重试");

            }

            @Override
            public void onResponse(TreatmentBean response, int id) {

                treatmentInfo = response;
                TreatmentEvent.getInstance().postApproveNoti(0);
                //第一次开始
                //设置开始启动
                UserDetailBean.TreatmentBean treatmentBean = userInfo.getTreatment();//用户信息中的理疗信息 包含理疗类型等
                if (treatmentBean.getType().equals("A")) {
                    app.portHelper.sendData(Agreement.START(1, intensity));
                } else if (treatmentBean.getType().equals("B")) {
                    app.portHelper.sendData(Agreement.START(2, intensity));
                } else if (treatmentBean.getType().equals("C")) {
                    app.portHelper.sendData(Agreement.START(3, intensity));
                } else {
                    app.portHelper.sendData(Agreement.START(1, intensity));
                }
                allTime = mCount = Integer.valueOf(treatmentInfo.getRemain_time()) / 1000;

                // myCountDownThread.start();

                //musicService.play(0);

                startTime = System.currentTimeMillis();//获取启动时间
                isTreatmenting = true;
                cb_CTFStart.setBackgroundResource(R.drawable.btn_pause);
                ischange = true;

            }
        });
    }

    /**
     * 2表示暂停 ,1表示开始
     */
    private void pauseOrRestartTreatment(final int state) {
        Map<String, String> map = new HashMap();
        map.put("object_id", treatmentInfo.getObject_id());
        map.put("state", String.valueOf(state));
        map.put("remain_time", String.valueOf(mCount * 1000));

        HttpHelper.httpPost(HttpHelper.getPauseTreat(), map, new TreatmentCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                // Toast.makeText(getActivity(), "请检查网络", Toast.LENGTH_SHORT).show();

                String tip = "";
                if (state == 1) {
                    tip = "开启理疗失败,请重试";
                } else {
                    tip = "暂停理疗失败,请重试";
                }
                startFiale(tip);

            }

            @Override
            public void onResponse(TreatmentBean response, int id) {
                try {
                    treatmentInfo = response;
                    if (state == 1) {
                        cb_CTFStart.setBackgroundResource(R.drawable.btn_pause);
                        ischange = true;
                        // myCountDownThread.start();
                        //  musicService.play(0);

                        CourseOfTreatmentFragment.state = ISRUN;

                        startTime = System.currentTimeMillis();//获取启动时间

                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                getDialogHelper().updateTitleDelayHide("开启理疗成功", 500, new DialogHelper.DialogHelperCallBack() {
                                    @Override
                                    public void hideSuccess() {
                                        dialogHelper = null;
                                    }
                                });

                            }
                        });

                    }
                    if (state == 2) {
                        cb_CTFStart.setBackgroundResource(R.drawable.ctf_btn_start);
                        ischange = false;
                        //  musicService.playPause();
                        CourseOfTreatmentFragment.state = ISPAUSE;
                        while (pb_CTFStrength.getProgress() != 0) {
                            app.portHelper.sendData(Agreement.setCurrentIntensity(0));
                            Thread.sleep(100);
                            pb_CTFStrength.setProgress(0);
//                            Thread.sleep(100);
//                            minv();
                        }

                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                getDialogHelper().updateTitleDelayHide("暂停理疗成功", 500, new DialogHelper.DialogHelperCallBack() {
                                    @Override
                                    public void hideSuccess() {
                                        dialogHelper = null;
                                    }
                                });

                            }
                        });

                    }
                } catch (Exception e) {
                    //Toast.makeText(getActivity(), "Error", Toast.LENGTH_SHORT).show();

                    String tip = "";
                    if (state == 1) {
                        tip = "开启理疗失败,请重试";
                    } else {
                        tip = "暂停理疗失败,请重试";
                    }
                    startFiale(tip);
                }
            }
        });
    }

    /**
     * 3完成治疗
     */
    boolean isFinish = false;//是否完成治疗标志

    private void finishTreatment() {
        Map<String, String> map = new HashMap();
        map.put("object_id", treatmentInfo.getObject_id());
        map.put("state", String.valueOf(3));
        map.put("finish_time", String.valueOf(Calendar.getInstance().getTimeInMillis()));

        HttpHelper.httpPost(HttpHelper.getCompeleteTreat(), map, new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                ToastUtils.showToast(getActivity(), "请检查网络");
            }

            @Override
            public void onResponse(String response, int id) {
                JSONObject dataObj = null;
                try {
                    dataObj = new JSONObject(response);

                    isFinish = true;
                    mflag = false;
                    cb_CTFStart.setBackgroundResource(R.drawable.ctf_btn_start);
                    ischange = false;
                    isTreatmenting = false;
                    replaFragment(new CompleteTreatmentFragment());
                } catch (JSONException e) {
                    Toast.makeText(getActivity(), "Error", Toast.LENGTH_SHORT).show();
                }


            }
        });
    }

    /**
     * 判断是否治疗中
     */
    private void isTreatment() {

        final MaterialDialog treatmentDialog = new MaterialDialog.Builder(getActivity())
                .customView(R.layout.treatmenting_dialog, true)
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
                completeTreatment();
            }
        });
    }

    /**
     * 强制完成治疗，人为退出治疗
     */
    private void completeTreatment() {

        Map<String, String> map = new HashMap();
        map.put("object_id", treatmentInfo.getObject_id());
        map.put("state", String.valueOf(3));
        map.put("finish_time", String.valueOf(Calendar.getInstance().getTimeInMillis()));

        final TextView tv_Reset = new TextView(getActivity());
        tv_Reset.setTextColor(Color.WHITE);
        tv_Reset.setTextSize(18);
        final KProgressHUD restHUD = KProgressHUD.create(getActivity())
                .setCustomView(tv_Reset);

        HttpHelper.httpPost(HttpHelper.getCompeleteTreat(), map, new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {

                tv_Reset.setText("请检查网络！");
                restHUD.show();
                Common.scheduleDismiss(restHUD);
            }

            @Override
            public void onResponse(String response, int id) {
                JSONObject dataObj = null;
                try {
                    while (pb_CTFStrength.getProgress() != 0) {
                        app.portHelper.sendData(Agreement.setCurrentIntensity(0));
                        Thread.sleep(100);
                        pb_CTFStrength.setProgress(0);
//                        Thread.sleep(100);
//                        minv();
                    }
                    dataObj = new JSONObject(response);
                    isFinish = true;
                    isTreatmenting = false;
                    mflag = false;
                    cb_CTFStart.setBackgroundResource(R.drawable.ctf_btn_start);
                    ischange = false;
                    cb_CTFStart.setChecked(false);
                    //结束指令
                    Thread.sleep(30);
                    app.portHelper.sendData(Agreement.END);
                    mCount = 0;

                    // musicService.stop();

                    state = ISSTOP;
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
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }


            }
        });
    }


    @Override
    public void onPause() {
        super.onPause();
        // mflag = false;


    }

    /**
     * 加载数据
     */
    private void loadData() {
        Map<String, String> map = new HashMap<>();
        map.put("user_id", userBean.getId());
        HttpHelper.httpPost(HttpHelper.getUserInfo(), map, new UserCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {


                //Toast.makeText(getActivity(), "请检查网络", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(UserDetailBean response, int id) {
                userInfo = response;
            }
        });

    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        App.getInstances().isWorking = false;
    }


    //设置音频播放的监听


    public static void onProgressChange(int progress) {

        Log.v("onProgressChange", "" + progress);

        if (!isRunning) return;
        if (mflag) mCount--;
        Message message = Message.obtain();
        message.what = 1;
        message.arg1 = mCount;
        timeHandler.sendMessage(message);


    }


    /****************20170606******************/

    private void startFiale(final String tip) {

        if (getActivity() != null) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (dialogHelper != null) {
                        dialogHelper.updateTitleDelayHide(tip, 500, new DialogHelper.DialogHelperCallBack() {
                            @Override
                            public void hideSuccess() {
                                dialogHelper = null;
                            }
                        });
                    }
                }
            });
        }

    }

    private String doubleToString(double num) {
        NumberFormat formatter = new DecimalFormat("0.##");
        String str = formatter.format(num);
        return str;
    }

    private void debug(byte[] bytes) {
        String s = "";

        for (int i = 0; i < bytes.length; i++) {
            s = s + bytes[i] + ",";
        }

        Log.v("Course", "发出数据：" + s);
        debug_tv.setText(s);


    }

    public void replaFragment(Fragment fragment) {

        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(
                R.anim.fragment_slide_right_in, R.anim.fragment_slide_left_out,
                R.anim.fragment_slide_left_in, R.anim.fragment_slide_right_out);
        transaction.replace(R.id.fl_HPFLayout, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

}