package com.goockr.nakedeyeguard.view.HealingProcessPage;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.goockr.nakedeyeguard.Base.BaseActivity;
import com.goockr.nakedeyeguard.widget.LockScreen.LockScreen;
import com.goockr.nakedeyeguard.widget.LockScreen.ScreenTouchReceiver;
import com.goockr.nakedeyeguard.data.Model.UserBean;
import com.goockr.nakedeyeguard.data.Model.UserDetailBean;
import com.goockr.nakedeyeguard.R;
import com.goockr.nakedeyeguard.Tools.ACache;
import com.goockr.nakedeyeguard.Tools.App;
import com.goockr.nakedeyeguard.Tools.Common;
import com.goockr.nakedeyeguard.Tools.PowerOffDialog;

public class HealingProcessActivity extends BaseActivity {

    private static final int lockDelayTime = 30000;

    public UserBean userBean;
    public UserDetailBean userInfo;
    RelativeLayout titleBar;

    private String TAG = "HealingProcessActivity";
    Animation operatingAnim;
    Handler handler;

    Handler lockHandler;

    @Override
    protected int getLoyoutId() {
        return R.layout.activity_healing_process;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        MainKeyEnable(false);
        super.onCreate(savedInstanceState);

        handler = new Handler();
        titleBar = (RelativeLayout) findViewById(R.id.rl_HeadingStateBar);
        userBean = getIntent().getParcelableExtra("UserBean");
        replaFragment(new HealingProcessFragment(), R.id.fl_HPFLayout);
        initReceiver();
        operatingAnim = AnimationUtils.loadAnimation(this, R.anim.tip);
        LinearInterpolator lin = new LinearInterpolator();
        operatingAnim.setInterpolator(lin);
        handlertouch();

    }

    @Override
    protected void onRestart() {
        super.onRestart();
        handlertouch();
    }

    /*********************************************暗屏***************************************************************/

    public static final String SCREENBRIGHTNESS = "screenbrightness";
    public static int DARKMIN = ScreenTouchReceiver.DARKMIN;
    private static int curMin = DARKMIN;
    private static boolean isRunning = false;
    private static boolean isCancle = false;
    private Thread timeThread = null;

