package com.goockr.nakedeyeguard.Tools;

import android.app.AlarmManager;
import android.app.Application;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import com.goockr.nakedeyeguard.view.HealingProcessPage.CourseOfTreatmentFragment;
import com.goockr.nakedeyeguard.net.Http.HttpHelper;
import com.goockr.nakedeyeguard.widget.LockScreen.ScreenTouchReceiver;
import com.goockr.nakedeyeguard.view.Music.MusicInfoBean;
import com.goockr.nakedeyeguard.view.Music.MusicService;
import com.goockr.nakedeyeguard.view.Music.OnPlayerListener;
import com.goockr.nakedeyeguard.R;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;

import android_serialport_api.SerialUtilOld;

/**
 * Created by JJT-ssd on 2017/3/2.
 */

public class App extends Application implements OnPlayerListener, Observer {

    private static final String TAG = "MyApplication";

    //创建串口通讯类
    private String path = "/dev/ttyS2";
    private int baudrate = 9600;
    private SerialUtilOld serialUtilOld;
    public SerialPortHelper portHelper;

    //获取数据初始设置
    public static SharedPreferences preferences;
    public static SharedPreferences.Editor editor;
    //时区
    public static AlarmManager alarmManager;
    public static Map timeZoneMap = new HashMap();
    public static ArrayList<String> restTimeList = new ArrayList<>();

    public static boolean isStartScreen = true;
    public static boolean interruptScreen = true;
    //
    public static boolean networkState = false;
    public static WifiHelper wifiHelper;

    public static Drawable iconDrawable = null;

    //正在理疗
    public boolean isWorking = false;

    private static Context context;

    public static Context getContext() {
        return context;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        Log.v(TAG, "ApponCreate");

        alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        alarmManager.setTimeZone("GMT+08:00");

        //获取数据初始设置
        initValue();
        context = getApplicationContext();
        instances = this;
        initWifi();
        initDeviceID();

        //sendMessageToPort();

        //设置异常捕获
        // initAppException();

        startMusicThreat();

        //创建默认的ImageLoader配置参数
        ImageLoaderConfiguration configuration = ImageLoaderConfiguration
                .createDefault(this);

        //Initialize ImageLoader with configuration.
        ImageLoader.getInstance().init(configuration);
    }

    //需要延时执行
    private void initState() {

        //  ACache.get(this).getAsString(ScreenTouchReceiver.SCREENBRIGHTNESS,"");
        String light = ACache.get(this).getAsString(ScreenTouchReceiver.SCREENBRIGHTNESS);
        light = "";

    }


