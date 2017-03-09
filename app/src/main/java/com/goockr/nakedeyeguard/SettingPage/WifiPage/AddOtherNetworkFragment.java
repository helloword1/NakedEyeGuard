package com.goockr.nakedeyeguard.SettingPage.WifiPage;

import android.app.Activity;
import android.content.Context;
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
import android.widget.ImageButton;
import android.widget.TextView;

import com.goockr.nakedeyeguard.Base.BaseFragment;
import com.goockr.nakedeyeguard.FirstUsePage.BindingFragment;
import com.goockr.nakedeyeguard.R;
import com.kaopiz.kprogresshud.KProgressHUD;

import java.util.Timer;
import java.util.TimerTask;

import static com.goockr.nakedeyeguard.App.editor;
import static com.goockr.nakedeyeguard.App.preferences;
import static com.goockr.nakedeyeguard.App.wifiHelper;

/**
 * Created by JJT-ssd on 2017/3/8.
 */

public class AddOtherNetworkFragment extends BaseFragment implements View.OnClickListener{

    EditText et_NewWifiNameInput;
    EditText et_NewWifiPWDInput;
    Button bt_AddWifiSure;

    ImageButton ib_AddWifiPWDDel;
    ImageButton ib_AddWifiNameDel;
    @Override
    protected int getLoyoutId() {return R.layout.add_other_network_fragment;}

    @Override
    protected void onCusCreate(View view) {
        setupUI(view);
        eventHandle();
    }


    private void setupUI(View view) {
        setWifiIcon(((WifiActivity)getActivity()).getNetWorkState());

        bt_AddWifiSure=(Button)view.findViewById(R.id.bt_AddWifiSure);
        ib_AddWifiPWDDel=(ImageButton)view.findViewById(R.id.ib_AddWifiPWDDel);
        ib_AddWifiNameDel=(ImageButton)view.findViewById(R.id.ib_AddWifiNameDel);

        ib_AddWifiPWDDel.setOnClickListener(this);
        ib_AddWifiNameDel.setOnClickListener(this);

        bt_AddWifiSure.setOnClickListener(this);
        et_NewWifiNameInput=(EditText)view.findViewById(R.id.et_NewWifiNameInput);
        et_NewWifiPWDInput=(EditText)view.findViewById(R.id.et_NewWifiPWDInput);
        showSoftInputFromWindow(getActivity(),et_NewWifiNameInput);

    }

    private void eventHandle() {
        getBackBtn().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finishFragment();
            }
        });
        et_NewWifiNameInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length()>0)ib_AddWifiNameDel.setVisibility(View.VISIBLE);
                else ib_AddWifiNameDel.setVisibility(View.GONE);
                if (et_NewWifiPWDInput.getText().length()>0&&et_NewWifiNameInput.getText().length()>0)
                {
                    bt_AddWifiSure.setBackgroundResource(R.drawable.heading_process_fragment_sure);
                    bt_AddWifiSure.setEnabled(true);
                }
                else {
                    bt_AddWifiSure.setBackgroundResource(R.drawable.btn_1_disable);
                    bt_AddWifiSure.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        et_NewWifiPWDInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length()>0)ib_AddWifiPWDDel.setVisibility(View.VISIBLE);
                else ib_AddWifiPWDDel.setVisibility(View.GONE);
                if (et_NewWifiPWDInput.getText().length()>0&&et_NewWifiNameInput.getText().length()>0) {
                    bt_AddWifiSure.setEnabled(true);
                    bt_AddWifiSure.setBackgroundResource(R.drawable.heading_process_fragment_sure);
                }
                else {
                    bt_AddWifiSure.setEnabled(false);
                    bt_AddWifiSure.setBackgroundResource(R.drawable.btn_1_disable);
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
        timer.schedule(new TimerTask()
        {
            public void run()
            {
                InputMethodManager inputManager =  (InputMethodManager)editText.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManager.showSoftInput(editText, 0);
            }}, 100);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.ib_AddWifiPWDDel:
                et_NewWifiPWDInput.setText("");
                break;
            case R.id.ib_AddWifiNameDel:
                et_NewWifiNameInput.setText("");
                break;
            case R.id.bt_AddWifiSure:
                connectWifi();
                break;

        }
    }

    private void connectWifi()
    {
        TextView tv_Reset = new TextView(getActivity());
        tv_Reset.setTextColor(Color.WHITE);
        tv_Reset.setTextSize(18);

        String wifiName=et_NewWifiNameInput.getText().toString();
        String wifiPWD =et_NewWifiPWDInput.getText().toString();
        int wifiSecurityType=2;

        boolean  isConnect=wifiHelper.addNetwork(wifiHelper.CreateWifiInfo(wifiName,wifiPWD,wifiSecurityType));
        if (isConnect)
        {
            editor.putString("WifiName",wifiName);
            editor.putString("WifiPWD",wifiPWD);
            editor.putString("WifiSecurityType",String.valueOf(wifiSecurityType));
            editor.commit();
            tv_Reset.setText("设备已成功连接WiFi！");
        }
        else
        {
            tv_Reset.setText("连接失败，请重新连接！");
        }

        final KProgressHUD restHUD= KProgressHUD.create(getActivity())
                .setCustomView(tv_Reset)
                .show();
        final boolean finalIsConnect = isConnect;
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                restHUD.dismiss();
                if (finalIsConnect)
                {
                    boolean isFirstUser = preferences.getBoolean("FirstUser",true);
                    if (isFirstUser) replaFragment(new BindingFragment());
                    else  finishFragment();
                }

            }
        }, 2000);
    }


    public void replaFragment(Fragment fragment)
    {

        FragmentTransaction transaction=getActivity().getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(
                R.anim.fragment_slide_right_in, R.anim.fragment_slide_left_out,
                R.anim.fragment_slide_left_in, R.anim.fragment_slide_right_out);
        transaction.replace(R.id.fl_WifiConnect,fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    private void finishFragment()
    {
        InputMethodManager imm =  (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        if(imm != null) {
            imm.hideSoftInputFromWindow(getActivity().getWindow().getDecorView().getWindowToken(), 0);
        }
        getActivity().getSupportFragmentManager().popBackStack();
    }
}
