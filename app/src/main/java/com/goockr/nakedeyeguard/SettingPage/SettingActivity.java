package com.goockr.nakedeyeguard.SettingPage;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.goockr.nakedeyeguard.Base.BaseActivity;
import com.goockr.nakedeyeguard.R;
import com.goockr.nakedeyeguard.SettingPage.WifiPage.WifiActivity;
import com.goockr.nakedeyeguard.StartActivity;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.weigan.loopview.LoopView;
import com.xw.repo.BubbleSeekBar;

import static com.goockr.nakedeyeguard.App.editor;
import static com.goockr.nakedeyeguard.App.preferences;
import static com.goockr.nakedeyeguard.App.restTimeList;

public class SettingActivity extends BaseActivity implements View.OnClickListener{

    RelativeLayout rl_SetWlan;
    RelativeLayout rl_SetVoice;
    RelativeLayout rl_SetBrightness;
    RelativeLayout rl_SetTime;
    RelativeLayout rl_SetScreensaver;
    RelativeLayout rl_SetSystemUpgrade;
    RelativeLayout rl_SetReset;
    RelativeLayout rl_SetAbout;

    TextView tv_RestTime;

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

        String resetTime = preferences.getString("RestTime","30秒");
        tv_RestTime=(TextView)findViewById(R.id.tv_RestTime);
        tv_RestTime.setText(resetTime);

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
                final AudioManager mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
                int current = mAudioManager.getStreamVolume( AudioManager.STREAM_SYSTEM );
                bsb_SetVoice.setProgress(current);

                bt_SetVoiceCancle.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        voiceDialog.dismiss();
                    }
                });
                bt_SetVoiceSave.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        mAudioManager.setStreamVolume(AudioManager.STREAM_SYSTEM,bsb_SetVoice.getProgress(),0);
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
                // 设置系统亮度
                final BubbleSeekBar bsb_SetBrightness = (BubbleSeekBar) brightnessView.findViewById(R.id.bsb_SetBrightness);
                Button bt_SetBrightnessCancle = (Button) brightnessView.findViewById(R.id.bt_SetBrightnessCancle);
                Button bt_SetBrightnessSave = (Button) brightnessView.findViewById(R.id.bt_SetBrightnessSave);

                bsb_SetBrightness.setProgress(preferences.getInt("Brightness",100));
                bt_SetBrightnessCancle.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        brightnessdialog.dismiss();
                    }
                });
                bt_SetBrightnessSave.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        editor.putInt("Brightness",bsb_SetBrightness.getProgress());
                        editor.commit();
                        Settings.System.putInt(getContentResolver(), Settings.System.SCREEN_BRIGHTNESS,bsb_SetBrightness.getProgress());
                        brightnessdialog.dismiss();
                    }
                });
                break;
            case R.id.rl_SetTime:
                final Intent intentTime=new Intent(SettingActivity.this,SetTimeActivity.class);
                startActivity(intentTime);
                overridePendingTransition(R.anim.fragment_slide_right_in, R.anim.fragment_slide_left_out);
                break;
            case R.id.rl_SetScreensaver:
                final MaterialDialog screensaverDialog =  new MaterialDialog.Builder(this)
                        .customView(R.layout.set_screensaver_dialog,true)
                        .show();
                screensaverDialog.setCanceledOnTouchOutside(false);
                View screensaverView = screensaverDialog.getCustomView();
                final LoopView loop_SetScreenSaverTime= (LoopView) screensaverView.findViewById(R.id.loop_SetScreenSaverTime);

                //设置是否循环播放
                loop_SetScreenSaverTime.setNotLoop();

                //设置原始数据
                loop_SetScreenSaverTime.setItems(restTimeList);

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
                        String restTime =restTimeList.get(loop_SetScreenSaverTime.getSelectedItem());
                        tv_RestTime.setText(restTime);
                        editor.putString("RestTime",restTime);
                        editor.commit();
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
                        final KProgressHUD  restHUD= KProgressHUD.create(SettingActivity.this)
                                .setCustomView(tv_Reset)
                                .show();
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                restHUD.dismiss();
                                Intent startIntent=new Intent(SettingActivity.this, StartActivity.class);
                                startActivity(startIntent);
                            }
                        }, 2000);
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
