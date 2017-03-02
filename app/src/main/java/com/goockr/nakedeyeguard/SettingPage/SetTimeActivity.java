package com.goockr.nakedeyeguard.SettingPage;


import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.goockr.nakedeyeguard.Base.BaseActivity;
import com.goockr.nakedeyeguard.R;
import com.kyleduo.switchbutton.SwitchButton;
import com.weigan.loopview.LoopView;
import com.weigan.loopview.OnItemSelectedListener;

import java.util.ArrayList;



public class SetTimeActivity extends BaseActivity implements View.OnClickListener,Switch.OnCheckedChangeListener{


    RelativeLayout rl_STDate;
    RelativeLayout rl_STTime;
    RelativeLayout rl_STSelectTimeZone;

    SwitchButton sw_STInternetTime;
    SwitchButton sw_STInternetTimeZone;
    SwitchButton sw_STComfirmTimeZone;

    final ArrayList<String> list = new ArrayList<>();
    @Override
    protected int getLoyoutId() {
        return R.layout.activity_set_time;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initValue();
        setupUI();
        eventHandle();

    }


    private void setupUI()
    {
        rl_STDate=(RelativeLayout)findViewById(R.id.rl_STDate);
        rl_STTime=(RelativeLayout)findViewById(R.id.rl_STTime);;
        rl_STSelectTimeZone=(RelativeLayout)findViewById(R.id.rl_STSelectTimeZone);

        sw_STInternetTime=(SwitchButton)findViewById(R.id.sw_STInternetTime);
        sw_STInternetTimeZone=(SwitchButton)findViewById(R.id.sw_STInternetTimeZone);
        sw_STComfirmTimeZone=(SwitchButton)findViewById(R.id.sw_STComfirmTimeZone);

        rl_STDate.setOnClickListener(this);
        rl_STTime.setOnClickListener(this);
        rl_STSelectTimeZone.setOnClickListener(this);

        sw_STInternetTime.setOnCheckedChangeListener(this);
        sw_STInternetTimeZone.setOnCheckedChangeListener(this);
        sw_STComfirmTimeZone.setOnCheckedChangeListener(this);
    }

    private void eventHandle()
    {
        getBackBtn().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(R.anim.fragment_slide_left_in, R.anim.fragment_slide_right_out);
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {

            case R.id.rl_STDate:
                final MaterialDialog dateDialog =  new MaterialDialog.Builder(this)
                        .customView(R.layout.set_data_dialog,true)
                        .show();
                dateDialog.setCanceledOnTouchOutside(false);
                View dateView = dateDialog.getCustomView();
                Button bt_SetDateCancle = (Button) dateView.findViewById(R.id.bt_SetDateCancle);
                Button bt_SetDateSave = (Button) dateView.findViewById(R.id.bt_SetDateSave);
                bt_SetDateCancle.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dateDialog.dismiss();
                    }
                });
                bt_SetDateSave.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(SetTimeActivity.this,"SET tIME",Toast.LENGTH_SHORT).show();
                        dateDialog.dismiss();
                    }
                });
                break;
            case R.id.rl_STTime:

                final MaterialDialog timeDialog =  new MaterialDialog.Builder(this)
                        .customView(R.layout.set_time_dialog,true)
                        .show();
                timeDialog.setCanceledOnTouchOutside(false);
                View timeView = timeDialog.getCustomView();
                Button bt_SetTimeCancle = (Button) timeView.findViewById(R.id.bt_SetTimeCancle);
                Button bt_SetTimeSave = (Button) timeView.findViewById(R.id.bt_SetTimeSave);
                bt_SetTimeCancle.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        timeDialog.dismiss();
                    }
                });
                bt_SetTimeSave.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(SetTimeActivity.this,"SET tIME",Toast.LENGTH_SHORT).show();
                        timeDialog.dismiss();
                    }
                });
                break;
            case R.id.rl_STSelectTimeZone:
                final MaterialDialog timeZoneDialog =  new MaterialDialog.Builder(this)
                        .customView(R.layout.set_time_zone_dialog,true)
                        .show();
                timeZoneDialog.setCanceledOnTouchOutside(false);
                View timeZoneView = timeZoneDialog.getCustomView();
                LoopView loop_SetTimeZone= (LoopView) timeZoneView.findViewById(R.id.loop_SetTimeZone);



                //设置是否循环播放
                loop_SetTimeZone.setNotLoop();
                //滚动监听
                loop_SetTimeZone.setListener(new OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(int index) {
                        Toast.makeText(SetTimeActivity.this, list.get(index), Toast.LENGTH_SHORT).show();
                    }
                });
                //设置原始数据
                loop_SetTimeZone.setItems(list);

                Button bt_SetTimeZoneCancle = (Button) timeZoneView.findViewById(R.id.bt_SetTimeZoneCancle);
                Button bt_SetTimeZoneSave = (Button) timeZoneView.findViewById(R.id.bt_SetTimeZoneSave);
                bt_SetTimeZoneCancle.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        timeZoneDialog.dismiss();
                    }
                });
                bt_SetTimeZoneSave.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        timeZoneDialog.dismiss();
                    }
                });
                break;

        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId())
        {
            case R.id.sw_STInternetTime:
                if (isChecked){}
                else {}
                break;
            case R.id.sw_STInternetTimeZone:
                if (isChecked){}
                else {}
                break;
            case R.id.sw_STComfirmTimeZone:
                if (isChecked){}
                else {}
                break;
        }
    }

    private void initValue()
    {
        list.add("伦敦");
        list.add("柏林");
        list.add("雅典");
        list.add("莫斯科");
        list.add("阿布扎比");
        list.add("伊斯兰堡");
        list.add("达卡");
        list.add("曼谷");
        list.add("北京");
        list.add("东京");
        list.add("堪培拉");
        list.add("霍尼亚拉");
        list.add("惠灵顿");
        list.add("蓬塔德尔加达");
        list.add("华盛顿");
        list.add("巴西利亚");
        list.add("加拉加斯");
        list.add("纽约");
        list.add("墨西哥城");
        list.add("盐湖城");
        list.add("洛杉矶");
        list.add("朱诺");
        list.add("檀香山");
        list.add("中途岛");
        list.add("马朱罗");

    }
}
