package com.goockr.nakedeyeguard.Base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.goockr.nakedeyeguard.R;

/**
 * Created by JJT-ssd on 2017/3/1.
 */

public abstract class BaseFragment extends Fragment {
    View view=null;
    //返回按钮
    private ImageButton bt_BaseFragmentBack;
    //时期显示
    private ImageView iv_BaseFragmenWifi;

    protected abstract int getLoyoutId();
    protected abstract void onCusCreate(View view);
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view =inflater.inflate(getLoyoutId(),container,false);
        setupView();
        onCusCreate(view);
        return view;
    }

    public void setupView()
    {
        //获取当前时间
        iv_BaseFragmenWifi=(ImageView)view.findViewById(R.id.iv_BaseFragmenWifi);
        bt_BaseFragmentBack=(ImageButton) view.findViewById(R.id.bt_BaseFragmentBack);

    }

    //设置标题
    public ImageButton getBackBtn(){return bt_BaseFragmentBack;}

    public void setWifiIcon(boolean isNetWork)
    {
        if (isNetWork) iv_BaseFragmenWifi.setImageResource(R.drawable.icon_wifi);
        else iv_BaseFragmenWifi.setImageResource(R.drawable.icon_wifi_10);
    }

}
