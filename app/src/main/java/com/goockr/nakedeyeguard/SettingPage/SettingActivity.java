package com.goockr.nakedeyeguard.SettingPage;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.goockr.nakedeyeguard.Base.BaseActivity;
import com.goockr.nakedeyeguard.R;
import com.goockr.nakedeyeguard.SettingPage.WifiPage.WifiActivity;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.weigan.loopview.LoopView;
import com.weigan.loopview.OnItemSelectedListener;
import com.xw.repo.BubbleSeekBar;

import java.util.ArrayList;

import static com.goockr.nakedeyeguard.Tools.Common.scheduleDismiss;

public class SettingActivity extends BaseActivity implements View.OnClickListener{

    RelativeLayout rl_SetWlan;
    RelativeLayout rl_SetVoice;
    RelativeLayout rl_SetBrightness;
    RelativeLayout rl_SetTime;
    RelativeLayout rl_SetScreensaver;
    RelativeLayout rl_SetSystemUpgrade;
    RelativeLayout rl_SetReset;
    RelativeLayout rl_SetAbout;

    @Override
    protected int getLoyoutId() {return R.layout.activity_setting;}

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupUI();
        eventHandle();
    }

    private void setupUI() {
        rl_SetWlan=(RelativeLayout)findViewById(R.id.rl_SetWlan);
        rl_SetVoice=(RelativeLayout)findViewById(R.id.rl_SetVoice);
        rl_SetBrightness=(RelativeLayout)findViewById(R.id.rl_SetBrightness);
        rl_SetTime=(RelativeLayout)findViewById(R.id.rl_SetTime);
        rl_SetScreensaver=(RelativeLayout)findViewById(R.id.rl_SetScreensaver);
        rl_SetSystemUpgrade=(RelativeLayout)findViewById(R.id.rl_SetSystemUpgrade);
        rl_SetReset=(RelativeLayout)findViewById(R.id.rl_SetReset);
        rl_SetAbout=(RelativeLayout)findViewById(R.id.rl_SetAbout);

        rl_SetWlan.setOnClickListener(this);
        rl_SetVoice.setOnClickListener(this);
        rl_SetBrightness.setOnClickListener(this);
        rl_SetTime.setOnClickListener(this);
        rl_SetScreensaver.setOnClickListener(this);
        rl_SetSystemUpgrade.setOnClickListener(this);
        rl_SetReset.setOnClickListener(this);
        rl_SetAbout.setOnClickListener(this);
    }
    private void eventHandle()
    {
        getBackBtn().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.rl_SetWlan:
                Intent wifiAbout=new Intent(SettingActivity.this,WifiActivity.class);
                startActivity(wifiAbout);
                overridePendingTransition(R.anim.fragment_slide_right_in, R.anim.fragment_slide_left_out);
                break;
            case R.id.rl_SetVoice:
                final MaterialDialog voiceDialog =  new MaterialDialog.Builder(this)
                        .customView(R.layout.set_voice_dialog,true)
                        .show();
                voiceDialog.setCanceledOnTouchOutside(false);
                View voiceView = voiceDialog.getCustomView();
                final BubbleSeekBar bsb_SetVoice = (BubbleSeekBar) voiceView.findViewById(R.id.bsb_SetVoice);
                Button bt_SetVoiceCancle = (Button) voiceView.findViewById(R.id.bt_SetVoiceCancle);
                Button bt_SetVoiceSave = (Button) voiceView.findViewById(R.id.bt_SetVoiceSave);
                bt_SetVoiceCancle.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        voiceDialog.dismiss();
                    }
                });
                bt_SetVoiceSave.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(SettingActivity.this,String.valueOf(bsb_SetVoice.getProgress()),Toast.LENGTH_SHORT).show();
                        voiceDialog.dismiss();
                    }
                });
                break;
            case R.id.rl_SetBrightness:
                final MaterialDialog brightnessdialog =  new MaterialDialog.Builder(this)
                        .customView(R.layout.set_brightness_dialog,true)
                        .show();
                brightnessdialog.setCanceledOnTouchOutside(false);
                View brightnessView = brightnessdialog.getCustomView();
                final BubbleSeekBar bsb_SetBrightness = (BubbleSeekBar) brightnessView.findViewById(R.id.bsb_SetBrightness);
                Button bt_SetBrightnessCancle = (Button) brightnessView.findViewById(R.id.bt_SetBrightnessCancle);
                Button bt_SetBrightnessSave = (Button) brightnessView.findViewById(R.id.bt_SetBrightnessSave);
                bt_SetBrightnessCancle.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        brightnessdialog.dismiss();
                    }
                });
                bt_SetBrightnessSave.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(SettingActivity.this,String.valueOf(bsb_SetBrightness.getProgress()),Toast.LENGTH_SHORT).show();
                        brightnessdialog.dismiss();
                    }
                });
                break;
            case R.id.rl_SetTime:
                Intent intentTime=new Intent(SettingActivity.this,SetTimeActivity.class);
                startActivity(intentTime);
                overridePendingTransition(R.anim.fragment_slide_right_in, R.anim.fragment_slide_left_out);
                break;
            case R.id.rl_SetScreensaver:
                final MaterialDialog screensaverDialog =  new MaterialDialog.Builder(this)
                        .customView(R.layout.set_screensaver_dialog,true)
                        .show();
                screensaverDialog.setCanceledOnTouchOutside(false);
                View screensaverView = screensaverDialog.getCustomView();
                LoopView loop_SetScreenSaverTime= (LoopView) screensaverView.findViewById(R.id.loop_SetScreenSaverTime);
               final ArrayList<String> list = new ArrayList<>();
                list.add("10秒");
                list.add("30秒");
                list.add("1分钟");
                list.add("3分钟");
                list.add("5分钟");
                list.add("10分钟");
                list.add("30分钟");
                list.add("1小时");
                list.add("从不");
                //设置是否循环播放
                loop_SetScreenSaverTime.setNotLoop();
                //滚动监听
                loop_SetScreenSaverTime.setListener(new OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(int index) {
                        Toast.makeText(SettingActivity.this, list.get(index), Toast.LENGTH_SHORT).show();
                    }
                });
                //设置原始数据
                loop_SetScreenSaverTime.setItems(list);

                Button bt_SetScreensaverCancle = (Button) screensaverView.findViewById(R.id.bt_SetScreensaverCancle);
                Button bt_SetScreensaverSave = (Button) screensaverView.findViewById(R.id.bt_SetScreensaverSave);
                bt_SetScreensaverCancle.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        screensaverDialog.dismiss();
                    }
                });
                bt_SetScreensaverSave.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        screensaverDialog.dismiss();
                    }
                });
                break;
            case R.id.rl_SetSystemUpgrade:
                 KProgressHUD upgradeHUDhud = KProgressHUD.create(SettingActivity.this)
                        .setStyle(KProgressHUD.Style.BAR_DETERMINATE)
                        .setLabel("请稍等设备版本升级中...");
                simulateProgressUpdate(upgradeHUDhud);
                upgradeHUDhud.show();
                break;
            case R.id.rl_SetReset:
                final MaterialDialog resetDialog =  new MaterialDialog.Builder(this)
                        .customView(R.layout.set_reset_dialog,true)
                        .show();
                resetDialog.setCanceledOnTouchOutside(false);
                View resetView = resetDialog.getCustomView();
                Button bt_SetResetCancle = (Button) resetView.findViewById(R.id.bt_SetResetCancle);
                Button bt_SetResetSure = (Button) resetView.findViewById(R.id.bt_SetResetSure);
                bt_SetResetCancle.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        resetDialog.dismiss();
                    }
                });
                bt_SetResetSure.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        resetDialog.dismiss();
                        TextView tv_Reset = new TextView(SettingActivity.this);
                        tv_Reset.setText("设备已恢复出厂设置！");
                        tv_Reset.setTextColor(Color.WHITE);
                        tv_Reset.setTextSize(18);
                        KProgressHUD  restHUD= KProgressHUD.create(SettingActivity.this)
                                .setCustomView(tv_Reset)
                                .show();
                        scheduleDismiss(restHUD);
                    }
                });
                break;
            case R.id.rl_SetAbout:
                Intent intentAbout=new Intent(SettingActivity.this,AboutActivity.class);
                startActivity(intentAbout);
                overridePendingTransition(R.anim.fragment_slide_right_in, R.anim.fragment_slide_left_out);
                break;
        }
    }


    private void simulateProgressUpdate(final KProgressHUD hud) {
        hud.setMaxProgress(100);
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            int currentProgress;
            @Override
            public void run() {
                currentProgress += 1;
                hud.setProgress(currentProgress);
                if (currentProgress == 90) {
                    hud.setLabel("设备版本已升级至V2.1.0");
                }
                if (currentProgress < 100) {
                    handler.postDelayed(this, 50);
                }
            }
        }, 200);
    }

}
