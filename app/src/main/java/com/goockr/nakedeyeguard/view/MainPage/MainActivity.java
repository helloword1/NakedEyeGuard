package com.goockr.nakedeyeguard.view.MainPage;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.RelativeLayout;

import com.goockr.nakedeyeguard.Base.ActivityCollector;
import com.goockr.nakedeyeguard.Base.BaseActivity;
import com.goockr.nakedeyeguard.R;
import com.goockr.nakedeyeguard.Tools.App;
import com.goockr.nakedeyeguard.Tools.Common;
import com.goockr.nakedeyeguard.Tools.NetUtils;
import com.goockr.nakedeyeguard.Tools.NetWorkSpeedUtils;
import com.goockr.nakedeyeguard.Tools.NotNull;
import com.goockr.nakedeyeguard.Tools.SerialPortHelper;
import com.goockr.nakedeyeguard.Tools.ToastUtils;
import com.goockr.nakedeyeguard.data.Model.UserBean;
import com.goockr.nakedeyeguard.net.Http.HttpHelper;
import com.goockr.nakedeyeguard.net.callBack.ListUserCallback;
import com.goockr.nakedeyeguard.service.DownloadApkService;
import com.goockr.nakedeyeguard.view.SettingPage.SettingActivity;
import com.goockr.nakedeyeguard.view.SettingPage.WifiPage.WifiActivity;
import com.goockr.nakedeyeguard.widget.Screensaver.ScreenReceiver;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.FileCallBack;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;

import static com.goockr.nakedeyeguard.Tools.App.preferences;
import static com.goockr.nakedeyeguard.Tools.LoadingAnimation.rotateDictAnimation;


