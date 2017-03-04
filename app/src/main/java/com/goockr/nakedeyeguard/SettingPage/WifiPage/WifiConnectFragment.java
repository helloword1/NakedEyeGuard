package com.goockr.nakedeyeguard.SettingPage.WifiPage;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.goockr.nakedeyeguard.Base.BaseFragment;
import com.goockr.nakedeyeguard.HealingProcessPage.HealingProcessActivity;
import com.goockr.nakedeyeguard.Model.WifiModel;
import com.goockr.nakedeyeguard.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by JJT-ssd on 2017/3/2.
 */

public class WifiConnectFragment extends BaseFragment {

    ListView lv_SetWifiList;
    WifiAdapter wifiAdapter;
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
        for (int i=0;i<20;i++)
        {
            WifiModel wifiModel=new WifiModel();
            wifiModel.setConnectState(false);
            wifiModel.setWifiLock(true);
            wifiModel.setWifiName("jetgege"+ String.valueOf(i));
            wifiModels.add(wifiModel);
        }
    }

    private void setupUI(View view)
    {
        LayoutInflater lif = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        lv_SetWifiList=(ListView)view.findViewById(R.id.lv_SetWifiList);
        View headView =lif.inflate(R.layout.set_wifi_list_headview, lv_SetWifiList, false);
        View footView =lif.inflate(R.layout.set_wifi_list_footview, lv_SetWifiList, false);
        lv_SetWifiList.addHeaderView(headView);
        lv_SetWifiList.addFooterView(footView);
        wifiAdapter=new WifiAdapter(wifiModels);
        lv_SetWifiList.setAdapter(wifiAdapter);
        setWifiIcon(((WifiActivity)getActivity()).getNetWorkState());

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
                ((WifiActivity)getActivity()).selectWifi=wifiModels.get(position-1);
                replaFragment(new WifiPWDFragment());
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
}
