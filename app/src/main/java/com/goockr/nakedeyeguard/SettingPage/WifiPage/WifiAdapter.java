package com.goockr.nakedeyeguard.SettingPage.WifiPage;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.goockr.nakedeyeguard.Model.WifiModel;
import com.goockr.nakedeyeguard.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by JJT-ssd on 2017/3/2.
 */

public class WifiAdapter extends BaseAdapter {

    List<WifiModel> wifiModels=new ArrayList<>();
   public WifiAdapter(List<WifiModel> wifiModels)
   {
        this.wifiModels=wifiModels;
   }
    @Override
    public int getCount() {
        return wifiModels.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view=  LayoutInflater.from(parent.getContext()).inflate(R.layout.set_wifi_list_item,parent,false);

        ImageView iv_WifiConnectStateIcon=(ImageView) view.findViewById(R.id.iv_WifiConnectStateIcon);
        TextView tv_WifiName=(TextView) view.findViewById(R.id.tv_WifiName);
        ImageView iv_WifiIsLock=(ImageView) view.findViewById(R.id.iv_WifiIsLock);

        WifiModel wifiModel=wifiModels.get(position);
        if (!wifiModel.isConnectState())iv_WifiConnectStateIcon.setVisibility(View.GONE);
        tv_WifiName.setText(wifiModel.getWifiName());
        if (!wifiModel.isWifiLock())iv_WifiIsLock.setVisibility(View.GONE);
        return  view;
    }
}
