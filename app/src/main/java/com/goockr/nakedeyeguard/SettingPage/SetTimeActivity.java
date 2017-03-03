package com.goockr.nakedeyeguard.SettingPage;


import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.goockr.nakedeyeguard.Base.BaseActivity;
import com.goockr.nakedeyeguard.R;
import com.kyleduo.switchbutton.SwitchButton;
import com.weigan.loopview.LoopView;
import com.weigan.loopview.OnItemSelectedListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;
import java.util.Set;

import static com.goockr.nakedeyeguard.App.alarmManager;
import static com.goockr.nakedeyeguard.App.editor;
import static com.goockr.nakedeyeguard.App.preferences;
import static com.goockr.nakedeyeguard.App.timeZoneMap;


public class SetTimeActivity extends BaseActivity implements View.OnClickListener,Switch.OnCheckedChangeListener{


    RelativeLayout rl_STDate;
    TextView tv_SetDate;
    RelativeLayout rl_STTime;
    TextView tv_SetTime;
    RelativeLayout rl_STSelectTimeZone;
    TextView tv_SetTimeZone;

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
        tv_SetDate=(TextView)findViewById(R.id.tv_SetDate);

        rl_STTime=(RelativeLayout)findViewById(R.id.rl_STTime);
        tv_SetTime=(TextView)findViewById(R.id.tv_SetTime);

        rl_STSelectTimeZone=(RelativeLayout)findViewById(R.id.rl_STSelectTimeZone);
        tv_SetTimeZone=(TextView)findViewById(R.id.tv_SetTimeZone);


        sw_STInternetTime=(SwitchButton)findViewById(R.id.sw_STInternetTime);
        sw_STInternetTimeZone=(SwitchButton)findViewById(R.id.sw_STInternetTimeZone);
        sw_STComfirmTimeZone=(SwitchButton)findViewById(R.id.sw_STComfirmTimeZone);

        rl_STDate.setOnClickListener(this);
        rl_STTime.setOnClickListener(this);
        rl_STSelectTimeZone.setOnClickListener(this);

        sw_STInternetTime.setOnCheckedChangeListener(this);
        sw_STInternetTimeZone.setOnCheckedChangeListener(this);
        sw_STComfirmTimeZone.setOnCheckedChangeListener(this);
        setTime();
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
                TimePicker tp_TimePicker=(TimePicker)timeView.findViewById(R.id.tp_TimePicker);
                Button bt_SetTimeCancle = (Button) timeView.findViewById(R.id.bt_SetTimeCancle);
                Button bt_SetTimeSave = (Button) timeView.findViewById(R.id.bt_SetTimeSave);
                tp_TimePicker.setIs24HourView(true);
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
               final LoopView loop_SetTimeZone= (LoopView) timeZoneView.findViewById(R.id.loop_SetTimeZone);

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
                        String selectTimeZone = list.get(loop_SetTimeZone.getSelectedItem());

                        for(Map.Entry entry:(Set<Map.Entry>)timeZoneMap.entrySet()){
                            if(selectTimeZone.equals(entry.getValue()))
                            {
                                tv_SetTimeZone.setText(selectTimeZone);
                                alarmManager.setTimeZone((String)entry.getValue());
                                editor.putString("TimeZone",selectTimeZone);
                                editor.commit();
                                setTime();
                            }

                        }

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
        new Thread(new Runnable() {
            @Override
            public void run() {
                String[] values=new String[timeZoneMap.size()];
                timeZoneMap.values().toArray(values);
                for (String item:values) {
                    list.add(item);
                }
            }
        }).start();

    }

    private void setTime()
    {
        SimpleDateFormat formatterDate=new  SimpleDateFormat("yyyy/MM/dd");
        SimpleDateFormat formatterTime=new  SimpleDateFormat("HH:mm");
        Date curDate  =new   Date(System.currentTimeMillis());//获取当前时间
        String  strDate  =  formatterDate.format(curDate);
        tv_SetDate.setText(strDate);
        String  strTime  =  formatterTime.format(curDate);
        tv_SetTime.setText(strTime);
        String timeZoneKey = preferences.getString("TimeZone",getString(R.string.北京));
        String timeZoneStr =(String) timeZoneMap.get(timeZoneKey);
        tv_SetTimeZone.setText(timeZoneStr);
    }


}
