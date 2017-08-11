package com.goockr.nakedeyeguard.view.SettingPage;


import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.goockr.nakedeyeguard.Base.BaseActivity;
import com.goockr.nakedeyeguard.R;
import com.goockr.nakedeyeguard.Tools.PowerOffDialog;
import com.kyleduo.switchbutton.SwitchButton;
import com.weigan.loopview.LoopView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;

import static com.goockr.nakedeyeguard.Tools.App.alarmManager;
import static com.goockr.nakedeyeguard.Tools.App.editor;
import static com.goockr.nakedeyeguard.Tools.App.preferences;
import static com.goockr.nakedeyeguard.Tools.App.timeZoneMap;


public class SetTimeActivity extends BaseActivity implements View.OnClickListener,Switch.OnCheckedChangeListener{


    RelativeLayout rl_STDate;
    TextView tv_SetDate;
    RelativeLayout rl_STTime;
   // TextView tv_SetTime;
    RelativeLayout rl_STSelectTimeZone;
    TextView tv_SetTimeZone;

    SwitchButton sw_STInternetTime;
    SwitchButton sw_STInternetTimeZone;
    SwitchButton sw_STC24HourSystem;

    private Handler timeHandler;

    final ArrayList<String> list = new ArrayList<>();
    @Override
    protected int getLoyoutId() {
        return R.layout.activity_set_time;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        timeHandler = new Handler();
        initValue();
        setupUI();
        eventHandle();

    }


