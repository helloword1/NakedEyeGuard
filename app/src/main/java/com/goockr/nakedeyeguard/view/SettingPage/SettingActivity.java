package com.goockr.nakedeyeguard.view.SettingPage;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.goockr.nakedeyeguard.Base.ActivityCollector;
import com.goockr.nakedeyeguard.Base.BaseActivity;
import com.goockr.nakedeyeguard.R;
import com.goockr.nakedeyeguard.Tools.ACache;
import com.goockr.nakedeyeguard.Tools.App;
import com.goockr.nakedeyeguard.Tools.Common;
import com.goockr.nakedeyeguard.Tools.NetUtils;
import com.goockr.nakedeyeguard.net.Http.HttpHelper;
import com.goockr.nakedeyeguard.view.FirstUsePage.FirstActivty;
import com.goockr.nakedeyeguard.view.SettingPage.WifiPage.WifiActivity;
import com.goockr.nakedeyeguard.widget.LockScreen.ScreenTouchReceiver;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.weigan.loopview.LoopView;
import com.xw.repo.BubbleSeekBar;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;

import static com.goockr.nakedeyeguard.Tools.App.preferences;
import static com.goockr.nakedeyeguard.Tools.App.restTimeList;

/**
 * 设置
 */
public class SettingActivity extends BaseActivity implements View.OnClickListener {


    RelativeLayout rl_SetWlan;
    RelativeLayout rl_SetVoice;
    RelativeLayout rl_SetBrightness;
    RelativeLayout rl_SetTime;
    RelativeLayout rl_SetScreensaver;
    RelativeLayout rl_SetSystemUpgrade;
    RelativeLayout rl_SetReset;
    RelativeLayout rl_SetAbout;

    private ImageView iv_MainBatteryChangring;
    private Handler timeHandler;

    TextView tv_RestTime;

    TextView dateAndtime_tv;

    public static SharedPreferences.Editor editor;
    private TextView tvVersionName;

    @Override
    protected int getLoyoutId() {
        return R.layout.activity_setting;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        timeHandler = new Handler();
        setupUI();
        eventHandle();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void setupUI() {
        rl_SetWlan = (RelativeLayout) findViewById(R.id.rl_SetWlan);
        rl_SetVoice = (RelativeLayout) findViewById(R.id.rl_SetVoice);
        rl_SetBrightness = (RelativeLayout) findViewById(R.id.rl_SetBrightness);
        rl_SetTime = (RelativeLayout) findViewById(R.id.rl_SetTime);
        rl_SetScreensaver = (RelativeLayout) findViewById(R.id.rl_SetScreensaver);
        rl_SetSystemUpgrade = (RelativeLayout) findViewById(R.id.rl_SetSystemUpgrade);
        rl_SetReset = (RelativeLayout) findViewById(R.id.rl_SetReset);
        rl_SetAbout = (RelativeLayout) findViewById(R.id.rl_SetAbout);
        tvVersionName = (TextView) findViewById(R.id.tvVersionName);

        String resetTime = preferences.getString("RestTime", "30秒");
        tv_RestTime = (TextView) findViewById(R.id.tv_RestTime);
        tv_RestTime.setText(ACache.get(App.getContext()).getAsString(ScreenTouchReceiver.SCREENDARKTIME));

        rl_SetWlan.setOnClickListener(this);
        rl_SetVoice.setOnClickListener(this);
        rl_SetBrightness.setOnClickListener(this);
        rl_SetTime.setOnClickListener(this);
        rl_SetScreensaver.setOnClickListener(this);
        rl_SetSystemUpgrade.setOnClickListener(this);
        rl_SetReset.setOnClickListener(this);
        rl_SetAbout.setOnClickListener(this);

        iv_MainBatteryChangring = (ImageView) findViewById(R.id.iv_MainBatteryChangring);

        dateAndtime_tv = (TextView) findViewById(R.id.activity_set_dateandtime_tv);
        dateAndtime_tv.setText(getCurrentDate());

    }

    private void eventHandle() {
        PackageManager manager = this.getPackageManager();
        String versionName = null;
        try {
            PackageInfo info = manager.getPackageInfo(this.getPackageName(), 0);
            versionName = info.versionName; // 版本名
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        tvVersionName.setText("V"+versionName);
        getBackBtn().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_SetWlan:
                NetUtils.openWifi();
                Intent wifiAbout = new Intent(SettingActivity.this, WifiActivity.class);
                startActivity(wifiAbout);
                overridePendingTransition(R.anim.fragment_slide_right_in, R.anim.fragment_slide_left_out);
//                Intent intent = new Intent(Settings.ACTION_WIFI_SETTINGS);
//                startActivity(intent);
                break;
            case R.id.rl_SetVoice:
                final MaterialDialog voiceDialog = new MaterialDialog.Builder(this)
                        .customView(R.layout.set_voice_dialog, true)
                        .show();
                voiceDialog.setCanceledOnTouchOutside(false);
                View voiceView = voiceDialog.getCustomView();

                final BubbleSeekBar bsb_SetVoice = (BubbleSeekBar) voiceView.findViewById(R.id.bsb_SetVoice);
                Button bt_SetVoiceCancle = (Button) voiceView.findViewById(R.id.bt_SetVoiceCancle);
                Button bt_SetVoiceSave = (Button) voiceView.findViewById(R.id.bt_SetVoiceSave);

                ImageButton min_im = (ImageButton) voiceView.findViewById(R.id.volume_min_im);
                min_im.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int Progress = bsb_SetVoice.getProgress();
                        if (Progress <= 0) return;
                        else if (Progress <= 1) {
                            bsb_SetVoice.setProgress(0);
                        } else if (Progress > 1) {
                            bsb_SetVoice.setProgress(Progress - 1);
                        }
                    }
                });

