package com.goockr.nakedeyeguard.service;

import android.app.Service;
import android.content.Intent;
import android.net.Uri;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.goockr.nakedeyeguard.net.Http.HttpHelper;
import com.zhy.http.okhttp.callback.FileCallBack;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import okhttp3.Call;

/**
 * Created by LJN on 2017/8/10.
 */

public class DownloadApkService  extends Service {
    private static final String TAG = "DownloadApkService";
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.e(TAG, "onStartCommand");
        downLoadTheNewstApk();
        return super.onStartCommand(intent, flags, startId);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
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
            }

            @Override
            public void onResponse(File response, int id) {
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
}