    private Runnable timeRunnable = new Runnable() {
        @Override
        public void run() {
            while (isRunning) {
                curMin--;
                if (curMin == DARKMIN / 2) {
                    if (!isCancle) {
                        isRunning = false;
                        timeThread = null;
                        isCancle = false;
                        curMin = DARKMIN;
                        //设置屏幕变暗
                        //Settings.System.putInt(App.getContext().getContentResolver(), Settings.System.SCREEN_BRIGHTNESS, 0);


                        if (App.getInstances().isWorking) {
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    RlScreensaver.setVisibility(View.VISIBLE);
                                    titleBar.setVisibility(View.GONE);
                                    if (operatingAnim != null) {
                                        cp_ScrenSaver.startAnimation(operatingAnim);
                                    }

                                    lockHandler = new Handler();
                                    lockHandler.postDelayed(lockRunnable, lockDelayTime);

                                }
                            });
                        } else {
                            startActivity(new Intent(HealingProcessActivity.this, LockScreen.Controller.class));//20170607 改

                        }
                    }
                }
                if (curMin == 0) {
                    isRunning = false;
                    timeThread = null;
                    isCancle = false;
                    curMin = DARKMIN;
                }
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    };


    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        handlertouch();
        return super.dispatchTouchEvent(ev);
    }

    private void handlertouch() {
        if (isRunning == false) {
            isRunning = true;
            isCancle = false;
            curMin = DARKMIN;
            String curbrightness = ACache.get(App.getContext()).getAsString(SCREENBRIGHTNESS);
            if (curbrightness != null) {
                if (Integer.valueOf(curbrightness) > 255 || Integer.valueOf(curbrightness) < 0) {
                    curbrightness = Common.defaultLight;
                }
            } else {
                curbrightness = Common.defaultLight;
            }
            //点击调节亮度
            Settings.System.putInt(App.getContext().getContentResolver(), Settings.System.SCREEN_BRIGHTNESS, Integer.valueOf(curbrightness));
            timeThread = new Thread(timeRunnable);
            timeThread.start();
        } else {

            if (lockHandler != null) {
                lockHandler.removeCallbacks(lockRunnable);
                lockHandler = null;
            }

            curMin = DARKMIN;
            String curbrightness = ACache.get(App.getContext()).getAsString(SCREENBRIGHTNESS);
            if (curbrightness != null) {
                if (Integer.valueOf(curbrightness) > 255 || Integer.valueOf(curbrightness) < 0) {
                    curbrightness = Common.defaultLight;
                }
            } else {
                curbrightness = Common.defaultLight;
            }
            Settings.System.putInt(App.getContext().getContentResolver(), Settings.System.SCREEN_BRIGHTNESS, Integer.valueOf(curbrightness));
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        curMin = DARKMIN;
        isCancle = true;
        isRunning = false;
        String curbrightness = ACache.get(App.getContext()).getAsString(SCREENBRIGHTNESS);
        if (curbrightness != null) {
            if (Integer.valueOf(curbrightness) > 255 || Integer.valueOf(curbrightness) < 0) {
                curbrightness = Common.defaultLight;
            }
        } else {
            curbrightness = Common.defaultLight;
        }
        Settings.System.putInt(App.getContext().getContentResolver(), Settings.System.SCREEN_BRIGHTNESS, Integer.valueOf(curbrightness));
    }


    /*********************************************暗屏***************************************************************/

    private AlertDialog dialog;

    @Override
    public void popDialog() {
        sendBroadcast(new Intent(ScreenTouchReceiver.SCREENON));
        String tip = "";
        if (App.getInstances().isWorking) {
            tip = getResources().getString(R.string.power_off_isrun);
        } else {
            tip = getResources().getString(R.string.poweroff);
        }
        PowerOffDialog.show(this, tip, new PowerOffDialog.PowerOffDialogCallBack() {
            @Override
            public void powerOffCancle() {
                MainKeyEnable(true);
            }
        });

    }


    public void replaFragment( Fragment fragment, int Rid) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(Rid, fragment);
        transaction.commit();
    }

    private long firstpress = 0;


    boolean mainKeyEn = true;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (mainKeyEn) {
            if (event.getScanCode() == 158) {
                if (firstpress == 0) {
                    firstpress = System.currentTimeMillis();
                }
                if (System.currentTimeMillis() - firstpress > 2000) {
                    popDialog();
                    mainKeyEn = false;
                    firstpress = 0;
                }
            }
        }
        return false;
    }

    Runnable lockRunnable = new Runnable() {
        @Override
        public void run() {
            if (RlScreensaver.getVisibility() == View.VISIBLE) {
                startActivity(new Intent(HealingProcessActivity.this, LockScreen.Controller.class));

            }
        }
    };

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (mainKeyEn) {
            if (event.getScanCode() == 158) {

                boolean a = App.getInstances().isWorking;

                if (App.getInstances().isWorking == false) {

                    if (System.currentTimeMillis() - firstpress > 2000) {
                        return false;
                    } else {
//                    startActivity(new Intent(this,ScreensaverActivity.class));
                        firstpress = 0;
                        startActivity(new Intent(this, LockScreen.Controller.class));//调用系统锁屏
                        return false;
                    }
                }


                if (RlScreensaver.getVisibility() == View.GONE) {
                    RlScreensaver.setVisibility(View.VISIBLE);
                    cp_ScrenSaver.setVisibility(View.VISIBLE);
                    titleBar.setVisibility(View.GONE);
                    if (operatingAnim != null) {
                        cp_ScrenSaver.startAnimation(operatingAnim);

                        lockHandler = new Handler();
                        lockHandler.postDelayed(lockRunnable, lockDelayTime);

                    }
                } else {

                    if (operatingAnim != null) {
                        cp_ScrenSaver.clearAnimation();
                    }
                    RlScreensaver.setVisibility(View.GONE);
                    titleBar.setVisibility(View.VISIBLE);
                    if (lockHandler != null) {
                        lockHandler.removeCallbacks(lockRunnable);
                        lockHandler = null;
                    }
                    handlertouch();
                }
                firstpress = 0;
            }
        }
        return false;
    }


    View RlScreensaver;
    ImageView cp_ScrenSaver;

    /**
     * 屏保
     */

    private void initReceiver() {
        RlScreensaver = findViewById(R.id.screensaver);
        cp_ScrenSaver = (ImageView) findViewById(R.id.cp_ScrenSaver);
        RlScreensaver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }


}