public class MainActivity extends BaseActivity implements View.OnClickListener {
    private Handler mHnadler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 100:
                    Log.i("handleMessage： ", "当前网速： " + msg.obj.toString());
                    break;
            }
            super.handleMessage(msg);
        }
    };
    static List<UserBean> userModels = new ArrayList<>();
    GridView gv_MainUser;
    WindowManager windowManager;
    ImageButton ib_MainSetting;
    GridAdapter gridAdapter;
    ImageButton ib_MainRefresh;
    App app;
    ScreenReceiver screenReceiver;
    private final String fileDir = Environment.getExternalStorageDirectory().getAbsolutePath();
    private Handler handler;
    private String TAG = "MainActivity";


    private Handler timeHandler;
    private String mVersion;


    @Override
    protected int getLoyoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i("mylog", "onCreate");
        new NetWorkSpeedUtils(this, mHnadler).startShowNetSpeed();
        handler = new Handler();
        timeHandler = new Handler();
        setupUI();
        initData();
        initWifiLister();
    }

    private void initWifiLister() {

//        WifiRssiTool.getInstance().create();
//        WifiRssiTool.getInstance().wifiImageView=(ImageView)findViewById(R.id.iv_BaseWifi);

    }


    /**
     * 初始化网络数据
     */

    private void initData() {
        String SSID = NetUtils.getCurWifi();
        Log.i("mylog", SSID);
        if (SSID.equals("0x") || SSID.equals("<unknown ssid>")) {
            if (NetUtils.isBind()) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        //网络判断
                        boolean isconnected = NetUtils.connect();
                        if (isconnected) {
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    // dialog.dismiss();
                                    initValue();

                                }
                            });

                        } else {
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    //dialog.dismiss();
                                    ToastUtils.showToast(MainActivity.this, "请检查网络");
                                }
                            });
                        }
                    }
                }).start();
            } else {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        ToastUtils.showToast(MainActivity.this, "请检查网络");
                    }
                });
            }
        } else {
            initValue();
        }
    }

    //跳转下载
    private void getAPPInfo() {
        PackageManager manager = this.getPackageManager();
        String versionName = null;
        try {
            PackageInfo info = manager.getPackageInfo(this.getPackageName(), 0);
            versionName = info.versionName; // 版本名
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        boolean serviceRunning = isServiceRunning(this, DownloadApkService.class.getName());
        Log.i("mylog", "versionName: " + versionName);
        Log.i("mylog", "serviceRunning: " + serviceRunning);
        if (NotNull.isNotNull(versionName) && !TextUtils.equals(versionName, mVersion) && !serviceRunning) {
//            Intent intent = new Intent().setClass(this, DownloadApkService.class);
//            startService(intent);
            App.editor.putBoolean("PROGRESSBAR",true);
            pbView.setVisibility(View.VISIBLE);
            downLoadTheNewstApk();
        }
    }

    public void downLoadTheNewstApk() {
        final String path = getApplication().getFilesDir().getAbsolutePath();
        HttpHelper.httpPost(HttpHelper.getTheNewstApkStr(), new HashMap<String, String>(), new FileCallBack(path, "123.apk") {
            @Override
            public void inProgress(float progress, long total, int id) {
                Log.d("12313", "onResponse: progress " + progress);
            }

            @Override
            public void onError(Call call, Exception e, int id) {
                Log.d("12313", "onResponse: onError " + e.toString());
                App.editor.putBoolean("PROGRESSBAR",false);
                pbView.setVisibility(View.GONE);
            }

            @Override
            public void onResponse(File response, int id) {
                App.editor.putBoolean("PROGRESSBAR",false);
                pbView.setVisibility(View.GONE);
                Log.d("12313", "onResponse: onSucceed " + response.toString());
                installApk(response);
                Log.d("12313", "onResponse: " + id);
            }
        });
    }

    /**
     * 安装apk
     */
    private void installApk(File response) {
        App.editor.putString("VERSION", mVersion);
        try {
            String command = "chmod 777 " + response.getAbsolutePath();
            Log.i("zyl", "command = " + command);
            Runtime runtime = Runtime.getRuntime();
            Process proc = runtime.exec(command);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i.setDataAndType(Uri.parse("file://" + response.toString()), "application/vnd.android.package-archive");
        startActivity(i);

    }

    /*
         * 判断服务是否启动,context上下文对象 ，className服务的name
         */
    public static boolean isServiceRunning(Context mContext, String className) {

        boolean isRunning = false;
        ActivityManager activityManager = (ActivityManager) mContext
                .getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> serviceList = activityManager
                .getRunningServices(30);

        if (!(serviceList.size() > 0)) {
            return false;
        }

        for (int i = 0; i < serviceList.size(); i++) {
            if (serviceList.get(i).service.getClassName().equals(className) == true) {
                isRunning = true;
                break;
            }
        }
        return isRunning;
    }

    private void setupUI() {
        app = (App) getApplication();
        getBackBtn().setVisibility(View.GONE);
        ib_MainRefresh = (ImageButton) findViewById(R.id.ib_MainRefresh);
        ib_MainRefresh.setOnClickListener(this);
        ib_MainSetting = (ImageButton) findViewById(R.id.ib_MainSetting);
        ib_MainSetting.setOnClickListener(this);
        windowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        int screenWidth = windowManager.getDefaultDisplay().getWidth();
        int screenHeight = windowManager.getDefaultDisplay().getHeight();
        int spacing = 0;
        int marginTop = 0;
        gv_MainUser = (GridView) findViewById(R.id.gv_MainUser);
        if (screenHeight >= 1080) {
            spacing = (screenWidth - 720) / 4;
            marginTop = 120;
        } else {
            spacing = (screenWidth - 600) / 4;
            marginTop = 80;
        }
        gv_MainUser.setHorizontalSpacing(spacing);
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(gv_MainUser.getLayoutParams());
        lp.setMargins(spacing, marginTop, spacing, 0);
        gv_MainUser.setLayoutParams(lp);
        gridAdapter = new GridAdapter(this, userModels);
        gv_MainUser.setAdapter(gridAdapter);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                for (Activity activty : ActivityCollector.activities) {
                    if (activty.getClass().equals(WifiActivity.class))
                        activty.finish();
                }
            }
        }, 1000);


    }

    private void initValue() {
        loadData();
    }

    private void loadData() {
        handler.post(new Runnable() {
            @Override
            public void run() {
                rotateDictAnimation(ib_MainRefresh, 3000, 3000);
            }
        });
        Log.d(TAG, "getNewstApkinfo:  ---begin--> ");
        final Map<String, String> map = new HashMap<>();
        map.put("c_pad_id", HttpHelper.deviceId);
        userModels.clear();
        HttpHelper.httpPost(HttpHelper.getUserList(), map, new ListUserCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                ToastUtils.showToast(getBaseContext(), "请检查网络");
            }

            @Override
            public void onResponse(List<UserBean> response, int id) {
                //根据 Tag 取消请求
                OkHttpUtils.getInstance().cancelTag(this);
                userModels.clear();
                userModels.addAll(response);
                Log.i("response", response.toString());
                gridAdapter.notifyDataSetChanged();
            }
        });
        HttpHelper.httpPost(HttpHelper.getNewstApkinfo(), new HashMap<String, String>(), new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {

            }

            @Override
            public void onResponse(String response, int id) {
                Log.d(TAG, "getNewstApkinfo:  getdata--> " + response);
                try {
                    JSONObject object = new JSONObject(response);
                    String version = preferences.getString("VERSION", "");
                    if (!TextUtils.equals(object.getString("version"), version)) {
                        mVersion = object.getString("version");
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                getAPPInfo();
                            }
                        }).start();

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ib_MainSetting:
                String SSID = NetUtils.getCurWifi();
                if (SSID.equals("0x") || SSID.equals("<unknown ssid>")) {
                    initData();
                }
                Intent intent = new Intent(MainActivity.this, SettingActivity.class);
                startActivity(intent);
                break;
            case R.id.ib_MainRefresh:
                Log.i("handleMessage： ", "点击： 111");
                registerBroadcast();
                initData();
                break;
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i(TAG, "onStop");
        try {
            //dialog.dismiss();
        } catch (Exception e) {

        }
    }


    @Override
    protected void onRestart() {
        super.onRestart();
        Log.i(TAG, "onRestart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i(TAG, "onResume");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    public void saveToSDCard(String name) throws Throwable {

        File musicFile = new File(Common.musicPath);

        if (musicFile.exists()) return;

        InputStream inStream = getResources().openRawResource(R.raw.abc);
        File file = new File(Environment.getExternalStorageDirectory(), name);
        FileOutputStream fileOutputStream = new FileOutputStream(file);//存入SDCard
        byte[] buffer = new byte[10];
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        int len = 0;
        while ((len = inStream.read(buffer)) != -1) {
            outStream.write(buffer, 0, len);
        }
        byte[] bs = outStream.toByteArray();
        fileOutputStream.write(bs);
        outStream.close();
        inStream.close();
        fileOutputStream.flush();
        fileOutputStream.close();
    }

    private void startReadThread() {
        //region开启线程读串口数据
        app.portHelper.setOnReceivedListener(new SerialPortHelper.OnReceivedListener() {
            @Override
            public void onReceived(final int[] bytes) {
                switch (bytes[1]) {

                    case 133://0x85 心跳指令回复

                        Log.v("心跳指令回复", "心跳指令回复");

                        break;
                    default:
                        break;
                }
            }
        });
    }

}
