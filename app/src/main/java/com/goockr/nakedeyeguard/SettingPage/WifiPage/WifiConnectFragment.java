package com.goockr.nakedeyeguard.SettingPage.WifiPage;

import android.content.Context;
import android.graphics.Color;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiInfo;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.goockr.nakedeyeguard.Base.BaseFragment;
import com.goockr.nakedeyeguard.FirstUsePage.BindingFragment;
import com.goockr.nakedeyeguard.Model.WifiModel;
import com.goockr.nakedeyeguard.R;
import com.kaopiz.kprogresshud.KProgressHUD;

import java.util.ArrayList;
import java.util.List;

import static com.goockr.nakedeyeguard.App.editor;
import static com.goockr.nakedeyeguard.App.preferences;
import static com.goockr.nakedeyeguard.App.wifiHelper;

/**
 * Created by JJT-ssd on 2017/3/2.
 */

public class WifiConnectFragment extends BaseFragment implements View.OnClickListener{

    ListView lv_SetWifiList;
    WifiAdapter wifiAdapter;
    Button bt_WifiScan;
    List<WifiModel> wifiModels=new ArrayList<>();
    @Override
    protected int getLoyoutId() {return R.layout.wifi_connect_fragment;}

    @Override
    protected void onCusCreate(View view) {
        initValue();
        setupUI(view);
        eventHandle();
    }

    private void initValue()
    {
        wifiModels.clear();
        List<ScanResult>scanResult = wifiHelper.startScan();
        wifiHelper.startScan();
        WifiInfo connectInfo =wifiHelper.connectInfo();

        for (int i=0;i<scanResult.size();i++)
        {
            WifiModel wifiModel=new WifiModel();
            ScanResult result = scanResult.get(i);
            if (result.SSID.length()<=0)continue;

            wifiModel.setWifiName(result.SSID);
            String connWifiSSID = connectInfo.getSSID();
            String connWifiName = connWifiSSID.substring(1,connWifiSSID.length()-1);

            if (result.SSID.equals(connWifiName)) wifiModel.setConnectState(true);
            else  wifiModel.setConnectState(false);
            if (result.capabilities.contains("WEP")) wifiModel.setSecurityType(WifiModel.SecurityType.WEP);
            else if (result.capabilities.contains("PSK")) wifiModel.setSecurityType(WifiModel.SecurityType.WPA_PSK);
            else if (result.capabilities.contains("EAP")) wifiModel.setSecurityType(WifiModel.SecurityType.WPA_EAP);
            else  wifiModel.setSecurityType(WifiModel.SecurityType.NONE);

            if (wifiModel.getSecurityType()== WifiModel.SecurityType.NONE) wifiModel.setWifiLock(false);
            else wifiModel.setWifiLock(true);
            wifiModels.add(wifiModel);
        }
    }

    private void setupUI(View view)
    {
        LayoutInflater lif = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        lv_SetWifiList=(ListView)view.findViewById(R.id.lv_SetWifiList);
        bt_WifiScan=(Button) view.findViewById(R.id.bt_WifiScan);
        bt_WifiScan.setOnClickListener(this);
        View headView =lif.inflate(R.layout.set_wifi_list_headview, lv_SetWifiList, false);
        View footView =lif.inflate(R.layout.set_wifi_list_footview, lv_SetWifiList, false);
        lv_SetWifiList.addHeaderView(headView);
        lv_SetWifiList.addFooterView(footView);
        wifiAdapter=new WifiAdapter(wifiModels);
        lv_SetWifiList.setAdapter(wifiAdapter);
        setWifiIcon(((WifiActivity)getActivity()).getNetWorkState());

        footView.findViewById(R.id.rl_WifiSelectOther).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                replaFragment(new AddOtherNetworkFragment());
            }
        });
    }

    private void eventHandle()
    {
        getBackBtn().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });


        lv_SetWifiList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
             //   ((WifiActivity)getActivity()).selectWifi=wifiModels.get(position-1);
                WifiModel model = wifiModels.get(position-1);
                if (model.isWifiLock())
                replaFragment(new WifiPWDFragment(wifiModels.get(position-1)));
                else {

                    TextView tv_Reset = new TextView(getActivity());
                    tv_Reset.setTextColor(Color.WHITE);
                    tv_Reset.setTextSize(18);
                    boolean  isConnect=wifiHelper.addNetwork(wifiHelper.CreateWifiInfo(model.getWifiName(),"",model.getSecurityType().ordinal()));
                    if (isConnect)
                    {
                        editor.putString("WifiName",model.getWifiName());
                        editor.putString("WifiPWD","");
                        editor.putString("WifiSecurityType",String.valueOf(model.getSecurityType().ordinal()));
                        editor.commit();
                        //model.setConnectState(true);
                        initValue();
                        wifiAdapter.notifyDataSetChanged();
                        tv_Reset.setText("设备已成功连接WiFi！");
                    }
                    else tv_Reset.setText("连接失败，请重新连接！");

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
                            }

                        }
                    }, 2000);
                }
            }
        });
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

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.bt_WifiScan:
                Toast.makeText(getActivity(),"扫描完成！",Toast.LENGTH_SHORT).show();
                initValue();
                wifiAdapter.notifyDataSetChanged();
                break;
        }

    }
}
