package com.goockr.nakedeyeguard.SettingPage.WifiPage;

import android.app.Activity;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.goockr.nakedeyeguard.Base.BaseFragment;
import com.goockr.nakedeyeguard.R;

import java.util.Timer;
import java.util.TimerTask;

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

        bt_AddWifiSure=(Button)view.findViewById(R.id.bt_WifiPwdSure);
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
        et_NewWifiNameInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length()>0)ib_AddWifiNameDel.setVisibility(View.VISIBLE);
                else ib_AddWifiNameDel.setVisibility(View.GONE);
                if (et_NewWifiPWDInput.getText().length()>0&&et_NewWifiNameInput.getText().length()>0)
                    bt_AddWifiSure.setBackgroundResource(R.drawable.heading_process_fragment_sure);
                //else bt_AddWifiSure.setBackgroundResource(Color.parseColor("#B8ddc2"));
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
                if (et_NewWifiPWDInput.getText().length()>0&&et_NewWifiNameInput.getText().length()>0)
                bt_AddWifiSure.setBackgroundResource(R.drawable.heading_process_fragment_sure);
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
                et_NewWifiNameInput.setText("");
                break;
            case R.id.ib_AddWifiNameDel:
                et_NewWifiPWDInput.setText("");
                break;
            case R.id.bt_AddWifiSure:

                break;

        }
    }
}