                ImageButton add_im = (ImageButton) voiceView.findViewById(R.id.volume_add_im);
                add_im.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        int Progress = bsb_SetVoice.getProgress();
                        if (Progress >= 7) return;
                        else if (Progress > 6) {
                            bsb_SetVoice.setProgress(7);
                        } else if (Progress < 7) {
                            bsb_SetVoice.setProgress(Progress + 1);
                        }

                    }
                });


                final AudioManager mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
                int current = mAudioManager.getStreamVolume(AudioManager.STREAM_SYSTEM);
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

                        mAudioManager.setStreamVolume(AudioManager.STREAM_SYSTEM, bsb_SetVoice.getProgress(), 0);
                        voiceDialog.dismiss();
                    }
                });
                break;
            case R.id.rl_SetBrightness:
                final MaterialDialog brightnessdialog = new MaterialDialog.Builder(this)
                        .customView(R.layout.set_brightness_dialog, true)
                        .show();
                brightnessdialog.setCanceledOnTouchOutside(false);
                View brightnessView = brightnessdialog.getCustomView();
                // 设置系统亮度
                final BubbleSeekBar bsb_SetBrightness = (BubbleSeekBar) brightnessView.findViewById(R.id.bsb_SetBrightness);

                Button bt_SetBrightnessCancle = (Button) brightnessView.findViewById(R.id.bt_SetBrightnessCancle);
                Button bt_SetBrightnessSave = (Button) brightnessView.findViewById(R.id.bt_SetBrightnessSave);
                brightnessView.findViewById(R.id.light_low).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int Progress = bsb_SetBrightness.getProgress();
                        if (Progress <= 1) return;
                        else if (Progress <= 10) {
                            bsb_SetBrightness.setProgress(1);
                        } else if (Progress > 10) {
                            bsb_SetBrightness.setProgress(Progress - 10);
                        }
                    }
                });


                brightnessView.findViewById(R.id.light_high).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        int Progress = bsb_SetBrightness.getProgress();
                        if (Progress >= 150) return;
                        else if (Progress > 140) {
                            bsb_SetBrightness.setProgress(150);
                        } else if (Progress < 150) {
                            bsb_SetBrightness.setProgress(Progress + 10);
                        }
                    }
                });

                try {
                    //int value = Settings.System.getInt(getContentResolver(),Settings.System.SCREEN_BRIGHTNESS);
                    int value = (int) ((Settings.System.getInt(getContentResolver(), Settings.System.SCREEN_BRIGHTNESS) - 90) / 0.6);
                    bsb_SetBrightness.setProgress(value);
                } catch (Settings.SettingNotFoundException e) {
                }

                bt_SetBrightnessCancle.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        brightnessdialog.dismiss();
                    }
                });
                bt_SetBrightnessSave.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                        editor.putInt("Brightness",bsb_SetBrightness.getProgress());
//                        editor.commit();

//                        Settings.System.putInt(getContentResolver(), Settings.System.SCREEN_BRIGHTNESS,bsb_SetBrightness.getProgress());
//                        ACache.get(App.getContext()).put(ScreenTouchReceiver.SCREENBRIGHTNESS,String.valueOf(bsb_SetBrightness.getProgress()));
                        int light = (int) ((bsb_SetBrightness.getProgress()) * 0.6 + 90);
                        Settings.System.putInt(getContentResolver(), Settings.System.SCREEN_BRIGHTNESS, light);
                        ACache.get(App.getContext()).put(ScreenTouchReceiver.SCREENBRIGHTNESS, String.valueOf(light));

                        brightnessdialog.dismiss();
                    }
                });
                break;
            case R.id.rl_SetTime:
                dateAndtime_tv.setText(getCurrentDate());