    private void startMusicThreat() {

        connectToMusicService();
        musicList();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                if (musicService != null) musicService.play(0);

            }
        }, 2000);


        TreatmentEvent.getInstance().addObserver(this);

    }

    private void initAppException() {
        AppException appException = AppException.getInstance();
        appException.init(instances);
    }

    @Override
    public void onTerminate() {
        // 程序终止的时候执行
        Log.v(TAG, "onTerminate");
        super.onTerminate();
    }

    @Override
    public void onLowMemory() {
        // 低内存的时候执行
        Log.v(TAG, "onLowMemory");
        super.onLowMemory();
    }

    @Override
    public void onTrimMemory(int level) {
        // 程序在内存清理的时候执行
        Log.v(TAG, "onTrimMemory");
        super.onTrimMemory(level);
    }


    private void sendMessageToPort() {

        new Thread(heartRunable).start();

    }

    /**
     * 提取CPU ID作为deviceID
     */

    private void initDeviceID() {
        InputStream is = null;
        try {
            is = new FileInputStream("/proc/cpuinfo");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        //new一个StringBuffer用于字符串拼接
        StringBuffer sb = new StringBuffer();
        String line = null;
        try {
            //当输入流内容读取完毕时
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
            //记得关闭流数据 节约内存消耗
            is.close();
            reader.close();
            String result = sb.toString();
            result = result.split("Serial")[1].split(":")[1].trim();
            Log.i("setting", "." + result + ".");
            HttpHelper.deviceId = result;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 初始化WIFI
     */

    private void initWifi() {
        NetUtils.openWifi();
    }

    private void initValue() {
        //串口调试
        serialUtilOld = new SerialUtilOld(path, baudrate, 0);
        portHelper = new SerialPortHelper(serialUtilOld);

        preferences = getSharedPreferences("preferences", MODE_PRIVATE);
        editor = preferences.edit();

        wifiHelper = new WifiHelper(getApplicationContext());
        HttpHelper.httpInits();


        //android.provider.Settings.System.putString(getContentResolver(),android.provider.Settings.System.TIME_12_24, "24");
        // final String timeZoneStr = preferences.getString("TimeZone","北京");
        //设置默认时区 中国标准时间 (北京)
        final boolean hourSystem = preferences.getBoolean("24HourSystem", true);

        new Thread(new Runnable() {
            @Override
            public void run() {
                timeZoneMap.put(getString(R.string.北京), "北京");
                timeZoneMap.put(getString(R.string.东京), "东京");
                timeZoneMap.put(getString(R.string.东加塔布), "东加塔布");
                timeZoneMap.put(getString(R.string.中途岛), "中途岛");
                timeZoneMap.put(getString(R.string.丹佛), "丹佛");


                //屏保功能
                restTimeList.add("20秒");
                restTimeList.add("30秒");
                restTimeList.add("40秒");
                restTimeList.add("50秒");
                if (ACache.get(getContext()).getAsString(ScreenTouchReceiver.SCREENDARKTIME) == null) {
                    ACache.get(getContext()).put(ScreenTouchReceiver.SCREENDARKTIME, "40秒");
                    ScreenTouchReceiver.DARKMIN = 40;
                } else {
                    String time = ACache.get(getContext()).getAsString(ScreenTouchReceiver.SCREENDARKTIME);
                    if (time.equals("20秒")) {
                        ScreenTouchReceiver.DARKMIN = 20;
                    } else if (time.equals("30秒")) {
                        ScreenTouchReceiver.DARKMIN = 30;
                    } else if (time.equals("40秒")) {
                        ScreenTouchReceiver.DARKMIN = 40;
                    } else if (time.equals("50秒")) {
                        ScreenTouchReceiver.DARKMIN = 50;
                    }
                }
                String restTime = preferences.getString("RestTime", "从不");
                editor.putString("RestTime", restTime);

                editor.putBoolean("24HourSystem", hourSystem);
                editor.commit();

            }
        }).start();

    }

    public static App instances;

    public static App getInstances() {
        return instances;
    }


    Runnable heartRunable = new Runnable() {
        @Override
        public void run() {

//            while (true){
//
//                portHelper.sendData(Common.Heart);
//               // Thread.sleep(500);
//                try {
//                    Thread.sleep(500);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//
//            }

            for (int i = 0; i < 3; i++) {
                portHelper.sendData(Common.Heart);
                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        }
    };


    //******************设置音频播放线程****************************//

    public static MusicService musicService;

    private void musicList() {
        MusicInfoBean musicInfoBean = new MusicInfoBean();
        musicInfoBean.setId(0);
        musicInfoBean.setTitle("");
        musicInfoBean.setAlbumId(0);
        musicInfoBean.setDuration(0);
//               musicInfoBean.setSize(439659);
//               musicInfoBean.setUrl("/storage/emulated/0/Music/wusheng.mp3");

        // Uri uri = Uri.parse("android.resource://com.example.myapp/" + R.raw.my_resource);
        musicInfoBean.setSize(16128);
        //  musicInfoBean.setUrl("/storage/emulated/0/music/世间始终你好.mp3");
//               android.resource://" + getPackageName() + "/" + R.raw.test

        String path = Common.musicPath;

        ///system/media/wusheng.mp3
//        musicInfoBean.setUrl("/system/media/audio/alarms"+"/Alarm_Beep_01.ogg");
        musicInfoBean.setUrl("system/media/wusheng.mp3");
        musicService.musicInfoList.add(musicInfoBean);
    }

    private void connectToMusicService() {

        Intent intent = new Intent(this, MusicService.class);
        this.bindService(intent, serviceConnection, this.BIND_AUTO_CREATE);

    }

    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            musicService = ((MusicService.MusicBinder) service).getMusicService();
            musicService.addPlayerListener(App.this);

        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };


    private boolean isBusy = false;

    @Override
    public void onProgressChange(int progress) {

        if (isWorking) {
            CourseOfTreatmentFragment.onProgressChange(progress);
            if (isBusy) return;
            portHelper.sendData(Agreement.WorkingHeart);

        } else {
            if (isBusy) return;
            portHelper.sendData(Common.Heart);
        }


    }

    private void startReadThread() {
        //region开启线程读串口数据
        portHelper.setOnReceivedListener(new SerialPortHelper.OnReceivedListener() {
            @Override
            public void onReceived(final int[] bytes) {
                switch (bytes[1]) {
                    case 133://0x85 心跳指令回复


                        Log.v("心跳指令回复", "心跳指令回复");

                        String s = "";
                        for (int i = 0; i < bytes.length; i++) {
                            s = s + bytes[i] + ",";
                        }
                        Toast.makeText(getApplicationContext(), s, Toast.LENGTH_SHORT).show();

                        break;
                    default:
                        break;
                }
            }
        });
    }

    @Override
    public void onMusicChange(MusicInfoBean musicInfon, int duration) {


    }

    @Override
    public void onPlayPause() {

    }

    @Override
    public void onPlayResume() {

    }

    @Override
    public void onTimer(long time) {

    }

    @Override
    public void update(Observable o, Object arg) {

        if (arg == null) return;

        int state = (int) arg;
        switch (state) {
            case (0):

                musicService.playPause();
                isBusy = true;
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        //startReadThread();
                        musicService.play(0);
                        isBusy = false;
                    }
                }, 200);

                break;
            case (1):


                break;

        }


    }

    public static Resources getAppResources() {
        return instances.getResources();
    }


}
