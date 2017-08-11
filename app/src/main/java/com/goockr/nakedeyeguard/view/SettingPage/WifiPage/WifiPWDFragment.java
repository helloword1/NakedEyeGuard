package com.goockr.nakedeyeguard.view.SettingPage.WifiPage;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.goockr.nakedeyeguard.Base.BaseFragment;
import com.goockr.nakedeyeguard.R;
import com.goockr.nakedeyeguard.Tools.NetUtils;
import com.goockr.nakedeyeguard.Tools.NotNull;
import com.goockr.nakedeyeguard.data.Model.WifiModel;
import com.goockr.nakedeyeguard.service.DownloadApkService;
import com.goockr.nakedeyeguard.view.FirstUsePage.OneFragment;
import com.goockr.nakedeyeguard.view.MainPage.MainActivity;
import com.kaopiz.kprogresshud.KProgressHUD;

import java.util.Timer;
import java.util.TimerTask;

import static com.goockr.nakedeyeguard.Tools.App.editor;
import static com.goockr.nakedeyeguard.Tools.App.preferences;

/**
 * Created by JJT-ssd on 2017/3/2.
 */

public class WifiPWDFragment extends BaseFragment implements View.OnClickListener {

    EditText et_WifiPWDInput;
    Button bt_WifiPwdCancle;
    Button bt_WifiPwdSure;
    WifiModel wifiModel;
    private Handler handler;
    ProgressDialog dialog;
    WifiActivity activity;

    @SuppressLint("ValidFragment")
    public WifiPWDFragment(WifiModel wifiModel) {
        this.wifiModel = wifiModel;
    }

    @Override
    protected int getLoyoutId() {
        return R.layout.set_wifipwd_fragment;
    }

    @Override
    protected void onCusCreate(View view) {
        handler = new Handler();
        setupUI(view);
        eventHandle();
    }
    public WifiPWDFragment(){

    }

    private void setupUI(View view) {
        bt_WifiPwdCancle = (Button) view.findViewById(R.id.bt_WifiPwdCancle);
        bt_WifiPwdSure = (Button) view.findViewById(R.id.bt_WifiPwdSure);
        bt_WifiPwdCancle.setOnClickListener(this);
        bt_WifiPwdSure.setOnClickListener(this);
        et_WifiPWDInput = (EditText) view.findViewById(R.id.et_WifiPWDInput);
        et_WifiPWDInput.setText("goockr86");
        showSoftInputFromWindow(getActivity(), et_WifiPWDInput);

    }

    private void eventHandle() {
         activity = (WifiActivity) getActivity();
        activity.getBackBtn().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finishFragment();
            }
        });

        et_WifiPWDInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    bt_WifiPwdSure.setBackgroundResource(R.drawable.btn_save);
                    bt_WifiPwdSure.setEnabled(true);
                } else {
                    bt_WifiPwdSure.setBackgroundResource(R.drawable.btn_3_disable);
                    bt_WifiPwdSure.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

    /**
     * EditText获取焦点并显示软键盘
     */
    public static void showSoftInputFromWindow(Activity activity, final EditText editText) {
        editText.setFocusable(true);
        editText.setFocusableInTouchMode(true);
        editText.requestFocus();
        Timer timer = new Timer(true);
        timer.schedule(new TimerTask() {
            public void run() {
                InputMethodManager inputManager = (InputMethodManager) editText.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManager.showSoftInput(editText, 0);
            }
        }, 100);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_WifiPwdSure:
                final TextView tv_Reset = new TextView(getActivity());
                tv_Reset.setTextColor(Color.WHITE);
                tv_Reset.setTextSize(24);

                final String wifiName = wifiModel.getWifiName();
                final String wifiPWD = et_WifiPWDInput.getText().toString();
                final int wifiSecurityType = wifiModel.getSecurityType().ordinal();

                dialog = new ProgressDialog(getActivity());
                dialog.setMessage("Connecting......");
                dialog.show();

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        final boolean isConnect =  NetUtils.connect(wifiName,wifiPWD,wifiSecurityType);
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                dialog.dismiss();
                                if (isConnect) {
                                    editor.putString("WifiName", wifiName);
                                    editor.putString("WifiPWD", wifiPWD);
                                    editor.putString("WifiSecurityType", String.valueOf(wifiSecurityType));
                                    editor.commit();
                                    tv_Reset.setText("设备已成功连接WiFi！");

                                } else {
                                    tv_Reset.setText("连接失败，请重新连接！");

                                }

                                if (getActivity()==null)return;
                                final KProgressHUD restHUD = KProgressHUD.create(getActivity())
                                        .setCustomView(tv_Reset)
                                        .show();
                                final boolean finalIsConnect = isConnect;
                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        restHUD.dismiss();
                                        if (finalIsConnect) {
                                            boolean isFirstUser = preferences.getBoolean("FirstUser", true);
                                            if (isFirstUser) replaFragment(new OneFragment());
                                            else finishFragment();
                                        }

                                    }
                                }, 2000);
                            }
                        });
                    }
                }).start();
                break;
            case R.id.bt_WifiPwdCancle:
                finishFragment();
                break;
        }
    }



    @Override
    public void onStop() {
        super.onStop();
        try {
            dialog.dismiss();
        }catch (Exception e){

        }

    }

    private void finishFragment() {
//        Intent intent= new Intent(getActivity(), MainActivity.class);
//        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        startActivity(intent);

   //    activity.getSupportFragmentManager().popBackStack();

        final boolean isFirstUser = preferences.getBoolean("FirstUser",true);
        if (!isFirstUser)
        {
            Intent intent= new Intent(getActivity(), MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);

        }else {
            InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm != null) {
                imm.hideSoftInputFromWindow(activity.getWindow().getDecorView().getWindowToken(), 0);
            }
            activity.getSupportFragmentManager().popBackStack();
        }

    }

    public void replaFragment(Fragment fragment) {

        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(
                R.anim.fragment_slide_right_in, R.anim.fragment_slide_left_out,
                R.anim.fragment_slide_left_in, R.anim.fragment_slide_right_out);
        transaction.replace(R.id.fl_WifiConnect, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

}