    private AlertDialog dialog;
    @Override
    public void popDialog() {

        PowerOffDialog.show(this,new PowerOffDialog.PowerOffDialogCallBack() {
            @Override
            public void powerOffCancle() {
                MainKeyEnable(true);
            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }


    private void setupUI()
    {
        rl_STDate=(RelativeLayout)findViewById(R.id.rl_STDate);
        tv_SetDate=(TextView)findViewById(R.id.tv_SetDate);

        rl_STTime=(RelativeLayout)findViewById(R.id.rl_STTime);
        //tv_SetTime=(TextView)findViewById(R.id.tv_SetTime);

        rl_STSelectTimeZone=(RelativeLayout)findViewById(R.id.rl_STSelectTimeZone);
        tv_SetTimeZone=(TextView)findViewById(R.id.tv_SetTimeZone);

        sw_STInternetTime=(SwitchButton)findViewById(R.id.sw_STInternetTime);
        sw_STInternetTimeZone=(SwitchButton)findViewById(R.id.sw_STInternetTimeZone);
        sw_STC24HourSystem=(SwitchButton)findViewById(R.id.sw_STC24HourSystem);

        rl_STDate.setOnClickListener(this);
        rl_STTime.setOnClickListener(this);
        rl_STSelectTimeZone.setOnClickListener(this);

        //sw_STInternetTime.setOnCheckedChangeListener(this);
        //sw_STInternetTimeZone.setOnCheckedChangeListener(this);
        sw_STC24HourSystem.setOnCheckedChangeListener(this);
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
                final DatePicker dp_SetDate = (DatePicker) dateView.findViewById(R.id.dp_SetDate);
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
                        setSysDate(dp_SetDate.getYear(),dp_SetDate.getMonth(),dp_SetDate.getDayOfMonth());
                        setTime();
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
               final TimePicker tp_TimePicker=(TimePicker)timeView.findViewById(R.id.tp_TimePicker);
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
                        setSysTime(tp_TimePicker.getCurrentHour(),tp_TimePicker.getCurrentMinute());
                        setTime();
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
//                loop_SetTimeZone.setListener(new OnItemSelectedListener() {
//                    @Override
//                    public void onItemSelected(int index) {
//                        Toast.makeText(SetTimeActivity.this, list.get(index), Toast.LENGTH_SHORT).show();
//                    }
//                });
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
                                alarmManager.setTimeZone((String)entry.getKey());
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
                Toast.makeText(SetTimeActivity.this,"sw_STInternetTime",Toast.LENGTH_SHORT).show();
                if (isChecked) isDateTimeAuto();
                break;
            case R.id.sw_STInternetTimeZone:
                Toast.makeText(SetTimeActivity.this,"sw_STInternetTimeZone",Toast.LENGTH_SHORT).show();
//                setAutoTimeZone(isChecked);
                getCurrentTimeZone();
                break;
            case R.id.sw_STC24HourSystem:
                set24HourSystem(isChecked);
                editor.putBoolean("24HourSystem",isChecked);
                editor.commit();
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
        final String  strDate  =  formatterDate.format(curDate);
        //final String  strTime  =  formatterTime.format(curDate);
        //final String timeZoneStr = preferences.getString("TimeZone",getString(R.string.北京));

        tv_SetDate.setText(strDate);
       // tv_SetTime.setText(strTime);
        tv_SetTimeZone.setText(getCurrentTimeZone() );

        final boolean hourSystem = preferences.getBoolean("24HourSystem",true);
        sw_STC24HourSystem.setChecked(hourSystem);
    }

    /**
     * 获取当前时区
     * @return
     */
    public static String getCurrentTimeZone() {
        TimeZone tz = TimeZone.getDefault();
        String strTz = tz.getDisplayName(false, TimeZone.SHORT)+" "+tz.getDisplayName();
        return strTz;

    }
    /**
     * 设置12-24小时制
     * @return
     */
    public void set24HourSystem(boolean checked){

        String numType="24";
        if (checked) numType="24";
        else numType="12";
        android.provider.Settings.System.putString(getContentResolver(),android.provider.Settings.System.TIME_12_24,numType);

    }



    public void setSysDate(int year,int month,int day){
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, month);
        c.set(Calendar.DAY_OF_MONTH, day);

        long now = c.getTimeInMillis();
        SystemClock.setCurrentTimeMillis(now);
    }
    public void setSysTime(int hour,int minute){

        Calendar c = Calendar.getInstance();
        c.set(Calendar.HOUR_OF_DAY, hour);
        c.set(Calendar.MINUTE, minute);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);

        long now = c.getTimeInMillis();
        SystemClock.setCurrentTimeMillis(now);
    }
    public void setTimeZone(String timeZone){
        final Calendar now = Calendar.getInstance();
        TimeZone tz = TimeZone.getTimeZone(timeZone);
        now.setTimeZone(tz);
    }
    public boolean isDateTimeAuto(){
        try {
            return android.provider.Settings.Global.getInt(getContentResolver(),
                    android.provider.Settings.Global.AUTO_TIME) > 0;
        } catch (Settings.SettingNotFoundException e) {
            e.printStackTrace();
            return false;
        }
    }
    public void setAutoDateTime(boolean checked){
        int numType=0;
        if (checked) numType=1;
        else numType=0;
        android.provider.Settings.Global.putInt(getContentResolver(),
                android.provider.Settings.Global.AUTO_TIME, numType);
    }
    public void setAutoTimeZone(boolean checked){
        String numType="1";
        if (checked) numType="1";
        else numType="0";
        try {

            String timeSettings = android.provider.Settings.System.getString(this.getContentResolver(),
                    android.provider.Settings.Global.AUTO_TIME);
            Settings.System.putInt(getContentResolver(),
                    Settings.System.AUTO_TIME,0);
            if (timeSettings.contentEquals("0")) {
                android.provider.Settings.System.putString(
                        this.getContentResolver(),
                        android.provider.Settings.Global.AUTO_TIME, "1");
            }else  android.provider.Settings.System.putString(
                    this.getContentResolver(),
                    android.provider.Settings.Global.AUTO_TIME, "0");
            Date now = new Date(System.currentTimeMillis());
            Log.e("Date====== ", now.toString());
//          int ii=  android.provider.Settings.Global.getInt(getContentResolver(), android.provider.Settings.Global.AUTO_TIME, 0);
//            Log.e("ii: ",String.valueOf(ii));
//                   android.provider.Settings.Global.putInt(getContentResolver(),android.provider.Settings.Global.AUTO_TIME, 0);
        }catch (Exception e)
        {
            Log.e("getMessage: ",e.getMessage());
        }

    }


}