//                final Intent intentTime=new Intent(SettingActivity.this,SetTimeActivity.class);
//                startActivity(intentTime);
//                overridePendingTransition(R.anim.fragment_slide_right_in, R.anim.fragment_slide_left_out);
                break;
            case R.id.rl_SetScreensaver:
                final MaterialDialog screensaverDialog = new MaterialDialog.Builder(this)
                        .customView(R.layout.set_screensaver_dialog, true)
                        .show();
                screensaverDialog.setCanceledOnTouchOutside(false);
                View screensaverView = screensaverDialog.getCustomView();
                final LoopView loop_SetScreenSaverTime = (LoopView) screensaverView.findViewById(R.id.loop_SetScreenSaverTime);

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
                        String restTime = restTimeList.get(loop_SetScreenSaverTime.getSelectedItem());
                        ACache.get(App.getContext()).put(ScreenTouchReceiver.SCREENDARKTIME, restTime);
                        tv_RestTime.setText(restTime);
                        if (restTime.equals("20秒")) {
                            ScreenTouchReceiver.DARKMIN = 20;
                        } else if (restTime.equals("30秒")) {
                            ScreenTouchReceiver.DARKMIN = 30;
                        } else if (restTime.equals("40秒")) {
                            ScreenTouchReceiver.DARKMIN = 40;
                        } else if (restTime.equals("50秒")) {
                            ScreenTouchReceiver.DARKMIN = 50;
                        }
                        screensaverDialog.dismiss();
                    }
                });
                break;
            case R.id.rl_SetSystemUpgrade://系统升级
                boolean isNewVersion = false;
                if (isNewVersion) {//有新版本
                    KProgressHUD upgradeHUDhud = KProgressHUD.create(SettingActivity.this)
                            .setStyle(KProgressHUD.Style.BAR_DETERMINATE)
                            .setLabel("请稍等设备版本升级中...");
                    simulateProgressUpdate(upgradeHUDhud);
                    upgradeHUDhud.show();
                } else //没有新版本
                {
                    TextView tv_Reset = new TextView(SettingActivity.this);
                    tv_Reset.setText("当前系统已是最新版本！");
                    tv_Reset.setTextColor(Color.WHITE);
                    tv_Reset.setTextSize(24);
                    final KProgressHUD restHUD = KProgressHUD.create(SettingActivity.this)
                            .setCustomView(tv_Reset)
                            .show();
                    Common.scheduleDismiss(restHUD);
                }

                break;
            case R.id.rl_SetReset://恢复出厂设置
                App.editor.putBoolean("PROGRESSBAR",false);
                pbView.setVisibility(View.GONE);
                final MaterialDialog resetDialog = new MaterialDialog.Builder(this)
                        .customView(R.layout.set_reset_dialog, true)
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
                        final Map<String, String> map = new HashMap<>();
                        map.put("c_pad_id", HttpHelper.deviceId);
                        HttpHelper.httpPost(HttpHelper.getReset(), map, new StringCallback() {
                            @Override
                            public void onError(Call call, Exception e, int id) {
                                timeHandler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(SettingActivity.this, "请检查网络", Toast.LENGTH_SHORT).show();
                                        resetDialog.dismiss();
                                    }
                                });
                            }

                            @Override
                            public void onResponse(String response, int id) {
                                OkHttpUtils.getInstance().cancelTag(this);
                                final String result = response;
                                Log.i("setting", result);

                                timeHandler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        resetDialog.dismiss();
                                        if (result.split(":")[1].indexOf("success") >= 0) {
                                            TextView tv_Reset = new TextView(SettingActivity.this);
                                            tv_Reset.setText(R.string.Factory_Reset);
                                            tv_Reset.setTextColor(Color.WHITE);
                                            tv_Reset.setTextSize(24);
                                            final KProgressHUD restHUD = KProgressHUD.create(SettingActivity.this)
                                                    .setCustomView(tv_Reset)
                                                    .show();
                                            new Handler().postDelayed(new Runnable() {
                                                @Override
                                                public void run() {
                                                    preferences = getSharedPreferences("preferences", MODE_PRIVATE);
                                                    editor = preferences.edit();
                                                    restHUD.dismiss();
                                                    editor.putBoolean("FirstUser", true);
                                                    editor.putString("TimeZone", "北京");
                                                    editor.putBoolean("24HourSystem", true);
                                                    editor.commit();
                                                    ActivityCollector.finishAll();
                                                    Intent startIntent = new Intent(SettingActivity.this, FirstActivty.class);
                                                    startActivity(startIntent);
                                                }
                                            }, 2000);
                                        } else {
                                            Toast.makeText(SettingActivity.this, "删除失败", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                            }
                        });


                    }
                });
                break;
            case R.id.rl_SetAbout://关于
                Intent intentAbout = new Intent(SettingActivity.this, AboutActivity.class);
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


    private String getCurrentDate() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy年MM月dd日");
        Date curDate = new Date(System.currentTimeMillis());

        return formatter.format(curDate);
    }

}
